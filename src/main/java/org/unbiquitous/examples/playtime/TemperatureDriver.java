package org.unbiquitous.examples.playtime;

import java.util.List;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;

public class TemperatureDriver implements UosDriver {

	public void init(Gateway arg0, InitialProperties arg1, String arg2) {}

	public UpDriver getDriver() {
		UpDriver driver = new UpDriver("uos.temperature");
		driver.addService("current");
		return driver;
	}

	public List<UpDriver> getParent() {
		return null;
	}

	public void destroy() {}
	
	public void current(Call request, Response response, CallContext ctx){
		response.addParameter("temperature", 27.0);
	}
}
