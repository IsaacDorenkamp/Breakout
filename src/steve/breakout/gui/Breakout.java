package steve.breakout.gui;

import steve.breakout.game.Game;
import steve.breakout.specs.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Color;
import java.awt.Robot;
import java.awt.AWTException;

import java.util.ArrayList;
import java.util.Random;

public class Breakout extends JFrame implements MouseMotionListener{
	private GameCanvas gc;
	private Game gm;
	
	private int wdt = 500;
	private int ht = 500;
	
	private ComponentAdapter cl = new ComponentAdapter(){
		@Override
		public void componentResized(ComponentEvent evt){
			wdt = getWidth();
			ht = getHeight();
			gm.getPaddle().adjust(gc);
			gm.getBall().adjust(gc);
			
			while( gm.getPaddle().getY() > getHeight() ){
				gm.getPaddle().adjust(gc);
				gm.getBall().adjust(gc);
			}
		}
	};
	
	public Breakout() {
		super("Breakout!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(wdt,ht);
		getContentPane().setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		
		addComponentListener(cl);
		
 
		gm = new Game( generateBrickField() , 3 );
		
		gc = new GameCanvas(gm);
		
		repaint();
		
		add(gc, BorderLayout.CENTER);
		setVisible(true);
		
		addMouseMotionListener(this);
	}
	public ArrayList<Brick> generateBrickField(){
		ArrayList<Brick> arrangement = new ArrayList<Brick>();
		
		Color[] cols = {Color.BLUE,Color.YELLOW,Color.MAGENTA,Color.WHITE,Color.GRAY};
		
		Random r = new Random();
		
		for(int y = 50; y < 250; y += Brick.BRICK_HEIGHT){
			for(int x = 100; x < 400; x += Brick.BRICK_WIDTH){
				boolean dobrick = r.nextBoolean();
				if(dobrick)
					arrangement.add( new Brick(cols[(int)(Math.random()*4)],x,y) );	
			}
		}
		return arrangement;
	}
	public void mouseMoved(MouseEvent evt){
		if(!paused){
			Paddle p = gm.getPaddle();
			int pos = p.getX();
		
			if( pos == 0 ){
				if(evt.getX() < pos)
					return;
			}
			if(evt.getX() - (Paddle.PADDLE_WIDTH / 2) >= getWidth() - (Paddle.PADDLE_WIDTH / 2)){
					return;
			}
			p.move(evt.getX() - (Paddle.PADDLE_WIDTH / 2), p.getY());
			repaint();
		}
	}
	public void mouseDragged(MouseEvent evt){}
	
	int vel_x = 2;
	int vel_y = 2;
	
	private void randomizeVelocity(){
		Random r = new Random();
		
		boolean rdmz_x = r.nextBoolean();
		boolean rdmz_y = r.nextBoolean();
		
		if(rdmz_x){
			if(vel_x < 0) vel_x--;
			else vel_x++;
		}
		if(rdmz_y){
			if(vel_y < 0) vel_x--;
			else vel_x++;
		}
	}
	
	private boolean has_touched = false;
	private boolean paused = false;
	
	public void animate(){
		Ball ball = gm.getBall();
		has_touched = true; //Touching paddle at beginning.
		boolean first = true;
		paused = true;
		try{
			Thread.sleep(2000);
		}catch(Exception e){}
		paused = false;
		while(true){
			if( touching_horiz_edge(ball) && touching_top(ball) ){
				if(!has_touched){
					vel_x = -vel_x;
					vel_y = -vel_y;
					has_touched = true;
					continue;
				}
			}
			if( touching_horiz_edge(ball) && touching_paddle(ball) ){
				if(!has_touched){
					int cur_vel_x = vel_x;
					if( vel_x > 0 ){
						if( ball.getX() < gm.getPaddle().getX() + (Paddle.PADDLE_WIDTH / 2) ){
							vel_x = -vel_x + ((int)(Math.random()));
						}
					}else if( vel_x < 0 ){
						if( ball.getX() > gm.getPaddle().getX() + (Paddle.PADDLE_WIDTH / 2) ){
							vel_x = -vel_x - ((int)(Math.random()));
						}
					}else;
					if( vel_x == 0 ){
						if(cur_vel_x < 0){
							vel_x = -1;
						}else{
							vel_x = 1;
						}
					}
					vel_y = -vel_y;
					has_touched = true;
				}
			}
			if( touching_horiz_edge(ball) ){
				if(!has_touched){
					vel_x = -vel_x;
					has_touched = true;
				}
			}
			if( touching_top(ball) ){
				if(!has_touched){
					vel_y = -vel_y;
					has_touched = true;
				}
			}
			if( touching_bottom(ball) ){
				vel_x = 0;
				vel_y = 0;
				if(gm.getBalls() == 0){
					if(JOptionPane.showConfirmDialog(this,"You lose! Try again?") == JOptionPane.YES_OPTION){
						gm = new Game( generateBrickField() , 3 );
						gc = new GameCanvas( gm );
						repaint();
					}else{
						break;
					}
				}else{
					if(!first){
						gm.updateBalls(gm.getBalls() - 1);
					}else{
						first = false;
					}
				}
				gm.getPaddle().adjust(gc);
				ball.adjust(gc);
				paused = true;
				try{
					Thread.sleep(2500);
				}catch(Exception e){}
				vel_x = 2;
				vel_y = 2;
				paused = false;
				has_touched = true;
			}
			if( touching_paddle(ball) ){
				if(!has_touched){
					if( vel_x > 0 ){
						if( ball.getX() < gm.getPaddle().getX() + (Paddle.PADDLE_WIDTH / 2) ){
							vel_x = -vel_x + ((int)(Math.random() * 10));
						}
					}else if( vel_x < 0 ){
						if( ball.getX() > gm.getPaddle().getX() + (Paddle.PADDLE_WIDTH / 2) ){
							vel_x = -vel_x + ((int)(Math.random() * 10));
						}
					}else;
					vel_y = -vel_y;
					has_touched = true;
				}
			}
			
			if( (!touching_horiz_edge(ball) && !touching_top(ball) && !touching_bottom(ball) && !touching_paddle(ball)) && has_touched){
				has_touched = false;
			}
			for( Brick brk : gm.getBricks() ){
				if( touching_brick ( ball, brk ) ) {
					gm.cueForRemoval( brk );
					int xedge = ball.getX();
					int xedge_far = ball.getX() + 10;
					
					int yedge = ball.getY();
					int yedge_far = ball.getY();
					
					boolean xcond = (xedge <= brk.getX() + vel_x) || ( xedge_far >= (brk.getX() + Brick.BRICK_WIDTH - vel_x) );
					boolean ycond = ( yedge <= brk.getY() + vel_y) || ( yedge_far >= (brk.getY() + Brick.BRICK_HEIGHT - vel_y) );
					
					if( xcond && !ycond ){
						vel_x = -vel_x;
					}else if( ycond && !xcond ){
						vel_y = -vel_y;
					}else{
						vel_x = -vel_x;
						vel_y = -vel_y;
					}
					
					break;
				}
			}
			if(gm.getBricks().size() == 0){
				vel_x = 0;
				vel_y = 0;
				gm.getPaddle().adjust(gc);
				gm.getBall().adjust(gc);
				JOptionPane.showMessageDialog(this,"Congrats! You win!");
				break;
			}
			if(first){
				first = false;
			}
			ball.move(ball.getX() + vel_x, ball.getY() - vel_y);
			try{
				Thread.sleep(12);
			}catch(Exception e){} //Can ignore an exception.
			repaint();
		}
		setVisible(false); //Animation finished.
		System.exit(0);
	}
	private boolean touching_brick(Ball b, Brick brk){
		int yedge = b.getY();
		int yedge_far = b.getY() + 10;
		int xedge = b.getX();
		int xedge_far = b.getX() + 10;
		int padymin = brk.getY();
		int padymax = brk.getY() + Brick.BRICK_HEIGHT;
		int minx = brk.getX();
		int maxx = brk.getX() + Brick.BRICK_WIDTH;
		
		if(((yedge_far >= padymin && yedge_far <= padymax) || (yedge >= padymin && yedge <= padymax)) && ((xedge >= minx && xedge <= maxx) || xedge_far >= minx && xedge_far <= maxx)){
			return true;
		}
		return false;
	}
	private boolean touching_horiz_edge(Ball b){
		int xedge = b.getX();
		int xedge_far = b.getX()+10;
		if (xedge_far >= getWidth() || xedge <= 0){
			return true;
		}
		return false;
	}
	private boolean touching_top(Ball b){
		int yedge = b.getY();
		if (yedge <= 0){
			return true;
		}
		return false;
	}
	private boolean touching_bottom(Ball b){
		int yedge_far = b.getY() + 10;
		if (yedge_far >= gc.getHeight()){
			return true;
		}
		return false;
	}
	private boolean touching_paddle(Ball b){
		int yedge_far = b.getY() + 10;
		int xedge = b.getX();
		int xedge_far = b.getX() + 10;
		int padymin = gm.getPaddle().getY();
		int padymax = gm.getPaddle().getY() + Paddle.PADDLE_HEIGHT;
		int minx = gm.getPaddle().getX();
		int maxx = gm.getPaddle().getX() + Paddle.PADDLE_WIDTH;
		
		if((yedge_far >= padymin && yedge_far <= padymax) && ((xedge >= minx && xedge <= maxx) || xedge_far >= minx && xedge_far <= maxx)){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args){
		new Breakout().animate();
	}
}
