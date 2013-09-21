package com.rustbyte;

import java.util.ArrayList;
import java.util.List;

public class SpriteAnimator {
	private static int numAnimations = 0;
	
	private long lastTime = System.nanoTime();
	private long now = 0;
	private double unprocessed = 0;
	private double nsPerTick = 1000000000.0 / 10.0;
	
	public class Animation {
		public int numFrames;
		public int currentFrame;
		public int frameOffsetX;
		public int frameOffsetY;
		public int frameWidth;
		public int frameHeight;
		public int border;
		public boolean flip;
		
		public Animation(int frames, int x, int y, int wid, int hgt, boolean flip, int border) {
			this.numFrames = frames;
			this.currentFrame = 0;
			this.frameOffsetX = x;
			this.frameOffsetY = y;
			this.frameWidth = wid;
			this.frameHeight = hgt;
			this.flip = flip;
			this.border = border;
		}
		
		public int getOffsetX() {
			return border + frameOffsetX + (frameWidth * currentFrame) + (border * currentFrame);
		}
		public int getOffsetY() {
			return frameOffsetY;
		}
	}
	
	private static List<Animation> animations = new ArrayList<Animation>();
	private int currentAnimation = 0;
	
	public SpriteAnimator() {
		
	}
	
	public String getStatusString() {
		return "curanim: " + currentAnimation + " curframe: " + getCurrentAnimation().currentFrame;
	}
	public void setFrameRate(double fps) {	
		nsPerTick = 1000000000.0 / fps;
	}
	public void tick() {
		Animation curAnim = getCurrentAnimation();
		
		now = System.nanoTime();
		unprocessed += (now - lastTime) / nsPerTick;
		while(unprocessed >= 1 ) {
			if(++curAnim.currentFrame == curAnim.numFrames )
				curAnim.currentFrame = 0;
			unprocessed -= 1;
		}			
		lastTime = now;
	}
	public int addAnimation(int frames, int x, int y, int wid, int hgt, boolean flip, int border) {
		animations.add(new Animation(frames, x, y, wid, hgt, flip, border));
		return numAnimations++;
	}
	
	public void setCurrentAnimation(int animID) {
		currentAnimation = animID;
	}
	public Animation getCurrentAnimation() {
		return animations.get(currentAnimation);
	}
	public void render(Bitmap dest, int xx, int yy) {
		Art.sprites.draw(dest, xx, yy, 
			getCurrentAnimation().getOffsetX(), 
			getCurrentAnimation().getOffsetY(), 
			getCurrentAnimation().frameWidth, 
			getCurrentAnimation().frameHeight,				
			getCurrentAnimation().flip);
	}

	public void renderScaled(Bitmap dest, int xx, int yy, int sx, int sy) {
		Art.sprites.drawScaled(dest, xx, yy, sx, sy, 
				getCurrentAnimation().getOffsetX(), 
				getCurrentAnimation().getOffsetY(), 
				getCurrentAnimation().frameWidth, 
				getCurrentAnimation().frameHeight,				
				getCurrentAnimation().flip);
	}
}
