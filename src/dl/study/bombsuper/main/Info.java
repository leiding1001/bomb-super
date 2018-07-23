package dl.study.bombsuper.main;

import android.graphics.Bitmap;
import dl.study.tools.Tools;

public class Info {
	
	public final static byte NB_DIR_NULL = -1; 
	public final static byte NB_DIR_UP = 0; 
	public final static byte NB_DIR_DOWN = 1; 
	public final static byte NB_DIR_LEFT = 2; 
	public final static byte NB_DIR_RIGHT = 3;
	public final static int NI_SCREEN_WIDTH = Main.getScreenWidth();
	public final static int NI_SCREEN_HEIGHT = Main.getScreenHeight();
	public final static int NI_LOADING_WIDTH = 30;
	public final static int NI_LOADING_HEIGHT = (int) (NI_SCREEN_WIDTH * 0.7);
	public final static int NI_MOVE_SPEED = (int) (2*5*Info.NI_SCALE_NUM);
	public final static int NI_SOUND_TYPE_MAP = 1;
	public final static int NI_SOUND_TYPE_WIN = 2;
	public final static int NI_SOUND_TYPE_LOSE = 3;
	public final static int NI_SOUND_TYPE_OPTION = 4;
	public final static String STR_SAVE_FILE = "save.dl"; 
	public final static Bitmap bmpBottom =  Tools.getBmpFromAssetManager("image/game/bottom.png");
	
	
	public final static float NI_SCALE_NUM = 0.65f;
	
	public  static final int[][] arrAnimationSeq={
		{12,13,14,15},
		{0,1,2,3},
		{4,5,6,7},
		{8,9,10,11}
	};
	public void move(byte dir)
	{
		switch(dir){
		case Info.NB_DIR_UP:
			break;
		case Info.NB_DIR_DOWN:
			break;
		case Info.NB_DIR_LEFT:
			break;
		case Info.NB_DIR_RIGHT:
			break;
		default :
			break;
		}
		
	}
}
