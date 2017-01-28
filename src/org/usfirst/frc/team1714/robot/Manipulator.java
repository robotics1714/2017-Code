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
		shootSpeed;
	private boolean
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
		
	}
	
	public void update(
				boolean shoot, 
				boolean intakeOn, 
				boolean intakeStop, 
				boolean intakeReverse, 
				boolean feedBeltReverse){
		
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
		
		if(feedBeltReverse){
			if(!shootingStarted){
				beltDOWN();
			}
		}
			
	}
	
	//shooting section
	private void shootingStart(){
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
