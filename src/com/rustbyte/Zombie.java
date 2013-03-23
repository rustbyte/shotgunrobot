package com.rustbyte;

import com.rustbyte.Game;
import com.rustbyte.vector.Vector2;

public class Zombie extends Mob {
	private int ANIM_WALK_RIGHT;
	private int ANIM_WALK_LEFT;
	private int ANIM_IDLE_LEFT;
	private int ANIM_IDLE_RIGHT;	
	private int actionTimer = 0;	
	
	public Zombie(int x, int y, int w, int h, Entity p, Game g) {
		super(x, y, w, h, p, g);
				
		ANIM_WALK_RIGHT = this.animator.addAnimation(5, 121, 101, w, h, false,1);
		ANIM_WALK_LEFT = this.animator.addAnimation(5, 121, 101, w, h, true,1);
		ANIM_IDLE_RIGHT = this.animator.addAnimation(1, 100, 101, w, h, false,1);
		ANIM_IDLE_LEFT = this.animator.addAnimation(1, 100, 101, w, h, true,1);
		
		this.hitpoints = 100;
		this.speed = 0.75;		
	}

	@Override
	public void tick() {
		super.tick();
		
		if(hitpoints <= 0) {
			// initiate death-sequence
			//this.explode(8, Art.getColor(255,0,0),50);
			//this.breakApart(16, Art.getColor(255,0,0), 10);
			this.explode(16, Art.getColor(255,0,0), 50);
			
			this.alive = false;
		}
		
		if(--actionTimer <= 0) {
			int tempNewDir = 0;
			rand.setSeed(System.nanoTime());
			switch(1 + rand.nextInt(3)) {
			case 1:
				tempNewDir = 0;
				break;
			case 2:
				tempNewDir = -1;
				break;
			case 3:
				tempNewDir = 1;
				break;
			}
			dirX = tempNewDir;
			actionTimer = 100 + rand.nextInt(200);
		}
						
		
		Vector2 v1 = new Vector2(xx,yy);
		Vector2 v2 = new Vector2(game.player.xx, game.player.yy);
		Vector2 v3 = v1.sub(v2);
		if(v3.length() < 5) {
			game.player.takeDamage(this, 5);
		}
		
		if(blockedX) {
			int tx = (int)this.xx / game.level.tileWidth;
			int ty = (int)this.yy / game.level.tileHeight;
			Tile t = game.level.getTile(tx + dirX, ty - 2);
			if( t == null || !t.blocking)
				jump();
			else
				dirX = -dirX;
		}
		velX = dirX * speed;
		
		move();
		
		if(velX < 0 ) this.animator.setCurrentAnimation(ANIM_WALK_LEFT);
		else if(velX > 0) this.animator.setCurrentAnimation(ANIM_WALK_RIGHT);
		else {
			if(facing == -1) this.animator.setCurrentAnimation(ANIM_IDLE_LEFT);
			if(facing == 1) this.animator.setCurrentAnimation(ANIM_IDLE_RIGHT);
		}
		
		animator.tick();		
	}

	@Override
	public void render() {
		renderFrame.clear(-1);
		
		animator.render(renderFrame, 0, 0);
		
		if(isHurt()) {
			renderHurtEffect();
		}
		
		renderFrame.draw(game.screen, (((int)xx) - (wid / 2)) - game.level.viewX, 
                				   	  (((int)yy) - (hgt / 2)) - game.level.viewY,
                				      0,0, wid, hgt);
	}

	@Override
	public void takeDamage(Entity source, int amount) {
		hitpoints -= amount;
		hurt(20);
		game.addEntity(new FloatingText("-" + amount, Art.getColor(255, 255, 0), xx,yy, new Vector2(0,-1), null, game));
		int px = 0;
		int py = -10;
		ParticleEmitter pe = new ParticleEmitter(px, py, (double)source.facing, -1.0, 2, 10, Art.getColor(255,0,0), this, game);
		game.addEntity(pe);
	}
}
