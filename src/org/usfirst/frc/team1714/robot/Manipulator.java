package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

public class Manipulator {
	private Victor intakeVictor, beltVictor, shootVictor;
	public DigitalInput gearDetect;
	public Encoder speedEncoder;
	
	//Pin placeholder
	private int
		intakeVictorPin 	= 9, //PWM port 9
		beltVictorPin 		= 8, //PWM port 8
		shootVictorPin 		= 7, //PWM port 7
		gearDetectPin 		= 11, //digital port 11
		speedEncoderPin1 	= 4, //digital port 4
		speedEncoderPin2 	= 5; //digital port 5
	//Pin placeholder
	
	private double
		intakeSpeed = 0.10,
		beltSpeed = 0.25,
		shootInitialSpeed = 0.05,
		shootSpeed,
		shootSpeedIncrement 	= 0.025, 
		shootSpeedBuffer 		= 0.05,
		expectedEncoderRate,//the ideal speed of the shooting wheel in terms of the encoder rate
	 	encoderRate;
	
	private boolean//boolean used to set up priority system which make sure feeding wheel and intake wheel won't run in wrong direction when shooting.
		shootingStarted,
		intakeIsOn,
		intakeIsStop,
		intakeIsReverse;
	
	
	Manipulator(){
		intakeVictor = new Victor(intakeVictorPin);
		beltVictor = new Victor(beltVictorPin);
		shootVictor = new Victor(shootVictorPin);
		gearDetect = new DigitalInput(gearDetectPin);
		speedEncoder = new Encoder(speedEncoderPin1,speedEncoderPin2);
		
		shootSpeed = shootInitialSpeed;//have initial speed so that the shooting wheel don't have to speed up from zero
	}
	
	public void update(
				boolean shoot, 
				boolean intakeOn, 
				boolean intakeStop, 
				boolean intakeReverse, 
				boolean feedBeltReverse){

		recordEncoderRate();//in every period this should be run last to record the rate of shooting wheel in the end of each period
		//resetSpeedEncoder();//in every period this should be run first to reset the encoder for shooting wheel
		
		//shooting section
		if(shoot){
			shootingStart();
			intakeIN();
			beltUP();
		}
		else{
			shootingStop();
			intakeSTOP();
			beltSTOP();
		}
		
		//intake section
		if(intakeOn && !intakeStop && !intakeReverse && !shootingStarted){
			if(!intakeIsOn){
				intakeIsOn = true;
				intakeIsStop = false;
				intakeIsReverse = false;
			}
			else{
				if(!shootingStarted){
					//If the robot is not shooting, the intake running normally and feed belt run reverse.
					//if the robot is shooting, the checking of shooting will let the intake run in normal direction
					//and the beilt run in normal direction, so there is no need for method call when the button is 
					//pressed while the robot is shooting.
					//
					intakeIN();
					beltDOWN();
				}	
			}
			/*If the robot is not shooting, the intake running normally and feed belt run reverse.
			*if the robot is shooting, the checking of shooting will let the intake run in normal direction
			*and the belt run in normal direction, so there is no need for method call when the button is 
			*pressed while the robot is shooting.
			*/
		}
		else if(intakeStop && !intakeOn && !intakeReverse && !shootingStarted){
			if(!intakeIsStop){
				intakeIsStop = true;
				intakeIsOn = false;
				intakeIsReverse = false;
			}
		}
		else if(intakeReverse && !intakeOn && !intakeStop && !shootingStarted){
			if(!intakeIsReverse){
				intakeIsReverse = true;
				intakeIsOn = false;
				intakeIsStop = false;
			}
		}
		else if(!intakeReverse && !intakeOn && !intakeStop && !shootingStarted){
			if(intakeIsOn){
				intakeIN();
				beltDOWN();
			}
			else if(intakeIsReverse){
				intakeOUT();
			}
			else if(intakeIsStop){
				intakeSTOP();
			}
		}
		
		//feeding belt section
		if(feedBeltReverse){
			if(!shootingStarted){
				beltDOWN();
			}
		}
			
	}
	
	//shooting section
	private void shootingStart(){
		if(encoderRate < (expectedEncoderRate - shootSpeedBuffer)){
			shootSpeed = (shootVictor.get() + shootSpeedIncrement);
			//if the speed is slower than the speed we want, increase the speed till the speed is within buffer zone
		}
		else if(encoderRate > (expectedEncoderRate + shootSpeedBuffer)){
			shootSpeed = (shootVictor.get() - shootSpeedIncrement);
			//if the speed is faster than the speed we want, decrease the speed till the speed is within buffer zone
		}
		shootVictor.set(shootSpeed);
		shootingStarted = true;
	}
	private void shootingStop(){
		shootVictor.set(0);
		shootingStarted = false;
	}
	public boolean shootingStarted(){
		return shootingStarted;
	}
	public void resetSpeedEncoder(){
		speedEncoder.reset();
	}
	public void recordEncoderRate(){
		encoderRate = speedEncoder.getRate();
	}
	
	
	//Intake roller section
	private void intakeIN(){
		intakeVictor.set(intakeSpeed);
	}
	private void intakeOUT(){
		intakeVictor.set(-intakeSpeed);
	}
	private void intakeSTOP(){
		intakeVictor.set(0);
	}
	
	
	//feeding belt section
	private void beltUP(){
		beltVictor.set(-beltSpeed);
	}
	private void beltDOWN(){
		beltVictor.set(beltSpeed);
	}
	private void beltSTOP(){
		beltVictor.set(0);
	}
	
	
	//coin slot gear detection
	public boolean gearDetection(){
		return gearDetect.get();
	}
	
	
	
}
