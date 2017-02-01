package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;

public class Autonomous {
	private Ultrasonic frontUSonic, rearUSonic;
	private AnalogGyro gyro;
	private Encoder leftEncoder,rightEncoder;
	
	private enum leftGearSelection{
		stage1, stage2, stage3, stage4
	}
	private leftGearSelection lGearSelect;
	private enum middleGearSelection{
		stage1, stage2, stage3, stage4
	}
	private middleGearSelection mGearSelect;
	private enum rightGearSelection{
		stage1, stage2, stage3, stage4
	}
	private rightGearSelection rGearSelect;
	
	
	//Pin placeholder
	private int
		frontUSonicPin1 	= 6, //digital port 6
		frontUSonicPin2 	= 7, //digital port 7
		rearUSonicPin1 		= 8, //digital port 8
		rearUSonicPin2 		= 9, //digital port 9 
		leftEncoderPin1 	= 2, //digital port 2 
		leftEncoderPin2 	= 3, //digital port 3
		rightEncoderPin1 	= 0, //digital port 0
		rightEncoderPin2 	= 1, //digital port 1
		gyroPin 			= 0; //analog port 0
	//Pin placeholder
	
	double temp;
	
	Autonomous(){
		 gyro = new AnalogGyro(gyroPin);
		 frontUSonic = new Ultrasonic(frontUSonicPin1, frontUSonicPin2);
		 rearUSonic = new Ultrasonic(rearUSonicPin1, rearUSonicPin2);
		 leftEncoder = new Encoder(leftEncoderPin1,leftEncoderPin2);
		 rightEncoder = new Encoder(rightEncoderPin1, rightEncoderPin2);
	}
	
	
	public void blueShooting(){
		
	}
	
	public void redShooting(){
		
	}
	
	public void leftGear(){
		
	}
	
	public void middleGear(){
		switch(mGearSelect){
		case stage1:
			if(frontUSonic.getRangeInches())
		}
	}
	
	public void rightGear(){
		
	}
	
	//Gyro section   
	  private void resetGyro() {
	    	gyro.reset();
	  }
	
}
