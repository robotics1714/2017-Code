package org.usfirst.frc.team1714.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
	 	gearShiftedHigh;
	 
	 //Pin placeholder
	 private int
	 	tRightFrontPin =2,
	 	tRightRearPin =3,
	 	tLeftFrontPin = 0,
	 	tLeftRearPin = 1,
	 	shiftsolenoidPin1 = 7,
	 	shiftsolenoidPin2 = 1, 
	 	PTOsolenoidPin1 = 0,
	 	PTOsolenoidPin2 = 6,
	 	pcmID = 10 ;
	 //Pin placeholder
	 
	 DriveTrain(){
		tRightFront = new CANTalon(tRightFrontPin);
	    tRightRear = new CANTalon(tRightRearPin);
	    tLeftFront = new CANTalon(tLeftFrontPin);
	    tLeftRear = new CANTalon(tLeftRearPin);
	    
	    shiftsolenoid = new DoubleSolenoid(pcmID, shiftsolenoidPin1, shiftsolenoidPin2);
	    //shiftsolenoid.set(DoubleSolenoid.Value.kForward);
	    PTOsolenoid = new DoubleSolenoid(pcmID, PTOsolenoidPin1, PTOsolenoidPin2);
	    PTOsolenoid.set(DoubleSolenoid.Value.kForward);
    	comp = new Compressor(pcmID);
    	comp.setClosedLoopControl(true);
    	dStation = DriverStation.getInstance();
    	drive = new RobotDrive(tLeftFront, tLeftRear, tRightFront, tRightRear);
    	drive.setSafetyEnabled(false);
	 }
	 
	 int placeholder;
	 boolean currentStop()
	 {
		 if(Robot.pdp.getCurrent(placeholder) > placeholder)
		 {
			 return true;
		 }
		 else
		 {
			 return false;
		 }
	 }
	 
	 public void update(
			 	boolean enablePTO,
			 	boolean disablePTO, 
			 	boolean shiftHigh, 
			 	boolean shiftLow, 
			 	boolean autoGear,
			 	double leftStickY,
			 	double rightStickY){
		 //driving section
		 if(!PTOenabled){
			 drive.tankDrive(leftStickY, rightStickY);
			 //System.out.println("drivin");
			 SmartDashboard.putNumber("right motor", tRightFront.get());
			 SmartDashboard.putNumber("left motor", tLeftFront.get());
			 SmartDashboard.putNumber("right stick", rightStickY);
			 SmartDashboard.putNumber("left stick", leftStickY);
		 }
		 else {
			 if((leftStickY > 0 && rightStickY > 0) || (leftStickY < 0 && rightStickY < 0)){//if both joystick is either in positive or negative direction

			 /*	When PTO is enabled, we want both side of the drive train to run in the same speed because we we do not we will break the transmission
			  * we agree that we will take the joystick that has the smaller value to drive when PTO is enable so that we won't run PTO in high speed easily 
			  */
				 if(leftStickY > 0 && rightStickY > 0){//if both joysticks are in positive direction
	
					 if(leftStickY < rightStickY){
						 drive.tankDrive(leftStickY, leftStickY);//if left joystick Y value is smaller, use left joystick Y value for both joystick
					 }
					 else{
						 drive.tankDrive(rightStickY, rightStickY);//if right joystick Y value is smaller, use right joystick Y value for both joystick
					 }
				 }
				 else if(leftStickY < 0 && rightStickY < 0){//if both joysticks are in negative direction
					 if(leftStickY > rightStickY){
						 drive.tankDrive(leftStickY, leftStickY);//if left joystick Y value is bigger, use left joystick Y value for both joystick
					 }
					 else{
						 drive.tankDrive(rightStickY, rightStickY);//if right joystick Y value is bigger, use right joystick Y value for both joystick
					 }
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
		 comp.start();//we power on the compressor when it is plug in and it will automatically running till it hit the pressure gap, so we don't need manual control of compressor
		 
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
	 	
}
