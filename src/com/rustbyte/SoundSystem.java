package com.rustbyte;

import java.util.HashMap;
import javax.sound.sampled.*;

public class SoundSystem {
	private static HashMap<String, String> soundList = new HashMap<String, String>();
	
	static {
		soundList.put("SHOTGUN_FIRE","/snd/shotgunshot_reload_last_xr51442.wav");
		soundList.put("PISTOL","/snd/10mm_gun_kp66060.wav");
	}	

	public static synchronized void playSound(final String name) {
		new Thread(new Runnable() {
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					String url = soundList.get(name);
			        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
			        SoundSystem.class.getResource(url));
			        clip.open(inputStream);
			        clip.start(); 
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}
}
