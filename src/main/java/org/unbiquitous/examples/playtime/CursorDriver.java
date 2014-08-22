package org.unbiquitous.examples.playtime;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.NotifyException;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.UosEventDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Notify;
import org.unbiquitous.uos.core.messageEngine.messages.Response;

public class CursorDriver implements UosEventDriver{

	Set<UpDevice> listeners = new HashSet<UpDevice>();
	private Gateway gateway;
	
	public UpDriver getDriver() {
		UpDriver driver = new UpDriver("uos_cursor");
		driver.addEvent("move");
		return driver;
	}

	public List<UpDriver> getParent() {
		return null;
	}

	public void init(Gateway gtw, InitialProperties arg1, final String id) {
		this.gateway = gtw;
		JFrame frame = new JFrame();
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Notify move = new Notify("move","uos_cursor", id);
				move.addParameter("x", e.getXOnScreen());
				move.addParameter("y", e.getYOnScreen());
				for(UpDevice d: listeners){
					try {
						gateway.notify(move, d);
					} catch (NotifyException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	public void destroy() {}

	public void registerListener(Call call, Response arg1, CallContext ctx) {
		listeners.add(ctx.getCallerDevice());
	}

	public void unregisterListener(Call call, Response arg1, CallContext ctx) {
		listeners.remove(ctx.getCallerDevice());
	}
}
