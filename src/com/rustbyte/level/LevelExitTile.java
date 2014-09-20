package com.rustbyte.level;

import com.rustbyte.Bitmap;
import com.rustbyte.Art;
import com.rustbyte.Entity;

public class LevelExitTile extends DoorTile {
	private boolean activated = false;
	private int nextLevelID = -1;
	private boolean signLightOn = false;
	
	public LevelExitTile(int x, int y, int wid, int hgt, Level lev) {
		super(x, y, wid, hgt, lev);
	}

	@Override
	public void init() {
		super.init();	
	}
	
	@Override
	public void tick() {
		if(level.game.isBossDefeated())
			activate();
		
		if(activated && (level.game.tickcount % 20 == 0)) 			
			signLightOn = !signLightOn;		
	}
	
	private void activate() {
		activated = true;		
	}
	
	@Override
	public void interact(Entity e) {
		if(activated)
			super.interact(e);
	}
	
	@Override
	public void takeDamage(Entity e, int amount) {
		// This door cannot be harmed.
		// overriden to hide inherited method.
	}
	
	@Override
	public void draw(Bitmap dest, int x, int y) {
		
		int signX = 43;
		int signY = signLightOn ? 106 : 85;
		
		Art.tiles.draw(dest, x, y - 20, signX, signY, 20, 20, false );
		super.draw(dest,x,y);
	}
}
