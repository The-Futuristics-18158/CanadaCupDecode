package org.firstinspires.ftc.teamcode.Subsystems.Shooter;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
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

    private final double TICKSPStoRPM = (1/28.0)*60.0;

    // target speed
    public static double TargetSpeed;
    public static double LeftCurrentSpeed;
    public static double RightCurrentSpeed;

    // PIF Controller Gains
    private final double FsGain = 0.0;
    private final double FvGain = 0.00021; // was 0.0002 // unit=power/rpm   initial value=1.0/6000rpm=0.00016667
    public final double PGain = 0.0012;// was 0.0003
    public final double IGain = 0.0002;

    // integrated error
    private double ShooterIError;
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

        // reset integrated error
        timer = new ElapsedTime();
        timer.reset();
        ShooterIError =0.0;

        // reset target speed (rpm)
        TargetSpeed=0.0;
    }

    /** Method called periodically by the scheduler
     * Place any code here you wish to have run periodically */
    @Override
    public void periodic() {

        LeftCurrentSpeed = leftShooterMotor.getVelocity() * TICKSPStoRPM;
        RightCurrentSpeed = rightShooterMotor.getVelocity() * TICKSPStoRPM;
// our current speed error
        double SpeedError = TargetSpeed - (LeftCurrentSpeed + RightCurrentSpeed) / 2;

        // integrated error
        // determine time since last iteration
        double dt = timer.seconds();
        timer.reset();
        // integrate speed error
        ShooterIError += IGain * SpeedError * 0.02;
        // anti-windup to prevent overshoots
        if (SpeedError < -50.0 && ShooterIError > 0.0)
            ShooterIError *= 0.90;
        if (SpeedError > 50.0 && ShooterIError < 0.0)
            ShooterIError *= 0.90;
        // integrated error limiter
        if (ShooterIError > 0.15) ShooterIError = 0.15;
        if (ShooterIError < -0.1) ShooterIError = -0.1;

        // PIF controller
        double NewPower = FsGain +                    // static feedforward
                FvGain * TargetSpeed +      // speed feedforward
                PGain * SpeedError +        // proportional gain
                ShooterIError;                     // integrated error
        // only drive motor in positive direction, otherwise let it coast
        if (SpeedError >= -50.0){
            leftShooterMotor.setPower(NewPower);
        rightShooterMotor.setPower(NewPower);}
        else{
        leftShooterMotor.setPower(0.0);
        rightShooterMotor.setPower(0.0);
    }

        //RobotContainer.Panels.FTCTelemetry.addData("Speed", CurrentSpeed);
        //RobotContainer.Panels.FTCTelemetry.addData("Target", TargetSpeed);
        //RobotContainer.Panels.FTCTelemetry.update();
    }


    /** Sets shooter flywheel speed in rpm
     * @param RPM a double representing the desired flywheel speed in rpm. Negative values will be treated as 0.0.
     */
    public void SetFlywheelSpeed(double RPM){
        // Setting velocity using the RPMToVelocity methode
        //TargetSpeed = RobotContainer.targeting.CalculateSpeed();
        TargetSpeed = RPM;

    }

    /**gets current flywheel speed in rpm
     * @return current flywheel speed in rpm
     */
    public double GetFlyWheelSpeed() {
        return (LeftCurrentSpeed + RightCurrentSpeed)/2;
    }

    /**gets target flywheel speed in rpm
     * @return target flywheel speed in rpm
     */
    public double GetFlyWheelTargetSpeed() {
        return TargetSpeed;
    }

}