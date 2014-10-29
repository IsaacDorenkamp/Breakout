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

public class Breakout extends JFrame implements MouseMotionListener{
	private GameCanvas gc;
	private Game gm;
	private final Breakout game = this;
	
	private ComponentAdapter cl = new ComponentAdapter(){
		@Override
		public void componentResized(ComponentEvent evt){
			gm.getPaddle().adjust(game);
			gm.getBall().adjust(game);
			
			while( gm.getPaddle().getY() > getHeight() ){
				gm.getPaddle().adjust(game);
				gm.getBall().adjust(game);
			}
		}
	};
	
	public Breakout() {
		super("Breakout!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,500);
		getContentPane().setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		
		addComponentListener(cl);
		ArrayList<Brick> arrangement = new ArrayList<Brick>();
		
		arrangement.add( new Brick(Color.BLUE,100,100) );
		arrangement.add( new Brick(Color.GREEN,150,100) );
		arrangement.add( new Brick(Color.YELLOW,200,100) );

		gm = new Game( arrangement );
		
		gc = new GameCanvas(gm);
		
		repaint();
		
		add(gc, BorderLayout.CENTER);
		setVisible(true);
		
		addMouseMotionListener(this);
	}
	public void mouseMoved(MouseEvent evt){
		if(!paused){
			Paddle p = gm.getPaddle();
			int pos = p.getX();
		
			if( pos == 0 ){
				if(evt.getX() < pos)
					return;
			}
			if(evt.getX() - 25 >= getWidth() - 25){
					return;
			}
			p.move(evt.getX() - 25, p.getY());
			repaint();
		}
	}
	public void mouseDragged(MouseEvent evt){}
	
	private boolean has_touched = false;
	private boolean paused = false;
	
	public void animate(){
		Ball ball = gm.getBall();
		int vel_x = 6;
		int vel_y = 6;
		has_touched = false;
		while(true){
			if( touching_horiz_edge(ball) && touching_top(ball) ){
				if(!has_touched){
					vel_x = -vel_x;
					vel_y = -vel_y;
					has_touched = true;
					continue;
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
				gm.getPaddle().adjust(this);
				ball.adjust(this);
				paused = true;
				try{
					Thread.sleep(2500);
				}catch(Exception e){}
				vel_x = 6;
				vel_y = 6;
				paused = false;
				has_touched = true;
			}
			if( touching_paddle(ball) ){
				if(!has_touched){
					if( vel_x > 0 ){
						if( ball.getX() < gm.getPaddle().getX() + (Paddle.PADDLE_WIDTH / 2) ){
							if( ball.getX() < gm.getPaddle().getX() + (Paddle.PADDLE_WIDTH / 4) ){
								vel_x = (int) -vel_x / 2;
								if(vel_x == 0){
									vel_x = 1;
								}else if(vel_x > 1){
									vel_x++;
								}else{
									vel_x--;
								}
							}
							if( ball.getX() > gm.getPaddle().getX() + (Paddle.PADDLE_WIDTH / 4) ){
								vel_x = -vel_x;
								if(vel_x == 0){
									vel_x = 1;
								}else if(vel_x > 1){
									vel_x++;
								}else{
									vel_x--;
								}
							}
						}
					}else if( vel_x < 0 ){
						if( ball.getX() > gm.getPaddle().getX() + (Paddle.PADDLE_WIDTH / 2) ){
							if( ball.getX() < gm.getPaddle().getX() + (2*(Paddle.PADDLE_WIDTH / 4)) ){
								vel_x = (int) -vel_x / 2;
								if(vel_x == 0){
									vel_x = 1;
								}else if(vel_x > 1){
									vel_x++;
								}else{
									vel_x--;
								}
							}
							if( ball.getX() > gm.getPaddle().getX() + (2*(Paddle.PADDLE_WIDTH / 4)) ){
								vel_x = -vel_x;
								if(vel_x == 0){
									vel_x = 1;
								}else if(vel_x > 1){
									vel_x++;
								}else{
									vel_x--;
								}
							}
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
				boolean rmvd = false;
				if( touching_brick ( ball, brk ) ) {
					gm.cueForRemoval( brk );
					rmvd = true;
				}
				if(rmvd){
					vel_y = -vel_y;
					vel_x = -vel_x;
				}
			}
			if(gm.getBricks().size() == 0){
				vel_x = 0;
				vel_y = 0;
				gm.getPaddle().adjust(this);
				gm.getBall().adjust(this);
				JOptionPane.showMessageDialog(this,"Congrats! You win!");
				break;
			}
			
			ball.move(ball.getX() + vel_x, ball.getY() - vel_y);
			try{
				Thread.sleep(25);
			}catch(Exception e){} //Can ignore an exception.
			repaint();
		}
		setVisible(false); //Animation finished.
		System.exit(0);
	}
	private boolean touching_brick(Ball b, Brick brk){
		int yedge_far = b.getY() + 10;
		int xedge = b.getX();
		int padymin = brk.getY();
		int padymax = brk.getY() + Brick.BRICK_HEIGHT;
		int minx = brk.getX();
		int maxx = brk.getX() + Brick.BRICK_WIDTH;
		
		if((yedge_far >= padymin && yedge_far <= padymax) && ((xedge >= minx && xedge <= maxx) || xedge_far >= minx && xedge_far <= maxx)){
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
