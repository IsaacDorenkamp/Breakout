package steve.breakout.specs;

import java.awt.Graphics;
import java.awt.Color;

public class MultiBrick extends Brick {
	public static final Color[] color_order = { Color.WHITE, Color.MAGENTA, Color.GRAY, Color.BLUE };
	public MultiBrick(int x, int y, int life){
		super( color_order[life], x, y, life );
	}
	private void updateColor(){
		if( getHealth() < 0 ) return;
		setColor( color_order[ getHealth() ] );
	}
	@Override
	public void paint(Graphics grfx){
		updateColor();
		super.paint(grfx);
		
		int[] xs = { getX() + (Brick.BRICK_WIDTH / 2), getX() + Brick.BRICK_WIDTH, getX() + (Brick.BRICK_WIDTH / 2), getX() };
		int[] ys = { getY(), getY() + (Brick.BRICK_HEIGHT / 2), getY() + Brick.BRICK_HEIGHT, getY() + (Brick.BRICK_HEIGHT / 2) };
		grfx.setColor( Color.BLACK );
		grfx.fillPolygon( xs, ys, 4 );
	}
}