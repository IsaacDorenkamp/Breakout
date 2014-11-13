package steve.breakout.specs;

import steve.breakout.framework.*;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JComponent;

public class Paddle extends Sprite {
	public static final int FIRST_PADDLE_WIDTH = 50;
	public static final int FIRST_PADDLE_HEIGHT = 10;
	
	public static int PADDLE_WIDTH = Paddle.FIRST_PADDLE_WIDTH;
	public static int PADDLE_HEIGHT = Paddle.FIRST_PADDLE_HEIGHT;
	public Paddle(){
		super(0,0);
	}
	public void adjust(JComponent jf){
		move( (jf.getWidth() / 2) - (PADDLE_WIDTH / 2), Sprite.getWorldY( 15 , jf.getHeight()) );
	}
	public void paint(Graphics grfx){
		grfx.setColor( Color.BLUE );
		grfx.fillRect(getX(), getY(),Paddle.PADDLE_WIDTH,Paddle.PADDLE_HEIGHT);
	}
}
