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
		//ȥ��������
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//����ȫ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//���ú���
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//��ȡĬ�ϵ���ʾ����
		Display display = getWindowManager().getDefaultDisplay();
		niScreenWidth = display.getWidth();
		niScreenHeigth = display.getHeight();
		//��ȡ�ļ�������
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
	 * ��ȡ��Ƶ������
	 * @return
	 */
	public static final AudioManager getAudioManager(){
		return (AudioManager)application.getSystemService(AUDIO_SERVICE);
	}
	
}
