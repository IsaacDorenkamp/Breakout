package steve.breakout.specs;

import steve.breakout.framework.Sprite;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

public class Brick extends Sprite {
	public static final int BRICK_WIDTH = 25;
	public static final int BRICK_HEIGHT = 12;
	
	private Color brick_color;
	public Brick(Color c, int x, int y){
		super(x,y);
		brick_color = c;
	}
	public void paint(Graphics grfx){
		grfx.setColor(brick_color);
		grfx.fillRect(getX(), getY(), Brick.BRICK_WIDTH, Brick.BRICK_HEIGHT);
	}
}