package org.usfirst.frc.team1714.robot;

import edu.wpi.first.wpilibj.DigitalInput;

public class CoinSlot {
	DigitalInput gearDetect;
	
	private int
		gearDetectPin;
	
	CoinSlot(){
		gearDetect = new DigitalInput(gearDetectPin);
	}
	
	public void update(){
		
	}
	
	public boolean gearAquired(){
		return gearDetect.get();
	}
}
 