package dl.study.bombsuper.game;

/**
 * 线程管理器
 * @author 龙博强
 */
public abstract class RunnableManager implements Runnable
{
	/** 休眠默认时间(毫秒) **/
	private static final byte NB_DEFAULT_SLEEP_TIME = 80;
	/** 休眠时间(毫秒) **/
	private long nlSleepTime;
	/** 开关-线程 **/
	private boolean isThread;
	
	public RunnableManager(){
		nlSleepTime = NB_DEFAULT_SLEEP_TIME;
	}
	
	/**
	 * 设置休眠时间
	 * @param nlSleepTime : 休眠时间（毫秒）
	 */
	public void setSleepTime(long nlSleepTime){
		this.nlSleepTime = nlSleepTime;
	}
	
	/**
	 * 返回线程情况
	 * @return true线程运行中, false线程已关闭
	 */
	public boolean isThread(){return isThread;}
	
	/**
	 * 开启线程（如果此线程已经开启，则不会再被开启，除非调用close()后才会再被启动）
	 */
	public void start(){
		if(isThread)
			return ;
		isThread = true;
		new Thread(this).start();
	}
	
	/**
	 * 开启线程（如果此线程已经开启，则不会再被开启，除非调用close()后才会再被启动）
	 * 更改线程休眠时间（既然线程未被启动，线程休眠时间也将会被指定时间更改）
	 * @param nlSleepTime : 休眠时间（毫秒）
	 */
	public void start(long nlSleepTime){
		this.nlSleepTime = nlSleepTime;
		start();
	}
	
	/**
	 * 关闭线程
	 */
	public void close(){
		isThread = false;
	}
	
	/**
	 * 线程工作的地方
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
