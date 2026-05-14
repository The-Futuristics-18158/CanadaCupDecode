package org.firstinspires.ftc.teamcode.Subsystems.Shooter.FlywheelSubsystem;

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
    private final DcMotorEx intakeMotor;

    private final double TICKSPStoRPM = (1/28.0)*60.0;

    public static double LeftCurrentSpeed;
    public static double RightCurrentSpeed;



    /** Place code here to initialize subsystem */
    public FlywheelSubsystem() {
        rightShooterMotor = RobotContainer.ActiveOpMode.hardwareMap.get(DcMotorEx.class, "rightShooterMotor");
        leftShooterMotor = RobotContainer.ActiveOpMode.hardwareMap.get(DcMotorEx.class, "leftShooterMotor");
        intakeMotor = RobotContainer.ActiveOpMode.hardwareMap.get(DcMotorEx.class, "intakeMotor");

        rightShooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftShooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        rightShooterMotor.setPower(0.0);
        leftShooterMotor.setPower(0.0);
        intakeMotor.setPower(0.0);

        rightShooterMotor.setDirection(DcMotor.Direction.REVERSE);
        leftShooterMotor.setDirection(DcMotor.Direction.REVERSE);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    /** Method called periodically by the scheduler
     * Place any code here you wish to have run periodically */
    @Override
    public void periodic() {

        LeftCurrentSpeed = leftShooterMotor.getVelocity() * TICKSPStoRPM;
        RightCurrentSpeed = rightShooterMotor.getVelocity() * TICKSPStoRPM;







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

}