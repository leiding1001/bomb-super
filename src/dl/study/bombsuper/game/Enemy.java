package dl.study.bombsuper.game;

import java.lang.reflect.Array;

import android.graphics.Point;
import dl.study.bombsuper.main.Info;
import dl.study.tools.Tools;
import lbq.tools.game.engine.Sprite;

public  class Enemy extends Role{
	private static final int NI_SPACE_MIN_TIME = 30 ;
	private static final int NI_SPACE_MAX_TIME = 50 ;
	private static final int NI_SPEED_MAX = 5*2/3;
	private static final int NI_SPEED_MIN = 3*2/3;
	private CallBack enemyCb;

	private final  int NI_SPACE_NUM;
	private int niSpaceNum=0;
	private int niEnemySpeed=0;
	public Enemy(Sprite sprite, CallBack cb,int rows,int cols,int tileWidth,int tileHeight) {
		super(sprite, cb);
		enemyCb=cb;
		Point pMap = enemyCb.getMapPoint();
		int row,col;
		int niX,niY;
		do{
			row = Tools.getRandom(2,rows);
			col = Tools.getRandom(2, cols);
			niX = col * tileWidth;
			niY = row * tileHeight;
			setPosition(pMap.x+niX, pMap.y+niY);
		}while(enemyCb.isCollideMap(this));
		changeDirection((byte)-1);
		NI_SPACE_NUM = Tools.getRandom(NI_SPACE_MIN_TIME,NI_SPACE_MAX_TIME);
		niEnemySpeed= Tools.getRandom(NI_SPEED_MIN, NI_SPEED_MAX);
		this.defineCollisionRectangle(12*2/3, 12*2/3, getWidth()-25*2/3, getHeight()-20*2/3);
	}
	@Override
	public boolean isMoveAble(byte dir) {
		Point pMap = enemyCb.getMapPoint();
		switch(dir){
			case Info.NB_DIR_UP:
				return this.getY()>pMap.y;
			case Info.NB_DIR_DOWN:
				return this.getY()+this.getHeight()<pMap.y+enemyCb.getMapHeight();
			case Info.NB_DIR_LEFT:
				return this.getX()>pMap.x;
			case Info.NB_DIR_RIGHT:
				return this.getX()+this.getWidth()<pMap.x+enemyCb.getMapWidth();
		}
		return false;
	}
	public boolean setState(byte dir) {
		if(super.setState(dir)){
			setFrameSequence(Info.arrAnimationSeq[dir]);
			setFrame(0);
			return true;
		}
		return false;
	}
	@Override
	public boolean move(byte dir,int niSpeed) {
		if(super.move(dir,niSpeed))
			return true;
		changeDirection(nbCurDir);
		return false;
	}
	private void changeDirection(byte dir) {
		niSpaceNum = 0 ;
		byte randomDir;
		randomDir=(byte)Tools.getRandom(0, 4);
		if(dir != -1){
			while(dir==randomDir)
				randomDir=(byte)Tools.getRandom(0, 4);
		}
		setState(randomDir);
	}
	@Override
	public void logic() {
		super.logic();
		move(nbCurDir,niEnemySpeed);
		if(enemyCb.isCollidePlayer(this))
			changeDirection(nbCurDir);
		if((++niSpaceNum)>NI_SPACE_NUM)
			changeDirection((byte)-1);
	}
	
	public interface CallBack extends Role.CallBack{
		boolean isExists(int row,int col);
		boolean isCollidePlayer(Sprite spr);
		Point getMapPoint();
		int getMapWidth();
		int getMapHeight();
	}
}
