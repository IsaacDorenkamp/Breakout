package steve.breakout.specs;

import steve.breakout.game.Game;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LongerPaddle extends Powerup {

	private BufferedImage img = null;
	
	private Thread run = new Thread(){
		@Override
		public void run(){
			try{
				Thread.sleep(15 * 1000);
			}catch(Exception e){}
			Paddle.PADDLE_WIDTH = Paddle.PADDLE_WIDTH - 30;
		}
	};
	
	public LongerPaddle(Game gm, int x, int y){
		super(gm,x,y);
		try{
			img = ImageIO.read( getClass().getResourceAsStream("LongPad_Powerup.png") );
		}catch(IOException ioe){
			System.out.println("ERROR: Could not load image.");
		}
	}
	public LongerPaddle(Game gm){
		this(gm,0,0);
	}
	public void onReception(){
		Paddle.PADDLE_WIDTH = Paddle.PADDLE_WIDTH + 30;
		run.start();
	}
	
	public void paint( Graphics grfx ){
		if(img != null){
			int w = img.getWidth();
			int h = img.getHeight();
			grfx.drawImage(img, getX(), getY(), w, h, null);
		}else{
			grfx.drawRect( getX(), getY(), Powerup.POWERUP_WIDTH, Powerup.POWERUP_HEIGHT );
			grfx.drawString( "L", getX()+5, getY()+Powerup.POWERUP_HEIGHT - 3 );
		}
	}
}