package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;

public class Autonomous {
	private Ultrasonic frontUSonic, rearUSonic;
	private AnalogGyro gyro;
	private Encoder leftEncoder,rightEncoder;
	
	private enum sideGearSelection{
		stage1, stage2, stage3, stage4, stage5
	}
	private sideGearSelection sideStage;
	
	private enum middleGearSelection{
		stage1, stage2, stage3, stage4
	}
	private middleGearSelection middleStage;
	
	
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
	
	int currentShootingStage = 0;
	boolean timerStarted = false;
	double startingTime;
	
	//blue = false, red = true
	public void shooting(boolean color){
		switch(currentShootingStage){
			case 0:
				if(rearUSonic.getRangeInches() > temp)
				{
					Robot.rightStickY = -0.5;
					Robot.leftStickY =  -0.5;
				}
				else{
					Robot.rightStickY = 0;
					Robot.leftStickY = 0;
					currentShootingStage++;
				}
				break;
			case 1:
				if(gyro.getAngle() > temp){
					Robot.leftStickY = -0.75;
					Robot.rightStickY = -0.5;
				}
				else{
					currentShootingStage++;
				}
				break;
			case 2:
				if(rearUSonic.getRangeInches() > temp)
				{
					Robot.rightStickY = -0.3;
					Robot.leftStickY =  -0.3;
				}
				else{
					Robot.rightStickY = 0;
					Robot.leftStickY = 0;
					currentShootingStage++;
				}
				break;
			case 3:
				if(!timerStarted){
					startingTime = Timer.getFPGATimestamp();
					timerStarted = true;
				}
				if((Timer.getFPGATimestamp() - startingTime) > 3){
					currentShootingStage++;
				}
				break;
			case 4:
				//turn into boiler
				break;
			case 5:
				//ram into boiler
				break;
			case 6:
				Robot.shoot = true;
				break;
		}
	}
	
	
	public void middleGear(){
		switch(middleStage){
		default://drive forward till the robot reach the designated distance
			if(frontUSonic.getRangeInches() >= temp){
				Robot.leftStickY = temp;
				Robot.rightStickY = temp;
			}
			else{
				middleStage = middleGearSelection.stage2;
			}
			break;
		case stage2:
			//use camera tracking to line up the peg, reserved for Cool Guy
			
			//after action is done, let middleStage = middleGearSelection.stage3; to start next stage of action
			break;
		case stage3://Wriggle or drive in curve line to get the gear on
			Robot.leftStickY = temp - temp;
			Robot.rightStickY = temp + temp;
			break;
		
		}
	}
	
	public void sideGear(){
		switch(sideStage){
		default://go straight for a certain distance
			if(rearUSonic.getRangeInches() <= temp){
				Robot.leftStickY = temp;
				Robot.rightStickY = temp;
			}
			else{
				gyro.reset();
				sideStage = sideGearSelection.stage2;
			}
			break;
			
		case stage2:
			if(Robot.doRightGear){//if we choose to do right gear
				if(gyro.getAngle() < temp && gyro.getAngle() > temp){//if the robot haven't turn to the wanted angle range, keep turning left
					Robot.leftStickY = -temp;
					Robot.rightStickY = temp;
				}
				else if(gyro.getAngle() < temp && gyro.getAngle() > temp){//if the robot turn too much, turn right
					Robot.leftStickY = temp;
					Robot.rightStickY = -temp;
				}
				else if(gyro.getAngle() < temp && gyro.getAngle() > temp){//if the robot turn to the angle we wanted, proceed to next stage
					sideStage = sideGearSelection.stage3;
				}
			}
			else{	//if we choose to do left gear
				if(gyro.getAngle() < temp && gyro.getAngle() > temp){//if the robot haven't turn to the wanted angle range, keep turning right
					Robot.leftStickY = temp;
					Robot.rightStickY = -temp;
				}
				else if(gyro.getAngle() < temp && gyro.getAngle() > temp){//if the robot turn too much, turn left
					Robot.leftStickY = -temp;
					Robot.rightStickY = temp;
				}
				else if(gyro.getAngle() < temp && gyro.getAngle() > temp){//if the robot turn to the angle we wanted, proceed to next stage
					sideStage = sideGearSelection.stage3;
				}
			}
			break;
			
		case stage3:
			//use camera to line up with the peg, reserved for the Cool Guy
			
			//after this is done, use sideStage = sideGearSelection.stage4; to proceed to next stage of action
			break;
			
		case stage4://go straight toward peg till robot is close
			if(frontUSonic.getRangeInches() >= temp){
				Robot.leftStickY = temp;
				Robot.rightStickY = temp;
			}
			else{
				sideStage = sideGearSelection.stage5;
			}
			break;
		
		case stage5://Wriggle or drive in curve line to get the gear on
			Robot.leftStickY = temp - temp;
			Robot.rightStickY = temp + temp;
			break;
		}
	}
	

}
