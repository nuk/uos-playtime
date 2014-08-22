package org.unbiquitous.examples.playtime;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.network.socket.TCPProperties;

public class Starter {

	public static void main(String[] args) {
		UOS middleware = new UOS();
		
		InitialProperties config = new TCPProperties();
		config.addApplication(FunApp.class);
		config.addDriver(TemperatureDriver.class);
		middleware.start(config);
	}
}
