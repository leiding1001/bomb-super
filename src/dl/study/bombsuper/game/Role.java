package dl.study.bombsuper.game;

import dl.study.bombsuper.main.Info;
import lbq.tools.game.engine.Sprite;

public abstract class Role extends Sprite {
	
	private int niSpeed  ;
	protected byte nbCurDir=-1;
	private CallBack cb;
	public Role(Sprite sprite,CallBack cb) {
		super(sprite);
		this.cb = cb;
	}
	public boolean setState(byte dir){
		if(nbCurDir == dir)
			return false;
		nbCurDir = dir;
		return true;
	}
	public abstract boolean isMoveAble(byte dir);
	
	public boolean move(byte dir,int speed){
		niSpeed = speed;
		switch(dir){
			case Info.NB_DIR_UP:
				return moveUp();
			case Info.NB_DIR_DOWN:
				return moveDown();
			case Info.NB_DIR_LEFT:
				return moveLeft();
			case Info.NB_DIR_RIGHT:
				return moveRight();
			case Info.NB_DIR_NULL:
				return false;
		}
		return false;
	}
	private boolean moveRight() {
		move(niSpeed,0);
		if(isCheck(Info.NB_DIR_RIGHT))
		{
			move(-niSpeed,0);
			return false;
		}
		return true;
	}
	private boolean moveLeft() {
		move(-niSpeed,0);
		if(isCheck(Info.NB_DIR_LEFT))
		{
			move(niSpeed,0);
			return false;
		}
		return true;	}
	private boolean moveDown() {
		move(0,niSpeed);
		if(isCheck(Info.NB_DIR_DOWN))
		{
			move(0,-niSpeed);
			return false;
		}
		return true;
	}
	private boolean moveUp() {
		move(0,-niSpeed);
		if(isCheck(Info.NB_DIR_UP))
		{
			move(0,niSpeed);
			return false;
		}
		return true;
		}
	private boolean isCheck(byte dir){
		return !isMoveAble(dir)||
				cb.isCollideProp(this)||
				cb.isCollideMap(this)||
				cb.isCollideBomb(this);
	}
	public interface CallBack{
		boolean isCollideMap(Sprite spr);
		boolean isCollideBomb(Sprite spr);
		boolean isCollideProp(Sprite spr);
	}
}
