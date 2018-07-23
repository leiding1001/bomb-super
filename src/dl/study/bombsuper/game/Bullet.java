package dl.study.bombsuper.game;

import lbq.tools.game.engine.Sprite;
import dl.study.bombsuper.main.Info;
import dl.study.tools.Tools;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Bullet extends Sprite{
	private static final Bitmap bmpBullet = Tools.getBmpFromAssetManager("image/bomb/bullet/0.png");
	public static final int NI_BULLET_NUM = 4;
	public static final int NI_MIN_MOVE_DIS = 32;
	public  static final int NI_SLEEPT_TIME = 40;
	private static final int niSpeed = (int) (10*Info.NI_SCALE_NUM);
	private int niMoveMaxLength;
	private Point pointBullet;
	private int niDisMapX,niDisMapY;
	private byte nbCurDir;
	private CallBack cb;
	public Bullet(CallBack cb,Point point,int size,byte dir) {
		super(bmpBullet,bmpBullet.getWidth()/8, bmpBullet.getHeight());
		this.cb = cb ;
		this.nbCurDir = dir;
		this.niMoveMaxLength = size * NI_MIN_MOVE_DIS;
		this.pointBullet = new Point(point.x-getWidth()/2, point.y-getHeight()/2);
		niDisMapX=pointBullet.x-cb.getMapPoint().x;
		niDisMapY=pointBullet.y-cb.getMapPoint().y;
		setPosition(pointBullet.x,pointBullet.y);
		setFrame(Tools.getRandom(0, 8));
	}
	
	@Override
	public void onDraw(Canvas canvas, Paint paint) {
		if(isThread())
			super.onDraw(canvas, paint);
	}

	@Override
	public void logic() {
			super.logic();
			if(!move(nbCurDir,niSpeed)){
				dead();
		}
	}
	private void dead() {
	//	Tools.testInfo("---------线程关闭---------------");
		this.closeThread();
		cb.notifyCloseThread();
	}
	public boolean move(byte dir,int speed) {
		switch(dir){
			case Info.NB_DIR_UP:
				return moveUp(dir,speed);
			case Info.NB_DIR_DOWN:
				return moveDown(dir,speed);
			case Info.NB_DIR_LEFT:
				return moveLeft(dir,speed);
			case Info.NB_DIR_RIGHT:
				return moveRight(dir,speed);
		}
		return false;
	}
	private boolean moveRight(byte dir,int speed) {
		if(isMoveAble(dir,speed))
			{
			move(speed, 0);
			return true;
			}
		else
			return false;
	}
	private boolean moveLeft(byte dir,int speed) {
		if(isMoveAble(dir,speed))
		{
			move(-speed, 0);
			return true;
		}
		else
			return false;
	}
	private boolean moveDown(byte dir,int speed) {
		if(isMoveAble(dir,speed))
		{
			move(0,speed);
			return true;
		}
		else
			return false;
	}
	private boolean moveUp(byte dir,int speed) {
		if(isMoveAble(dir,speed))
		{
			move(0,-speed);
			return true;
		}
		else
			return false;
	}
	public boolean isOverDistance(byte dir){
		pointBullet.set(niDisMapX+cb.getMapPoint().x,niDisMapY+cb.getMapPoint().y);
		switch(dir){
		case Info.NB_DIR_UP:
			if(pointBullet.y-getY()>niMoveMaxLength)
				return true;
			break;
		case Info.NB_DIR_DOWN:
			if(getY()-pointBullet.y>niMoveMaxLength)
				return true;
			break;
		case Info.NB_DIR_LEFT:
			if(pointBullet.x-getX()>niMoveMaxLength)
				return true;
			break;
		case Info.NB_DIR_RIGHT:
			if(getX()-pointBullet.x>niMoveMaxLength)
				return true;
			break;
		}
		return false;
	}
	private boolean isMoveAble(byte dir,int speed) {
		//预移动
		//是否超出移动的最大距离
		switch(dir){
			case Info.NB_DIR_UP:
				move(0,-speed);
				break;
			case Info.NB_DIR_DOWN:
				move(0,speed);
				break;
			case Info.NB_DIR_LEFT:
				move(-speed,0);
				break;
			case Info.NB_DIR_RIGHT:
				move(speed,0);
				break;
		}
		if(isOverDistance(dir))
			return false;
		//是否与障碍物层地图碰撞
		if(cb.isCollideBarrier(this))
			return false;
		//是否与掩饰层地图碰撞
		if(cb.isCollideConver(this))
			return false;
		//是否与玩家碰撞
		if(cb.isCollidePlayer(this))
			return false;
		//是否与敌人碰撞
		if(cb.isCollideEnemy(this))
			return false;
		//还原预移动
		switch(dir){
			case Info.NB_DIR_UP:
				move(0,speed);
				break;
			case Info.NB_DIR_DOWN:
				move(0,-speed);
				break;
			case Info.NB_DIR_LEFT:
				move(speed,0);
				break;
			case Info.NB_DIR_RIGHT:
				move(-speed,0);
				break;
		}		
		//是否超出地图边界
		Point pMap = cb.getMapPoint();
		switch(dir){
			case Info.NB_DIR_UP:
				return this.getY()>pMap.y;
			case Info.NB_DIR_DOWN:
				return this.getY()+this.getHeight()<pMap.y+cb.getMapHeight();
			case Info.NB_DIR_LEFT:
				return this.getX()>pMap.x;
			case Info.NB_DIR_RIGHT:
				return this.getX()+this.getWidth()<pMap.x+cb.getMapWidth();
		}
		return false;
	}
	public interface CallBack {
		boolean isCollidePlayer(Sprite spr);
		boolean isCollideEnemy(Sprite spr);
		boolean isCollideConver(Sprite spr);
		boolean isCollideBarrier(Sprite spr);
		void notifyCloseThread();
		int getMapWidth();
		int getMapHeight();
		Point getMapPoint();
	}

}
