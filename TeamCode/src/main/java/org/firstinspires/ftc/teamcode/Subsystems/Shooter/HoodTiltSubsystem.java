package org.firstinspires.ftc.teamcode.Subsystems.Shooter;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.RobotContainer;

/**
 * Place description of subsystem here
 *
 * @author superzokabear
 */
 //@Configurable
public class HoodTiltSubsystem extends SubsystemBase {

    // Local objects and variables here
    public static double hoodPosition; // set as a static when no using pannels
    private Servo Tilt;

    /** Place code here to initialize subsystem */
    public HoodTiltSubsystem() {
        Tilt = RobotContainer.ActiveOpMode.hardwareMap.get(Servo.class, "hoodAngleServo");
        Tilt.setDirection(Servo.Direction.FORWARD);
        hoodPosition = minAngle;
    }

    /** Method called periodically by the scheduler
     * Place any code here you wish to have run periodically */
    @Override
    public void periodic() {
        //SetHoodPosition(hoodPosition);
    }

    // place special subsystem methods here
    // Raise left or right hood servo
    public double maxAngle = 1.0; // No higher under penalty of a creative death
    public double minAngle = 0.3;

    /**Sets shooter hood position
     * @param position Desired position between 0.3 and 1.0
     *      Last updated for Canada Cup Robot
     */
    public void SetHoodPosition(double position){
        double pos = position;
        if (pos < minAngle){ pos = minAngle;}
        if (pos > maxAngle){ pos = maxAngle;}
        Tilt.setPosition(pos);
    }


}