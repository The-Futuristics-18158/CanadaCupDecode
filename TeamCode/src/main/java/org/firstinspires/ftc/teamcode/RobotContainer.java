package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.util.ElapsedTime;
//import org.firstinspires.ftc.teamcode.Commands.ClimbCommand;
import org.firstinspires.ftc.teamcode.Commands.Drive.ManualDrive;
//import org.firstinspires.ftc.teamcode.Subsystems.Utils.Blinkin;
//import org.firstinspires.ftc.teamcode.Subsystems.Climb.ClimbSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter.Flywheel;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter.HoodTilt;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter.RampLift;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter.ShotBlock;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Subsystems.Utils.OperatorControlsSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry.Gyro;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry.Odometry;
import org.firstinspires.ftc.teamcode.Subsystems.Odometry.PinpointOdometry;
import org.firstinspires.ftc.teamcode.Subsystems.Utils.TelemetrySubsystem;
import org.firstinspires.ftc.teamcode.Utility.AutoFunctions;

import java.util.List;

/** TODO
 * Adjust UptakeSubsystem to use one servo instead of two, and update all commands that use it    (no uptake)
 * Adjust Flywheel to use two motors instead of one, and update all commands that use it (in progress)
 * Remove Operator controls and references to it, or adjust to be used for something else         (completed)
 * Adjust shooting commands to not need to decide left vs right
 * Add constant tracking for the turret to the goal using the limelight
 * refactor ClimbSubsystem code to work with the DriveTrain
 *
 *
 * Update pinpointDriver.setOffsets() with correct values, line 48 of PinpointOdometry.java (completed)
 * Update Drive Kinematics with correct values, lines 31-32 of DriveTrain.java (completed)
 * Adjust ShotBlock subsystem (completed)
 * Adjust HoodTilt (completed)
 * Add the turret's subsystem (completed)
 * Remove code related to colour detection and pattern matching (completed)
    * Remove Obelisk.java and references to it                  (completed)
    * Remove code related to artifact colour detection          (completed)
        * Remove references to ColourSensor subsystem           (completed)
 */

public class RobotContainer {

    // active OpMode - used so any subsystem and command and access it and its members
    public static CommandOpMode ActiveOpMode;

    // team alliance color = false if robot on blue alliance, true for red
    public static boolean isRedAlliance;

    // FTC dashboard and telemetries
    //public static Panels Panels;
    public static TelemetrySubsystem telemetrySubsystem;

    // timer used to determine how often to run scheduler periodic
    private static ElapsedTime timer;
    public static ElapsedTime exectimer;

    // create robot GamePads
    public static GamepadEx driverOp;
    public static GamepadEx toolOp;

    // create pointers to robot subsystems
    public static OperatorControlsSubsystem operatorControls;
    public static Gyro gyro;
    public static PinpointOdometry odometryPod;
    public static DriveTrain drivesystem;
    public static Odometry odometry;
    public static HoodTilt hoodtilt;
    public static ShotBlock shotblock;
    public static RampLift ramplift;
    public static Intake intake;
    public static Flywheel shooter;
    public static Turret turret;

    //public static LimeLight limeLight;
    //public static RampCamera rampCamera;
    //public static DistanceSensor distance;
    //public static GoalTargeting targeting;
    //public static Turret turret;
    //public static ClimbSubsystem climb;
    //public static Blinkin blinkin;


    // Angle of the robot at the start of auto
    public static double RedStartAngle = 90;
    public static double BlueStartAngle = -90;

    // List of robot control and expansion hubs - used for caching of I/O
    static List<LynxModule> allHubs;

    // Robot Modes
    public enum Modes { Off, AutoInit, Auto, TeleOp}
    private static Modes CurrentRobotMode;

    public static double intervaltime;

    /**Robot initialization - common to both auto and teleop
     * @param mode A value from the Modes enum representing the current opmode being run, valid Modes as of 2/9/2026: Off, AutoInit, Auto, TeleOp
     * @param RedAlliance True if red alliance, false if blue alliance
     */
    public static void Init(CommandOpMode mode, boolean RedAlliance) {

        // save pointer to active OpMode
        ActiveOpMode = mode;

        // set alliance colour
        isRedAlliance = RedAlliance;

        // set robot mode - robot is off until we have initialized
        CurrentRobotMode = Modes.Off;

        // create list of robot control and expansion hubs
        // set each for manual caching - cache updated in periodic()
        allHubs = ActiveOpMode.hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        // create and reset timer
        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timer.reset();
        exectimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timer.reset();

        // set up dashboard and various telemetries
        // Panels = new Panels();
        telemetrySubsystem = new TelemetrySubsystem();

        // cancel any commands previously running by scheduler
        CommandScheduler.getInstance().cancelAll();

        // create gamepads
        driverOp = new GamepadEx(ActiveOpMode.gamepad1);
        toolOp = new GamepadEx(ActiveOpMode.gamepad2);

        // create systems
        operatorControls = new OperatorControlsSubsystem();
        gyro = new Gyro();
        odometryPod = new PinpointOdometry();
        drivesystem = new DriveTrain();
        odometry = new Odometry();
        hoodtilt = new HoodTilt();
        shotblock = new ShotBlock();
        ramplift = new RampLift();
        intake = new Intake();
        shooter = new Flywheel();
        turret = new Turret();


        //limeLight = new LimeLight();
        //rampCamera = new RampCamera("RampCam");
        //distance = new DistanceSensor();
        //targeting = new GoalTargeting();
        //climb = new ClimbSubsystem();
        //blinkin = new Blinkin();
        //artifactCamera = new ArtifactCamera("CookieCam");

        // depending on red or blue team, set which camera gets displayed
        // on driver's station in preview mode
        //if (isRedAlliance())
        //    rampCamera.enableCameraStream();
    }

    /**Init teleop runs when you hit play*/
    public static void Init_TeleOp() {

        // robot is in teleop mode
        CurrentRobotMode = Modes.TeleOp;

        // set drivetrain default command to manual driving mode
        drivesystem.setDefaultCommand(new ManualDrive());

        // set default shooter speed control
        //shooter.setDefaultCommand(new DefaultShooterSpeed());

        //      -------------------------- Driver Controls --------------------------

        // Reset odometry
        driverOp.getGamepadButton(GamepadKeys.Button.BACK).whenPressed(new InstantCommand(()-> odometry.setCurrentPos
                (AutoFunctions.redVsBlue(new Pose2d(0.0, 0.0, new Rotation2d(Math.toRadians(-90.0)))))));

        // Climb in three seconds
        //driverOp.getGamepadButton(GamepadKeys.Button.START).whenHeld(new ClimbCommand());


        //      -------------------------- (Driver) Shooting Controls  --------------------------

        // Block/Unblock
        driverOp.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(new InstantCommand(()->shotblock.Unblock()));
        driverOp.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(new InstantCommand(()->shotblock.Block()));

        // Hood Adjust
        driverOp.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(new InstantCommand(()->hoodtilt.SetHoodPosition(hoodtilt.GetHoodPosition()-0.02)));
        driverOp.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(new InstantCommand(()->hoodtilt.SetHoodPosition(hoodtilt.GetHoodPosition()+0.02)));

        // Raise/Lower Ramp
        driverOp.getGamepadButton(GamepadKeys.Button.Y).whenPressed(new InstantCommand(()-> ramplift.Raise()));
        driverOp.getGamepadButton(GamepadKeys.Button.B).whenPressed(new InstantCommand(()-> ramplift.Lower()));

        // shooter
        //driverOp.getGamepadButton(GamepadKeys.Button.A).whenPressed(new InstantCommand(()->shooter.SetFlywheelSpeed(720.0)));
        //driverOp.getGamepadButton(GamepadKeys.Button.A).whenReleased(new InstantCommand(()->shooter.SetFlywheelSpeed(0.0)));


        //      -------------------------- (Driver) Intake Systems --------------------------

        driverOp.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenHeld(new InstantCommand(()-> intake.intakeRun()));
        driverOp.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenReleased(new InstantCommand(()-> intake.intakeStop()));

        driverOp.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenHeld(new InstantCommand(()-> intake.intakeReverse()));
        driverOp.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenReleased(new InstantCommand(()-> intake.intakeStop()));


        //      -------------------------- (Driver) Turret System --------------------------

        //driverOp.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON).whenPressed(new InstantCommand(()-> turret.moveTurret(90.0)));
        //driverOp.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON).whenPressed(new InstantCommand(()-> turret.moveTurret(-90.0)));





        // ***********************************************
        // Stuff from previous project we may/may not need
        // ***********************************************

        // Shoot Green
        //driverOp.getGamepadButton(GamepadKeys.Button.A).whenHeld(new FastShootGreen());
        //driverOp.getGamepadButton(GamepadKeys.Button.A).whenHeld(new FastShootAll());

        // Reset odometry to apriltag before shooting all according to obelisk pattern
//        driverOp.getGamepadButton(GamepadKeys.Button.B).whenHeld(new SequentialCommandGroup(
//                                                                         new ResetOdometryXYAngle(),
//                                                                        new FastShootAll() ));

        //driverOp.getGamepadButton(GamepadKeys.Button.X).whenPressed(new InstantCommand(()-> turret.moveTurret(turret.getTurretDegrees()-10)));

        //driverOp.getGamepadButton(GamepadKeys.Button.Y).whenPressed(new InstantCommand(()-> turret.moveTurret(turret.getTurretDegrees()+10)));

        // Although this would normally be a run whenHeld, as we want it to be perpertual... once on, its on.
        //driverOp.getGamepadButton(GamepadKeys.Button.X).whenPressed(new TurnTurretToTarget());

        // Hunt Mode
        //driverOp.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenHeld(new HuntModeCommand());

        //driverOp.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenReleased(new JogBackIntakeFull());

        // Manual Intake
       // driverOp.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenHeld(new IntakeCommand());

//      -------------------------- (Driver) Turning To Exact Angle --------------------------
//        // Turn To 0 degrees
//        driverOp.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(new TurnTo(AutoFunctions.redVsBlue(0.0), false, 5.0));
//
//        // Turn To 90 degrees
//        driverOp.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(new TurnTo(AutoFunctions.redVsBlue(-90.0), false, 5.0));
//
//        // Turn To 180 degrees
//        driverOp.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(new TurnTo(AutoFunctions.redVsBlue(180.0), false, 5.0));
//
//        // Turn To 270 degrees
//        driverOp.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(new TurnTo(AutoFunctions.redVsBlue(90.0), false, 5.0));
//
//      -------------------------- Operator Controls --------------------------
//        toolOp.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(new InstantCommand(()->  operatorControls.increaseRampTotal()));
//        toolOp.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(new InstantCommand(()->  operatorControls.decreaseRampTotal()));
//        toolOp.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(new InstantCommand(()-> operatorControls.resetRampTotal()));
//        toolOp.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(new InstantCommand(()-> telemetrySubsystem.telemetryModeToggle()));


        //      -------------------------- Examples --------------------------
        // bind commands to buttons
        // bind gyro reset to back button.
        // Note: since reset is very simple command, we can just use 'InstandCommand'
        // instead of creating a full command, just to run one line of java code.

        // example turn to command
        // driverOp.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenHeld((new TurnTo(AutoFunctions.redVsBlue(0.0), true, 3.0)));

        // example sequential command
        // driverOp.getGamepadButton(GamepadKeys.Button.Y).whileHeld(new ExampleCommandGroup());


        // example of binding more complex command to a button. This would be in a separate command file
        // driverOp.getGamepadButton(GamepadKeys.Button.BACK).whenPressed(new ExampleCommand());

        // add other button commands here
        // Note: can trigger commands on
        // whenPressed - once when button is pressed
        // whenHeld - runs command while button held, but does not restart if command ends
        // whileHeld - runs command while button held, but will restart command if it ends
        // whenReleased - runs once when button is released
        // togglewhenPressed - turns command on and off at each button press

        // set limelight to apriltag pipeline
        //limeLight.SetPipelineMode(0);
    }

    /**Robot initialization for auto - This runs once at initialization of auto*/
    public static void Init_Auto() {

        // robot is in auto init mode
        CurrentRobotMode = Modes.AutoInit;

        // perform any autonomous-specific initialization here
        // set limelight to obelisk pipeline
        //if (RobotContainer.isRedAlliance == false){
        //    limeLight.SetPipelineMode(1);
        //}else{
        //    limeLight.SetPipelineMode(2);
        //}
    }

    /**Robot starting code for auto - This runs once at start of auto*/
    public static void Start_Auto() {

        // robot is in auto mode
        CurrentRobotMode = Modes.Auto;

        // set limelight to apriltag pipeline
        //limeLight.SetPipelineMode(0);

        // set default shooter speed control
        //shooter.setDefaultCommand(new DefaultShooterSpeed());
    }


    /**call this function periodically to operate scheduler*/
    public static void Periodic() {

        // clear I/O cache for robot control and expansion hubs
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        // actual interval time
        intervaltime = timer.milliseconds();

        // execute robot periodic function 50 times per second (=50Hz)
        if (intervaltime>=20.0) {

            // reset timer
            timer.reset();

            // start execution timer
            exectimer.reset();

            // run scheduler
            CommandScheduler.getInstance().run();

            // report robot odometry on robot controller
            telemetrySubsystem.odometryTelemetry();

            // report time interval on robot controller
            telemetrySubsystem.timerOdometry();
            //telemetrySubsystem.addData("Turret Target (degrees): ", turret.getTurretTargetDegrees());
            //telemetrySubsystem.addData("Turret Current (degrees): ", turret.getTurretDegrees());
            //telemetrySubsystem.addData("Intake Power: ", intake.readIntakeSetPower());
            //telemetrySubsystem.addData("Intake Motor Speed (rps): ", intake.intakeMotorCurrentSpeed());

            telemetrySubsystem.update();
        }
    }


    /**Gets the current alliance colour
     * @return True if red alliance, false if blue alliance
     */
    public static boolean isRedAlliance() {
        return isRedAlliance;
    }

    /**Returns the current robot mode
     * @return a value from the Modes enum, valid Modes as of 2/9/2026: Off, AutoInit, Auto, TeleOp*/
    public static Modes GetCurrentMode() { return CurrentRobotMode; }

    /**Gets our most commonly used starting angles for auto when we're on blue alliance
     * @return The angle of the robot when it's facing the red alliance drive team*/
    public static double getBlueStartAngle() {
        return BlueStartAngle;
    }

    /**Gets our most commonly used starting angles for auto when we're on red alliance
     * @return The angle of the robot when it's facing the blue alliance drive team*/
    public static double getRedStartAngle() {
        return RedStartAngle;
    }

}
