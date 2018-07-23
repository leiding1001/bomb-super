package dl.study.bombsuper.main;

import dl.study.bombsuper.game.RunnableManager;
import dl.study.tools.Tools;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Message;
import android.view.View;

public class LoadingGame1 extends View{
	private static final int NI_RANDOM_MAX = 50;
	private int niAlpha;
	private boolean isLoadingPic;

	private Bitmap bmpBottom ;
	private Bitmap bmpTop ;
	private Bitmap bmpCurTop ;
	private Rect rectBmp ;
	private Paint paint;
	
	private LoadingThread  loadingThread;
	private int niCurGate,niPassMaxGate;
	public LoadingGame1(Context context,int niCurGate,int niPassMaxGate) {
		super(context);
		this.niCurGate = niCurGate;
		this.niPassMaxGate = niPassMaxGate;
		isLoadingPic = true;
		paint = new Paint();
		niAlpha = 255 ;
		
		bmpBottom = Tools.getBmpFromAssetManager("image/loading/l1.png");
		bmpTop = Tools.getBmpFromAssetManager("image/loading/l2.png");
		bmpCurTop = Bitmap.createBitmap(bmpTop, 0, 0, 1, bmpTop.getHeight());
		rectBmp = new Rect(
				(Info.NI_SCREEN_WIDTH-bmpBottom.getWidth())/2,
				(Info.NI_SCREEN_HEIGHT-bmpBottom.getHeight())/2,
				(Info.NI_SCREEN_WIDTH+bmpBottom.getWidth())/2,
				(Info.NI_SCREEN_HEIGHT+bmpBottom.getHeight())/2
				);
		loadingThread = new LoadingThread();
		loadingThread.start(80);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(Info.bmpBottom, 0,0, null);
		if(niAlpha==255)
			canvas.drawBitmap(bmpBottom, rectBmp.left, rectBmp.top, paint);
		paint.setAlpha(niAlpha);
		canvas.drawBitmap(bmpCurTop, rectBmp.left, rectBmp.top, paint);
		
	}
	public class LoadingThread extends RunnableManager{
		public void logic() {
			int niRandom = Tools.getRandom(0, NI_RANDOM_MAX);
			if(isLoadingPic){
				if(bmpCurTop.getWidth()+niRandom<bmpTop.getWidth())
					bmpCurTop = Bitmap.createBitmap(bmpTop, 0, 0, bmpCurTop.getWidth()+niRandom, bmpTop.getHeight());
				else{
					bmpCurTop = Bitmap.createBitmap(bmpTop, 0, 0, (int)(650*Info.NI_SCALE_NUM), bmpTop.getHeight());
					isLoadingPic = false;
				}
			}else{
				if(niAlpha-niRandom>=0)
					{
					niAlpha -= niRandom;

					if(bmpCurTop.getWidth()+niRandom<bmpTop.getWidth())
						bmpCurTop = Bitmap.createBitmap(bmpTop, 0, 0, bmpCurTop.getWidth()+niRandom, bmpTop.getHeight());
					else
						bmpCurTop = Bitmap.createBitmap(bmpTop, 0, 0,  (int)(650*Info.NI_SCALE_NUM), bmpTop.getHeight());
					}
				else{
					niAlpha = 0;
					close();
					//Main.handler.sendEmptyMessage(0);
					Message msg = new Message();
					msg.arg1=niCurGate;
					msg.arg2=niPassMaxGate;
					msg.what=1;
					Main.handler.sendMessage(msg);
				}
			}
			postInvalidate();
		}
		
	}
}
