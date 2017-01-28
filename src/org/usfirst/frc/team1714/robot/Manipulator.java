package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

public class Manipulator {
	private Victor intakeVictor, beltVictor, shootVictor;
	private DigitalInput gearDetect;
	private Encoder speedEncoder;
	
	//Pin placeholder
	private int
		intakeVictorPin,
		beltVictorPin,
		shootVictorPin,
		gearDetectPin,
		speedEncoderPin1,
		speedEncoderPin2;
	//Pin placeholder
	
	private double
		intakeSpeed,
		beltSpeed,
		shootInitialSpeed,
		shootSpeed,
		shootSpeedIncrement = 0.025, 
		shootSpeedBuffer = 0.05,
		expectedEncoderRate;//the ideal speed of the shooting wheel in terms of the encoder rate
	
	private boolean//boolean used to set up priority system which make sure feeding wheel and intake wheel won't run in wrong direction when shooting.
		shootingStarted,
		intakeIsOn,
		intakeIsStop,
		intakeIsReverse;
	
	public static double
		EncoderRate;

	
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
		if(intakeOn && !intakeStop && !intakeReverse){
			if(!intakeIsOn){
				intakeIsOn = true;
				intakeIsStop = false;
				intakeIsReverse = false;
			}
			else{
				if(!shootingStarted){
					/*If the robot is not shooting, the intake running normally and feed belt run reverse.
					*if the robot is shooting, the checking of shooting will let the intake run in normal direction
					*and the beilt run in normal direction, so there is no need for method call when the button is 
					*pressed while the robot is shooting.
					*/
					intakeIN();
					beltDOWN();
				}	
			}
		}
		else if(intakeStop && !intakeOn && !intakeReverse){
			if(!intakeIsStop){
				intakeIsStop = true;
				intakeIsOn = false;
				intakeIsReverse = false;
			}
			else{
				if(!shootingStarted){
					//You can only manually stop the intake when the robot is not shooting.
					intakeSTOP();
				}
			}
		}
		else if(intakeReverse && !intakeOn && !intakeStop){
			if(!intakeIsReverse){
				intakeIsReverse = true;
				intakeIsOn = false;
				intakeIsStop = false;
			}
			else{
				if(!shootingStarted){
					//You can only manually run the intake reverse when the robot is not shooting.
					intakeOUT();
				}
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
		if(EncoderRate < (expectedEncoderRate - shootSpeedBuffer)){
			shootSpeed = (shootVictor.getSpeed() + shootSpeedIncrement);
			//if the speed is slower than the speed we want, increase the speed till the speed is within buffer zone
		}
		else if(EncoderRate > (expectedEncoderRate + shootSpeedBuffer)){
			shootSpeed = (shootVictor.getSpeed() - shootSpeedIncrement);
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
		EncoderRate = speedEncoder.getRate();
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
		beltVictor.set(beltSpeed);
	}
	private void beltDOWN(){
		beltVictor.set(-beltSpeed);
	}
	private void beltSTOP(){
		beltVictor.set(0);
	}
	
	
	//coin slot gear detection
	public boolean gearDetection(){
		return gearDetect.get();
	}
	
	
	
}
