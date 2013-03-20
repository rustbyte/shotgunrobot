package com.rustbyte;

public class BackgroundLayer {
	public double x1;
	public double x2;
	public double yy;
	public int width;
	public int height;
	public Bitmap bitmap;
	
	public BackgroundLayer(Bitmap background, int layerY, int hgt) {
		bitmap = background;
		width = bitmap.width;
		x1 = 0;
		x2 = width;
		yy = layerY;
		height = hgt;		
	}
	public void move(double vel) {
		x1 += vel;
		x2 += vel;
		if( (x1 + width) < 0 ) {				
			x1 = x2 + width;				
		}
		if( x1 > width ) {
			x1 = x2 - width;
		}
		if( (x2 + width) < 0 ) {
			x2 = x1 + width;
		}
		if( x2 > width ) {
			x2 = x1 - width;
		}
	}
	public void draw(Bitmap dest) {
		Art.background.draw(dest, (int)x1 , (int)yy, 0, (int)yy, width, height);
		//screen.drawText(Art.font, "First half", (int)x1 , yy + 20, 0xFFFF00, true);
								
		Art.background.draw(dest, (int)x2 , (int)yy, 0, (int)yy, width, height);
		//screen.drawText(Art.font, "Second half",(int)x2 , yy + 20, 0xFFFF00, true);			
		
	}
}
