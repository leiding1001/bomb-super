package dl.study.bombsuper.game;

import dl.study.bombsuper.main.Info;
import dl.study.tools.Tools;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class CallBackResult {
	public static final int NI_TYPE_WIN = 0 ;
	public static final int NI_TYPE_LOSE = 1 ;
	public static final int NI_SPEED = 10 ;
	public static final int NI_ALPHA_NORMAL =100;
	public static final int NI_ALPHA_PRESS =250;
	private int niCurType ;
	private Paint paint;
	private Bitmap bmpLoseOrWin;
	private Bitmap[] arrBmpLoseOrWin ;
	private Bitmap bmpSelect;
	private Bitmap[] arrBmp;
	private Rect[] arrRect;
	
	private int[] arrCurAlpha;
	private CallBack cb;
	private int index = -1;
	private MonitorThread monitorThread ;
	public CallBackResult(CallBack cb) {
		this.cb= cb ;
		monitorThread = new MonitorThread();
		paint = new Paint();
		paint.setColor(Color.RED);
		arrCurAlpha = new int[]{NI_ALPHA_PRESS,NI_ALPHA_NORMAL,NI_ALPHA_NORMAL};
		bmpLoseOrWin = Tools.getBmpFromAssetManager("image/game/loseorwin.png");
		arrBmpLoseOrWin = new Bitmap[2];
		for(int i=0;i<arrBmpLoseOrWin.length;i++)
			arrBmpLoseOrWin[i] = Bitmap.createBitmap(bmpLoseOrWin, 0, (int)(i*133*Info.NI_SCALE_NUM), bmpLoseOrWin.getWidth(), bmpLoseOrWin.getHeight()/2);
		bmpSelect = Tools.getBmpFromAssetManager("image/game/selectbtn.png");
		arrRect = new Rect[3];
		arrRect[0] =new Rect(
				(Info.NI_SCREEN_WIDTH-arrBmpLoseOrWin[0].getWidth())/2,
				(Info.NI_SCREEN_HEIGHT-arrBmpLoseOrWin[0].getHeight())/2-(int)(100*Info.NI_SCALE_NUM),
				(Info.NI_SCREEN_WIDTH+arrBmpLoseOrWin[0].getWidth())/2,
				(Info.NI_SCREEN_HEIGHT+arrBmpLoseOrWin[0].getHeight())/2-(int)(100*Info.NI_SCALE_NUM)
				);
		arrRect[1] = new Rect((int)(350*Info.NI_SCALE_NUM),(int)(450*Info.NI_SCALE_NUM),(int)(350*Info.NI_SCALE_NUM+bmpSelect.getWidth()/3),(int)(450*Info.NI_SCALE_NUM+bmpSelect.getHeight()));
		arrRect[2] = new Rect((int)(700*Info.NI_SCALE_NUM),(int)(450*Info.NI_SCALE_NUM),(int)(700*Info.NI_SCALE_NUM+bmpSelect.getWidth()/3),(int)(450*Info.NI_SCALE_NUM+bmpSelect.getHeight()));
		arrBmp =new Bitmap[3];
	}
	public void result(int type){
		index = -1;
		arrCurAlpha[0] = NI_ALPHA_PRESS;
		arrCurAlpha[1] = NI_ALPHA_NORMAL;
		arrCurAlpha[2] = NI_ALPHA_NORMAL;
		niCurType = type;
		switch(type){
		case NI_TYPE_WIN:
			arrBmp[0]=arrBmpLoseOrWin[1];
			arrBmp[1]=Bitmap.createBitmap(bmpSelect, 0, 0, bmpSelect.getWidth()/3, bmpSelect.getHeight());
			arrBmp[2]=Bitmap.createBitmap(bmpSelect, bmpSelect.getWidth()/3, 0, bmpSelect.getWidth()/3, bmpSelect.getHeight());
			break;
		case NI_TYPE_LOSE:
			arrBmp[0]=arrBmpLoseOrWin[0];
			arrBmp[1]=Bitmap.createBitmap(bmpSelect, 0, 0, bmpSelect.getWidth()/3, bmpSelect.getHeight());
			arrBmp[2]=Bitmap.createBitmap(bmpSelect, 2*bmpSelect.getWidth()/3, 0, bmpSelect.getWidth()/3, bmpSelect.getHeight());
			break;
		}
	}
	public void onDraw(Canvas canvas){
		canvas.drawBitmap(Info.bmpBottom, 0,0, null);
		for(int i=0;i<arrBmp.length;i++){
			paint.setAlpha(arrCurAlpha[i]);
			if(arrBmp[i]!=null)
			canvas.drawBitmap(arrBmp[i],arrRect[i].left,arrRect[i].top, paint);
		}

	}
	public class MonitorThread extends RunnableManager{
		@Override
		public void logic() {
			arrCurAlpha[0]-=NI_SPEED;
			if(arrCurAlpha[0]<NI_ALPHA_NORMAL)
			{
				arrCurAlpha[0] = NI_ALPHA_NORMAL ;
					notifyResult();
					close();
			}
			}
			
		}

		private void notifyResult() {
				 if(niCurType==NI_TYPE_WIN&&index == 2)
					 cb.notifyNext();
				 else if(niCurType==NI_TYPE_LOSE&&index == 2)
					cb.notifyRestart();
				 else
					cb.notifyBack();
			}
	
	public boolean onTouchDown(float niX, float niY) {	
		for(int i=1;i<arrRect.length;i++)
		{
			if(Tools.pointCollideWithRect(niX, niY, arrRect[i])){
				arrCurAlpha[i] = NI_ALPHA_PRESS;
				index  = i;
				break;
			}
		}
		return true;
	}
	public boolean onTouchUp(float niX, float niY) {
		if(index!=-1){
			arrCurAlpha[index] = NI_ALPHA_NORMAL;
			monitorThread.start();
		}
		return true;
	}
	public boolean onTouchMove(Point []arrPoint) {
	return false;
	}
	public interface CallBack {
		void notifyRestart();
		void notifyNext();
		void notifyBack();
	}
}
