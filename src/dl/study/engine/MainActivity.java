package dl.study.engine;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity{
	private static int niScreenWidth,niScreenHeigth;
	private static AssetManager am;
	public Context context;
	private static Application application;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//设置横屏
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//获取默认的显示属性
		Display display = getWindowManager().getDefaultDisplay();
		niScreenWidth = display.getWidth();
		niScreenHeigth = display.getHeight();
		//获取文件管理器
		am = this.getAssets();
		context = this ;
		application = getApplication();
	}
	public static int getScreenWidth(){
		return niScreenWidth;
	}
	public static int getScreenHeight(){
		return niScreenHeigth;
	}
	public static AssetManager getAssetManager(){
		return am;
	}
	/**
	 * 获取音频管理器
	 * @return
	 */
	public static final AudioManager getAudioManager(){
		return (AudioManager)application.getSystemService(AUDIO_SERVICE);
	}
	
}
