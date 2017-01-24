package org.usfirst.frc.team1714.robot;

import com.ctre.CANTalon; 
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.AnalogGyro;

public class DriveTrain {
	 public CANTalon tRightFront, tRightRear, tLeftFront, tLeftRear;
	 AnalogGyro gyro;
	 private DoubleSolenoid shiftsolenoid,PTOsolenoid;
	 Encoder leftEncoder,rightEncoder;
	 private Compressor comp;
	 
	 private boolean
	 	shiftingGearHigh,
	 	shiftingGearLow,
	 	turningCompressorOn,
	 	turningCompressorOff,
	 	resettingGyro;
	 
	 private int
	 	tRightFrontPin,
	 	tRightRearPin,
	 	tLeftFrontPin,
	 	tLeftRearPin,
	 	gyroPin,
	 	shiftsolenoidPin1,
	 	shiftsolenoidPin2,
	 	PTOsolenoidPin1,
	 	PTOsolenoidPin2,
	 	pcmID;
	 
	 DriveTrain(){
		tRightFront = new CANTalon(tRightFrontPin);
	    tRightRear = new CANTalon(tRightRearPin);
	    tLeftFront = new CANTalon(tLeftFrontPin);
	    tLeftRear = new CANTalon(tLeftRearPin);
	    gyro = new AnalogGyro(gyroPin);
	    shiftsolenoid = new DoubleSolenoid(pcmID, shiftsolenoidPin1, shiftsolenoidPin2);
	    //shiftsolenoid.set(DoubleSolenoid.Value.kForward);
	    PTOsolenoid = new DoubleSolenoid(pcmID, PTOsolenoidPin1, PTOsolenoidPin2);
	    //PTOsolenoid.set(DoubleSolenoid.Value.kForward);
    	comp = new Compressor(pcmID);
    	comp.setClosedLoopControl(true);
	 }
	 
	 public void update(){
		 if (shiftingGearLow) {
	    		shiftGearLow();
	    	}
	    	else if (shiftingGearHigh) {
	    		shiftGearHigh();
	    	}
	    	
	    	if(turningCompressorOff) {
	    		turnCompressorOff();
	    	}
	    	else if(turningCompressorOn) {
	    		turnCompressorOn();
	    	}
	 }
	 //Shifting section
	 
	  void shiftGearHigh() {
	    	shiftsolenoid.set(DoubleSolenoid.Value.kForward);
	    	shiftingGearHigh = false;
	   }
	    
	  void shiftGearLow() {
			shiftsolenoid.set(DoubleSolenoid.Value.kReverse);
			shiftingGearLow = false;
	   }

	  public void setShiftGearHigh() {
	    	shiftingGearHigh = true;
	  }
	    
	  public void setShiftGearLow() {
	    	shiftingGearLow = true;
	  }
	  
	  //Gyro section
	  
	  public void setResetGyro() {
	    	resettingGyro = true;
	  }
	    
	  private void resetGyro() {
	    	gyro.reset();
			resettingGyro = false;
	  }
	  
	  //Compressor section
	  public void setCompressorOn() {
	    	turningCompressorOff = false;
	    	turningCompressorOn = true;
	  }
	    
	  public void setCompressorOff() {
	    	turningCompressorOff = true;
	    	turningCompressorOn = false;
	  }
	    
	  private void turnCompressorOn() {
	    	comp.start();
	    	turningCompressorOn = false;
	  }
	    
	  private void turnCompressorOff() {
	    	comp.stop();
	    	turningCompressorOff = false;
	  }
			 
	 	
}
