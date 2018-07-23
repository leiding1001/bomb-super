package dl.study.bombsuper.main;

import dl.study.bombsuper.game.RunnableManager;
import dl.study.tools.Tools;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Message;
import android.view.View;

public class LoadingGame2 extends View{
	private static final int NI_BORDER_DISTANCE = 5 ;
	private static final int NI_ADD_SPEED_MIN = 0 ;
	private static final int NI_ADD_SPEED_MAX = 15 ;
	private static final int NI_APHAL_SPEED = 30 ;
	private static Paint paint ;
	private int niSpeed ;
	private String srInfo = "ÕýÔÚ¼ÓÔØ  BombSuper..";
	private Rect rectBorder;
	private Rect rectProgress;
	private Point point ;
	private LoadingThread  loadingThread;
	private int niCurGate,niPassMaxGate;

	public LoadingGame2(Context context,int niCurGate,int niPassMaxGate) {
		super(context);
		this.niCurGate = niCurGate;
		this.niPassMaxGate = niPassMaxGate;
		paint = new Paint();
		paint.setColor(Color.rgb(255, 134, 81));
		paint.setTextSize(30);
		rectBorder = new Rect(
				(Info.NI_SCREEN_WIDTH-Info.NI_LOADING_HEIGHT)/2,
				(Info.NI_SCREEN_HEIGHT-Info.NI_LOADING_WIDTH)/2,
				(Info.NI_SCREEN_WIDTH+Info.NI_LOADING_HEIGHT)/2,
				(Info.NI_SCREEN_HEIGHT+Info.NI_LOADING_WIDTH)/2
				);
		rectProgress = new Rect(
				rectBorder.left+NI_BORDER_DISTANCE,
				rectBorder.top+NI_BORDER_DISTANCE,
				rectBorder.left+NI_BORDER_DISTANCE,
				rectBorder.bottom-NI_BORDER_DISTANCE);
		
		point = new Point((int) (Info.NI_SCREEN_WIDTH-paint.measureText(srInfo))/2,rectBorder.top-20);
		loadingThread = new LoadingThread();
		loadingThread.start(20);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(Info.bmpBottom, 0,0, null);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(rectBorder, paint);
		paint.setStyle(Style.FILL);
		canvas.drawRect(rectProgress, paint);
		canvas.drawText(srInfo, point.x, point.y, paint);
	}
	public class LoadingThread extends RunnableManager{
		public void logic() {
			niSpeed = Tools.getRandom(NI_ADD_SPEED_MIN, NI_ADD_SPEED_MAX);
			if(rectProgress.right+niSpeed< rectBorder.right-NI_BORDER_DISTANCE){
				rectProgress.set(
						rectBorder.left+NI_BORDER_DISTANCE,
						rectBorder.top+NI_BORDER_DISTANCE,
						rectProgress.right+niSpeed,
						rectBorder.bottom-NI_BORDER_DISTANCE);
			}else if(rectProgress.right == rectBorder.right-NI_BORDER_DISTANCE){
				if(paint.getAlpha()-NI_APHAL_SPEED >= 0)
					paint.setAlpha(paint.getAlpha()-NI_APHAL_SPEED);
				else{
					close();
					
//					Main.handler.sendEmptyMessage(0);
					Message msg = new Message();
					msg.arg1=niCurGate;
					msg.arg2=niPassMaxGate;
					msg.what=1;
					Main.handler.sendMessage(msg);
					}
			}else{
				rectProgress.set(
						rectBorder.left+NI_BORDER_DISTANCE,
						rectBorder.top+NI_BORDER_DISTANCE,
						rectBorder.right-NI_BORDER_DISTANCE,
						rectBorder.bottom-NI_BORDER_DISTANCE);
			}
			postInvalidate();
		}
		
	}
}
