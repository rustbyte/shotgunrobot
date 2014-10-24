package com.rustbyte.gui;

import java.util.ArrayList;
import java.util.List;

import com.rustbyte.Bitmap;

public abstract class Control {
	protected List<Control> children;
	protected boolean autoSize = true;
	protected int xMargin = 2;
	protected int yMargin = 2;
	protected int width;
	protected int height;
	protected int xpos;
	protected int ypos;
	
	public Control(int x, int y, int wid, int hgt) {
		xpos = x;
		ypos = y;
		width = wid;
		height = hgt;
		children = new ArrayList<Control>();
	}
	
	public abstract void init();
	
	protected void resize() {
		
		int maxWidth = 0;
		int maxHeight = 0;
		int nextWidth = 0;
		int nextHeight = 0;
		
		for(int i=0; i < children.size(); i++) {
			Control c = children.get(i);
			nextWidth = c.xpos + c.width;
			nextHeight = c.ypos + c.height;
			
			if(nextWidth > maxWidth) maxWidth = nextWidth;
			if(nextHeight > maxHeight) maxHeight = nextHeight;
		}
		
		width = maxWidth <= 0 ? width : maxWidth;
		height = maxHeight <= 0 ? height : maxHeight;
	}
	
	public void addControl(Control c) {
		children.add(c);
		resize();
	}
	public abstract void render(Bitmap dest, int offsetx, int offsety) throws Exception;	
}
