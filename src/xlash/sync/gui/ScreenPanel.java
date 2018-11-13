package xlash.sync.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import xlash.sync.gui.computer.Computer;
import xlash.sync.util.Screen;

public class ScreenPanel extends JPanel implements MouseListener, MouseWheelListener{

	private static final long serialVersionUID = 4989913223889523594L;
	
	public ArrayList<Computer> computers;
	
	public double scale = 0.1;
	public int tX = 0, tY = 0;
	public Point prevMouse;
	public boolean doScroll, doMove, firstClick;
	
	public ScreenPanel() {
		computers = new ArrayList<>();
		computers.add(new Computer(Screen.getScreensBounds()));
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g);
		
		g2d.scale(scale, scale);
		
		Point mouse = this.getMousePosition();
		if(doScroll) {
			if(prevMouse == null) prevMouse = this.getMousePosition();
			if(mouse != null) {
				tX -= (prevMouse.x - mouse.x)/scale;
				tY -= (prevMouse.y - mouse.y)/scale;
				prevMouse = mouse;
			}
		}else {
			prevMouse = null;
		}
		
		
		g2d.translate(tX, tY);
		
		Point mouseScaled = new Point(-1, -1);
		if(mouse != null) {
			mouseScaled.x = (int) (mouse.x / scale - tX);
			mouseScaled.y = (int) (mouse.y / scale - tY);
		}
		
		for(Computer cmp : computers) {
			if(doMove) {
				if(mouseScaled != null) {
					if(firstClick && cmp.getSetupContainer().contains(mouseScaled)) {
						firstClick = false;
						cmp.move = mouseScaled;
					}
					if(cmp.move != null) {
						cmp.translate(mouseScaled.x - cmp.move.x, mouseScaled.y - cmp.move.y);
						cmp.move.x = mouseScaled.x;
						cmp.move.y = mouseScaled.y;
					}
				}
			}else {
				if(cmp.move != null) {
					cmp.move = null;
					//Drop container
					//If this is the only container, drop it at 0,0
					if(computers.size() == 1) {
						cmp.move(0, 0);
					}else {
						//Find the nearest cmp, then attach to its side depending on which is closer
					}
				}
			}
			
			//Draw logic
			g2d.setColor(Color.black);
			g2d.translate(cmp.position.x, cmp.position.y);
			for(Rectangle screen : cmp.screens) {
				g2d.draw(screen);
			}
			g2d.translate(-cmp.position.x, -cmp.position.y);
			g2d.setColor(Color.green);
			g2d.draw(cmp.getSetupContainer());
		}
		
		firstClick = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 2) {
			doScroll = true;
		}else if(e.getButton() == 1) {
			doMove = true;
			firstClick = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == 2) {
			doScroll = false;
		}else if(e.getButton() == 1) {
			doMove = false;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
//		Point mouse = this.getMousePosition();
		double scalar = Math.pow(0.9, e.getPreciseWheelRotation());
//		tX -= (int) (mouse.x/(scale*scalar) - mouse.x/scale);
//		tY -= (int) (mouse.y/(scale*scalar) - mouse.y/scale);
		scale *= scalar;
	}

}
