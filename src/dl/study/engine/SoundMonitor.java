package dl.study.engine;

import java.util.HashMap;

import dl.study.bumbsuper.main.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public final class SoundMonitor 
{
	/**************************常量&静态数据区*********************************/
	/** 空值 **/
	public static final int NI_NULL = -128;
	/** 音量最大值 **/
	private final byte NB_VOLUME_MAX = 15;
	/** 音量最小值 **/
	private final byte NB_VOLUME_MIN = 0;
	/**当前音量**/
	private static int niCurrentVolume;
	/**静音前音量**/
	private static int niOldVolume;
	/** 静音设置(静态) **/
	private static boolean isMute;
	/**************************引用数据区*********************************/
	/**************************对象数据区*********************************/
	/** 音效池 **/
	private HashMap<Integer, Integer> soundMap;
	/** 音乐池 **/
	private HashMap<Integer, MediaPlayer> mediaMap;
	/** 音效播放器 **/
	private SoundPool soundPool ;
	/** 音量控制器 **/
	private AudioManager audioManager;
	/** 当前音乐对象引用 **/
	private MediaPlayer mediaPlayerCurrent;
	/**************************基本数据区*********************************/
	/** 当前播放音乐文件Id,如果无音乐播放，则为-128 **/
	private int niCurMediaId = NI_NULL;
	/**************************构造方法区*********************************/
	
	public SoundMonitor(AudioManager audioManager)
	{
		//获取音量管理对象
		this.audioManager = audioManager; 
		soundMap = new HashMap<Integer, Integer>();
		mediaMap = new HashMap<Integer, MediaPlayer>();
		//设置音量
		niOldVolume = niCurrentVolume = NB_VOLUME_MAX;
	}
	
	/**************************逻辑方法区*************** ******************/
	
	private void readInit(){
		//释放所有Media资源
		releaseMediaPlayerAll();
		//释放所有Sound资源
		releaseSoundAll();
		//加载Sound资源
		if(soundPool == null)
			soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	}
	/**
	 * 初始化对应状态的音乐和音效 
	 */
	public void init(Context context, int niMonitorState)
	{
		readInit();
		/*********************自定义部分******************/
		switch(niMonitorState)
		{
		case 1 :
			//向管理器中加入音乐
			mediaMap.put(R.raw.map, MediaPlayer.create(context, R.raw.map));
			break;
		case 2 :
			mediaMap.put(R.raw.win, MediaPlayer.create(context, R.raw.win));
			break;
		case 3 :
			mediaMap.put(R.raw.lose, MediaPlayer.create(context, R.raw.lose));
			break;
		}
		//向管理器中加入音效
		//soundMap.put(R.raw.pk, soundPool.load(context, R.raw.pk, 1));
	}
	
	/**
	 * 释放所有的音乐资源
	 */
	private void releaseMediaPlayerAll()
	{
		for(MediaPlayer _mP : mediaMap.values())
		{
			if(_mP != null)
			{
				if(_mP.isPlaying())
				{
					try {_mP.stop();} catch (Exception e) {}
				}
				_mP.release();
				_mP = null;
			}
		}
		mediaMap.clear();
		niCurMediaId = NI_NULL;
	}
	/**
	 * 释放所有的音效资源
	 */
	private void releaseSoundAll()
	{
		//soundPool.release()后就不能再load了,应将对象置空
		if(soundPool != null)
		{
			soundPool.release();
			soundPool = null;
		}
		soundMap.clear();
	}
	/**
	 * 释放所有声音资源(包括音效和音乐)
	 */
	public void release(){
		releaseMediaPlayerAll();
		releaseSoundAll(); 
	} 
	/**
	 * 播放指定的音乐
	 * @param niRawMediaId : R.raw.id
	 * @param isLoop : 是否循环
	 * @param nfVolume : 音量(0~100),左右声道共用此值
	 */
	public void startMediaPlayer(int niRawMediaId, final boolean isLoop)
	{
		niCurMediaId = niRawMediaId;
		if(isMute)
			return ;
			
		final MediaPlayer _mPlayer = mediaMap.get(niRawMediaId);
		
		if(_mPlayer != null && _mPlayer.isPlaying() == false)
		{
			_mPlayer.setLooping(isLoop);
			_mPlayer.setVolume(niCurrentVolume, niCurrentVolume);
			mediaPlayerCurrent = _mPlayer;
			_mPlayer.start();
		}
	}
	
	/**
	 * 暂停音乐
	 * 比如在重新开始游戏前先将背景音乐暂停，再次开始后再通过startMediaPlayer打开
	 * @param niRawMediaId
	 */
	public void pauseMediaPlayer(int niRawMediaId)
	{
		MediaPlayer _mPlayer = mediaMap.get(niRawMediaId);
		if(_mPlayer != null && _mPlayer.isPlaying())
			_mPlayer.pause();
	}
	/**
	 * 停止音乐
	 * 此步骤将释放音乐，除非重新加载(init)，否则无法再次播放此音乐
	 * @param niRawMediaId
	 */
	public void stopMediaPlayer(int niRawMediaId)
	{
		MediaPlayer _mPlayer = mediaMap.get(niRawMediaId);
		if(_mPlayer != null && _mPlayer.isPlaying())
			_mPlayer.stop();//mediaPlay.stop();	必须在mediaPlay.isPlaying()返回true时才能执行stop，否则Logcat里可以看到报error
	}
	/**
	 * 关闭音乐
	 * @param niRawMediaId
	 */
	public void close(int niRawMediaId)
	{
		MediaPlayer _mPlayer = mediaMap.get(niRawMediaId);
		if(_mPlayer != null)
		{
			if(_mPlayer.isPlaying())
				_mPlayer.stop();
			_mPlayer.release();
			_mPlayer = null;
			mediaMap.remove(niRawMediaId);
		}
	}
	/**
	 * 播放音效
	 * @param niRawId
	 * @param niLoop : 0为播放一次，-1为无限循环，其他值为播放loop+1次
	 */
	public void playEffectOgg(int niRawId, int niLoop)
	{
		//静音中忽略播放音效
		if(isMute)
			return ;
		
		Integer _niSoundID = soundMap.get(niRawId);
		if(_niSoundID != null && _niSoundID.byteValue() != 0 && soundPool != null && niCurrentVolume > 0)
		{
	    	/**
	    	 * public final int play (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) 
	    	 * 	soundID  声音R中的ID号
				leftVolume  左声道音量(range = 0.0 to 1.0) 
				rightVolume  右声道音量 (range = 0.0 to 1.0) 
				priority  流的优先级，值越大优先级高，影响当同时播放数量超出了最大支持数时SoundPool对该流的处理 (0 = 最低) 
				loop  循环次数 (0为值播放一次，-1为无限循环，其他值为播放loop+1次) 
				rate  播放的速率，范围0.5-2.0(0.5为一半速率，1.0为正常速率，2.0为两倍速率) 

	    	 */
    		soundPool.play(_niSoundID, niCurrentVolume, niCurrentVolume, 1, niLoop, 1F);
		}
	}
	/**
	 * 增加音量
	 */
	public final void addVolume()
	{
		if(niCurrentVolume < NB_VOLUME_MAX)
			setVolume(++niCurrentVolume);
	}
	/**
	 * 降低音量
	 */
	public final void reduceVolume()
	{
		if(niCurrentVolume > NB_VOLUME_MIN)
			setVolume(--niCurrentVolume);
	}
    /**
     * 设置系统音量
     * @param context
     * @param niVolume : 设置为0则为静音, 15为最大声音
     */
    public final void setVolume(int niVolume)
    {
    	if(niVolume <= NB_VOLUME_MIN)
    	{
    		niVolume = NB_VOLUME_MIN;
    		niOldVolume = niCurrentVolume;
    	}
    	else if(niVolume > NB_VOLUME_MAX)
    		niVolume = NB_VOLUME_MAX;
    	
    	niCurrentVolume = niVolume;
    	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, niCurrentVolume, AudioManager.FLAG_PLAY_SOUND);
    }
    /**
     * 获取当前播放的音乐文件是否处于播放中
     * @return
     */
    public final boolean isPlayingCurrentMP()
    {
    	if(mediaPlayerCurrent != null)
    		return mediaPlayerCurrent.isPlaying();
    	return false;
    }
    /**
     * 静音
     */
    public void muteSound()
    {
    	if(isMute)
    		return ;
    	
    	isMute = true;
    	if(niCurrentVolume > 0)
		{
			niOldVolume = niCurrentVolume;
			niCurrentVolume = 0;
		}
    	if(mediaPlayerCurrent != null)
    		mediaPlayerCurrent.pause();
    }
    /**
     * 恢复声音
     */
    public void resumeSound()
    {
    	if(isMute == false)
    		return ;
    	
    	isMute = false;
    	niCurrentVolume = niOldVolume;
    	if(niCurMediaId != NI_NULL)
    		startMediaPlayer(niCurMediaId, true);
    }
    /**
     * 获取系统音量
     * @param context
     * @return
     */
    public final int getStreamVolume()
    {
    	return niCurrentVolume;
    }
    /**
     * 获取当前播放音乐文件Id
     * @return
     */
    public final int getCurrentMediaId(){
    	return niCurMediaId;
    }
	/**
	 * 获取静音前音量
	 * @return
	 */
	public final int getMuteBeforeVolume(){return niOldVolume;}
	
	/************************静态逻辑方法区*******************************/
    /**
     * 是否为静音状态
     * @return true:静音  false:非静音
     */
    public static final boolean isMute(){
    	return isMute;
    }
}
