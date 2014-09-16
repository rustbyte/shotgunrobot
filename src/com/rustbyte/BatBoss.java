package com.rustbyte;

import com.rustbyte.vector.Vector2;

public class BatBoss extends Mob {
	private int ANIM_IDLE = 0;
	
	private double xAng = 0.0;
	private double yAng = 0.0;
	private double xAngIncrement = 0.0;
	private double yAngIncrement = 0.0;
	private Vector2 rotVec;
	private Vector2 rotPoint;
	private double TWOPI = Math.PI * 2;
	
	public BatBoss(double x, double y, int w, int h, Entity p, Game g) {
		super(x, y, w, h, p, g);
		
		ANIM_IDLE = animator.addAnimation(5, 0, 1, w, h, false, 1);
		
		animator.bitmap = Art.sprites2;
		animator.setCurrentAnimation(ANIM_IDLE);
		this.ignoresGravity = true;
		
		xAngIncrement = 0.04;
		yAngIncrement = 0.04;
		
		rotPoint = new Vector2(170, 100);
		
		hitpoints = 1000;
	}

	@Override
	public void tick() {
		super.tick();		
		
		if( hitpoints <= 0)
			this.alive = false;
		
		ignoresGravity = false;
		
		yAngIncrement = 0.02;
		xAngIncrement = 0.04;
		
		xAng += xAngIncrement;
		yAng += yAngIncrement;
		
		if(xAng > TWOPI)
			xAng = 0.0;
		if(yAng > TWOPI) 
			yAng = 0.0;
		
		rotVec = new Vector2(Math.cos(xAng), Math.sin(yAng));
		rotVec = rotVec.mult(2);
		
		velocity.x = rotVec.x;
		velocity.y = rotVec.y;
		
		//xx = rotPoint.x + rotVec.x;
		//yy = rotPoint.y + rotVec.y;
		
		move();
		
		animator.tick();
	}
	
	@Override
	public void takeDamage(Entity source, int amount) {
		hitpoints -= amount;
		if(hitpoints > 0) {
			hurt(20);
			this.knockBack( (source.xx - xx), 2.0 );
		} else {
			// initiate death-sequence
			this.explode(16, Art.getColor(255,0,0), 50);
		}
		game.addEntity(new FloatingText("-" + amount, Art.getColor(255, 255, 0), xx,yy - 10, new Vector2(0,-1), null, game));
		int px = 0;
		int py = -10;
		ParticleEmitter pe = new ParticleEmitter(px, py, (double)source.facing, -1.0, 1, 10, Art.getColor(255,0,0), this, game);
		game.addEntity(pe);	
	}

	@Override
	public void render() throws Exception {

		animator.render(game.screen, (((int)xx) - (wid / 2)) - game.level.viewX, 
					 				 (((int)yy) - (hgt / 2)) - game.level.viewY);		
	}

}
