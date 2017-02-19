package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.cscore.*;
import org.opencv.core.*;
import org.opencv.imgproc.*;
import edu.wpi.first.wpilibj.CameraServer;


public class Autonomous {
	public Ultrasonic intakeUSonic, gearUSonic;
	public AnalogGyro gyro;
	public Encoder leftEncoder,rightEncoder;
	Mat pic;
	CvSink sink;
	CvSource rectOut;
	GripPipelineTape pipe;
	
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
		intakeUSonicPin1 	= 6, //digital port 6
		intakeUSonicPin2 	= 7, //digital port 7
		gearUSonicPin1 		= 8, //digital port 8
		gearUSonicPin2 		= 9, //digital port 9 
		rightEncoderPin1 	= 2, //digital port 2 
		rightEncoderPin2 	= 3, //digital port 3
		leftEncoderPin1 	= 0, //digital port 0
		leftEncoderPin2 	= 1, //digital port 1
		gyroPin 			= 0; //analog port 0
	//Pin placeholder
	
	double temp;
	
	Autonomous(UsbCamera cam){
		 gyro = new AnalogGyro(gyroPin);
		 intakeUSonic = new Ultrasonic(intakeUSonicPin1, intakeUSonicPin2);
		 gearUSonic = new Ultrasonic(gearUSonicPin1, gearUSonicPin2);
		 leftEncoder = new Encoder(rightEncoderPin1,rightEncoderPin2);
		 rightEncoder = new Encoder(leftEncoderPin1, leftEncoderPin2);
		 pic = new Mat();
		 sink = CameraServer.getInstance().getVideo();
		 pipe = new GripPipelineTape();
		 middleStage = middleGearSelection.stage1;
		 rectOut = CameraServer.getInstance().putVideo("Rectangle", 320, 240);
	}
	
	int currentShootingStage = 0;
	boolean timerStarted = false;
	double startingTime;
	
	//blue = false, red = true
	public void shooting(boolean red){
		switch(currentShootingStage){
			case 0:
				if(gearUSonic.getRangeInches() > 80)
				{
					Robot.rightStickY = -0.5;
					Robot.leftStickY =  -0.5;
				}
				else{
					Robot.rightStickY = 0;
					Robot.leftStickY = 0;
					//currentShootingStage++;
				}
				break;
			case 1:
				if(gyro.getAngle() > temp){
					if(!red) {
						Robot.leftStickY = -0.75;
						Robot.rightStickY = -0.5;
					}
					else{
						Robot.leftStickY = -0.5;
						Robot.rightStickY = -0.75;
					}
				}
				else{
					currentShootingStage++;
				}
				break;
			case 2:
				if(gearUSonic.getRangeInches() > 20)
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
				if(gyro.getAngle() < temp) {
					if(!red){
						Robot.leftStickY = 0.8;
						Robot.rightStickY = 0.5;
					}
					else
					{
						Robot.leftStickY = 0.5;
						Robot.rightStickY = 0.8;
					}
				}
				break;
			case 5:
				if(gearUSonic.getRangeInches() > 5)
				{
					Robot.leftStickY = 0.6;
					Robot.rightStickY = 0.6;
				}
				break;
			case 6:
				Robot.shoot = true;
				break;
		}
	}
	
	int centerX;
	public void middleGear(){
		sink.grabFrame(pic);
		pipe.process(pic);
		
		switch(middleStage){
		default:
		case stage1://drive forward till the robot reach the designated distance
			/*if(intakeUSonic.getRangeInches() >= temp){
				Robot.leftStickY = temp;
				Robot.rightStickY = temp;
			}
			else{
				middleStage = middleGearSelection.stage2;
			}*/
			middleStage = middleGearSelection.stage2;
			break;
		case stage2:
			//use camera tracking to line up the peg, reserved for Cool Guy
			if(!pipe.filterContoursOutput().isEmpty())
			{
				//int leftX, leftY, leftW, leftH;
				//int rightX, rightY, rightW, rightH;
				int xFinal, yFinal, wFinal, hFinal;
				Rect rFinal;
				System.out.println("pipin");
				Rect r = Imgproc.boundingRect(pipe.filterContoursOutput().get(0));
				if(pipe.filterContoursOutput().size() > 1) {
					Rect r2 = Imgproc.boundingRect(pipe.filterContoursOutput().get(1));
					
					if(r.y < r2.y) {
						yFinal = r.y;
						hFinal = ((r2.y + r2.height) - r.y);
					}
					else {
						yFinal = r2.y;
						hFinal = ((r.y + r.height) - r2.y);
					}
					if(r.x < r2.x) {
						xFinal = r.x;
						wFinal = ((r2.x + r2.width) - r.x);
					}
					else {
						xFinal = r2.x;
						wFinal = ((r.x + r.width) - r2.x);
					}
					
					rFinal = new Rect(xFinal, yFinal, wFinal, hFinal);
					/*
					if(r.x < r2.x){
						leftX = r.x;
						leftY = r.y;
						leftW = r.width;
						leftH = r.height;
						rightX = r2.x;
						rightY = r2.y;
						rightW = r2.width;
						rightH = r2.height;
					}
					else{
						leftX = r2.x;
						leftY = r2.y;
						leftW = r2.width;
						leftH = r2.height;
						rightX = r.x;
						rightY = r.y;
						rightW = r.width;
						rightH = r.height;
					}
					rFinal = new Rect(leftX, leftY, ((rightX + rightW) - leftX), (rightY + rightH) - leftY);
					*/
					centerX = rFinal.x + (rFinal.width/2);
					Imgproc.rectangle(pic, new Point(rFinal.x, rFinal.y), new Point((rFinal.x + rFinal.width), (rFinal.y + rFinal.height)),
							new Scalar(255, 255, 255), 2);
				}
				else
				{
					centerX = r.x + (r.width / 2);
					Imgproc.rectangle(pic, new Point(r.x, r.y), new Point((r.x + r.width), (r.y + r.height)),
							new Scalar(255, 255, 255), 5);
				}
				
				// Give the output stream a new image to display
				rectOut.putFrame(pic);
				
				if(centerX > 195){
					System.out.println("right");
					Robot.leftStickY = -0.75;
					Robot.rightStickY = -0.85;
				}
				else if(centerX < 185){
					System.out.println("left");
					Robot.leftStickY = -0.85;
					Robot.rightStickY = -0.75;
				}
				else
				{
					System.out.println("straight ahead");
					Robot.leftStickY = -0.75;
					Robot.rightStickY = -0.75;
				}
			}
			else
			{
				System.out.println("it's 0 time");
				Robot.leftStickY = 0;
				Robot.rightStickY = 0;
			}
			if(gearUSonic.getRangeInches() < 35)
			{
				middleStage = middleGearSelection.stage3;
			}
			//after action is done, let middleStage = middleGearSelection.stage3; to start next stage of action
			break;
		case stage3://Wriggle or drive in curve line to get the gear on
			//Robot.leftStickY = temp - temp;
			//Robot.rightStickY = temp + temp;
			if(gearUSonic.getRangeInches() > 6)
			{
				Robot.leftStickY = -0.55;
				Robot.rightStickY = -0.55;
			}
			else
			{
				Robot.leftStickY = 0;
				Robot.rightStickY = 0;
			}
			break;
		
		}
	}
	
	public void sideGear(){
		switch(sideStage){
		default://go straight for a certain distance
			if(gearUSonic.getRangeInches() <= temp){
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
			if(intakeUSonic.getRangeInches() >= temp){
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
