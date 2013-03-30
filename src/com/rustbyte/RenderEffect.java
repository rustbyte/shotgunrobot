package com.rustbyte;

public abstract class RenderEffect {
	public Bitmap renderFrame = null;
	protected int width = 0;
	protected int height = 0;
	
	public RenderEffect(int wid, int hgt){
		this.width = wid;
		this.height = hgt;
		this.renderFrame = new Bitmap(wid, hgt);
		this.clear();
	}
	public void clear() {
		this.renderFrame.clear(-1);
	}
	protected abstract void animate(int ticks);
	
	public void render(int ticks, Bitmap dest, int xx, int yy) {
		animate(ticks);
		renderFrame.draw(dest, xx, yy, 0,0,width,height);
	}
}

