package dl.study.bombsuper.game;

import dl.study.bombsuper.main.Info;
import dl.study.tools.Tools;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Handler {
	private static final int NI_DISTANCE = (int) (40*Info.NI_SCALE_NUM) ;
	private static final int NI_NORMAL_ALPHA = 150 ;
	private static final int NI_PRESS_ALPHA = 255 ;
	private int[] arrNiAlpha;
	private Bitmap[] arrBmpDir;
	private Bitmap bmpDir;
	private Rect[] arrRect;
	private Matrix matrix;
	private Paint  paint ;
	private Point dirCenterPoint;
	private CallBack callback;
	public Handler(CallBack callback) {
		this.callback = callback;
		paint = new Paint();
		paint.setAlpha(150);
		paint.setColor(Color.RED);
		arrBmpDir = new Bitmap[5];
		arrRect = new Rect[5];
		arrNiAlpha = new int[]{NI_NORMAL_ALPHA,NI_NORMAL_ALPHA,NI_NORMAL_ALPHA,NI_NORMAL_ALPHA,NI_NORMAL_ALPHA};
		 bmpDir = Tools.getBmpFromAssetManager("image/handle/dir_r.png");	
		 matrix = new Matrix();
		 matrix.setRotate(-90);
		 arrBmpDir[0] = Bitmap.createBitmap(bmpDir, 0, 0, bmpDir.getWidth(), bmpDir.getHeight(), matrix,true);
		 matrix.setRotate(90);
		 arrBmpDir[1] = Bitmap.createBitmap(bmpDir, 0, 0, bmpDir.getWidth(), bmpDir.getHeight(), matrix,true);
		 matrix.setRotate(180);
		 arrBmpDir[2] = Bitmap.createBitmap(bmpDir, 0, 0, bmpDir.getWidth(), bmpDir.getHeight(), matrix,true);
		 arrBmpDir[3] = bmpDir;
		 arrBmpDir[4] = Tools.getBmpFromAssetManager("image/handle/shot1.png");
		 dirCenterPoint = new Point();
		 for(int i=0;i<arrRect.length-1;i++)
			 arrRect[i] = new Rect();
		 resetCenterPoint(Info.NB_DIR_DOWN,170*Info.NI_SCALE_NUM);
		 arrRect[4] = new Rect(
				 Info.NI_SCREEN_WIDTH-arrBmpDir[4].getWidth()-2*NI_DISTANCE,dirCenterPoint.y-arrBmpDir[4].getHeight()/2,
				 Info.NI_SCREEN_WIDTH-2*NI_DISTANCE,dirCenterPoint.y+arrBmpDir[4].getHeight()/2
				 );
	}
	public void onDraw(Canvas canvas){
		for(byte i=0;i<arrBmpDir.length;i++)
			{
			//canvas.drawRect(arrRect[i],paint);
			paint.setAlpha(arrNiAlpha[i]);
			if(i==0)
				canvas.drawBitmap(arrBmpDir[i],arrRect[i].left,arrRect[i].top+NI_DISTANCE,paint);
			else if(i==2)
				canvas.drawBitmap(arrBmpDir[i],arrRect[i].left+NI_DISTANCE,arrRect[i].top,paint);
			
			else
				canvas.drawBitmap(arrBmpDir[i],arrRect[i].left,arrRect[i].top,paint);
			}
	}
	public void onTouchDown(float niX ,float niY){
		for(byte i=0;i<arrRect.length;i++)
			if(Tools.pointCollideWithRect(niX,niY,arrRect[i])){
				return ;
			}
		resetCenterPoint(niY>360?Info.NB_DIR_DOWN:Info.NB_DIR_UP,niX);
	}
	public void onTouchMove(Point[] arrPoint) {
		byte dirState = -1;
		byte shotState = -1;
		for(byte i=0;i<arrPoint.length;i++)
		{
			for(byte j=0;j<arrRect.length-1;j++)
				if(Tools.pointCollideWithRect(arrPoint[i].x,arrPoint[i].y,arrRect[j]))
					dirState = j;
			if(Tools.pointCollideWithRect(arrPoint[i].x,arrPoint[i].y,arrRect[4]))
				shotState=i;
		}
		if(dirState!=-1){
			arrNiAlpha[dirState] = NI_PRESS_ALPHA;
			callback.notifyPlayerMove(dirState);
			for(int i=0;i<arrRect.length-1;i++)
				if(i!=dirState)
					resetState(i);
		}else{
			for(int i=0;i<arrRect.length-1;i++)
				resetState(i);
			callback.notifyPlayerStandUp();
		}
		if(shotState!=-1){
			arrNiAlpha[4] = NI_PRESS_ALPHA;
			callback.notifyPlayerThrowBomb();
		}else{
			resetState(4);
		}
	}
	private void resetState(int idx){
		arrNiAlpha[idx]=NI_NORMAL_ALPHA;
	}
	public void onTouchUp(){
		for(int i=0;i<arrRect.length-1;i++)
			resetState(i);
		callback.notifyPlayerStandUp();
//		if(index != -1){
//			arrNiAlpha[index] = NI_NORMAL_ALPHA;
//			if(index!=(arrRect.length-1))
//				callback.notifyPlayerStandUp();
//			index = -1;
//		}
	}
	private void resetCenterPoint(byte dir,float niX){
		int niY = 0;
		switch(dir){
		case Info.NB_DIR_UP:
			niY = (int) (200*Info.NI_SCALE_NUM) ;
			break;
		case Info.NB_DIR_DOWN:
			niY = (int) (Info.NI_SCREEN_HEIGHT-180*Info.NI_SCALE_NUM);
			break;
		}
		if(niX > Info.NI_SCREEN_WIDTH*2/3){
			 arrRect[4].set(
					 arrRect[4].left,niY-bmpDir.getHeight()/2,
					 arrRect[4].right,niY+bmpDir.getHeight()/2
					 );
			 return ;
		}
		niX = 170*Info.NI_SCALE_NUM ;
		dirCenterPoint.set((int)niX,niY);
		 arrRect[0].set(
				 dirCenterPoint.x-bmpDir.getWidth()/2,dirCenterPoint.y-bmpDir.getHeight()-2*NI_DISTANCE,
				 dirCenterPoint.x+bmpDir.getWidth()/2,dirCenterPoint.y-NI_DISTANCE				 
				 );
		 arrRect[1].set(
				 dirCenterPoint.x-bmpDir.getWidth()/2,dirCenterPoint.y+NI_DISTANCE,
				 dirCenterPoint.x+bmpDir.getWidth()/2,dirCenterPoint.y+2*NI_DISTANCE+bmpDir.getHeight()
				 );
		 arrRect[2].set(
				 dirCenterPoint.x-bmpDir.getWidth()-2*NI_DISTANCE,dirCenterPoint.y-bmpDir.getHeight()/2,
				 dirCenterPoint.x-NI_DISTANCE,dirCenterPoint.y+bmpDir.getHeight()/2
				 );
		 arrRect[3].set(
				 dirCenterPoint.x+NI_DISTANCE,dirCenterPoint.y-bmpDir.getHeight()/2,
				 dirCenterPoint.x+2*NI_DISTANCE+bmpDir.getWidth(),dirCenterPoint.y+bmpDir.getHeight()/2
				 );
	}
	public interface CallBack{
		void notifyPlayerMove(byte index);
		void notifyPlayerStandUp();
		void notifyPlayerThrowBomb();
	}
	
}
