package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

public class Manipulator {
	private Talon intakeTalon, beltTalon, shootTalon;
	private DigitalInput gearDetect;
	private Encoder speedEncoder;
	
	//Pin placeholder
	private int
		intakeTalonPin,
		beltTalonPin,
		shootTalonPin,
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
		intakeTalon = new Talon(intakeTalonPin);
		beltTalon = new Talon(beltTalonPin);
		shootTalon = new Talon(shootTalonPin);
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
		shootTalon.set(shootSpeed);
		shootingStarted = true;
	}
	private void shootingStop(){
		shootTalon.set(0);
		shootingStarted = false;
	}
	public boolean shootingStarted(){
		return shootingStarted;
	}
	
	
	//Intake roller section
	private void intakeIN(){
		intakeTalon.set(intakeSpeed);
	}
	private void intakeOUT(){
		intakeTalon.set(-intakeSpeed);
	}
	private void intakeSTOP(){
		intakeTalon.set(0);
	}
	
	
	//feeding belt section
	private void beltUP(){
		beltTalon.set(beltSpeed);
	}
	private void beltDOWN(){
		beltTalon.set(-beltSpeed);
	}
	private void beltSTOP(){
		beltTalon.set(0);
	}
	
	
	
	
	
}
