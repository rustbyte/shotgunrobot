package com.rustbyte;

public class ColorFadeEffect extends RenderEffect {
	private int color = 0;
	public ColorFadeEffect(int col, int wid, int hgt) {
		super(wid, hgt);
		this.color = col;
	}

	@Override
	protected void animate(int ticks) {
		if(++color > 255)
			color = 255;
		
		for(int i=0; i < width * height; i++) {
			int col = renderFrame.pixels[i];
			int r = (col & 0xFF0000) >> 16;
			int g = (col & 0x00FF00) >> 8;
			int b = col & 0x0000FF;
			r += color;
			if( r > 255)
				r = 255;
			renderFrame.pixels[i] = (r << 16); // | (g << 8) | (b);
		}
	}
}
