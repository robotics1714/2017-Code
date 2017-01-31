package org.usfirst.frc.team1714.robot;

import com.ctre.CANTalon; 
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class DriveTrain {
	 public CANTalon tRightFront, tRightRear, tLeftFront, tLeftRear;
	 
	 private DoubleSolenoid shiftsolenoid,PTOsolenoid;
	 private Compressor comp;
	 private DriverStation dStation;
	 private RobotDrive drive;
	 
	 
	 
	 private boolean
	 	PTOenabled,
	 	PTOIsEnable,
	 	PTOIsDisable,
	 	gearShiftedLow,
	 	gearShiftedHigh,
	 	compressorIsOn,
	 	compressorIsOff;
	 
	 //Pin placeholder
	 private int
	 	tRightFrontPin,
	 	tRightRearPin,
	 	tLeftFrontPin,
	 	tLeftRearPin,
	 	shiftsolenoidPin1,
	 	shiftsolenoidPin2,
	 	PTOsolenoidPin1,
	 	PTOsolenoidPin2,
	 	pcmID;
	 //Pin placeholder
	 
	 DriveTrain(){
		tRightFront = new CANTalon(tRightFrontPin);
	    tRightRear = new CANTalon(tRightRearPin);
	    tLeftFront = new CANTalon(tLeftFrontPin);
	    tLeftRear = new CANTalon(tLeftRearPin);
	    
	   
	    
	    shiftsolenoid = new DoubleSolenoid(pcmID, shiftsolenoidPin1, shiftsolenoidPin2);
	    shiftsolenoid.set(DoubleSolenoid.Value.kForward);
	    PTOsolenoid = new DoubleSolenoid(pcmID, PTOsolenoidPin1, PTOsolenoidPin2);
	    PTOsolenoid.set(DoubleSolenoid.Value.kForward);
    	comp = new Compressor(pcmID);
    	comp.setClosedLoopControl(true);
    	dStation = DriverStation.getInstance();
    	drive = new RobotDrive(tLeftFront, tLeftRear, tRightFront, tRightRear);
    	
    	
	 }
	 
	 public void update(
			 	boolean enablePTO,
			 	boolean disablePTO, 
			 	boolean shiftHigh, 
			 	boolean shiftLow, 
			 	boolean startCompressor, 
			 	boolean stopCompressor,
			 	double leftStickY,
			 	double rightStickY){
		 //driving section
		 if(!PTOenabled){
			 drive.tankDrive(Robot.leftStickY, Robot.rightStickY);
		 }
		 else{
			 if((leftStickY > 0 && rightStickY > 0) || (leftStickY < 0 && rightStickY < 0)){//if both joystick is either in positive or negative direction
				 if(leftStickY > rightStickY){
					 drive.tankDrive(leftStickY, leftStickY);//if left joystick Y value is bigger, use left joystick Y value for both joystick
				 }
				 else if(leftStickY < rightStickY){
					 drive.tankDrive(rightStickY, rightStickY);//if right joystick Y value is bigger, use right joystick Y value for both joystick
				 }
			 }
		 }
		 
		 //PTO section
		 if(enablePTO && !disablePTO){
			 if(!PTOIsEnable){
				 PTOIsEnable = true;
				 PTOIsDisable = false;
			 }
			 else{
				 PTOenable();
			 }
		 }
		 else if(disablePTO && !enablePTO){
			 if(!PTOIsDisable){
				 PTOIsDisable = true;
				 PTOIsEnable = false;
			 }
			 else{
				 PTOdisable();
			 }
		 }
		 
		 //shifting section
		 if (shiftLow && !shiftHigh) {
			 	if(!gearShiftedLow){
			 		gearShiftedLow = true;
			 		gearShiftedHigh = false;
			 	}
			 	else{
			 		shiftGearLow();
			 	}
			 
	    	}
	    	else if (shiftHigh && !shiftLow) {
	    		if(!gearShiftedHigh){
	    			gearShiftedHigh = true;
	    			gearShiftedLow = false;
	    		}
	    		else{
	    			shiftGearHigh();
	    		}
	    	}
	    	
		 //compressor section
	    	if(stopCompressor && !startCompressor) {
	    		if(!compressorIsOff){
	    			compressorIsOff = true;
	    			compressorIsOn = false;
	    		}
	    		else{
	    			turnCompressorOff();
	    		}
	    	}
	    	else if(startCompressor && !stopCompressor) {
	    		if(!compressorIsOn){
	    			compressorIsOn = true;
	    			compressorIsOff = false;
	    		}
	    		else{
	    			turnCompressorOn();
	    		}
	    	}
	 }
	 //Power Take Off section
	 private void PTOenable(){
		 if(dStation.getMatchTime() <= 30 && PTOsolenoid.get() == DoubleSolenoid.Value.kForward){//only enable PTO in the last 30s of the match
			 PTOsolenoid.set(DoubleSolenoid.Value.kReverse);
			 PTOenabled = true;
		 }
	 }
	 private void PTOdisable(){
		 PTOsolenoid.set(DoubleSolenoid.Value.kForward);
		 PTOenabled = false;
	 }
	 public boolean PTOenabled(){
		 return PTOenabled;
	 }
	 public boolean IsPTOenable(){
		 return PTOIsEnable;
	 }
	 
	 
	 //Shifting section
	  void shiftGearHigh() {
	    	shiftsolenoid.set(DoubleSolenoid.Value.kForward);
	  }
	  void shiftGearLow() {
			shiftsolenoid.set(DoubleSolenoid.Value.kReverse);
	  }
	  public boolean IsInHighGear(){
		  return gearShiftedHigh;
	  }
	  
	  
	  //Compressor section    
	  private void turnCompressorOn() {
	    	comp.start();
	  }	    
	  private void turnCompressorOff() {
	    	comp.stop();
	  }
			 
	 	
}
