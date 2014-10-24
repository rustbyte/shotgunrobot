package com.rustbyte.gui;

import com.rustbyte.Art;
import com.rustbyte.Bitmap;
import com.rustbyte.gui.Control;

public class TextBlock extends Control {
	private String text;
	private int color;
	
	public TextBlock(String t, int x, int y, int c) {
		super(x,y, 0, 0);
		this.text = t;
		this.color = c;
	}
	
	/*
	 * Returns the length of "normal" text, not including color codes.
	 */
	public int getTextLength() {
		int length = 0;
		boolean isNormalText = false;
		
		for(int i=0; i < text.length(); i++) {
			char nextChar = text.charAt(i);
					
			if( nextChar == '{' ) {
				//colorCode = "#";
				//useColorCode = true;
				isNormalText = false;
				continue;
			}
			
			if( nextChar == '}') {
				isNormalText = true;
				continue;
			}
			
			if(isNormalText) {
				++length;
			}
		}
		return length;
	}
	
	public String getText() {
		return this.text;
	}
	public void setText(String t) {
		this.text = t;
	}
	public void setColor(int c) {
		this.color = c;
	}
	public int getColor() {
		return this.color;
	}
	@Override
	public void init() {		
	}

	@Override
	public void render(Bitmap dest, int offsetx, int offsety) throws Exception {
		dest.drawText(Art.font, text, offsetx + xpos, offsety + ypos, color, true );
	}		
}
