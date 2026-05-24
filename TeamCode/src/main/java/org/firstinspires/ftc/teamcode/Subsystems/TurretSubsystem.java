package org.firstinspires.ftc.teamcode.Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.Utility.Utils;

/**
 * Turret Subsystem
 * @author superzokabear
 * @author Kw126
 */
public class TurretSubsystem extends SubsystemBase {

    //private final TurretSubsystem turretSubsystem;
    // Initialize motor
    private final DcMotorEx turret;

    private final double TICKS_TO_DEGREES = 410.0/360.0;

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

        // Setting target to zero upon initialization
        // turret.setTargetPosition(0);

//        // Will get moved to Turn Turret to Target
//        double turnError = Math.abs (turret.getCurrentPosition() - turret.getTargetPosition());
//        if (turnError >= 20) {
//            // Sets the motor to PID values for large distances
//            turret.setVelocityPIDFCoefficients(5.0, 5.0, 12.0, 10.0);// Long distance settings are (p:0.03 , i:0.0 , d:0.0 , f:40.0)
//        }else {
//            // Sets the motor to PID values for short distances
//            turret.setVelocityPIDFCoefficients(5.0, 5.0, 12.0, 10.0);// Short distance settings are (p:220.0, i:10.0, d:0.00, f:40.0)
//        }

        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        turret.setPower(1.0);

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
    public void moveTurret(double turretTargetDegrees, double turretRemainingError) {
        int targetPosition = (int)(turretTargetDegrees * TICKS_TO_DEGREES);

        // create variable PI control for remaining error and smooth operation
        double variableP = -0.47 * turretRemainingError + 99.37; // -0.71 * x + 107.86 was a bit too aggressive
        double variableI = -0.047 * turretRemainingError + 9.937; // -0.071 * x + 10.786 was a bit too aggressive

        // enable mins
        variableP = Math.max(15.0, variableP);
        variableI = Math.max(1.5, variableI);

        // enable maxes
        variableP = Math.min(90.0, variableP);
        variableI = Math.min(15.0, variableI);

        // set variable PI
        turret.setVelocityPIDFCoefficients(variableP, variableI, 0.0, 0.0);

//        // adjust PIDF for large moves and small for speed and stability
//        if (Math.abs(turretRemainingError) > 25){
//            // Sets the motor to PID values for large distances
//            turret.setVelocityPIDFCoefficients(15.0, 1.5, 0.0, 0.0);// Long distance settings are (p:15.0 , i:1.5 , d:0.0)
//        } else {
//            // Sets the motor to PID values for short distances
//            turret.setVelocityPIDFCoefficients(90.0, 9.0, 0.0, 0.0);// Short distance settings are (p:90.0, i:9.0.0, d:0.0)
//        }

        // move the turret motor
        turret.setTargetPosition(targetPosition);
    }

    public double getTurretTargetDegrees(){
        return turret.getTargetPosition() / TICKS_TO_DEGREES;
    }

    public double getTurretDegrees(){
        return turret.getCurrentPosition()/TICKS_TO_DEGREES;
    }

    public int getTurretTargetTicks(){
        return turret.getTargetPosition();
    }

    public double getTurretTicks(){
        return turret.getCurrentPosition();
    }

}
