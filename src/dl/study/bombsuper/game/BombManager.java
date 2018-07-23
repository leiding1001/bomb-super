package dl.study.bombsuper.game;

import java.util.ArrayList;

import lbq.tools.game.engine.Sprite;
import lbq.tools.game.engine.TiledLayer;

import dl.study.bombsuper.main.GameController.BulletCallBack;
import dl.study.tools.Tools;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class BombManager {
	private ArrayList<Bomb>list;
	private BulletCallBack cb;
	
	public BombManager(BulletCallBack cb) {
		list = new ArrayList<Bomb>();
		this.cb = cb  ;
	}
	@SuppressLint("WrongCall")
	public void onDraw(Canvas canvas,Paint paint){
		for(int i=0;i<list.size();i++){
			list.get(i).onDraw(canvas, paint);
		}
	}
	public void move(byte dir,int speed){
		for(int i=0;i<list.size();i++){
			list.get(i).move(dir, speed);
		}
	}
	/***
	 * 添加炸弹
	 * @param point 图块中心点
	 * @return
	 */
	public boolean addBomb(Rect rectTile,int niMaxBombNum){
		Rect rect = null;
		if(list.size()<niMaxBombNum)
			{
			rect=new Rect(
					rectTile.centerX()-Bomb.NI_BOMB_WIDTH/2,rectTile.centerY()-Bomb.NI_BOMB_HEIGHT/2,
					rectTile.centerX()+Bomb.NI_BOMB_WIDTH/2,rectTile.centerY()+Bomb.NI_BOMB_HEIGHT/2
					);
			for(int i=0;i<list.size();i++){
				if(rect.intersect(list.get(i).getBombtRect()))
					return false;
			}
			list.add(new Bomb(cb, rect, niMaxBombNum));
			list.get(list.size()-1).start();
			return true;
			}
		else
			return false;
	}
	public void removeBomb(){
		
		for(int i=0;i<list.size();i++){
			if(list.get(i).isDead()){
				list.remove(i);
				break;
			}
		}
	}
	public boolean isCollideSprite(Sprite spr){
		Bomb bomb = null;
		for(int i=0;i<list.size();i++)
		{
			bomb = list.get(i);
			if(bomb.isBombing())
				continue;
			if(spr.collidesWith(bomb.getBombtRect()))
				{
					if(spr instanceof Player)
					{
						if(bomb.isPlayerMoveAble())
							return false;
						else
							return true;
					}
					return true;
				}
			else{
				if(spr instanceof Player&&bomb.isPlayerMoveAble())
					bomb.setPlayerMoveAble(false);
				}
			}
		return false;
	}
	public boolean isMoveAble(byte dir, int niSpeed,Sprite spr) {
		Bomb bomb = null;
		boolean isMoveAble = true;
		move(dir, niSpeed);
		for(int i=0;i<list.size();i++)
		{
			bomb = list.get(i);
			if(bomb.isBombing())
				continue;
			if(spr.collidesWith(bomb.getBombtRect()))
				{
				isMoveAble= false;
				break;
				}
		}
		move(Tools.translateDirection(dir), niSpeed);
		return isMoveAble;
	}
	public Rect getThrowBombRect(int col, int row, byte curDir,Rect rectPlayer,
			TiledLayer tileBarrier, TiledLayer tileCover) {
		Rect rect = null;
		//第一种：在payer位置上扔炸弹
		rect = new Rect(
				col*tileBarrier.getCellWidth()+tileBarrier.getX(),
				row*tileBarrier.getCellHeight()+tileBarrier.getY(),
				(col+1)*tileBarrier.getCellWidth()+tileBarrier.getX(),
				(row+1)*tileBarrier.getCellHeight()+tileBarrier.getY()
				);
		//第二种：在payer面向的位置上扔炸弹
//		int offX=0,offY=0;
//			switch(curDir){
//			case Info.NB_DIR_UP:
//				row= row - 1 ;
//				break;
//			case Info.NB_DIR_DOWN:
//				row= row + 1 ;
//				break;
//			case Info.NB_DIR_LEFT:
//				col = col - 1; 
//				break;
//			case Info.NB_DIR_RIGHT:
//				col = col + 1; 
//				break;
//			}
//			if(tileBarrier.getCell(col, row)==0&&tileCover.getCell(col, row)==0){
//				rect = new Rect(
//						col*tileBarrier.getCellWidth()+tileBarrier.getX(),
//						row*tileBarrier.getCellHeight()+tileBarrier.getY(),
//						(col+1)*tileBarrier.getCellWidth()+tileBarrier.getX(),
//						(row+1)*tileBarrier.getCellHeight()+tileBarrier.getY()
//						);
//				switch(curDir){
//					case Info.NB_DIR_UP:
//						rect.set(rect.left, rect.top+(rectPlayer.top-rect.bottom), rect.right, rect.bottom+(rectPlayer.top-rect.bottom));
//						break;
//					case Info.NB_DIR_DOWN:
//						rect.set(rect.left, rect.top+(rectPlayer.bottom-rect.top), rect.right, rect.bottom+(rectPlayer.bottom-rect.top));
//						break;
//					case Info.NB_DIR_LEFT:
//						rect.set(rect.left+(rectPlayer.left-rect.right), rect.top, rect.right+(rectPlayer.left-rect.right), rect.bottom);
//						break;
//					case Info.NB_DIR_RIGHT:
//						rect.set(rect.left+(rectPlayer.right-rect.left), rect.top, rect.right+(rectPlayer.right-rect.left), rect.bottom);
//						break;
//				}
//			}
		return rect;
		
	}
	
}
