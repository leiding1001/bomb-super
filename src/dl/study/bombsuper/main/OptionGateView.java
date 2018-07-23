package dl.study.bombsuper.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Message;
import android.view.MotionEvent;
import dl.study.bumbsuper.main.R;
import dl.study.engine.MonitorBase;
import dl.study.tools.Tools;

@SuppressLint("ViewConstructor")
public class OptionGateView extends MonitorBase{

	private static final int NI_DISTANCE = 100;
	private static final int NI_MOVE_SPEED  = 150;

	private boolean[] arrIsPass;
	private static Bitmap arrBmpScnaleAll;
	private static Bitmap[] arrBmpOrial;
	private Bitmap[] arrBmpScanle;
	private Bitmap bmpKey;
	private Bitmap bmpTitle;
	private Rect  rectTitle;
	private Rect[] arrRect;
	private Matrix matrix;
	private int niCurId;
	private int niBmpWidth;
	private int niBmpHeight;
	private int niDistance;
	private boolean isTouchAble;

	private int disCenterX;
	private int tempX;
	private boolean isLeft;
	private int passMaxGate;
	/**
	 * 
	 * @param context
	 * @param curGate ：当前所在的关
	 * @param passMaxGate：玩家角色现在通过的关数
	 */
	public OptionGateView(Context context,int curGate,int passMaxGate) {
		super(context);
		if(curGate<=0)
			curGate = 1;
		if(passMaxGate<=0)
			passMaxGate = 1;
		this.passMaxGate = passMaxGate;
		isTouchAble = false;
		niCurId = curGate -1 ;
		isTouchAble = false;
		bmpKey = Tools.getBmpFromAssetManager("image/game/lock.png");
		bmpTitle = Tools.getBmpFromAssetManager("image/game/title.png");
		arrBmpScnaleAll = Tools.getBmpFromAssetManager("image/map/scanle/map_scanle_all.png");
		
		int itemWidth = arrBmpScnaleAll.getWidth()/3;
		int itemHeight = arrBmpScnaleAll.getHeight()/2;
		
		arrBmpOrial = new Bitmap[6];
		for(int i=0;i<arrBmpOrial.length;i++){
			Tools.testInfo(i%3*itemWidth+"--"+(i/3*itemHeight)+"--"+(i%3+1)*itemWidth+"--"+(i/3+1)*itemHeight);
			arrBmpOrial[i] = Bitmap.createBitmap(arrBmpScnaleAll, i%3*itemWidth, i/3*itemHeight, itemWidth, itemHeight);
			Tools.testInfo("---");
		}
		arrIsPass = new boolean[arrBmpOrial.length];
		arrBmpScanle = new Bitmap[arrBmpOrial.length];
		matrix = new Matrix();
		matrix.setScale(0.9f, 0.9f);
		bmpKey= Bitmap.createBitmap(bmpKey, 0, 0, bmpKey.getWidth(), bmpKey.getHeight(), matrix,true);
		for(int i=0,iLen=arrBmpOrial.length;i<iLen;i++){
			 arrIsPass[i] = false;
			 arrBmpScanle[i]= Bitmap.createBitmap(arrBmpOrial[i], 0, 0, arrBmpOrial[i].getWidth(), arrBmpOrial[i].getHeight(), matrix,true);
		 }
		 for(int i=0;i<passMaxGate;i++)
			 arrIsPass[i]= true;
		 arrRect = new Rect[arrBmpOrial.length];
		 this.niBmpWidth = arrBmpScanle[0].getWidth();
		 this.niBmpHeight = arrBmpScanle[0].getHeight();
		 for(int i=0,iLen=arrBmpOrial.length;i<iLen;i++){
			 arrRect[i] = new Rect(
					 (Info.NI_SCREEN_WIDTH-niBmpWidth)/2+(i-niCurId)*(niBmpWidth+NI_DISTANCE),
					 (Info.NI_SCREEN_HEIGHT-niBmpHeight)/2,
					 (Info.NI_SCREEN_WIDTH+niBmpWidth)/2+(i-niCurId)*(niBmpWidth+NI_DISTANCE),
					 (Info.NI_SCREEN_HEIGHT+niBmpHeight)/2
					 );
		 }
		 rectTitle = new Rect(
				 (Info.NI_SCREEN_WIDTH-bmpTitle.getWidth())/2,
				 ( arrRect[niCurId].top-bmpTitle.getHeight())/2,
				 (Info.NI_SCREEN_WIDTH+bmpTitle.getWidth())/2,
				 ( arrRect[niCurId].top+bmpTitle.getHeight())/2
				 );
		 isTouchAble = true;
		 startThread(80);
	}
	/**
	 * @param curGate ：当前所在的关
	 * @param passMaxGate：玩家角色现在通过的关数
	 */
	public void reset(int curGate,int passMaxGate){
		if(curGate<=0)
			curGate = 1;
		if(passMaxGate<=0)
			passMaxGate = 1;
//		Tools.SOUND.init(context, Info.NI_SOUND_TYPE_OPTION);
//		Tools.SOUND.startMediaPlayer(R.raw.option, true);
		isTouchAble = false;
		niCurId = curGate -1 ;
		for(int i=0;i<passMaxGate;i++)
			 arrIsPass[i]= true;
		disCenterX = (Info.NI_SCREEN_WIDTH/2-(arrRect[niCurId].left+arrRect[niCurId].right)/2);
				if(disCenterX<0)
					isLeft = true;
				else 
					isLeft = false;
	}
	@Override
	protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(Info.bmpBottom, 0,0, null);
			canvas.drawBitmap(bmpTitle, rectTitle.left,rectTitle.top, null);
			for(int i=0,iLen=arrBmpScanle.length;i<iLen;i++){
				canvas.drawBitmap(arrBmpScanle[i], arrRect[i].left, arrRect[i].top, null);
				if(!arrIsPass[i]){
					canvas.drawBitmap(bmpKey, arrRect[i].left+(niBmpWidth-bmpKey.getWidth())/2, arrRect[i].top+(niBmpHeight-bmpKey.getHeight())/2, null);
				}
			}
	}
	@Override
	public void monitorLogic() {
		if(!isTouchAble){
			if(isLeft){
				move(-NI_MOVE_SPEED);
				tempX=(Info.NI_SCREEN_WIDTH/2-(arrRect[niCurId].left+arrRect[niCurId].right)/2);
				if(tempX>0)
					{
					move(tempX);
					isTouchAble = true;
					}
			}else
				{
				move(NI_MOVE_SPEED);
				tempX=(Info.NI_SCREEN_WIDTH/2-(arrRect[niCurId].left+arrRect[niCurId].right)/2);
				if(tempX<0)
					{
					move(tempX);
					isTouchAble = true;
					}
				}
		}
	}
	@Override
	public boolean onTouchDown(float niX, float niY) {
		niDistance = (int)niX - arrRect[niCurId].left;
		return false;
		}
	@Override
	public boolean onTouchMove(float niX, float niY) {
		moveFollowTouch((int)niX);
		return false;
	}
	
	private void moveFollowTouch(int niX) {
		for(int i=0,iLen=arrRect.length;i<iLen;i++){
			 arrRect[i].set(niX-niDistance+(i-niCurId)*(niBmpWidth+NI_DISTANCE), arrRect[i].top, niX-niDistance+niBmpWidth+(i-niCurId)*(niBmpWidth+NI_DISTANCE), arrRect[i].bottom);
		 }
	}
	private void move(int off){
			for(int i=0,iLen=arrRect.length;i<iLen;i++)
				arrRect[i].set( arrRect[i].left+off, arrRect[i].top, arrRect[i].right+off, arrRect[i].bottom);
	}
	@Override
	public boolean onTouchUp(float niX, float niY) {
		if((arrRect[niCurId].left+arrRect[niCurId].right)/2<Info.NI_SCREEN_WIDTH*1/5)
			{
			niCurId++;
			if(niCurId==arrRect.length)
				niCurId = arrRect.length-1;
			}
		else if((arrRect[niCurId].left+arrRect[niCurId].right)/2>Info.NI_SCREEN_WIDTH*4/5)
		{
			niCurId--;
			if(niCurId<0)
				niCurId = 0;
			}
		disCenterX = (Info.NI_SCREEN_WIDTH/2-(arrRect[niCurId].left+arrRect[niCurId].right)/2);
		if(disCenterX<0)
			isLeft = true;
		else 
			isLeft = false;
		if(disCenterX==0&&Tools.pointCollideWithRect(niX, niY, arrRect[niCurId])&&arrIsPass[niCurId])
			{
				Message msg = new Message();
				msg.arg1=niCurId+1;
				msg.arg2=passMaxGate;
				msg.what=0;
//				 Tools.SOUND.stopMediaPlayer(R.raw.option);
				Main.handler.sendMessage(msg);
		}else{
			isTouchAble = false;
		}
		//move();
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(isTouchAble){
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				onTouchDown(event.getX(),event.getY());
				break;
			case MotionEvent.ACTION_MOVE:
				onTouchMove(event.getX(),event.getY());
				break;
			case MotionEvent.ACTION_UP:
				onTouchUp(event.getX(),event.getY());
				break;
			}
			return super.onTouchEvent(event);
		}
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode) {return false;	}
	
	@Override
	public boolean onKeyUp(int keyCode) {return false;}

	
}
