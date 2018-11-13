package xlash.sync.gui.computer;

import java.awt.Point;
import java.awt.Rectangle;

public class Computer {
	
	public Rectangle[] screens;
	private Rectangle setupContainer;
	
	public Point position;
	public Point move;
	
	public Computer(Rectangle[] screens) {
		this.screens = screens;
		position = new Point();
		updateSetupContainer();
	}
	
	public Rectangle getSetupContainer() {
		return this.setupContainer;
	}
	
	public void move(int x, int y) {
		int dx = x - position.x, dy = y - position.y;
		translate(dx, dy);
	}
	
	public void translate(int dx, int dy) {
		position.translate(dx, dy);
		this.setupContainer.translate(dx, dy);
	}
	
	public void updateSetupContainer() {
		setupContainer = new Rectangle();
		int greatestWidth = 0, greatestHeight = 0;
		for(Rectangle screen : this.screens) {
			if(screen.x < setupContainer.x) {
				setupContainer.x = screen.x;
			}
			if(screen.y < setupContainer.y) {
				setupContainer.y = screen.y;
			}
			int width = screen.x + screen.width;
			int height = screen.y + screen.height;
			if(width > greatestWidth) greatestWidth = width;
			if(height > greatestHeight) greatestHeight = height;
		}
		setupContainer.width = greatestWidth - setupContainer.x;
		setupContainer.height = greatestHeight - setupContainer.y;
	}
	
}
