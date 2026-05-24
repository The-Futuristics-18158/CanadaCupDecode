package org.firstinspires.ftc.teamcode.Commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.Utility.Utils;

/** A command to follow a path generated from input parameters
 * */
public class TurnTurretToTarget extends CommandBase {

    double m_endangle;
    double m_timeout;

    // speed to rotate robot - determined by PID controller
    double m_rotatespeed;
    double m_angleerror;

    // PID gains for rotating robot towards ball target
    public static double kpmax = 0.05; // was 0.11 Feb 5/2026 KN
    public static double kpmin = 0.04; // was 0.04 Feb 5/2026 KN
    public static double kp_deg = 90.0;
    public static double ki = 0.2; // 0.20;
    public static double ki_range = 0.1; // was 5.0
    public static double ki_zerorange = 0.5;
    public static double kd = 0.019; // 0.0035;
    public static double kd_deg = 110.0;

    // filtered error angle
    double filtered_error;
    public static double LPFvalue = 0.93; // Low Pass Filter: was 0.93

    // p=0.1, d=0.01

    PIDController pidController = new PIDController(kpmax, ki, kd);

    // on-target timer
    ElapsedTime OnTargetTime;

    // Timeout timer
    ElapsedTime TimeOutTime;


    /** Turn to/by angle
     * Input: angle - degrees (-180<angle<180)
     * relative - true if relative to current angle, false if absolute to field
     * clockwise - true if rotate clockwise, false if counter-clockwise */
    public TurnTurretToTarget(double timeout) {

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(RobotContainer.turret);

        m_timeout = timeout;
        OnTargetTime = new ElapsedTime();
        TimeOutTime = new ElapsedTime();

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

        // our current facing angle
        double ourcurrentangle = RobotContainer.turret.getTurretDegrees();

        // determine target angle

        // get our current position and the target position
        Pose2d pose = RobotContainer.odometry.getCurrentPos();
        Translation2d targetPose = RobotContainer.targeting.GetShotTaget();

        // determine target angle
        double angle_rad = (new Vector2d(pose.getX() - targetPose.getX(), pose.getY() - targetPose.getY())).angle();
        m_endangle = Math.toDegrees(angle_rad) - 180.0; // switched as this turret shoots forward not back

        // reset PID controller
        pidController.reset();
        m_angleerror = 0.0;

        OnTargetTime.reset();
        TimeOutTime.reset();

        // determine initial error
        filtered_error = Utils.AngleDifference(m_endangle, RobotContainer.turret.getTurretDegrees());


    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        // determine speed to rotate robot
        m_angleerror = Utils.AngleDifference(m_endangle, RobotContainer.turret.getTurretDegrees());

        // apply LPF to angle error
        filtered_error = LPFvalue*filtered_error + (1.0-LPFvalue)*m_angleerror;


        // set p gain
        double pgain = kpmax - (kpmax - kpmin) * Math.abs(m_angleerror) / kp_deg;
        pgain = Math.max(kpmin, pgain);
        pgain = Math.min(kpmax, pgain);

        pidController.setP(pgain);

        // only integrate if within 10deg
        if (Math.abs(m_angleerror) < ki_range)
            pidController.setI(ki);
        else
            pidController.setI(0.0);

        // zero out the integrated error if very close to target
        if (Math.abs(m_angleerror) < ki_zerorange)
            pidController.reset();

        //if (Math.abs(m_angleerror) < 90.0)
        //    pidController.setD(kd);
        //else
        //    pidController.setD(kdmin);

        double gain = Math.max(0.0, kd*(1.0-Math.abs(filtered_error)/kd_deg));
        pidController.setD(gain);

        // execute PID controller
        m_rotatespeed = pidController.calculate(m_angleerror);

        //RobotContainer.telemetrySubsystem.addData("m_angleError", m_angleerror);
        //RobotContainer.telemetrySubsystem.addData("m_endAngle", m_endangle);

        // rotate robot
        RobotContainer.turret.moveTurret(m_angleerror);

        // if we are within target, allow timer to count up
        if (Math.abs(m_angleerror) >= 1.0)
            OnTargetTime.reset();


        // RobotContainer.Panels.FTCTelemetry.addData("TargetAngle", m_endangle);
        // RobotContainer.Panels.FTCTelemetry.addData("AngleError", Math.max(-10.0, Math.min(10.0, m_angleerror)));
        // RobotContainer.Panels.FTCTelemetry.update();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        // stop robot
        RobotContainer.turret.getTurretDegrees();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // we are finished when within tolerance of target for required duration,
        // or have timed out
        return (OnTargetTime.seconds()>0.25 || TimeOutTime.seconds() >=m_timeout);
    }
}