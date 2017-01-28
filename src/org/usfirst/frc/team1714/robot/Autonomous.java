package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

import edu.wpi.first.wpilibj.AnalogGyro;

public class Autonomous {
	private Ultrasonic frontUSonic, rearUSonic;
	private AnalogGyro gyro;
	
	//Pin placeholder
	private int
		frontUSonicPin1,
		frontUSonicPin2,
		rearUSonicPin1,
		rearUSonicPin2,
		gyroPin;
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
