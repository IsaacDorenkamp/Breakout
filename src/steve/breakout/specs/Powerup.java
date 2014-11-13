package steve.breakout.specs;

import java.awt.Graphics;

import steve.breakout.game.Game;
import steve.breakout.framework.Sprite;

public abstract class Powerup extends Sprite{
	public static final int POWERUP_WIDTH = 25;
	public static final int POWERUP_HEIGHT = 25;
	protected Game game;
	public Powerup(Game g, int x, int y){
		super(x,y);
		game = g;
	}
	public abstract void paint( Graphics grfx );
	public abstract void onReception();
	public void updatePosition(){
		move( getX(), getY() + 1 );
	}
}