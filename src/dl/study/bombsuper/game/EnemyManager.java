package dl.study.bombsuper.game;

import java.util.ArrayList;

import lbq.tools.game.engine.Sprite;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import dl.study.bombsuper.main.GameController.EnemyCallBack;
import dl.study.tools.Tools;

public class EnemyManager {
	private static final int NI_ENEMY_NUM = 5 ;
	private ArrayList<Enemy> listEnemy;
	private EnemyCallBack cb;
	int rows, cols, tileWidth, tileHeight;
	public EnemyManager(int gate,EnemyCallBack cb,int rows,int cols,int tileWidth,int tileHeight) {
		listEnemy = new ArrayList<Enemy>();
		this.cb = cb;
		this.rows = rows;
		this.cols = cols;
		this.tileHeight = tileHeight;
		this.tileWidth = tileWidth;
		reset(gate);
	}
	public void reset(int gate){
		removeAll();
		for(int i=0;i<NI_ENEMY_NUM*gate;i++)
			listEnemy.add(new Enemy(
					SpriteFactory.getInstance().make(getEenemyType()),
					cb,
					rows,
					cols,
					tileWidth,
					tileHeight
					));
	}
	public void removeAll(){
		if(listEnemy.size()!=0)
		{
		for(int i=0;i<listEnemy.size();i++)
			listEnemy.get(i).closeThread();
		listEnemy.clear();
		}
	}
	public int getEenemyType(){
		return SpriteFactory.getInstance().getEnemyType(Tools.getRandom(0, SpriteFactory.NI_SPRITE_TYPE_ENEMY_NUM));
	}
	@SuppressLint("WrongCall")
	public void onDraw(Canvas canvas,Paint paint)
	{
			for(int i=0;i<listEnemy.size();i++)
				{
				listEnemy.get(i).onDraw(canvas, null);
				//listEnemy.get(i).onDrawCollides(canvas);			
				}
	}
	public void move(byte dir, int niMapSpeed){
		for(int i=0;i<listEnemy.size();i++)
			listEnemy.get(i).move(dir,niMapSpeed);
	}
	public void startThread(int nsSleepTime) {
		for(int i=0;i<listEnemy.size();i++)
			listEnemy.get(i).startThread(nsSleepTime);
	}
	public boolean isCollidesWithSprite(Sprite spr) {
		for(int i=0;i<listEnemy.size();i++)
			if(listEnemy.get(i).collidesWith(spr)){
				listEnemy.get(i).closeThread();
				listEnemy.remove(i);
				return true;
			}
		return false;
	}
	public boolean isCollidesWithRect(Rect rect) {
		for(int i=0;i<listEnemy.size();i++)
			if(listEnemy.get(i).getRect().intersect(rect)){
				return true;
			}
		return false;
	}
}
