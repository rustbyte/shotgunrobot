package com.rustbyte;

public class InputHandler {
	public class Key {
		boolean pressed = false;
		boolean clicked = false;
	}
	public Key[] keys = new Key[256];
	
	public InputHandler() {
		for(int i=0; i < 256; i++)
			keys[i] = new Key();
	}
	public void keyPressed(int keyCode) {
		keys[keyCode].pressed = true;
	}
	public void keyReleased(int keyCode) {
		keys[keyCode].pressed = false;
	}
}
