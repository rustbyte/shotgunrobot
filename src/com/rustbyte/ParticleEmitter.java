package com.rustbyte;

import java.util.Random;

public class ParticleEmitter extends Entity {
	
	private int emitRate = 0;
	private int emitCount = 0;
	private double emitVelY = 0.0;
	private double emitVelX = 0.0;
	private int particleColor = 0;
	
	public ParticleEmitter(double x, double y, double xv, double yv, int rate,int count, int col, Entity p, Game g) {
		super(x, y, 0, 0, p, g);
		this.emitRate = rate;
		this.emitCount = count;
		this.emitVelX = xv;
		this.emitVelY = yv;
		this.particleColor = col;
	}

	public void emitParticle() {
		Random rand = new Random();
		int lifetime = rand.nextInt(50);
		double dir = (rand.nextInt(10) < 5) ? -1.0 : 1.0;		
		
		double xv = emitVelX + (((double)rand.nextInt(1)) + rand.nextDouble()) * Math.signum(emitVelX);
		double yv = emitVelY + (((double)rand.nextInt(3)) + rand.nextDouble()) * Math.signum(emitVelY);
		/*double accel = 1 + ((double)rand.nextInt(3)) * rand.nextDouble();
		double xv = (emitVelX == 0) ? (dir * Math.abs(accel)) : emitVelX * accel;		
		double yv = (emitVelY == 0) ? (dir * Math.abs(accel)) : emitVelY * (accel + 1.5);*/
		
		
		double xxx, yyy = 0;
		if(parent != null) {
			xxx = parent.xx;
			yyy = parent.yy;
		} else {
			xxx = xx;
			yyy = yy;
		}
		Particle p = new Particle(lifetime, xxx,yyy, xv, yv, particleColor, null ,game);
		game.addEntity(p);
	}
	@Override
	public void tick() {
		if(emitCount > 0) {
			if(game.tickcount % emitRate == 0) {
				emitParticle();
				emitCount--;
			}
		}
		else
			this.alive = false;
	}

	@Override
	public void render() {

	}
}
