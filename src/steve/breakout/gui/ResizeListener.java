package steve.breakout.gui;

import steve.breakout.game.Game;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ResizeListener extends ComponentAdapter {
	private Breakout brk;
	private GameCanvas gc;
	private Game gm;
	public ResizeListener(Breakout brk, GameCanvas gc, Game gm){
		this.brk = brk;
		this.gc = gc;
		this.gm = gm;
	}
	@Override
	public void componentResized(ComponentEvent evt){
		gm.getPaddle().adjust(gc);
		if(brk.isPaused())
			gm.getBall().adjust(gc);
		
		while( gm.getPaddle().getY() > gc.getHeight() ){
			gm.getPaddle().adjust(gc);
			gm.getBall().adjust(gc);
		}
	}
}