package org.firstinspires.ftc.teamcode.CommandGroups.AutoCommandGroups;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.ParallelRaceGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.Commands.Intake.HuntMode.HuntModeAuto;
import org.firstinspires.ftc.teamcode.Commands.UptakeRampControle;

// Example Sequential Command Group
// There are also:
// ParallelCommandGroup
// ParallelRaceGroup
// ParallelDeadlineGroup

public class AutoIntakeSequence extends SequentialCommandGroup {

    // constructor
    public AutoIntakeSequence() {

        addCommands (
             new ParallelCommandGroup(
                     new HuntModeAuto(1.75),
                     new UptakeRampControle(1.75)
             )
        );
    }
}
