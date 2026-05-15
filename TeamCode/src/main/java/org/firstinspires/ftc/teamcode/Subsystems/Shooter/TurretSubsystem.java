package org.firstinspires.ftc.teamcode.Subsystems.Shooter;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RobotContainer;

/**
 * Place description of subsystem here
 *
 * @author Blackthrush
 */
public class TurretSubsystem extends SubsystemBase {

    // Local objects and variables here

    private final DcMotor TurretMotor;

    public static double TurretTargetSpeed;
    public static double TargetSpeed;
    public static int TurretTargetPosition;


    /** Place code here to initialize subsystem */
    public TurretSubsystem() {
    TurretMotor = RobotContainer.ActiveOpMode.hardwareMap.get(DcMotor.class, "TurretMotor");

    TurretMotor.setPower(0.0);

    TargetSpeed=0.0;

    TurretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    TurretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    /** Method called periodically by the scheduler
     * Place any code here you wish to have run periodically */
    @Override
    public void periodic() {
        TurretMotor.setTargetPosition(TurretTargetPosition);



    }

    // place special subsystem methods here
    // 1 rotation =28 pulses
    // motor is 12:1
    //1 rotation of the turret is 1680
    public void IncreaseTurretPosition(){
        TurretTargetPosition += 4;
    }
    public void DecreaseTurretPosition(){
        TurretTargetPosition -= 4;
    }
}