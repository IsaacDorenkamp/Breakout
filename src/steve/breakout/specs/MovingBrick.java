package steve.breakout.specs;

import java.awt.Color;
import java.awt.Graphics;

public class MovingBrick extends Brick {
	private int maxx;
	private int maxy;
	private int minx;
	private int miny;
	
	private int velx;
	private int vely;
	
	public MovingBrick(Color col,int startx, int starty, int endx, int endy){
		super(col,startx,starty);
		maxx = endx;
		minx = startx;
		
		maxy = endy;
		miny = starty;
		
		int slope = (int) (endy - starty) / (endx - startx);
		velx = 1;
		vely = slope;
	}
	public void updatePosition(){
		move( getX() + velx, getY() + vely );
		if( getX() > maxx ){
			velx = -velx;
		}
		if( getY() > maxy ){
			vely = -vely;
		}
		
		if( getY() < miny ){
			vely = -vely;
		}
		if( getX() < minx){
			velx = -velx;
		}
	}
	@Override
	public void paint(Graphics grfx){
		super.paint(grfx);
	}
}