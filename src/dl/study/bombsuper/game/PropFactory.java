package dl.study.bombsuper.game;

import android.graphics.Bitmap;
import android.graphics.Rect;
import dl.study.tools.Tools;

public class PropFactory {
	private static final Bitmap bmpAllProp = Tools.getBmpFromAssetManager("image/prop/prop.png");
	private static final int NI_PROP_NUM = 3;
	public static final byte NB_PROP_ADD_SPEED = 0 ;
	public static final byte NB_PROP_KEY = 1 ;
	public static final byte NB_PROP_ADD_BOMB = 2 ;
	/**
	 * µ¥ÀýÄ£Ê½
	 */
	private static PropFactory instance;
	private PropFactory() {}
	public static PropFactory getInstance(){
		if(instance == null)
			instance = new PropFactory();
		return instance;
	}
	public Prop make(Rect rect,byte niType){
		Prop prop = null;
//		switch(niType){
//			case NB_PROP_ADD_SPEED:
//				prop = new Prop(
//						rect, niType,
//					Bitmap.createBitmap(
//							bmpAllProp, niType*bmpAllProp.getWidth()/NI_PROP_NUM, 0,
//							bmpAllProp.getWidth()/NI_PROP_NUM, 
//							bmpAllProp.getHeight()
//								)
//						);
//				break;
//			case NB_PROP_KEY:
//				prop = new Prop(
//						rect, niType,
//					Bitmap.createBitmap(
//							bmpAllProp, niType*bmpAllProp.getWidth()/NI_PROP_NUM, 0,
//							bmpAllProp.getWidth()/NI_PROP_NUM, 
//							bmpAllProp.getHeight()
//							)
//					);
//				break;
//			case NB_PROP_ADD_BOMB:
//				prop = new Prop(
//						rect, niType,
//					Bitmap.createBitmap(
//							bmpAllProp, niType*bmpAllProp.getWidth()/NI_PROP_NUM, 0,
//							bmpAllProp.getWidth()/NI_PROP_NUM, 
//							bmpAllProp.getHeight()
//							)
//					);
//				break;
//			}
		prop = new Prop(
				rect, niType,
			Bitmap.createBitmap(
					bmpAllProp, niType*bmpAllProp.getWidth()/NI_PROP_NUM, 0,
					bmpAllProp.getWidth()/NI_PROP_NUM, 
					bmpAllProp.getHeight()
					)
			);
		return prop;
	}

}
