package com.rustbyte.gui;

import com.rustbyte.Bitmap;
import com.rustbyte.Art;

public class Frame extends Control {
	private final int DEFAULT_BACKGROUND_COLOR = 0x303030;
	private int backgroundColor = DEFAULT_BACKGROUND_COLOR;
	private Bitmap bitmap;	
	
	public Frame(int x, int y, int wid, int hgt) {
		super(x,y,wid,hgt);
		this.xpos = x;
		this.ypos = y;
		this.width = wid;
		this.height = hgt;
	}
	
	/*
	 * NOTE:
	 * This method must be called after controls are added to the frame.
	 * Otherwise frame border/background will be incorrectly sized. 
	 * @see com.rustbyte.gui.Control#init()
	 */
	public void init() {
		int x = 0;
		int y = 0;
		
		this.bitmap = new Bitmap( width, height);
		
		for(x=0; x < width; x++) {
			for(y=0; y < height; y++) {
				int color = backgroundColor;
				if((x == 0 && y == 0) ||
				   (x == width - 1) && (y == 0) ||
				   (x == 0) && (y == height - 1) ||
				   (x == width - 1) && (y == height - 1))
					color = 0xFF5DFF;
				bitmap.pixels[x + y * width] = color;
			}
		}
		
		// Top left corner
		Art.gui.draw(bitmap, 0,0, 1, 23, 2, 2);
		
		// Top right corner
		Art.gui.draw(bitmap, width - 2, 0, 16,23, 2,2 );
		
		// Bottom left corner
		Art.gui.draw(bitmap, 0, height - 2, 1, 35, 2, 2 );
		
		// Bottom right corner
		Art.gui.draw(bitmap, width - 2, height - 2, 16, 35, 2, 2);
		
		// Top / bottom border
		for(x = 2; x < width - 2; x++) {
			Art.gui.draw(bitmap, x, 0, 3, 23, 1, 2 );
			Art.gui.draw(bitmap, x, height - 2, 3,35, 1, 2 );
		}

		// Left / right border
		for(y = 2; y < height - 2; y++) {
			Art.gui.draw(bitmap, 0, y, 1, 25, 2, 1 );
			Art.gui.draw(bitmap, width - 2, y, 16, 25, 2, 1 );
		}		
	}
	
	public void render(Bitmap dest, int offsetx, int offsety) throws Exception {
		bitmap.draw(dest, offsetx + xpos, offsety + ypos, true);
		for(int i=0; i < children.size(); i++)
			children.get(i).render(dest, offsetx + xpos, offsety + ypos);
	}
}
