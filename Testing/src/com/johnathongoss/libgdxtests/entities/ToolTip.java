package com.johnathongoss.libgdxtests.entities;

import com.johnathongoss.libgdxtests.ImageCache;

public class ToolTip extends TextBox{	
	
	public ToolTip() {
		background = ImageCache.CreatePatch("tooltip");		
	}
	
	@Override
	void animate() {
		
	}	
}
