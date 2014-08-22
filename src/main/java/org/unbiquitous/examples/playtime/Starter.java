package org.unbiquitous.examples.playtime;

import java.util.logging.Level;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.UOSLogging;
import org.unbiquitous.uos.network.socket.TCPProperties;
import org.unbiquitous.uos.network.socket.radar.MulticastRadar;

public class Starter {

	public static void main(String[] args) {
		UOS middleware = new UOS();
		
//		UOSLogging.setLevel(Level.ALL);
		InitialProperties config = new MulticastRadar.Properties();
//		config.addApplication(FunApp.class);
		config.addDriver(TemperatureDriver.class);
		config.addDriver(CursorDriver.class);
		middleware.start(config);
	}
}
