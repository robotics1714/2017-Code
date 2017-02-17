package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public class Control {
	Joystick leftStick, rightStick;
	XboxController xboxStick;
	
	//Pin placeholder
	private int
		leftStickPin 	= 1, //USB device number 0
		rightStickPin 	= 0, //USB device number 1
		xboxPin			= 2; //USB device number 2
	//Pin placeholder
	
	//int tempPin;
	
	
	
	Control(){
		leftStick = new Joystick(leftStickPin);
		rightStick = new Joystick(rightStickPin);
		xboxStick = new XboxController(xboxPin);
	}
	
	public void update(){//assign the value of the variable based on the input of the driver station
		Robot.shiftHigh = rightStick.getRawButton(3);
		Robot.shiftLow = rightStick.getRawButton(2);
		Robot.enablePTO = leftStick.getRawButton(3);
		Robot.disablePTO = leftStick.getRawButton(2);
		Robot.startCompressor = leftStick.getRawButton(9);
		Robot.stopCompressor = leftStick.getRawButton(8);
		
		Robot.shoot = xboxStick.getAButton();
		Robot.feedBeltReverse = xboxStick.getBackButton();
		Robot.intakeOn = xboxStick.getYButton();
		Robot.intakeReverse = xboxStick.getStartButton();
		Robot.intakeStop = xboxStick.getBButton();
		
		Robot.leftStickY = - leftStick.getAxis(Joystick.AxisType.kY);
		Robot.rightStickY = - rightStick.getAxis(Joystick.AxisType.kY);
	}
	  	
}