package org.firstinspires.ftc.teamcode.Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.RobotContainer;

/**
 * Turret Subsystem
 * @author superzokabear
 * @author Kw126
 */
public class TurretSubsystem extends SubsystemBase {

    //private final TurretSubsystem turretSubsystem;
    // Initialize motor
    private final DcMotorEx turret;

    private final double TICKS_TO_DEGREES = 410/360;

    /**
     * Place code here to initialize subsystem
     */
    public TurretSubsystem() {
        // Creates the motor using the hardware map
        turret = RobotContainer.ActiveOpMode.hardwareMap.get(DcMotorEx.class, "turretMotor");

        // Resets the encoder for motor
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Motor direction
        turret.setDirection(DcMotorSimple.Direction.REVERSE);

        // Sets the motor to PIDF values
        turret.setVelocityPIDFCoefficients(8.0, 0.1, 0.00, 1.0);//needs to be tuned for the turret

        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Setting target to zero upon initialization
        turret.setTargetPosition(0);

        turret.setPower(0.3);

    }

    /**
     * Method called periodically by the scheduler
     * Place any code here you wish to have run periodically
     */
    @Override
    public void periodic() {

        //RobotContainer.DBTelemetry.addData("TurretPose ", turret.getCurrentPosition());
        //RobotContainer.DBTelemetry.update();

    }

    public void turretStop(){
        turret.setPower(0.0);
    }

    /**
     * Causes the turret to turn.
     */
    public void moveTurret(double degrees) {
        int targetPosition = (int)(degrees * TICKS_TO_DEGREES);
        turret.setTargetPosition(targetPosition);
    }

    public double getTurretDegrees(){
        return turret.getCurrentPosition() /TICKS_TO_DEGREES;
    }

    public double getTurretTicks(){
        return turret.getCurrentPosition();
    }
}
