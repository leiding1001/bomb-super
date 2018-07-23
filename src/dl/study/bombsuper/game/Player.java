package dl.study.bombsuper.game;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import dl.study.bombsuper.main.Info;
import dl.study.tools.Tools;
public class Player extends Role{
	private static final short NI_SLEEP_MIN_TIME = 50;
	private static final short NI_SLEEP_NORMAL_TIME = 100;
	private static final short NI_BOMB_MAX_NUM = 4;
	private static int niPlayerSpeed = Info.NI_MOVE_SPEED;
	private int niBombNum ;
	private boolean isMoving ;
	private boolean isStop ;
	private CallBack callback;
	private int niCurGate;
	private static int niPassMaxGate  ; 
	public Player(int gate,int passMaxGate, int niType,CallBack cb){
		super(SpriteFactory.getInstance().make(niType),cb);
			niPassMaxGate = passMaxGate;
			callback = cb ;
			reset(gate);
		this.defineCollisionRectangle((int)(6*Info.NI_SCALE_NUM),(int)(30*Info.NI_SCALE_NUM), getWidth()-(int)(12*Info.NI_SCALE_NUM), (int)(30*Info.NI_SCALE_NUM));
	}
	public void reset(int gate) {
		Tools.testInfo("--------------???--Player.reset----???-------------");
		Bitmap bmp = null;
		if(gate!=niCurGate)	
		{
			bmp= getBitmap(gate);
			if(bmp!=null)
				setBitmap(bmp, bmp.getWidth()/4, bmp.getHeight()/4);
		}
		this.niCurGate = gate;
		this.nsSleepTime = (short) (NI_SLEEP_NORMAL_TIME - 2*(gate-1)) ;
		if(this.nsSleepTime<NI_SLEEP_MIN_TIME)
			this.nsSleepTime = NI_SLEEP_MIN_TIME;
		niBombNum = gate;
		if(niBombNum>NI_BOMB_MAX_NUM)
			niBombNum = NI_BOMB_MAX_NUM;
		isStop = true;
		isMoving = false;
		setPosition(32,32);
	}
	
	public void startThread() {
			startThread(nsSleepTime);
	}

	private Bitmap getBitmap(int gate){
		switch(gate){
		case 1:
			return SpriteFactory.getInstance().getSpriteBitmap(SpriteFactory.NI_SPRITE_TYPE_PLAYER1);
		case 2:
			return SpriteFactory.getInstance().getSpriteBitmap(SpriteFactory.NI_SPRITE_TYPE_PLAYER2);
		case 3:
			return SpriteFactory.getInstance().getSpriteBitmap(SpriteFactory.NI_SPRITE_TYPE_PLAYER3);
		}
		return null;
	}
	public boolean setState(byte dir,boolean isMoveAble) {
		isStop = false;
		isMoving = isMoveAble;
		if(setState(dir)){
			this.setFrameSequence(Info.arrAnimationSeq[dir]);
			this.setFrame(0);
			return true;
		}
		return false;
	}
	@SuppressLint("WrongCall")
	public void onDraw(Canvas canvas){
		if(isThread())
			onDraw(canvas, null);
	}
	@Override
	public boolean isMoveAble(byte dir) {
		switch(dir){
			case Info.NB_DIR_UP:
				return this.getY()>=0;
			case Info.NB_DIR_DOWN:
				return this.getY()+this.getHeight()<=Info.NI_SCREEN_HEIGHT;
			case Info.NB_DIR_LEFT:
				return this.getX()>=0;
			case Info.NB_DIR_RIGHT:
				return this.getX()+this.getWidth()<=Info.NI_SCREEN_WIDTH;
			}
		return false;
	}
	public boolean isCenterINScreen(byte dir){
		switch(dir){
			case Info.NB_DIR_UP:
				return this.getY()<Info.NI_SCREEN_HEIGHT/2 ;
			case Info.NB_DIR_DOWN:
				return this.getY()+this.getHeight()>Info.NI_SCREEN_HEIGHT/2 ;
			case Info.NB_DIR_LEFT:
				return this.getX()<Info.NI_SCREEN_WIDTH/2 ;
			case Info.NB_DIR_RIGHT:
				return this.getX()+this.getWidth()>Info.NI_SCREEN_WIDTH/2 ;
			}
		return false;
	}
	@Override
	public void logic() {
		if(!isStop){
			//重新检测一下(由不知道什么原因吃不掉道具的奖励)
			//Tools.testInfo("由不知道什么原因碰撞后道具不消失");
			callback.isCollideProp(this);
			super.logic();
			//判断是否可以移动地图
			if(isCenterINScreen(nbCurDir)&&!callback.moveMap(nbCurDir,niPlayerSpeed))
					isMoving = true;
			//移动玩家角色
			if(isMoving)
					move(nbCurDir,niPlayerSpeed);
		}
	}
	public void standUp() {
		isStop = true;
		this.setFrame(0);
	}
	
	public Rect getRect() {
		Rect r = super.getRect();
		return new Rect(r.left+(int)(12*Info.NI_SCALE_NUM),r.top+(int)(12*Info.NI_SCALE_NUM),r.left+(int)(12*Info.NI_SCALE_NUM+getWidth()-25*Info.NI_SCALE_NUM),r.top+(int)(12*Info.NI_SCALE_NUM)+getHeight()-(int)(20*Info.NI_SCALE_NUM));
	}
	public int getBombNum(){return niBombNum;}
	private void setBombNum(int num){ niBombNum = num ;}
	public byte getCurDir() {return nbCurDir;}
	public void reward(byte niType){
		switch(niType){
			case PropFactory.NB_PROP_ADD_BOMB:
				setBombNum(niBombNum+1);
				break;
			case PropFactory.NB_PROP_KEY:
				//next();
				niCurGate++;
				if(this.niCurGate>niPassMaxGate)
					niPassMaxGate = niCurGate ;
				niCurGate--;
				callback.notifyPass();
				break;
			case PropFactory.NB_PROP_ADD_SPEED:
				this.nsSleepTime -= 5;
				if(this.nsSleepTime<NI_SLEEP_MIN_TIME)
					this.nsSleepTime = NI_SLEEP_MIN_TIME;
				break;
		}
	}
	public int getPassMaxGate(){
		return  niPassMaxGate;
	}
	public int getCurGate(){
		return  niCurGate;
	}

	public interface CallBack extends Role.CallBack{
		boolean moveMap(byte dir,int niSpeed);
		void notifyPass();
	}


}
