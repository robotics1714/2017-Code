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
	 Encoder leftEncoder,rightEncoder;
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
	 	pcmID,
	 	leftEncoderPin1 	= 2, //digital port 2 
	 	leftEncoderPin2 	= 3, //digital port 3
	 	rightEncoderPin1 	= 0, //digital port 0
	 	rightEncoderPin2 	= 1; //digital port 1
	 //Pin placeholder
	 
	 DriveTrain(){
		tRightFront = new CANTalon(tRightFrontPin);
	    tRightRear = new CANTalon(tRightRearPin);
	    tLeftFront = new CANTalon(tLeftFrontPin);
	    tLeftRear = new CANTalon(tLeftRearPin);
	    leftEncoder = new Encoder(leftEncoderPin1,leftEncoderPin2);
	    rightEncoder = new Encoder(rightEncoderPin1, rightEncoderPin2);
	   
	    
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
			 	boolean stopCompressor){
		 //driving section
		 if(!PTOenabled){
			 drive.tankDrive(Robot.leftStickX, Robot.rightStickX);
		 }
		 else{
			 if((Robot.leftStickX > 0 && Robot.rightStickX > 0) || (Robot.leftStickX < 0 && Robot.rightStickX < 0)){
				 if(Robot.leftStickX > Robot.rightStickX){
					 drive.tankDrive(Robot.leftStickX, Robot.leftStickX);
				 }
				 else if(Robot.leftStickX < Robot.rightStickX){
					 drive.tankDrive(Robot.rightStickX, Robot.rightStickX);
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
