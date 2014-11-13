package steve.breakout.gui;

import steve.breakout.game.Game;
import steve.breakout.specs.*;
import steve.breakout.framework.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Random;

public class Breakout extends JFrame implements MouseMotionListener{
	private GameCanvas gc;
	private Game gm;
	
	private JButton emergency = new JButton("EMERGENCY ADJUST");
	private JPanel bpan = new JPanel();
	
	private ResizeListener cl;
	public Breakout() {
		super("Breakout!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,500);
		getContentPane().setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		
		addComponentListener(cl);
		
 
		gm = new Game( 3 );
		gm.setBricks( generateBrickField() );
		gc = new GameCanvas(gm);
		cl = new ResizeListener(this, gc, gm);
		
		emergency.setBackground( Color.RED );
		emergency.addActionListener( new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					gm.getPaddle().adjust(gc);
					gm.getBall().adjust(gc);
				}
		});
		bpan.add(emergency);
		bpan.setOpaque(false);
		
		add(gc, BorderLayout.CENTER);
		add(bpan, BorderLayout.SOUTH);
		setVisible(true);
		
		gm.getPaddle().adjust(gc);
		gm.getBall().adjust(gc);
		
		addMouseMotionListener(this);
	}
	public boolean isPaused(){
		return paused;
	}
	public ArrayList<Brick> generateBrickField(){
		ArrayList<Brick> arrangement = new ArrayList<Brick>();
		
		Color[] cols = {Color.BLUE,Color.YELLOW,Color.MAGENTA,Color.WHITE,Color.GRAY};
		
		Random r = new Random();

		for(int y = Brick.BRICK_HEIGHT; y < 274; y += Brick.BRICK_HEIGHT){
			for(int x = 100; x < getWidth() - Brick.BRICK_WIDTH; x += Brick.BRICK_WIDTH){
				boolean dobrick = r.nextBoolean();
				int brktype = (int)(Math.random() * 6);
				if(dobrick){
					Color col = cols[(int)(Math.random()*4)];
					if(brktype >= 5.8){
						arrangement.add( new PowerupBrick(col, x,y, new LongerPaddle(gm, x, y), gm) );
						continue;
					}
					switch(brktype){
					case 0:
						arrangement.add( new MovingBrick( col, x, y, x + Brick.BRICK_WIDTH, y ) );
						x += Brick.BRICK_WIDTH;
						break;
					case 1:
						int life = (int)((Math.random() * 3) + 1);
						if( life == 1){
							life++;
						}
						arrangement.add( new MultiBrick( x, y, life ) );
						break;
					default:
						arrangement.add( new Brick ( col, x, y ) );
					}
				}
			}
		}
		return arrangement;
	}
	public void mouseMoved(MouseEvent evt){
		if(!paused){
			Paddle p = gm.getPaddle();
			cuePaddleMove(evt.getX() - (Paddle.PADDLE_WIDTH / 2));
			repaint();
		}
	}
	public void mouseDragged(MouseEvent evt){}
	
	int vel_x = 4;
	int vel_y = 4;
	
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
	
	private boolean move_paddle = false;
	private int dest_x = 0;
	
	private void cuePaddleMove(int x){
		move_paddle = true;
		dest_x = x;
	}
	
	public void animate(){
		Paddle p = gm.getPaddle();
		Ball ball = gm.getBall();
		
		p.adjust(gc);
		ball.adjust(gc);
		repaint();
		
		has_touched = true; //Touching paddle at beginning.
		boolean first = true;
		paused = true;
		try{
			Thread.sleep(2000);
		}catch(Exception e){}
		paused = false;
		while(true){
			for( Powerup pow : gm.getPowers() ){
				if( touching_powerup(pow, gm.getPaddle()) ){
					pow.onReception();
					gm.cuePowerForRemoval(pow);
				}else if( touching_bottom(pow) ){
					gm.cuePowerForRemoval(pow);
				}else pow.updatePosition();
			}
			if(move_paddle){
				p.move(dest_x,Sprite.getWorldY(15, gc.getHeight()));
				move_paddle = false;
			}
			fix_corner(ball);
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
						gm = new Game( 3 );
						gm.setBricks( generateBrickField() );
						gc.setGame(gm);
						ball = gm.getBall();
						p = gm.getPaddle();
						p.adjust(gc);
						ball.adjust(gc);
						Paddle.PADDLE_WIDTH = Paddle.FIRST_PADDLE_WIDTH;
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
				p.adjust(gc);
				ball.adjust(gc);
				paused = true;
				repaint();
				try{
					Thread.sleep(2500);
				}catch(Exception e){}
				vel_x = 4;
				vel_y = 4;
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
					}else{
						vel_x = 1;
					};
					vel_y = -vel_y;
					has_touched = true;
				}
			}
			boolean cond = ( !touching_paddle(ball) && !touching_bottom(ball) && !touching_top(ball) && !touching_horiz_edge(ball) );
			if( has_touched && cond){
				has_touched = false;
			}
			ArrayList<Brick> toCue = new ArrayList<Brick>();
			for( Brick brk : gm.getBricks() ){
				if(brk instanceof MovingBrick){
					( (MovingBrick) brk).updatePosition();
				}
				if( touching_brick ( ball, brk ) ) {
					brk.loseHealth();
					if(brk.getHealth() == 0){
						toCue.add( brk );
					}
					
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
			for( Brick brk : toCue ){
				gm.cueForRemoval( brk );
			}
			if(gm.getBricks().size() == 0){
				vel_x = 0;
				vel_y = 0;
				paused = true;
				p.adjust(gc);
				ball.adjust(gc);
				int playagain = JOptionPane.showConfirmDialog(this, "Congratulations, you won! Play again?");
				if( playagain == JOptionPane.YES_OPTION){
					gm = new Game( 3 );
					gm.setBricks( generateBrickField() );
					gc.setGame( gm );
					ball = gm.getBall();
					p = gm.getPaddle();
					p.adjust(gc);
					ball.adjust(gc);
					Paddle.PADDLE_WIDTH = Paddle.FIRST_PADDLE_WIDTH;
				}
				continue;
			}
			if(first){
				first = false;
			}
			if(vel_x == 0) vel_x = 1;
			ball.move(ball.getX() + vel_x, ball.getY() - vel_y);
			Toolkit.getDefaultToolkit().sync();
			repaint();
			try{
				Thread.sleep(25);
			}catch(Exception e){}
		}
		setVisible(false); //Animation finished.
		System.exit(0);
	}
	private void fix_corner(Ball b){
		int xedge = b.getX();
		int xedge_far = b.getX()+10;
		int yedge = b.getY();
		int yedge_far = b.getY()+10;
		boolean xcond_right = ( xedge_far >= (getWidth() - 10) );
		boolean xcond_left = xedge <= 10 ;
		boolean ycond_top = yedge <= 10 ;
		boolean ycond_bottom = ( yedge_far >= (getHeight() - 10) );
		
		int newx = b.getX();
		int newy = b.getY();
		
		if( xcond_right && (ycond_top || ycond_bottom) ){
			vel_x = -vel_x;
			newx = getWidth() - 10;
		}else if( xcond_left && (ycond_top || ycond_bottom) ){
			vel_x = -vel_x;
			newx = 10;
		}else;
		if( ycond_top && (xcond_left || xcond_right) ){
			vel_y = -vel_y;
			newy = 10;
		}else if( ycond_bottom && (xcond_left || xcond_right) ){
			vel_y = -vel_y;
			newy = getHeight() - 10;
		}else;
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
	private boolean touching_powerup(Powerup p, Paddle s){
		int yedge = p.getY();
		int yedge_far = p.getY() + Powerup.POWERUP_HEIGHT;
		int xedge = p.getX();
		int xedge_far = p.getX() + Powerup.POWERUP_WIDTH;
		int padymin = s.getY();
		int padymax = s.getY() + Paddle.PADDLE_HEIGHT;
		int minx = s.getX();
		int maxx = s.getX() + Paddle.PADDLE_WIDTH;
		
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
	private boolean touching_bottom(Sprite s){
		int yedge_far = s.getY() + 10;
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
