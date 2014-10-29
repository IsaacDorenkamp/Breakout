package steve.breakout.framework;

import java.awt.Graphics;
import java.awt.Point;

public abstract class Static implements GameObject{
	private final int x;
	private final int y;
	public Static(int x, int y){
		this.x = x;
		this.y = y;
	}
	public abstract void paint(Graphics grfx);
}
