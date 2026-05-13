package org.firstinspires.ftc.teamcode.Subsystems.Utils;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotContainer;

/**
 * Place description of subsystem here
 *
 * @author kaitlyn
 */
public class Blinkin extends SubsystemBase {
    private RevBlinkinLedDriver blinkin;
    private boolean hasGreen = false;
    private boolean hasPurple = false;
    private  boolean hasArtifact = false;
    private boolean hasTag = false;
//    private boolean seesPurple;
//    private boolean seesGreen;
    private ElapsedTime Blinktimer;
    // Local objects and variables here

    /** Place code here to initialize subsystem */
    public Blinkin() {
        blinkin = RobotContainer.ActiveOpMode.hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        Blinktimer = new ElapsedTime();
        Blinktimer.reset();
    }

    /** Method called periodically by the scheduler
     * Place any code here you wish to have run periodically */
    @Override
    public void periodic() {

        Update();
    }

    // place special subsystem methods here

    /**
     * shows alliance color on the blinkin - red for red alliance, blue for blue alliance
     */

    public void Update(){
       ShowAlliance();
    }

    public void ShowAlliance(){
        if (RobotContainer.isRedAlliance()){
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
        }else{
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
        }
    }

    public void ShowGoal(){
        blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
    }


}