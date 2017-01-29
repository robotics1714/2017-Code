package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

import edu.wpi.first.wpilibj.AnalogGyro;

public class Autonomous {
	private Ultrasonic frontUSonic, rearUSonic;
	private AnalogGyro gyro;
	
	//Pin placeholder
	private int
		frontUSonicPin1 	= 6, //digital port 6
		frontUSonicPin2 	= 7, //digital port 7
		rearUSonicPin1 		= 8, //digital port 8
		rearUSonicPin2 		= 9, //digital port 9 
		gyroPin 			= 0; //analog port 0
	//Pin placeholder
	
	Autonomous(){
		 gyro = new AnalogGyro(gyroPin);
		 frontUSonic = new Ultrasonic(frontUSonicPin1, frontUSonicPin2);
		 rearUSonic = new Ultrasonic(rearUSonicPin1, rearUSonicPin2);
	}
	
	public void blueShooting(){
		
	}
	
	public void redShooting(){
		
	}
	
	public void leftGear(){
		
	}
	
	public void middleGear(){
		
	}
	
	public void rightGear(){
		
	}
	
	//Gyro section   
	  private void resetGyro() {
	    	gyro.reset();
	  }
	
}
