package com.rustbyte.gui;

import com.rustbyte.Bitmap;
import com.rustbyte.Art;
import com.rustbyte.gui.TextBlock;

public class Label extends Control{ 
	private TextBlock textBlock = null;
	private int color = 0xFFFFFF;
	
	public Label(String t,int x, int y, int c) {
		super(x, y, 0, 0);
		
		setText(t);
		setColor(c);
		init();
	}

	@Override
	public void init() {
		width = textBlock.getTextLength() * Bitmap.FONTWIDTH;
		height = Bitmap.FONTHEIGHT;
	}

	@Override
	public void render(Bitmap dest, int offsetx, int offsety) throws Exception {
		textBlock.render(dest, offsetx, offsety);
	}

	public void setText(String t) { 
		this.textBlock = new TextBlock(t, xpos, ypos, color);
	}
	public String getText() {
		return this.textBlock.getText();
	}
	public void setColor(int c) {
		this.color = c;
	}
	public int getColor() {
		return this.color;
	}
}
