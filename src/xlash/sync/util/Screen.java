package xlash.sync.util;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;

public class Screen {
	
	public static GraphicsDevice getScreenFromMouse(Point point) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		for(GraphicsDevice screen : ge.getScreenDevices()) {
			Rectangle test = screen.getDefaultConfiguration().getBounds();
			if(test.contains(point)) {
				return screen;
			}
		}
		
		return null;
	}
	
	public static Rectangle[] getScreensBounds() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = ge.getScreenDevices();
		Rectangle[] rects = new Rectangle[ge.getScreenDevices().length];
		for(int i=0; i<screens.length; i++) {
			rects[i] = screens[i].getDefaultConfiguration().getBounds();
		}
		
		return rects;
	}
	
}
