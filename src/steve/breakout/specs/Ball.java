package steve.breakout.specs;

import steve.breakout.framework.*;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JComponent;

public class Ball extends Sprite {
	private Paddle p;
	public Ball(Paddle p){
		super(0, 0);
		this.p = p;
	}
	public void adjust(JComponent jf){
		move( p.getX() + (Paddle.PADDLE_WIDTH / 2) - 5, p.getY() - Paddle.PADDLE_HEIGHT); //Paddle is already adjusted.
	}
	public void paint(Graphics grfx){
		grfx.setColor( Color.WHITE );
		grfx.fillOval(getX(),getY(),10,10);
	}
}
