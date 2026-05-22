package org.firstinspires.ftc.teamcode.OpModes.Tuning;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RobotContainer;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RobotContainer;

/*
 * This file contains an example of an "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode is executed.
 */
@TeleOp(name="Blue TeleOp", group="OpMode")
//@Disabled
public class KsTuner extends CommandOpMode {
public double Ks = 0;

public double[] increments = {0.000001, 0.00001, 0.0001, 0.001, 0.01};
int incrementIdx = 4; //Start at 0.001


    // Initialize all objects, set up subsystems, etc...
    @Override
    public void initialize() {
        // initialize robot
        // set team alliance color to blue (isRedAlliance=false)
        RobotContainer.Init(this, false);

        // perform any teleop initialization
        RobotContainer.Init_TeleOp();

        // wait for start button
        waitForStart();

        // if start button has been pressed
        if (opModeIsActive()) {

            // ---------- teleop command ----------

            // add any command to run automatically at start of teleop
        }

    }

    // Run Op Mode. Is called after user presses play button
    // called continuously
    @Override
    public void run() {
        // execute robot periodic function

        if (RobotContainer.toolOp.wasJustPressed(GamepadKeys.Button.DPAD_UP)&& incrementIdx < 4) {
            incrementIdx++;
        }
        else if (RobotContainer.toolOp.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)&& incrementIdx > 0) {
            incrementIdx--;
        }
        RobotContainer.shooter.SetMotorSpeed(Ks);
        RobotContainer.telemetrySubsystem.addData("Ks",Ks);

        RobotContainer.telemetrySubsystem.addData("RPM", RobotContainer.shooter.GetLeftMotorFlyWheelSpeed());
        //RobotContainer.telemetrySubsystem.addData("TPS", RobotContainer.shooter.G);
    }

}