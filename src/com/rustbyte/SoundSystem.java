package com.rustbyte;

import java.io.*;
import java.net.URL;
import java.util.HashMap;

import javax.sound.sampled.*;

public class SoundSystem {
	public SoundSystem() {
		
	}
	
	public static synchronized void playSound(final String url) {
		new Thread(new Runnable() {
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
			        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
			        SoundSystem.class.getResource("/snd/shotgunshot_reload_last_xr51442.wav"));
			        clip.open(inputStream);
			        clip.start(); 
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}
}
