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
		double xv = emitVelX + (((double)rand.nextInt(1)) + rand.nextDouble()) * Math.signum(emitVelX);
		double yv = emitVelY + (((double)rand.nextInt(3)) + rand.nextDouble()) * Math.signum(emitVelY);
		double xxx, yyy = 0;
		if(parent != null) {
			xxx = parent.xx + xx;
			yyy = parent.yy + yy;
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

	@Override
	public void move() {
		// TODO Auto-generated method stub

	}
}
