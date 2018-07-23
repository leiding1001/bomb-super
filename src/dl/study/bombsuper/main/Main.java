package dl.study.bombsuper.main;

import java.io.FileNotFoundException;

import dl.study.bombsuper.game.MapDataDecode;
import dl.study.engine.MainActivity;
import dl.study.tools.Tools;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Main extends MainActivity {
	public static Handler handler ;
	
	private GameController gameController  =  null ;
	private OptionGateView optionGateView  =  null ;
	private int viewType=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int[] arrNiData =new int[]{1,1};
		try {
			String strOrialContent = Tools.loadRom(this.context.openFileInput(Info.STR_SAVE_FILE));
			String[] arrStrData = Tools.splitString(strOrialContent, MapDataDecode.STR_SIGN_1);
			arrNiData = Tools.decodeStringToIntArray(arrStrData);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
					case 0:
						viewType = 0 ;
						if(gameController==null)
							gameController = new GameController(context,msg.arg1,msg.arg2);
						else
							gameController.reset(msg.arg1);
						setContentView(gameController);
						break;
					case 1:
						viewType = 1 ;
						if(optionGateView==null)
							optionGateView = new OptionGateView(context,msg.arg1,msg.arg2);
						else
							optionGateView.reset(msg.arg1,msg.arg2);
						setContentView(optionGateView);
						break;
					case 2:
						break;
					case 3:
						break;
					}
				super.handleMessage(msg);
			}
		};
		//加载游戏类型一
		this.setContentView(new LoadingGame1(this,arrNiData[0],arrNiData[1]));
		//加载游戏类型二
//		this.setContentView(new LoadingGame2(this,arrNiData[0],arrNiData[1]));
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(viewType){
			case 0:
				gameController.onTouchEvent(event);
				break;
			case 1:
				optionGateView.onTouchEvent(event);
				break;
			case 2:
				break;
		}
		return super.onTouchEvent(event);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			switch(viewType){
				case 0:
					gameController.back();
					return false;
				case 1:
					if(gameController!=null)
					gameController.save();
					break;
				case 2:
					break;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
