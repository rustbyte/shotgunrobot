package com.rustbyte;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.*;

import javax.swing.JFrame;

import com.rustbyte.Game;

public class PlatformerGame extends Canvas implements Runnable, WindowListener, KeyListener{
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Platformer Game";
	public static int HEIGHT = 240;
	public static int WIDTH = 320;
	public static int SCALE = 3;
	
	public BufferedImage screenImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public int[] pixels = ((DataBufferInt)screenImage.getRaster().getDataBuffer()).getData();		
	
	private Game game = null;
	private boolean running = false;
	private Thread thread = null;
	
	public PlatformerGame() {
		
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setMaximumSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		
		addKeyListener(this);
		game = new Game(WIDTH, HEIGHT);		
	}
	
	public synchronized void start() {		
		if(!running) {
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public synchronized void stop() {
		if(running) {
			try {
				running = false;
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long elapsedTime = 0;
		double unprocessed = 0;
		double secondsPerTick = 1.0 / 60.0;		
		long fpsLastTime = System.currentTimeMillis();
		int frames = 0;
		int ticks = 0;
		requestFocus();		
		
		while(running) {
			long now = System.nanoTime();
			elapsedTime = now - lastTime;
			
			unprocessed += elapsedTime / 1000000000.0;
			lastTime = now;
			boolean shouldRender = false;
			int skippedFrames = 0;			
			while(unprocessed > secondsPerTick) {				
				tick(unprocessed);
				unprocessed -= secondsPerTick;
				shouldRender = true;
				ticks++;
			}
			
			//if(shouldRender) {
				render();
				frames++;
			/*} else {
				skippedFrames++;
			}*/
			
			try {
				Thread.yield();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			if((System.currentTimeMillis() - fpsLastTime) > 1000) {
				System.out.println("FPS: " + frames + " Ticks: " + ticks);
				game.FPS = frames;
				frames = 0;
				ticks = 0;
				fpsLastTime += 1000;
			}			
		}
		
		System.out.println("thread closing.");
	}

	private void tick(double elapsedTime) {
		game.tick();
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}				

		game.render();
		
		for(int i=0; i < WIDTH * HEIGHT;i++)
			pixels[i] = game.screen.pixels[i];			
		
		Graphics g = bs.getDrawGraphics();		
		g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
		g.drawImage(screenImage,0, 0, WIDTH * SCALE, HEIGHT * SCALE, 0, 0, WIDTH, HEIGHT, null);
		bs.show();
	}
	
	public static void main(String[] args) {		
				
		PlatformerGame game = new PlatformerGame();
		
		JFrame frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setVisible(true);
		frame.addWindowListener(game);		
		
		game.start();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		stop();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		game.input.keyPressed(arg0.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		game.input.keyReleased(arg0.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
