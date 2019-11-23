package MinePumpSystem;

import MinePumpSystem.Environment;
import specifications.Configuration;

public class MinePump {

	boolean pumpRunning = false;

	boolean systemActive = true;

	Environment env;

	public MinePump(Environment env) {
		super();
		this.env = env;
	}

	public void timeShift() {
		if (pumpRunning)
			env.lowerWaterLevel();
		if (systemActive)
			processEnvironment();
	}
	void processEnvironment() {
		if(Configuration.highWaterSensor) {
			if (!pumpRunning) {
				activatePump();
				//original();
			} else {
				//original();
			}
		}
		
		if(Configuration.lowWaterSensor) {
			if (pumpRunning) {
				deactivatePump();
			} else {
			//	original();
			}
		}
		
		if(Configuration.methaneAlarm) {
			if (pumpRunning) {
				deactivatePump();
			} else {
				//original();
			}
		}
	}

	void activatePump() {
		pumpRunning = true;
	}

	void deactivatePump() {
		pumpRunning = false;
	}
	
	boolean isMethaneAlarm() {
		return env.isMethaneLevelCritical();
	}

	@Override
	public String toString() {
		return "Pump(System:" + (systemActive?"On":"Off") + ",Pump:" + (pumpRunning?"On":"Off") +") " + env.toString(); 
	}
	
	private Environment getEnv() {
		return env;
	}
}
