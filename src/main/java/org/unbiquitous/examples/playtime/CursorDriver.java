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
		UpDriver driver = new UpDriver("uos.cursor");
		driver.addEvent("move");
		return driver;
	}

	public List<UpDriver> getParent() {
		return null;
	}

	public void init(Gateway gtw, InitialProperties arg1, final String id) {
		this.gateway = gtw;
		JFrame frame = new JFrame();
		frame.setTitle("Cursor Driver : "+id);
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.addMouseMotionListener(new NotifierMouseAdapter(id));
	}

	public void destroy() {}

	public void registerListener(Call call, Response arg1, CallContext ctx) {
		System.out.println("Registering"+ctx.getCallerDevice());
		listeners.add(ctx.getCallerDevice());
	}

	public void unregisterListener(Call call, Response arg1, CallContext ctx) {
		listeners.remove(ctx.getCallerNetworkDevice());
	}
	
	private final class NotifierMouseAdapter extends MouseMotionAdapter {
		private final String id;

		private NotifierMouseAdapter(String id) {
			this.id = id;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Notify move = createEvent(id, e);
			notifyListeners(move);
		}

		private Notify createEvent(final String id, MouseEvent e) {
			Notify move = new Notify("move","uos.cursor", id);
			move.addParameter("x", e.getXOnScreen());
			move.addParameter("y", e.getYOnScreen());
			return move;
		}

		private void notifyListeners(Notify move) {
			for(UpDevice d: listeners){
				notify(move, d);
			}
		}

		private void notify(Notify move, UpDevice d) {
			try {
				System.out.println(String.format("Senting move to %s", d.getName()));
				gateway.notify(move, d);
			} catch (NotifyException e1) {
				e1.printStackTrace();
			}
		}
	}
}
