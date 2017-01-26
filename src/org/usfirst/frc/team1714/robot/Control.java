package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Control {
	Joystick leftStick, rightStick, xboxStick;
	
	private DriveTrain train;
	private Shooter shooter;
	private Climbing climb;
	private CoinSlot gear;
	
	//Pin placeholder
	private int
		leftStickPin,
		rightStickPin,
		xboxPin;
	//Pin placeholder
	
	
	
	Control(Shooter fuelShooter, Climbing climber, CoinSlot coinSlot, DriveTrain driveTrain){
		train = driveTrain;
		shooter = fuelShooter;
		climb = climber;
		gear = coinSlot;
		
		leftStick = new Joystick(leftStickPin);
		rightStick = new Joystick(rightStickPin);
		xboxStick = new Joystick(xboxPin);
	}
	
	public void update(){
		Robot.shiftHigh = rightStick.getRawButton(3);
		Robot.shiftLow = rightStick.getRawButton(2);
		Robot.enablePTO = leftStick.getRawButton(3);
		Robot.disablePTO = leftStick.getRawButton(2);
	}
	  
	
	
}

/*
void update()
{
	int button1 = 0;
	Controls.update(button1);
	DriveTrain.update(button1);
}
*/