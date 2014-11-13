package steve.breakout.framework;

import java.awt.Graphics;
import java.awt.Point;

public abstract class Sprite implements GameObject{
	private int x;
	private int y;
	public Sprite(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void move(int x, int y){
		this.x = x;
		this.y = y;
	}
	public Point getLocation(){
		return new Point(x,y);
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void setX(int x){
		move(x,y); //Remember: x is local to setX
	}
	public void setY(int y){
		move(x,y); //Y is local...
	}

	public static final int getWorldY(int cartY, int world_height){
		return world_height - cartY;
	}

	public abstract void paint(Graphics grfx);
}
