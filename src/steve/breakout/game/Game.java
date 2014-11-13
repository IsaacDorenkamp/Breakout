package steve.breakout.game;

import steve.breakout.specs.*;
import java.util.ArrayList;

public class Game {
	private Paddle pad = new Paddle();
	private Ball ball = new Ball(pad);
	private int balls;
	private ArrayList<Brick> bricks = new ArrayList<Brick>();
	private ArrayList<Brick> toRemove = new ArrayList<Brick>();
	
	private ArrayList<Powerup> pw_inplay;
	private ArrayList<Powerup> pwToRemove = new ArrayList<Powerup>();
	
	public Game(int balls){ 
		pw_inplay = new ArrayList<Powerup>();
		this.balls = balls;
	}
	public void setBricks(ArrayList<Brick> alb){
		bricks = alb;
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
	public void addPowerup(Powerup p){
		pw_inplay.add(p);
	}
	public ArrayList<Powerup> getPowers(){
		return pw_inplay;
	}
	public void cuePowerForRemoval(Powerup p){
		pwToRemove.add(p);
	}
	public ArrayList<Powerup> getCuedPower(){
		return pwToRemove;
	}
}
