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
		shootingStarted,
		startShooting;
	
	public enum intakeDirection{
		IN, OUT, STOP
	}
	public enum beltDirection{
		UP, DOWN, STOP
	}
	private intakeDirection IntakeDirection;
	private beltDirection BeltDirection;
	
	
	Shooter(){
		intakeTalon = new Talon(intakeTalonPin);
		beltTalon = new Talon(beltTalonPin);
		shootTalon = new Talon(shootTalonPin);
		
	}
	
	public void update(){
		
		if(startShooting==true){
			shootingStart();
		}
		else{
			shootingStop();
		}
		
		switch(IntakeDirection){
		case IN:
			intakeIN();
			break;
		case OUT:
			intakeOUT();
			break;
		case STOP:
			intakeSTOP();
			break;
		}
		
		switch(BeltDirection){
		case UP:
			beltUP();
			break;
		case DOWN:
			beltDOWN();
			break;
		case STOP:
			beltSTOP();
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
	public void setShootingStart(){
		startShooting = true;
	}
	public void setShootingStop(){
		startShooting = false;
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
	public void setIntakeIN(){
		IntakeDirection = intakeDirection.IN;
	}
	public void setIntakeOUT(){
		IntakeDirection = intakeDirection.OUT;
	}
	public void setIntakeSTOP(){
		IntakeDirection = intakeDirection.STOP;
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
	public void setBeltUP(){
		BeltDirection = beltDirection.UP;
	}
	public void setBeltDOWN(){
		BeltDirection = beltDirection.DOWN;
	}
	public void setBeltSTOP(){
		BeltDirection = beltDirection.STOP;
	}
	
	
	
	
	
	
}
