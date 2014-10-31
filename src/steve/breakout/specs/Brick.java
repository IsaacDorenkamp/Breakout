package steve.breakout.specs;

import steve.breakout.framework.Static;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

public class Brick extends Static {
	public static int BRICK_WIDTH = 50;
	public static int BRICK_HEIGHT = 25;
	
	private Color brick_color;
	public Brick(Color c, int x, int y){
		super(x,y);
		brick_color = c;
	}
	public void paint(Graphics grfx){
		grfx.setColor(brick_color);
		grfx.fillRect(getX(), getY(), Brick.BRICK_WIDTH, Brick.BRICK_HEIGHT);
	}
	
	public static void setBrickWidth(int w){
		Brick.BRICK_WIDTH = w;
	}
	public static void setBrickHeight(int h){
		Brick.BRICK_HEIGHT = h;
	}
}