package dl.study.bombsuper.main;

import java.io.FileNotFoundException;

import lbq.tools.game.engine.Sprite;
import lbq.tools.game.engine.Sprite.TileLayerCollideAttribute;
import lbq.tools.game.engine.TiledLayer;
import dl.study.bombsuper.game.BombManager;
import dl.study.bombsuper.game.Bullet;
import dl.study.bombsuper.game.CallBackResult;
import dl.study.bombsuper.game.Enemy;
import dl.study.bombsuper.game.EnemyManager;
import dl.study.bombsuper.game.Handler;
import dl.study.bombsuper.game.Map;
import dl.study.bombsuper.game.MapFactory;
import dl.study.bombsuper.game.Player;
import dl.study.bombsuper.game.Prop;
import dl.study.bombsuper.game.PropManager;
import dl.study.bombsuper.game.Role;
import dl.study.bombsuper.game.SpriteFactory;
import dl.study.bumbsuper.main.R;
import dl.study.engine.MonitorBase;
import dl.study.tools.Tools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Message;
import android.view.MotionEvent;

public class GameController extends MonitorBase{
	private boolean isGameOver ;
	private Point[] arrPoint;
	private Paint paint;
	private Map map;
	private Handler handler;
	private Player player ;
	private EnemyManager eManager;
	private BombManager bManager;
	private PropManager propManager;
	private CallBackResult callBackResult;
	private int passMaxGate ;
	public  GameController(Context context,int gate, int passMaxGate) {
		super(context);
		arrPoint = new Point[2];
		arrPoint[0]=new Point(-1,-1);
		arrPoint[1]=new Point(-1,-1);
		paint = new Paint();
		callBackResult = new CallBackResult(new ResultCallBack());
		handler = new Handler(new HandlerCallback());
		bManager = new BombManager(new BulletCallBack());
		this.passMaxGate = passMaxGate;
		reset(gate);
	}
	public void reset(int gate){
		Tools.SOUND.init(context, Info.NI_SOUND_TYPE_MAP);
		Tools.SOUND.startMediaPlayer(R.raw.map, true);
		isGameOver = false;
			map = MapFactory.getInstance().makeMap(gate);
			map.addCallback(new MapCallBack());
		if(player==null){
			player = new Player(
					gate,passMaxGate,
					SpriteFactory.NI_SPRITE_TYPE_PLAYER1,
					new PlayerCallBack()
					);
		}
		else
			player.reset(gate);
		if(eManager==null){
			eManager = new EnemyManager(
					gate,
					new EnemyCallBack(),
					map.getMapRows(),
					map.getMapCols(),
					map.getMapTileWidth(),
					map.getMapTileHeight()
					);
		}else
			eManager.reset(gate);
		if(propManager==null)
			propManager = new PropManager(map.getTileCover());
		else
			propManager.reset(map.getTileCover());
		//开启重回线程
				startThread(50);
				player.startThread();
				eManager.startThread(100);
				
				
	}
	@SuppressLint("WrongCall")
	@Override
	protected void onDraw(Canvas canvas) {
		if(!isGameOver){
			map.onDraw(canvas);
			propManager.onDraw(canvas, paint);
			map.onDrawCover(canvas);
//			propManager.onDraw(canvas, paint);
			//propManager.onDrawCollide(canvas, paint);
			if(bManager!=null)
				bManager.onDraw(canvas, null);
			eManager.onDraw(canvas, paint);
			player.onDraw(canvas);
//			player.onDrawCollides(canvas);
			handler.onDraw(canvas);
		}else{
			callBackResult.onDraw(canvas);
		}
	}
	
	/**
	 * 移动地图
	 * @param index
	 */
	private void move(byte index,int niSpeed) {
			map.move(index,niSpeed);
			eManager.move(Tools.translateDirection(index),niSpeed);
			bManager.move(Tools.translateDirection(index),niSpeed);
			propManager.move(Tools.translateDirection(index),niSpeed);
			player.setState(index, false);
	}
	public class ResultCallBack implements CallBackResult.CallBack{

		@Override
		public void notifyRestart() {
			Tools.SOUND.stopMediaPlayer(R.raw.lose);
			reset(player.getCurGate());
		}

		@Override
		public void notifyNext() {
			Tools.SOUND.stopMediaPlayer(R.raw.win);
			reset(player.getCurGate()+1);
		}

		@Override
		public void notifyBack() {
			back();
		}
		
	}
	
	public class RoleCallBack implements Role.CallBack{
		@Override
		public boolean isCollideMap(Sprite spr) {
			return map.collideSprite(spr);
		}

		@Override
		public boolean isCollideBomb(Sprite spr) {
			return bManager.isCollideSprite(spr);
		}

		@Override
		public boolean isCollideProp(Sprite spr) {
			Prop prop = propManager.isCollide(spr);
			if(prop!=null)
				{
				if(spr instanceof Player)
					{
						((Player)spr).reward(prop.getType());
						propManager.removeProp(prop);
					}
				return true;
				}
			else
				return false;
		}	
	}
	public class PlayerCallBack extends RoleCallBack implements Player.CallBack {
		@Override
		public boolean moveMap(byte dir,int niSpeed) {
			if(map.isMoveAble(dir,niSpeed)&&bManager.isMoveAble(Tools.translateDirection(dir),niSpeed,player)){
				move(dir,niSpeed);
				return true;
			}
			return false;
		}

		@Override
		public void notifyPass() {
			isGameOver = true;
			closeOtherThread();
			Tools.SOUND.pauseMediaPlayer(R.raw.map);
			Tools.SOUND.init(context, Info.NI_SOUND_TYPE_WIN);
			Tools.SOUND.startMediaPlayer(R.raw.win,true);
			callBackResult.result(CallBackResult.NI_TYPE_WIN);
		}

	}
	public class BulletCallBack implements Bullet.CallBack{

		public boolean isCollidePlayer(Sprite spr) {
			if(player.collidesWith(spr)){
				closeOtherThread();
				isGameOver = true;
				Tools.SOUND.pauseMediaPlayer(R.raw.map);
				Tools.SOUND.init(context, Info.NI_SOUND_TYPE_LOSE);
				Tools.SOUND.startMediaPlayer(R.raw.lose,true);
				callBackResult.result(CallBackResult.NI_TYPE_LOSE);
				return true;
			}
			return false;
		}
		public boolean isCollideEnemy(Sprite spr) {
			if(eManager.isCollidesWithSprite(spr)){
				return true;
			}
			return false;
		}
		public boolean isCollideConver(Sprite spr) {
			TiledLayer tileCover = map.getTileCover();
			if(tileCover!=null&&spr.collidesWith(tileCover))
			{
				TileLayerCollideAttribute tileAttr= spr.collidesWith_Attribute(tileCover);
				map.setTileCoverValue(player.getCurDir(),tileAttr.getColumn(), tileAttr.getRow(), 0);
				return true;
			}
			return false;
		}
		public boolean isCollideBarrier(Sprite spr) {
			return spr.collidesWith(map.getTileBarrier());
		}
		@Override
		public Point getMapPoint() {
			return map.getMapPoint();
		}
		@Override
		public int getMapWidth() {
			return map.getMapWidth();
		}
		@Override
		public int getMapHeight() {
			return map.getMapHeight();
		}
		@Override
		public void notifyCloseThread() {
			bManager.removeBomb();
		}
	}
	public class EnemyCallBack extends RoleCallBack implements Enemy.CallBack {

		@Override
		public boolean isExists(int row, int col) {
			return map.isExists(row, col);
		}
		@Override
		public boolean isCollidePlayer(Sprite spr) {
			if(spr.collidesWith(player)){
				closeOtherThread();
				isGameOver = true;
				Tools.SOUND.pauseMediaPlayer(R.raw.map);
				Tools.SOUND.init(context, Info.NI_SOUND_TYPE_LOSE);
				Tools.SOUND.startMediaPlayer(R.raw.lose,true);
				callBackResult.result(CallBackResult.NI_TYPE_LOSE);
				return true;
			}
			return false;
		}

		@Override
		public Point getMapPoint() {
			return map.getMapPoint();
		}
		@Override
		public int getMapWidth() {
			return map.getMapWidth();
		}
		@Override
		public int getMapHeight() {
			return map.getMapHeight();
		}
	}
	public class MapCallBack implements Map.Callback{

		@Override
		public boolean isCollideSprite(TiledLayer collide,TiledLayer cover) {
			if(player.collidesWith(cover)||player.collidesWith(collide)){
				return true;
			}
			return false;
		}
	}
	
	public void closeOtherThread(){
		player.closeThread();
		eManager.removeAll();
	}
	public void back(){
		Tools.SOUND.stopMediaPlayer(R.raw.map);
		Tools.SOUND.stopMediaPlayer(R.raw.lose);
		Tools.SOUND.stopMediaPlayer(R.raw.win);
		closeOtherThread();
		closeMonitorThread();
		Message msg = new Message();
		msg.arg1=player.getCurGate();
		msg.arg2=player.getPassMaxGate();
		msg.what=1;
		Main.handler.sendMessage(msg);
	}
	public boolean onKeyDown(int keyCode) {return false;}
	public boolean onKeyUp(int keyCode) {return false;}
	@Override
	public boolean onTouchMove(float niX, float niY) {
		return false;
	}
	public void save() {
		String strContent = player.getCurGate()+","+player.getPassMaxGate();
		try {
			Tools.saveFile(context.openFileOutput(Info.STR_SAVE_FILE, Activity.MODE_PRIVATE), strContent);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public boolean onTouchDown(float niX, float niY) {	
		if(!isGameOver)	
			handler.onTouchDown(niX,niY);
		else
			callBackResult.onTouchDown(niX, niY);
		return true;
		}
	public boolean onTouchUp(float niX, float niY) {
		if(!isGameOver)	
			handler.onTouchUp();
		else
			callBackResult.onTouchUp(niX, niY);
			return true;
	}
	public boolean onTouchMove(Point []arrPoint) {
		if(!isGameOver)	
			handler.onTouchMove(arrPoint);
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		arrPoint[0].set((int)event.getX(0), (int)event.getY(0));
		if(event.getPointerCount()>=2)
			arrPoint[1].set((int)event.getX(1), (int)event.getY(1));
		else
			arrPoint[1].set(-1,-1);
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			onTouchDown(arrPoint[0].x,arrPoint[0].y);
			break;
		case MotionEvent.ACTION_MOVE:
			onTouchMove(arrPoint);
			break;
		case MotionEvent.ACTION_UP:
			onTouchUp(event.getX(),event.getY());
			break;
		}
		return super.onTouchEvent(event);
	}
	public class HandlerCallback implements Handler.CallBack{
		public void notifyPlayerMove(byte index) {
			player.setState(index, true);
		}
		public void notifyPlayerThrowBomb() {
			Rect rect = bManager.getThrowBombRect(
					(player.getXCenter()-map.getX())/map.getMapTileWidth(),
					(player.getYCenter()-map.getY())/map.getMapTileHeight(),
					player.getCurDir(),
					player.getRect(),
					map.getTileBarrier(),
					map.getTileCover()
					);
			if(eManager.isCollidesWithRect(rect))
				return ;
			if(rect != null)
				bManager.addBomb(rect,player.getBombNum());
		}
		@Override
		public void notifyPlayerStandUp() {
			player.standUp();
		}
	}
	
}
