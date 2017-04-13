package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Control {
	Joystick leftStick, rightStick;
	XboxController xboxStick;
	boolean lastInvert;
	boolean inverted = false;
	
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
		if(leftStick.getRawButton(4))
		{
			inverted = false;
		}
		else if(leftStick.getRawButton(5)) 
		{
			inverted = true;
		}
		
		if(!leftStick.getRawButton(1)) {
			Robot.shiftHigh = rightStick.getRawButton(3);
			Robot.shiftLow = rightStick.getRawButton(2);
		}
		
		Robot.enablePTO = xboxStick.getTrigger(Hand.kRight);
		if(Robot.enablePTO){
			inverted = true;
			Robot.shiftHigh = false;
			Robot.shiftLow = true;
		}
		Robot.disablePTO = xboxStick.getTrigger(Hand.kLeft);
		
		Robot.shoot = xboxStick.getAButton();
		Robot.feedBeltReverse = xboxStick.getBackButton();
		Robot.intakeOn = xboxStick.getYButton();
		Robot.intakeReverse = xboxStick.getStartButton();
		Robot.intakeStop = xboxStick.getBButton();
		Robot.autoGear = leftStick.getRawButton(1);
		Robot.spinUp = xboxStick.getBumper(Hand.kRight);
		Robot.spinOff = xboxStick.getBumper(Hand.kLeft);
		
		
		if(!leftStick.getRawButton(1))
		{
			if(!inverted) {
			Robot.leftStickY = - leftStick.getAxis(Joystick.AxisType.kY);
			Robot.rightStickY = - rightStick.getAxis(Joystick.AxisType.kY);
			}
			else {
				Robot.leftStickY =  rightStick.getAxis(Joystick.AxisType.kY);
				Robot.rightStickY =  leftStick.getAxis(Joystick.AxisType.kY);
			}
		}
	}
	  	
}