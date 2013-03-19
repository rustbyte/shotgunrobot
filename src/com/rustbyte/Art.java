package com.rustbyte;

import java.awt.Color;
import java.awt.image.*;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Art {
	public static Bitmap sprites = loadBitmap("/tex/spritesheet.png");
	public static Bitmap level1 = loadBitmap("/tex/level1.png");
	public static Bitmap font = loadBitmap("/tex/font.png");
	public static Bitmap background = loadBitmap("/tex/background_city1.png");	
	
	public static Bitmap loadBitmap(String filename) {
		try {
			BufferedImage img = ImageIO.read(Art.class.getResource(filename));
			
			int width = img.getWidth();
			int height = img.getHeight();
			
			Bitmap bitmap = new Bitmap(width, height);
			img.getRGB(0, 0, width, height, bitmap.pixels, 0, width);
			return bitmap;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}				
	}
	
	public static int getColor(int r, int g, int b) {
		return r << 16 | g << 8 | b;
	}
	
	public static Color getColor(int hex) {
		Color c = new Color(hex);
		return c;
	}	
}
