package com.rustbyte;

public class Particle extends Entity {
	private int lifetime;
	private double px = 0.0;
	private double py = 0.0;
	private int color;
	
	public Particle(int l,double xxx, double yyy, double xv, double yv, int col, Entity p, Game g) {
		super(xxx, yyy, 0, 0, p, g);
		lifetime = l;	
		alive = true;
		velX = xv;
		velY = yv;
		px = xxx;
		py = yyy;
		color = col;
		this.xr = 1;
		this.yr = 1;
	}

	@Override
	public void tick() {
		if(--lifetime <= 0 )
			alive = false;
		
		move();
	}

	@Override
	public void postTick() {
		// overriden to bypass entity postTick standard behaivor.
	}
	
	@Override
	public void render() {
		int projX = ((int)xx) - game.level.viewX;
		int projY = ((int)yy) - game.level.viewY;
		if(projX < 0 || projY < 0 || projX >= game.WIDTH || projY >= game.HEIGHT) return;
				
		game.screen.pixels[projX + projY * game.WIDTH] = color;
	}

	@Override
	public void move() {		
		
		px += velX;
		py += velY;
		xx = (int)px;
		yy = (int)py;
	}
}
