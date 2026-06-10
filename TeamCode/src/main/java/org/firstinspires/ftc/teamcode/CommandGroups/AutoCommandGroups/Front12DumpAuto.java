package org.firstinspires.ftc.teamcode.CommandGroups.AutoCommandGroups;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;

import org.firstinspires.ftc.teamcode.CommandGroups.TeleOpSequences.ShotSequence;
import org.firstinspires.ftc.teamcode.Commands.Drive.MoveToPose;
import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.Utility.AutoFunctions;

public class Front12DumpAuto extends SequentialCommandGroup {

    // Constructor
    public Front12DumpAuto() {

        // Max speed of 1.4 and max acceleration 3.0 works well
        addCommands (

                new InstantCommand(()-> RobotContainer.odometry.setCurrentPos(AutoFunctions.redVsBlue(new Pose2d(-1.29, -1.29, new Rotation2d(Math.toRadians(-135.0)))))), // was x = -1.2, y = -1.35
//      -------------------------- Artifact Cycle #1  --------------------------
                // Move to a shot #1
                new MoveToPose(
                        1.4, // was 1.4
                        3.0, // was 2.5
                        AutoFunctions.redVsBlue((new Pose2d(-0.3, -0.3, new Rotation2d(Math.toRadians(-90.0)))))),//  -90 degrees // was -0.33, -0.47

                // Shot #1
                new ShotSequence(),

//      -------------------------- Start of Artifact Cycle #2 --------------------------
                // Hunt
                new AutoIntakeSequence(2.15),

//    -------------------------- Start Of Dump --------------------------

                // Line up to dump gate
                new MoveToPose(
                        1.4, // was 1.5
                        3.0,// was 2.5
                        AutoFunctions.redVsBlue((new Pose2d(-0.10, -1.2, new Rotation2d(Math.toRadians(180.0)))))),

                // Dump Gate
                new MoveToPose(
                        1.4,// was 1.5
                        2.5,// was 2.2
                        AutoFunctions.redVsBlue((new Pose2d(-0.10, -1.42, new Rotation2d(Math.toRadians(180.0)))))),

//    -------------------------- End Of Dump --------------------------

                // Move to shot #2
                new MoveToPose(
                        1.4,// was 1.5
                        3.0,// was 2.0
                        AutoFunctions.redVsBlue((new Pose2d(-0.3, -0.3, new Rotation2d(Math.toRadians(-90.0)))))),

                // Shot #2
                new ShotSequence(),

//    -------------------------- Start of Artifact Cycle #3 --------------------------
                // Move to pickup
                new MoveToPose(
                        1.4,// was 1.5
                        3.0,// was 2.5
                        AutoFunctions.redVsBlue((new Pose2d(0.3, -0.6, new Rotation2d(Math.toRadians(-90.0)))))),

                // Hunt
                new AutoIntakeSequence(2.15),

                // Move to Shot #3
                new MoveToPose(
                        1.4, // was 1.5
                        3.0,// was 2.5
                        AutoFunctions.redVsBlue(new Pose2d(0.3, -1.2, new Rotation2d(Math.toRadians(-90.0))))
                ),

                new MoveToPose(
                        1.4,// 1.5
                        3.0,// 2.5
                        AutoFunctions.redVsBlue(new Pose2d(-0.3, -0.4, new Rotation2d(Math.toRadians(-45.0))))
                ),

                // Shot #3
                new ShotSequence(),

//      -------------------------- Start of Artifact Cycle #4 --------------------------

                new MoveToPose(
                        1.4,// 1.5
                        3.0,// 2.5
                        AutoFunctions.redVsBlue((new Pose2d(0.9, -0.6, new Rotation2d(Math.toRadians(-90.0)))))),

                new AutoIntakeSequence(2.0),



                new MoveToPose(
                        1.4,// 1.5
                        3.0,// 2.5
                        AutoFunctions.redVsBlue(new Pose2d(0.9, -1.35, new Rotation2d(Math.toRadians(-45.0))))),



                new MoveToPose(
                        1.4,// 1.5
                        3.0,// 2.5
                        AutoFunctions.redVsBlue(new Pose2d(-0.5, -0.4, new Rotation2d(Math.toRadians(-45.0))))),

                // Shot #4
                new ShotSequence(),

//      -------------------------- Leave --------------------------

                new MoveToPose(
                        1.4,//1.5
                        3.0,// 2.5
                        AutoFunctions.redVsBlue((new Pose2d(0.0, -0.9, new Rotation2d(Math.toRadians(0.0))))))

        );
    }
}

//Example MoveToPose
// new MoveToPose(
//           1.5,
//           1.0,
// AutoFunctions.redVsBlue(new Pose2d(-0.22, 1.2, new Rotation2d(Math.toRadians(-90))))
//        ),

//Example FollowPath
// new FollowPath(
//             2.0,
//             1.0,
//             0.0,
//             0.0,
// AutoFunctions.redVsBlue(new Rotation2d(Math.toRadians(-90.0))),
// new ArrayList<Translation2d>() {{ }},
// AutoFunctions.redVsBlue(new Pose2d(0.55, 0.25, new Rotation2d(Math.toRadians(-180)))),
// AutoFunctions.redVsBlue(new Rotation2d(Math.toRadians(-180)))),


// Example #2: Using conditional command
// conditionally run commands depending on condition
/*      new FourWayConditionalCommand(
            ()-> { return true; },
            new SequentialCommandGroup(
                commands to do this
            ),
            ()-> { return false; },
            new SequentialCommandGroup(
                commands to do that
            ),
            ()-> { return false; },
            new SequentialCommandGroup(
                commands to do those
            ),
            new SequentialCommandGroup(
                commands to do something else
            )

        ) // end FourWayCondition
*/