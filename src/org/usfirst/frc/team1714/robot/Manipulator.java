package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
		intakeSpeed = 0.75,
		beltSpeed = 0.7,
		shootInitialSpeed = 0.82,
		shootSpeed,
		shootSpeedIncrement 	= 0.0025, 
		shootSpeedBuffer 		= 0.02,
		expectedEncoderRate = 144000,//changing from 138500 to 144000 on 2017-02-21 Karl ||  old: RIVEST CHANGED FROM 45000the ideal speed of the shooting wheel in terms of the encoder rate
	 	encoderRate;
	private int state = 0;
	private boolean spinningUp = false;
	// STATES:
	// 0 = NOTHING
	// 1 = SHOOTING
	// 2 = SUCKING
	// 3 = INTAKE REVERSE
	// 4 = FEED BELT REVERSE
	
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
				boolean feedBeltReverse,
				boolean spinUp,
				boolean spinOff				){
	
		SmartDashboard.putNumber("Shooting wheel power:", shootVictor.get());
		recordEncoderRate();//in every period this should be run last to record the rate of shooting wheel in the end of each period
		//resetSpeedEncoder();//in every period this should be run first to reset the encoder for shooting wheel
		
		//states
		if(shoot){
			state = 1;
		}
		else if(intakeOn){
			state = 2;
		}
		else if(intakeReverse){
			state = 3;
		}
		else if(feedBeltReverse){
			state = 4;
		}
		else
		{
			if(state == 1 || state == 3 || state == 4 || intakeStop)
			{
				state = 0;
			}
		}
		
		if(spinOff){
			spinningUp = false;
		}
		else if(spinUp){
			spinningUp = true;
		}

		switch(state){
			case 0:
			{
				shootingSTOP();
				intakeSTOP();
				beltSTOP();
				break;
			}
			case 1:
			{
				spinningUp = true;
				//if(speedEncoder.getRate() > 132000)
				//{
					beltUP();
					intakeIN();
				//}
				break;
			}
			case 2:
			{
				shootingSTOP();
				intakeIN();
				beltDOWN();
				break;
			}
			case 3:
			{
				shootingSTOP();
				intakeOUT();
				beltUP();
				break;
			}
			case 4:
			{
				shootingSTOP();
				intakeSTOP();
				beltDOWN();
				break;
			}
		}
		
		if(spinningUp) {
			shootingSTART();
		}
		else {
			shootingSTOP();
		}
	}
	
	//shooting section
	private void shootingSTART(){
		if(encoderRate < (expectedEncoderRate - shootSpeedBuffer)){
			shootSpeed = (shootVictor.get() + shootSpeedIncrement);
			//if the speed is slower than the speed we want, increase the speed till the speed is within buffer zone
		}
		else if(encoderRate > (expectedEncoderRate + shootSpeedBuffer)){
			shootSpeed = (shootVictor.get() - shootSpeedIncrement);
			//if the speed is faster than the speed we want, decrease the speed till the speed is within buffer zone
		}
		if(shootVictor.get() < 0.8)
		{
			shootVictor.set(shootInitialSpeed);
		}
		else{
		shootVictor.set(0.95); //RIVEST HARDCODE MOTOR SPEED
		//shootVictor.set(shootSpeed); RIVEST REMOVED TO HARDCODE MOTOR SPEED
		}
	}
	private void shootingSTOP(){
		shootVictor.set(0);
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
		return !gearDetect.get();
	}
	
	
	
}
