package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public class Control {
	Joystick leftStick, rightStick;
	XboxController xboxStick;
	
	private DriveTrain train;
	private Manipulator manip;
	
	//Pin placeholder
	private int
		leftStickPin,
		rightStickPin,
		xboxPin;
	//Pin placeholder
	
	
	int tempPin;
	
	
	
	Control(Manipulator manipulator, DriveTrain driveTrain){
		train = driveTrain;
		manip = manipulator;
		leftStick = new Joystick(leftStickPin);
		rightStick = new Joystick(rightStickPin);
		xboxStick = new XboxController(xboxPin);
	}
	
	public void update(){
		Robot.shiftHigh = rightStick.getRawButton(3);
		Robot.shiftLow = rightStick.getRawButton(2);
		Robot.enablePTO = leftStick.getRawButton(3);
		Robot.disablePTO = leftStick.getRawButton(2);
		Robot.shoot = xboxStick.getRawButton(tempPin);
		Robot.feedBeltReverse = xboxStick.getRawButton(tempPin);
		Robot.intakeOn = xboxStick.getRawButton(tempPin);
		Robot.intakeReverse = xboxStick.getRawButton(tempPin);
		Robot.intakeStop = xboxStick.getRawButton(tempPin);
	}
	  	
}
