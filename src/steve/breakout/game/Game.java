package steve.breakout.game;

import steve.breakout.specs.*;
import java.util.ArrayList;

public class Game {
	private Paddle pad = new Paddle();
	private Ball ball = new Ball(pad);
	private ArrayList<Brick> bricks;
	private ArrayList<Brick> toRemove = new ArrayList<Brick>();
	public Game(ArrayList<Brick> brks){ 
		bricks = brks;
	}
	public Paddle getPaddle(){
		return pad;
	}
	public Ball getBall(){
		return ball;
	}
	public ArrayList<Brick> getBricks(){
		return bricks;
	}
	public void cueForRemoval(Brick brk){
		toRemove.add(brk);
	}
	public ArrayList<Brick> getCued(){
		return toRemove;
	}
}
