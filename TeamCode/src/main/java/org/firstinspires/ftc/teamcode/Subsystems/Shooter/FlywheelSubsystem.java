package org.firstinspires.ftc.teamcode.Subsystems.Shooter;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RobotContainer;

/**
 * Place description of subsystem here
 *
 * @author Blackthrush
 */
public class FlywheelSubsystem extends SubsystemBase {


    // Local objects and variables here
    private final DcMotorEx rightShooterMotor;
    private final DcMotorEx leftShooterMotor;


    public static double TargetSpeed;  // When disabling dashboard/panels turn back to privet. Make static when not using pannels
    public static double CurrentSpeed;

    private final double MAXRPM = 6000.0;
    private final double TICKSPStoRPM = (1/28.0)*60.0;

    public static double LeftCurrentSpeed;
    public static double RightCurrentSpeed;

    // PIF Controller Gains
    private final double FsGain = 0.0;
    private final double FvGain = 0.00021; // was 0.0002 // unit=power/rpm   initial value=1.0/6000rpm=0.00016667
    public final double PGain = 0.0012;// was 0.0003
    public final double IGain = 0.0002;

    private double IError;
    private ElapsedTime timer;

    /** Place code here to initialize subsystem */
    public FlywheelSubsystem() {
        rightShooterMotor = RobotContainer.ActiveOpMode.hardwareMap.get(DcMotorEx.class, "rightShooterMotor");
        leftShooterMotor = RobotContainer.ActiveOpMode.hardwareMap.get(DcMotorEx.class, "leftShooterMotor");

        rightShooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftShooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        rightShooterMotor.setPower(0.0);
        leftShooterMotor.setPower(0.0);

        rightShooterMotor.setDirection(DcMotor.Direction.REVERSE);
        leftShooterMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    /** Method called periodically by the scheduler
     * Place any code here you wish to have run periodically */
    @Override
    public void periodic() {

        LeftCurrentSpeed = leftShooterMotor.getVelocity() * TICKSPStoRPM;
        RightCurrentSpeed = rightShooterMotor.getVelocity() * TICKSPStoRPM;


        // our current speed error
        double SpeedError = TargetSpeed - CurrentSpeed;

        // integrated error
        // determine time since last iteration
        double dt = timer.seconds();
        timer.reset();
        // integrate speed error
        IError += IGain * SpeedError * 0.02;
        // anti-windup to prevent overshoots
        if (SpeedError < -50.0 && IError > 0.0)
            IError *=0.90;
        if (SpeedError > 50.0 && IError < 0.0)
            IError *=0.90;
        // integrated error limiter
        if (IError > 0.15) IError=0.15;
        if (IError < -0.1) IError=-0.1;

        // PIF controller
        double NewPower = FsGain +                    // static feedforward
                FvGain * TargetSpeed +      // speed feedforward
                PGain * SpeedError +        // proportional gain
                IError;                     // integrated error
        // only drive motor in positive direction, otherwise let it coast
        if (SpeedError>=-50.0){
            rightShooterMotor.setPower(NewPower);
            leftShooterMotor.setPower(NewPower);
        }
        else{
            rightShooterMotor.setPower(0.0);
            leftShooterMotor.setPower(0.0);
        }



//        rightShooterMotor.setPower();
//        leftShooterMotor.setPower();
//        intakeMotor.setPower();
//        ShooterRPM = (60 * ((DcMotorEx) leftShooterMotor).getVelocity()) / 28;
//        IntakeRPM = (60 * ((DcMotorEx) intakeMotor).getVelocity()) / (28 * 4);
////        telemetry.addData("ShooterPower", leftShooterMotor.getPower());
////        telemetry.addData("ShooterRPM", ShooterRPM);
////        telemetry.addData("IntakePower", intakeMotor.getPower());
////        telemetry.addData("IntakeRPM", IntakeRPM);
////        telemetry.update();
    }

    // place special subsystem methods here
public void SetFlywheelSpeed(double speed){

}
}