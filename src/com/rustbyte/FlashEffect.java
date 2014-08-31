package com.rustbyte;

public class FlashEffect extends RenderEffect {
	private int flashColor = 0xFFFFFF;
	private int flashFrequency = 5;
	public FlashEffect(int col, int f, int wid, int hgt) {
		super(wid,hgt);
		this.flashColor = col;
		this.flashFrequency = f;
	}
	public void setFrequency(int f) {
		flashFrequency = f;
	}
	public void setColor(int col) {
		flashColor = col;
	}
	@Override
	public void animate(int ticks) {
		if(ticks % flashFrequency == 0) {
			for(int i=0; i < width * height; i++){
				if(renderFrame.pixels[i] != 0xFF5DFF)
					renderFrame.pixels[i] = flashColor;
			}		
		}
	}

}
