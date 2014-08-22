package org.unbiquitous.examples.playtime;

import java.util.ArrayList;
import java.util.List;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.applicationManager.UosApplication;
import org.unbiquitous.uos.core.driverManager.DriverData;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyDeploy;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyStart;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyUndeploy;

public class FunApp implements UosApplication {

	private Gateway gateway;
	private List<DriverData> pastSensors = new ArrayList<DriverData>();

	public void init(OntologyDeploy arg0, InitialProperties arg1, String arg2) {
	}

	public void start(Gateway gateway, OntologyStart arg1) {
		this.gateway = gateway;
		while(true){
			printAllSensedTemperatureSensors();
		}
	}

	private void printAllSensedTemperatureSensors() {
		List<DriverData> sensors = gateway.listDrivers("uos.temperature");
		if(sensors.size() != pastSensors.size()){
			System.out.println(String.format(
					"Found %s temperature sensors in the environment",sensors.size()));
			for (DriverData s : sensors) {
				printTemperature(s);
			}
		}
		pastSensors = sensors;
	}

	private void printTemperature(DriverData s) {
		try {
			Call senseTemperature = new Call("uos.temperature", "current",
					s.getInstanceID());
			Response tempResponse = gateway.callService(null, senseTemperature);
			System.out.println(String.format("Sensor %s says its %s °C", s
					.getDevice().getName(), tempResponse
					.getResponseData("temperature")));
		} catch (ServiceCallException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void stop() throws Exception {
	}

	public void tearDown(OntologyUndeploy arg0) throws Exception {
	}

}
