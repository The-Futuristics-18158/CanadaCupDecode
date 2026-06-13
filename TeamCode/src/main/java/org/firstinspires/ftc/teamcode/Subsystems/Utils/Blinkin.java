package org.firstinspires.ftc.teamcode.Subsystems.Utils;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.RobotContainer;

/**
 * Runs the BLinkin LEDs
 *
 * @author YourGithubName
 */
public class Blinkin extends SubsystemBase {
    private RevBlinkinLedDriver blinkin;

    //private int artifactsInRamp;
    //private boolean artifactWait;
    //private ElapsedTime timer;

    // Local objects and variables here

    /** Place code here to initialize subsystem */
    public Blinkin() {
        blinkin = RobotContainer.ActiveOpMode.hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        //artifactsInRamp=0;
        //artifactWait=false;
        //timer=new ElapsedTime();
    }

    /** Method called periodically by the scheduler
     * Place any code here you wish to have run periodically */
    @Override
    public void periodic() {

        // show team colour as default
        if(RobotContainer.isRedAlliance()==true) {
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
        } else {
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
        }

        // does limelight have valid target for shooting?
        LLResult results = RobotContainer.limeLight.getLimeLightResults();
        if (results != null && results.isValid() && results.getFiducialResults() != null &&
                !results.getFiducialResults().isEmpty() && results.getStaleness() < 100)
            for (int i = 0; i < results.getFiducialResults().size(); ++i)
                if ((RobotContainer.isRedAlliance() && results.getFiducialResults().get(i).getFiducialId() == 24) ||
                        (!RobotContainer.isRedAlliance() && results.getFiducialResults().get(i).getFiducialId() == 20))

                    blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.VIOLET);

        // is robot in safe zone to shoot?
        if (RobotContainer.targeting.IsRobotInAllowableShotZone())
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);

        // show fancy green pattern if currently in vacuum mode
        if (RobotContainer.isVacuuming)
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.SINELON_FOREST_PALETTE);

        //if(RobotContainer.uptakeSensor.isUpakeArtifactPresent()==true) {
        //            if(artifactWait==false) {
        //                artifactsInRamp++;
        //                  blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);//            }
        //}
        //artifactWait=RobotContainer.uptakeSensor.isUpakeArtifactPresent();
    }
}