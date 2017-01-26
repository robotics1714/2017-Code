package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Talon;

public class Shooter {
	private Talon intakeTalon, beltTalon, shootTalon;
	
	//Pin placeholder
	private int
		intakeTalonPin,
		beltTalonPin,
		shootTalonPin;
	//Pin placeholder
	
	private double
		intakeSpeed,
		beltSpeed,
		shootSpeed;
	private boolean
		shootingStarted;

	
	Shooter(){
		intakeTalon = new Talon(intakeTalonPin);
		beltTalon = new Talon(beltTalonPin);
		shootTalon = new Talon(shootTalonPin);
		
	}
	
	public void update(boolean shoot, int intake){
		switch(intake){
			case 0:
				intakeSTOP();
				break;
			case 1:
				intakeIN();
				break;
			case 2:
				intakeOUT();
				break;
		}
		
		if(shoot){
			shootingStart();
			
		}
		else if(!shoot){
			shootingStop();
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
