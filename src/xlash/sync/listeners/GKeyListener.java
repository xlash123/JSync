package xlash.sync.listeners;

import java.util.ArrayList;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GKeyListener implements NativeKeyListener {
	
	public static ArrayList<Integer> keysPressed;
	public static ArrayList<Integer> keysReleased;
	
	public GKeyListener() {
		keysPressed = new ArrayList<>();
		keysReleased = new ArrayList<>();
	}
	
	public synchronized static void clearKeys() {
		keysPressed.clear();
		keysReleased.clear();
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		keysPressed.add(e.getKeyCode());
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		keysReleased.add(e.getKeyCode());
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		
	}

}
