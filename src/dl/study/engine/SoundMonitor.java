package dl.study.engine;

import java.util.HashMap;

import dl.study.bumbsuper.main.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public final class SoundMonitor 
{
	/**************************����&��̬������*********************************/
	/** ��ֵ **/
	public static final int NI_NULL = -128;
	/** �������ֵ **/
	private final byte NB_VOLUME_MAX = 15;
	/** ������Сֵ **/
	private final byte NB_VOLUME_MIN = 0;
	/**��ǰ����**/
	private static int niCurrentVolume;
	/**����ǰ����**/
	private static int niOldVolume;
	/** ��������(��̬) **/
	private static boolean isMute;
	/**************************����������*********************************/
	/**************************����������*********************************/
	/** ��Ч�� **/
	private HashMap<Integer, Integer> soundMap;
	/** ���ֳ� **/
	private HashMap<Integer, MediaPlayer> mediaMap;
	/** ��Ч������ **/
	private SoundPool soundPool ;
	/** ���������� **/
	private AudioManager audioManager;
	/** ��ǰ���ֶ������� **/
	private MediaPlayer mediaPlayerCurrent;
	/**************************����������*********************************/
	/** ��ǰ���������ļ�Id,��������ֲ��ţ���Ϊ-128 **/
	private int niCurMediaId = NI_NULL;
	/**************************���췽����*********************************/
	
	public SoundMonitor(AudioManager audioManager)
	{
		//��ȡ�����������
		this.audioManager = audioManager; 
		soundMap = new HashMap<Integer, Integer>();
		mediaMap = new HashMap<Integer, MediaPlayer>();
		//��������
		niOldVolume = niCurrentVolume = NB_VOLUME_MAX;
	}
	
	/**************************�߼�������*************** ******************/
	
	private void readInit(){
		//�ͷ�����Media��Դ
		releaseMediaPlayerAll();
		//�ͷ�����Sound��Դ
		releaseSoundAll();
		//����Sound��Դ
		if(soundPool == null)
			soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	}
	/**
	 * ��ʼ����Ӧ״̬�����ֺ���Ч 
	 */
	public void init(Context context, int niMonitorState)
	{
		readInit();
		/*********************�Զ��岿��******************/
		switch(niMonitorState)
		{
		case 1 :
			//��������м�������
			mediaMap.put(R.raw.map, MediaPlayer.create(context, R.raw.map));
			break;
		case 2 :
			mediaMap.put(R.raw.win, MediaPlayer.create(context, R.raw.win));
			break;
		case 3 :
			mediaMap.put(R.raw.lose, MediaPlayer.create(context, R.raw.lose));
			break;
		}
		//��������м�����Ч
		//soundMap.put(R.raw.pk, soundPool.load(context, R.raw.pk, 1));
	}
	
	/**
	 * �ͷ����е�������Դ
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
	 * �ͷ����е���Ч��Դ
	 */
	private void releaseSoundAll()
	{
		//soundPool.release()��Ͳ�����load��,Ӧ�������ÿ�
		if(soundPool != null)
		{
			soundPool.release();
			soundPool = null;
		}
		soundMap.clear();
	}
	/**
	 * �ͷ�����������Դ(������Ч������)
	 */
	public void release(){
		releaseMediaPlayerAll();
		releaseSoundAll(); 
	} 
	/**
	 * ����ָ��������
	 * @param niRawMediaId : R.raw.id
	 * @param isLoop : �Ƿ�ѭ��
	 * @param nfVolume : ����(0~100),�����������ô�ֵ
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
	 * ��ͣ����
	 * ���������¿�ʼ��Ϸǰ�Ƚ�����������ͣ���ٴο�ʼ����ͨ��startMediaPlayer��
	 * @param niRawMediaId
	 */
	public void pauseMediaPlayer(int niRawMediaId)
	{
		MediaPlayer _mPlayer = mediaMap.get(niRawMediaId);
		if(_mPlayer != null && _mPlayer.isPlaying())
			_mPlayer.pause();
	}
	/**
	 * ֹͣ����
	 * �˲��轫�ͷ����֣��������¼���(init)�������޷��ٴβ��Ŵ�����
	 * @param niRawMediaId
	 */
	public void stopMediaPlayer(int niRawMediaId)
	{
		MediaPlayer _mPlayer = mediaMap.get(niRawMediaId);
		if(_mPlayer != null && _mPlayer.isPlaying())
			_mPlayer.stop();//mediaPlay.stop();	������mediaPlay.isPlaying()����trueʱ����ִ��stop������Logcat����Կ�����error
	}
	/**
	 * �ر�����
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
	 * ������Ч
	 * @param niRawId
	 * @param niLoop : 0Ϊ����һ�Σ�-1Ϊ����ѭ��������ֵΪ����loop+1��
	 */
	public void playEffectOgg(int niRawId, int niLoop)
	{
		//�����к��Բ�����Ч
		if(isMute)
			return ;
		
		Integer _niSoundID = soundMap.get(niRawId);
		if(_niSoundID != null && _niSoundID.byteValue() != 0 && soundPool != null && niCurrentVolume > 0)
		{
	    	/**
	    	 * public final int play (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) 
	    	 * 	soundID  ����R�е�ID��
				leftVolume  ����������(range = 0.0 to 1.0) 
				rightVolume  ���������� (range = 0.0 to 1.0) 
				priority  �������ȼ���ֵԽ�����ȼ��ߣ�Ӱ�쵱ͬʱ�����������������֧����ʱSoundPool�Ը����Ĵ��� (0 = ���) 
				loop  ѭ������ (0Ϊֵ����һ�Σ�-1Ϊ����ѭ��������ֵΪ����loop+1��) 
				rate  ���ŵ����ʣ���Χ0.5-2.0(0.5Ϊһ�����ʣ�1.0Ϊ�������ʣ�2.0Ϊ��������) 

	    	 */
    		soundPool.play(_niSoundID, niCurrentVolume, niCurrentVolume, 1, niLoop, 1F);
		}
	}
	/**
	 * ��������
	 */
	public final void addVolume()
	{
		if(niCurrentVolume < NB_VOLUME_MAX)
			setVolume(++niCurrentVolume);
	}
	/**
	 * ��������
	 */
	public final void reduceVolume()
	{
		if(niCurrentVolume > NB_VOLUME_MIN)
			setVolume(--niCurrentVolume);
	}
    /**
     * ����ϵͳ����
     * @param context
     * @param niVolume : ����Ϊ0��Ϊ����, 15Ϊ�������
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
     * ��ȡ��ǰ���ŵ������ļ��Ƿ��ڲ�����
     * @return
     */
    public final boolean isPlayingCurrentMP()
    {
    	if(mediaPlayerCurrent != null)
    		return mediaPlayerCurrent.isPlaying();
    	return false;
    }
    /**
     * ����
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
     * �ָ�����
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
     * ��ȡϵͳ����
     * @param context
     * @return
     */
    public final int getStreamVolume()
    {
    	return niCurrentVolume;
    }
    /**
     * ��ȡ��ǰ���������ļ�Id
     * @return
     */
    public final int getCurrentMediaId(){
    	return niCurMediaId;
    }
	/**
	 * ��ȡ����ǰ����
	 * @return
	 */
	public final int getMuteBeforeVolume(){return niOldVolume;}
	
	/************************��̬�߼�������*******************************/
    /**
     * �Ƿ�Ϊ����״̬
     * @return true:����  false:�Ǿ���
     */
    public static final boolean isMute(){
    	return isMute;
    }
}
