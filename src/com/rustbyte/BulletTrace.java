package com.rustbyte;

public class BulletTrace extends Entity {
	
	private double xl;
	private double xs;
	private final int traceDirection;
	private final double maxlifetime = 5.0;
	private double lifetime = maxlifetime;	
	
	public BulletTrace(double x1, double y1, double x2, double y2, int w, int h, Entity p, Game g) {
		super(x1, y1, w, h, p, g);
		if( x1 >= x2 ) {
			traceDirection = -1;
			xs = x1;
			xl = x2;
			wid = (int) (x1 - x2);
			xx = x2 + wid / 2;
		} else {
			traceDirection = 1;
			xs = x1;
			xl = x2;
			wid = (int) (x2 - x1);
			xx = x1 + wid / 2;
		}		
		if(x1 == x2 )
			alive = false;
		
		ignoresGravity = true;
	}

	@Override
	public void tick() {
		if(--lifetime <= 0)
			this.alive = false;
	}
	
	
	@Override
	public void render() {
		int currentTraceLength=0;
		int maxTraceLength = Math.abs((int)xl - (int)xs);
		if(maxTraceLength > game.WIDTH / 2)
			maxTraceLength = game.WIDTH / 2;		
		int plotx = (int)xs;		
		
		while(currentTraceLength != maxTraceLength) {
			double percentTraceRemaining = ( ((double)currentTraceLength) / ((double)maxTraceLength)) * 100.0;			
			int fade = ((int)((255.0 / 100.0) * percentTraceRemaining));
			if( fade < 1)
				fade = 1;
			
			plotx = (int)xs + (currentTraceLength * traceDirection);
			if( plotx >= game.level.viewX && plotx < game.level.viewX + game.level.viewWidth) {
				int xxx = (int)plotx - game.level.viewX;
				int yyy = (int)yy - game.level.viewY;				
				int srccol = game.screen.pixels[xxx + yyy * game.WIDTH];
				int r1 = (srccol & 0xFF0000) >> 16;
				int g1 = (srccol & 0x00FF00) >> 8;
				int b1 = srccol & 0x0000FF;
				int r2 = (Art.getColor(fade, fade,fade) & 0xFF0000) >> 16;
				int g2 = (Art.getColor(fade, fade,fade) & 0x00FF00) >> 8;
				int b2 = Art.getColor(fade, fade,fade) & 0x0000FF;
				
				game.screen.pixels[xxx + yyy * game.WIDTH] = Art.getColor(r1 / 2 + r2 / 2, g1 / 2 + g2 / 2, b1 / 2 +  b2 / 2);
			}			
			currentTraceLength += 1;			
		}
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub

	}

}
