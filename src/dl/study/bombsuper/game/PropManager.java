package dl.study.bombsuper.game;

import java.util.ArrayList;

import dl.study.bombsuper.main.Info;
import dl.study.tools.Tools;

import lbq.tools.game.engine.Sprite;
import lbq.tools.game.engine.TiledLayer;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class PropManager {
	private ArrayList<Prop> listProps;
	private TiledLayer tileCover;
	private int rows,cols,niMapX,niMapY,niMapCellWidth,niMapCellHeight;
	public PropManager(TiledLayer tileCover) {
		listProps = new ArrayList<Prop>();
		reset(tileCover);
	}
	public void reset( TiledLayer tileCover) {
		this.tileCover = tileCover ;
		rows=this.tileCover.getRows();
		cols = this.tileCover.getColumns();
		niMapX = this.tileCover.getX();
		niMapY = this.tileCover.getY();
		niMapCellWidth = this.tileCover.getCellWidth();
		niMapCellHeight = this.tileCover.getCellHeight();
		
		if(listProps.size()!=0)
			listProps.clear();
		addProp(PropFactory.NB_PROP_ADD_BOMB);
		addProp(PropFactory.NB_PROP_ADD_SPEED);
		addProp(PropFactory.NB_PROP_KEY);
	}
	@SuppressLint("WrongCall")
	public void onDraw(Canvas canvas, Paint paint){
		for(int i=0;i<listProps.size();i++)
			listProps.get(i).onDraw(canvas, paint);
	}
	public void onDrawCollide(Canvas canvas, Paint paint) {
		for(int i=0;i<listProps.size();i++)
			listProps.get(i).onDrawCollide(canvas, paint);
	}
	public void addProp(byte niType){
		int niRow,niCol;
		Rect rect = new  Rect();
		do{
			niRow = Tools.getRandom(0, rows);
			niCol = Tools.getRandom(0, cols);
			rect.set(
					niCol*niMapCellWidth+niMapX,
					niRow*niMapCellHeight+niMapY,
					(niCol+1)*niMapCellWidth+niMapX,
					(niRow+1)*niMapCellHeight+niMapY
					);
		}while(!(tileCover.getCell(niCol, niRow)!=0)||isCollideOtherProp(rect));
		Prop prop = PropFactory.getInstance().make(rect,niType);
		listProps.add(prop);
	}
	public void setPropVisible(){
		
	}
	public void move(byte dir,int speed){
		switch(dir){
			case Info.NB_DIR_UP:
				move(0,-speed);
				break;
			case Info.NB_DIR_DOWN:
				move(0,speed);
				break;
			case Info.NB_DIR_LEFT:
				move(-speed,0);
				break;
			case Info.NB_DIR_RIGHT:
				move(speed,0);
				break;
		}
	}
	private void move(int offX,int offY){
		for(int i=0;i<listProps.size();i++)
			listProps.get(i).move(offX, offY);
	}
	public  void removeProp(Prop prop){
		if(listProps.size()>0)
			{
			prop.setVisible(false);
			listProps.remove(prop);
			}
	}
	/**
	 * 检测是否与玩家角色碰撞
	 * 是:返回道具类型
	 * 否:返回-1
	 * @param spr
	 * @return
	 */
	public Prop isCollide(Sprite spr){
		Prop prop = null;
		for(int i=0;i<listProps.size();i++){
			 prop = listProps.get(i);
			if(spr.collidesWith(prop.getPropCollideRect()))
				return prop;
		}
		return null;
	}
	public boolean isCollideOtherProp(Rect rect){
		for(int i=0;i<listProps.size();i++){
			if(rect.intersect(listProps.get(i).getPropCollideRect()))
				return true;
			}
		return false;
	}
	
	
}
