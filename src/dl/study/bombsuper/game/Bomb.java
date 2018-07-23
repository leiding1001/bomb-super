package dl.study.bombsuper.game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import dl.study.bombsuper.main.Info;
import dl.study.bombsuper.main.GameController.BulletCallBack;
import dl.study.tools.Tools;


public class Bomb {
	private static final int NI_BOMB_SLEEP_TIME = 3000;
	private static final Bitmap bmp = Tools.getBmpFromAssetManager("image/bomb/bomb/bomb_64.png");
	public static final int NI_BOMB_WIDTH = bmp.getWidth();
	public static final int NI_BOMB_HEIGHT = bmp.getHeight();
	/** 玩家是否能在炸弹上移动 默认是true **/
	private  boolean isPlayerMoveAble;
	private Rect rectBomb;
	private ArrayList<Bullet> listBullet;
	private  Timer timer ;
	private BulletCallBack cb;
	private int size;
	public Bomb(BulletCallBack cb,Rect rect,int size) {
		this.cb = cb;
		this.size = size;
		isPlayerMoveAble = true;
		rectBomb = rect;
		listBullet = new ArrayList<Bullet>();
		//Bullet.NI_BULLET_NUM
	}
	public void start(){
		if(timer==null)
			timer = new Timer();
		timer.schedule(new BombTimer(), NI_BOMB_SLEEP_TIME);
	}
	private void startThread(){
		for(int i=0,iLen=listBullet.size();i<iLen;i++){
			listBullet.get(i).startThread(Bullet.NI_SLEEPT_TIME);
		}
	}
	@SuppressLint("WrongCall")
	public void onDraw(Canvas canvas , Paint  paint){
		if(listBullet.isEmpty())
			canvas.drawBitmap(bmp, rectBomb.left, rectBomb.top, null);
		else{
			for(int i=0;i<listBullet.size();i++)
				listBullet.get(i).onDraw(canvas, null);
		}
	}
	public void move(byte dir,int speed){
		if(listBullet.isEmpty())
			switch(dir){
			case Info.NB_DIR_UP:
				rectBomb.set(rectBomb.left, rectBomb.top-speed, rectBomb.right, rectBomb.bottom-speed);
				break;
			case Info.NB_DIR_DOWN:
				rectBomb.set(rectBomb.left, rectBomb.top+speed, rectBomb.right, rectBomb.bottom+speed);
				break;
			case Info.NB_DIR_LEFT:
				rectBomb.set(rectBomb.left-speed, rectBomb.top, rectBomb.right-speed, rectBomb.bottom);
				break;
			case Info.NB_DIR_RIGHT:
				rectBomb.set(rectBomb.left+speed, rectBomb.top, rectBomb.right+speed, rectBomb.bottom);
				break;
			}
		for(int i=0;i<listBullet.size();i++){
			listBullet.get(i).move(dir, speed);
		}
	}
	public Rect getBombtRect(){
		return rectBomb;
	}
	/**
	 * 获取玩家是否能在炸弹上移动
	 * @return
	 */
	public boolean isPlayerMoveAble(){
		return isPlayerMoveAble;
	}
	public void setPlayerMoveAble(boolean isMoveAble){
		isPlayerMoveAble = isMoveAble ;
	}
	public boolean isBombing(){
		return !listBullet.isEmpty();
	}
	public boolean isDead(){
		if(listBullet.isEmpty())
			return false;
		for(int i=0;i<listBullet.size();i++)
			if(listBullet.get(i).isThread()){
				return false;
			}
		return true;
	}
	public class BombTimer extends TimerTask{
		@Override
		public void run() {
			Point point = new Point((rectBomb.left+rectBomb.right)/2,(rectBomb.top+rectBomb.bottom)/2);
			listBullet.add(new Bullet(cb,point,size,Info.NB_DIR_UP));
			listBullet.add(new Bullet(cb,point,size,Info.NB_DIR_DOWN));
			listBullet.add(new Bullet(cb,point,size,Info.NB_DIR_LEFT));
			listBullet.add(new Bullet(cb,point,size,Info.NB_DIR_RIGHT));
			startThread();
		}
	}
	/*****
	 * 结束点
	 */
}
