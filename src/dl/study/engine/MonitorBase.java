package dl.study.engine;

import dl.study.bombsuper.game.RunnableManager;
import android.content.Context;
import android.view.View;

public abstract class MonitorBase extends View{

	private ReplayDraw monitorThread;
	protected Context context;
	public MonitorBase(Context context) {
		super(context);
		this.context = context ;
	}
	public void startThread(int sleepTime){
		if(monitorThread==null)
			monitorThread = new ReplayDraw();
		if(sleepTime<10)
			sleepTime = 10 ;
		monitorThread.start(sleepTime);
	}
	public void closeMonitorThread(){
		monitorThread.close();
	}
	public void monitorLogic() {}
	public abstract boolean onKeyDown(int keyCode);
	public abstract boolean onKeyUp(int keyCode);
	public abstract boolean onTouchDown(float niX,float niY);
	public abstract boolean onTouchMove(float niX,float niY);
	public abstract boolean onTouchUp(float niX,float niY);
	public class ReplayDraw extends RunnableManager{

		@Override
		public void logic() {
			monitorLogic();
			postInvalidate();
		}
	}

}
