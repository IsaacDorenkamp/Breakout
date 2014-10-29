package steve.breakout.game;

import steve.breakout.specs.*;
import java.util.ArrayList;

public class Game {
	private Paddle pad = new Paddle();
	private Ball ball = new Ball(pad);
	private int balls;
	private ArrayList<Brick> bricks;
	private ArrayList<Brick> toRemove = new ArrayList<Brick>();
	public Game(ArrayList<Brick> brks, int balls){ 
		bricks = brks;
		this.balls = balls;
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
	public void updateBalls(int b){
		balls = b;
	}
	public int getBalls(){
		return balls;
	}
}
