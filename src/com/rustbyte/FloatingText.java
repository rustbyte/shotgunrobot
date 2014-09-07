package com.rustbyte;

import com.rustbyte.vector.Vector2;

public class FloatingText extends Entity {

	private String text;
	private Vector2 floatDir;
	private int color = 0xFFFFFF;
	private int timer = 80;
	
	public FloatingText(String t, int col,double x, double y, Vector2 dir, Entity p, Game g) {
		super(x, y, t.length() * Bitmap.FONTWIDTH, Bitmap.FONTHEIGHT, p, g);
		
		text = t;
		floatDir = dir;
		color = col;
	}

	@Override
	public void render() throws Exception {
		int xo = (int)xx - game.level.viewX;
		int yo = (int)yy - game.level.viewY;
		
		game.screen.drawText(Art.font, text, xo, yo, color, true);
	}

	@Override
	public void tick() {
		if(--timer <= 0)
			alive = false;
		move();
	}
	@Override
	public void move() {
		xx += floatDir.x;
		yy += floatDir.y;
	}
}
