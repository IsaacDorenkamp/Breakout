package steve.breakout.specs;

import steve.breakout.framework.*;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JComponent;

public class Paddle extends Sprite {
	public static final int PADDLE_WIDTH = 75;
	public static final int PADDLE_HEIGHT = 10;
	public Paddle(){
		super(0,0);
	}
	public void adjust(JComponent jf){
		move( (jf.getWidth() / 2) - 25, Sprite.getWorldY(25, jf.getHeight()) );
	}
	public void paint(Graphics grfx){
		grfx.setColor( Color.BLUE );
		grfx.fillRect(getX(), getY(),Paddle.PADDLE_WIDTH,Paddle.PADDLE_HEIGHT);
	}
}
