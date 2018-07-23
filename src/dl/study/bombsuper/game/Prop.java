package dl.study.bombsuper.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Prop {
	private byte niType;
	private Rect rectProp;
	private Rect rectPropCollide;
	private Bitmap bmpProp;
	private boolean isVisible;
	
	public Prop(Rect rect,byte niType,Bitmap bmpProp) {
		this.isVisible = true;
		this.niType = niType;
		this.bmpProp = bmpProp;
		rectProp = rect;
		rectPropCollide = new Rect(
				rectProp.left+10,
				rectProp.top+10,
				rectProp.right-10,
				rectProp.bottom-10
				);
	}
	public void onDraw(Canvas canvas ,Paint paint){
		if(isVisible)
			canvas.drawBitmap(bmpProp, rectProp.left,rectProp.top, null);
	}
	public void onDrawCollide(Canvas canvas ,Paint paint){
		if(isVisible)
			canvas.drawRect(rectPropCollide, paint);
	}
	public byte getType(){
		return niType;
	}
	public Rect getPropCollideRect(){
		return rectPropCollide;
	}
	public void setVisible(boolean b) {
		isVisible = false;
	}
	public void move(int offX ,int offY){
		rectProp.set(rectProp.left+offX, rectProp.top+offY, rectProp.right+offX, rectProp.bottom+offY);
		rectPropCollide = new Rect(
				rectProp.left+10,
				rectProp.top+10,
				rectProp.right-10,
				rectProp.bottom-10
				);
	}
}
