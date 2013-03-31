package com.rustbyte;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public class SoundSystem {	
	public SoundSystem() {		
	}
	
	public void playWaveSound() {
		try {
			new Thread() {
				public synchronized void run() {
					try {
						URL url = getClass().getResource("/snd/10mm_gun_kp66060.wav");
						AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
						Clip clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
					} catch(UnsupportedAudioFileException e) {
						e.printStackTrace();
					} catch( IOException e){
						e.printStackTrace();
					} catch(LineUnavailableException e){
						e.printStackTrace();
					}	
				}
			}.start();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
