package com.rustbyte;

public class BatBoss extends Mob {
	private int ANIM_IDLE = 0;
	
	public BatBoss(double x, double y, int w, int h, Entity p, Game g) {
		super(x, y, w, h, p, g);
		
		ANIM_IDLE = animator.addAnimation(5, 0, 1, w, h, false, 1);
		
		animator.bitmap = Art.sprites2;
		animator.setCurrentAnimation(ANIM_IDLE);
	}

	@Override
	public void tick() {
		super.tick();
		
		animator.tick();
	}
	
	@Override
	public void takeDamage(Entity source, int amount) {
		
	}

	@Override
	public void render() throws Exception {

		animator.render(game.screen, (((int)xx) - (wid / 2)) - game.level.viewX, 
					 				 (((int)yy) - (hgt / 2)) - game.level.viewY);		
	}

}
