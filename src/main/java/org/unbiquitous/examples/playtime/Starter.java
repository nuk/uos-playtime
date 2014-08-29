package org.unbiquitous.examples.playtime;

import java.util.Arrays;
import java.util.logging.Level;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.UOSLogging;
import org.unbiquitous.uos.network.socket.radar.MulticastRadar;

public class Starter {

	public static void main(String[] args) {
		if(Arrays.asList(args).contains("--debug")){
			UOSLogging.setLevel(Level.ALL);
		}
		UOS middleware = new UOS();
		InitialProperties config = new MulticastRadar.Properties();
		config.addApplication(FunApp.class);
		config.addDriver(TemperatureDriver.class);
		middleware.start(config);
	}
}
