package dl.study.bombsuper.game;

/**
 * �̹߳�����
 * @author ����ǿ
 */
public abstract class RunnableManager implements Runnable
{
	/** ����Ĭ��ʱ��(����) **/
	private static final byte NB_DEFAULT_SLEEP_TIME = 80;
	/** ����ʱ��(����) **/
	private long nlSleepTime;
	/** ����-�߳� **/
	private boolean isThread;
	
	public RunnableManager(){
		nlSleepTime = NB_DEFAULT_SLEEP_TIME;
	}
	
	/**
	 * ��������ʱ��
	 * @param nlSleepTime : ����ʱ�䣨���룩
	 */
	public void setSleepTime(long nlSleepTime){
		this.nlSleepTime = nlSleepTime;
	}
	
	/**
	 * �����߳����
	 * @return true�߳�������, false�߳��ѹر�
	 */
	public boolean isThread(){return isThread;}
	
	/**
	 * �����̣߳�������߳��Ѿ��������򲻻��ٱ����������ǵ���close()��Ż��ٱ�������
	 */
	public void start(){
		if(isThread)
			return ;
		isThread = true;
		new Thread(this).start();
	}
	
	/**
	 * �����̣߳�������߳��Ѿ��������򲻻��ٱ����������ǵ���close()��Ż��ٱ�������
	 * �����߳�����ʱ�䣨��Ȼ�߳�δ���������߳�����ʱ��Ҳ���ᱻָ��ʱ����ģ�
	 * @param nlSleepTime : ����ʱ�䣨���룩
	 */
	public void start(long nlSleepTime){
		this.nlSleepTime = nlSleepTime;
		start();
	}
	
	/**
	 * �ر��߳�
	 */
	public void close(){
		isThread = false;
	}
	
	/**
	 * �̹߳����ĵط�
	 */
	public abstract void logic();
	
	@Override
	public void run() 
	{
		while(isThread)
		{
			try {Thread.sleep(nlSleepTime);} catch (InterruptedException e){};
			logic();
		}
	}
}
