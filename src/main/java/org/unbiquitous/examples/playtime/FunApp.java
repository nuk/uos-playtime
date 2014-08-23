package org.unbiquitous.examples.playtime;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.NotifyException;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.adaptabitilyEngine.UosEventListener;
import org.unbiquitous.uos.core.applicationManager.UosApplication;
import org.unbiquitous.uos.core.driverManager.DriverData;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Notify;
import org.unbiquitous.uos.core.messageEngine.messages.Response;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyDeploy;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyStart;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyUndeploy;

public class FunApp implements UosApplication, UosEventListener {

	private Gateway gateway;
	private List<DriverData> pastSensors = new ArrayList<DriverData>();
	private Set<UpDevice> knownCursors = new HashSet<UpDevice>();

	public void init(OntologyDeploy arg0, InitialProperties arg1, String arg2) {
	}

	public void start(Gateway gateway, OntologyStart arg1) {
		this.gateway = gateway;
		while(true){
			printAllSensedTemperatureSensors();
			checkCursors();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void checkCursors() {
		List<DriverData> cursors = gateway.listDrivers("uos.cursor");
		if(!cursors.isEmpty()){
			for (DriverData c : cursors) {
				registerCursor(c);
			}
		}else{
			System.out.println("No cursors");
		}
	}

	private void registerCursor(DriverData c) {
		if(!knownCursors.contains(c.getDevice())){
			knownCursors.add(c.getDevice());
			try {
				System.out.println("Registering @ "+c.getDevice().getName());
				gateway.register(this, c.getDevice(), "uos.cursor", "move");
			} catch (NotifyException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	public void handleEvent(Notify event) {
		if("move".equalsIgnoreCase(event.getEventKey())){
			try {
				Robot r = new Robot(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
				System.out.println(String.format("Moving to (%s,%s)", (Integer)event.getParameter("x"), (Integer)event.getParameter("y")));
				r.mouseMove((Integer)event.getParameter("x"), (Integer)event.getParameter("y"));
			} catch (AWTException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
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
			Response tempResponse = gateway.callService(s.getDevice(), senseTemperature);
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
