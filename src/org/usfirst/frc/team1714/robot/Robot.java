package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.cscore.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String bsAuto = "Blue Shooting";
	final String rsAuto = "Red Shooting";
	final String lgAuto = "Left Gear";
	final String mgAuto = "Middle Gear";
	final String rgAuto = "Right Gear";
	
	String autoSelected;
	SendableChooser<String> autoChooser;
	
	Servo cameraServo;
	UsbCamera camera;
	
	DriveTrain train;
	Manipulator manipulator;
	Control control;
	Autonomous auto;
	
	//Below are the variable that represent driver control input
	public static boolean 
		startCompressor,
		stopCompressor,
		shiftHigh,
		shiftLow,
		shoot,
		enablePTO,
		disablePTO,
		intakeOn,
		intakeStop,
		intakeReverse,
		feedBeltReverse;
	public static double
		leftStickY,
		rightStickY;
	//Above are the variable that represent driver control input
	
	public static boolean doRightGear;
	
	private int
		cameraServoPin = 6; //PWM port 6
		
		
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		autoChooser = new SendableChooser<>();
		autoChooser.addObject(bsAuto, bsAuto);
		autoChooser.addObject(rsAuto, rsAuto);
		autoChooser.addObject(rgAuto, rgAuto);
		autoChooser.addObject(mgAuto, mgAuto);
		autoChooser.addObject(lgAuto, lgAuto);
		SmartDashboard.putData("Autonomous: ", autoChooser);
		
		cameraServo = new Servo(cameraServoPin);
		
		// camera setup + settings
		camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setVideoMode(VideoMode.PixelFormat.kMJPEG, 320, 240, 30);
		camera.setExposureManual(5);
		camera.setExposureHoldCurrent();
		camera.setWhiteBalanceManual(4500);
		camera.setWhiteBalanceHoldCurrent();
		
		train = new DriveTrain();
		manipulator = new Manipulator();
		control = new Control();
		auto = new Autonomous();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case bsAuto:
			auto.shooting(false);
			break;
		case rsAuto:
			auto.shooting(true);
			break;
		case lgAuto:
			doRightGear = false;
			auto.sideGear();
			break;
		case mgAuto:
			auto.middleGear();
			break;
		case rgAuto:
			doRightGear = true;
			auto.sideGear();
			break;
		}
		train.update(enablePTO, disablePTO, shiftHigh, shiftLow, startCompressor, stopCompressor, leftStickY, rightStickY);
		manipulator.update(shoot, intakeOn, intakeStop, intakeReverse, feedBeltReverse);
	}

	/**
	 * This function is called periodically during operator control
	 */
	
	//DEBUG VARS REMOVE LATER
	boolean lastCycle10 = false;
	boolean lastCycle11 = false;
	
	@Override
	public void teleopPeriodic() {		
		//SmartDashboard section
		SmartDashboard.putBoolean("Robot in high gear?", train.IsInHighGear());
		SmartDashboard.putBoolean("Is PTO enabled?", train.IsPTOenable());
		SmartDashboard.putBoolean("Do we have gear?", manipulator.gearDetection());
		SmartDashboard.putNumber("Gyro angle",auto.gyro.getAngle());
		SmartDashboard.putNumber("Rear Ultrasonic reading", auto.gearUSonic.getRangeInches());
		SmartDashboard.putNumber("Front Ultrasonic reading", auto.intakeUSonic.getRangeInches());
		SmartDashboard.putNumber("Shooting wheel encoder value", manipulator.speedEncoder.get());
		SmartDashboard.putNumber("Left drive train encoder value", auto.leftEncoder.get());
		SmartDashboard.putNumber("Right drive train encoder value", auto.rightEncoder.get());
		SmartDashboard.putNumber("Servo value", cameraServo.get());
		SmartDashboard.putBoolean("button:", control.leftStick.getRawButton(10));
		SmartDashboard.putBoolean("camerathing", lastCycle10);
		SmartDashboard.putNumber("servovalue", cameraServo.get());
		
		// DEBUG CODE REMOVE LATER
		if(control.leftStick.getRawButton(10) && !lastCycle10)
		{
			cameraServo.set((cameraServo.get()+0.05));
		}
		lastCycle10 = control.leftStick.getRawButton(10);
		if(control.leftStick.getRawButton(11) && !lastCycle11)
		{
			cameraServo.set((cameraServo.get()+0.05));
		}
		lastCycle11 = control.leftStick.getRawButton(11);
		
		
		control.update();
		train.update(enablePTO, disablePTO, shiftHigh, shiftLow, startCompressor, stopCompressor,leftStickY, rightStickY);
		manipulator.update(shoot, intakeOn, intakeStop, intakeReverse, feedBeltReverse);
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}

