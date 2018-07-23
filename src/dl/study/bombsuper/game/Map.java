package dl.study.bombsuper.game;

import dl.study.bombsuper.main.Info;
import dl.study.tools.Tools;
import lbq.tools.game.engine.Sprite;
import lbq.tools.game.engine.TiledLayer;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Point;

public class Map {
	/**µÿ±Ì≤„ **/
	private TiledLayer tileBottom;
	/**’œ∞≠ŒÔ≤„ **/
	private TiledLayer tileBarrier;
	/**’⁄’÷≤„ **/
	private TiledLayer tileCover;

	private Callback cb;
	public Map(TiledLayer[] arrTile){
		tileBottom = arrTile[0];
		if(arrTile.length>=2)
			tileBarrier = arrTile[1];
		if(arrTile.length>=3)
			tileCover = arrTile[2];
	}
	public void addCallback(Callback cb){
		if(this.cb==null)
			this.cb = cb ;
	}
	public void removeCallback(){
		if(this.cb!=null)
			this.cb= null;
	}
	@SuppressLint("WrongCall")
	public void onDraw(Canvas canvas){
		tileBottom.onDraw(canvas, null);
		if(tileBarrier != null)
			tileBarrier.onDraw(canvas, null);
	}
	@SuppressLint("WrongCall")
	public void onDrawCover(Canvas canvas){
		if(tileCover != null)
			tileCover.onDraw(canvas, null);
	}
	public void move(int offX ,int offY){
		tileBottom.move(offX, offY);
		if(tileBarrier != null)
			tileBarrier.move(offX, offY);
		if(tileCover != null)
			tileCover.move(offX, offY);
	}
	public void move(byte dir,int niSpeed){
		switch(dir){
		case Info.NB_DIR_UP:
			move(0,niSpeed);
			break;
		case Info.NB_DIR_DOWN:
			move(0,-niSpeed);
			break;
		case Info.NB_DIR_RIGHT:
			move(-niSpeed,0);
			break;
		case Info.NB_DIR_LEFT:
			move(niSpeed,0);
			break;
		}
	}
	public boolean isMoveAble(byte dir,int niSpeed){
		 boolean isCollide = false;
		 //”ÎºÏ≤‚
		move(dir,niSpeed);
			isCollide = cb.isCollideSprite(tileBarrier,tileCover);
		move(Tools.translateDirection(dir),niSpeed);
		if(isCollide)
			return false;
		switch(dir){
			case Info.NB_DIR_UP:
				return tileBottom.getY()+niSpeed<0;
			case Info.NB_DIR_DOWN:
				return tileBottom.getY()+tileBottom.getHeight()-niSpeed>Info.NI_SCREEN_HEIGHT;
			case Info.NB_DIR_LEFT:
				return tileBottom.getX()+niSpeed<0;
			case Info.NB_DIR_RIGHT:
				return tileBottom.getX()+tileBottom.getWidth()-niSpeed>Info.NI_SCREEN_WIDTH;
		}
		return false;
	}
	public TiledLayer getTileBarrier(){
			return tileBarrier;
	}
	public TiledLayer getTileCover(){
		return tileCover;
	}
	public void setTileCoverValue(byte nbCurDir , int col,int row,int value){
		tileCover.setCell(col, row, value);
	}
	public boolean isExists(int row,int col){
		return tileBarrier.getCell(col, row)!=0||tileCover.getCell(col, row)!=0;
	}
	public  int getMapWidth(){
		return tileBottom.getWidth();
	}
	public  int getMapHeight(){
		return tileBottom.getHeight();
	}
	public  int getMapRows(){
		return tileBottom.getRows();
	}
	public  int getMapCols(){
		return tileBottom.getColumns();
	}
	public  int getMapTileWidth(){
		return tileBottom.getCellWidth();
	}
	public  int getMapTileHeight(){
		return tileBottom.getCellHeight();
	}
	public Point getMapPoint(){
		return new Point(tileBottom.getX(),tileBottom.getY());
	}
	public interface Callback
	{
		boolean isCollideSprite(TiledLayer collide,TiledLayer cover);
	}
	public int getX() {
		return tileBottom.getX();
	}
	public int getY() {
		return tileBottom.getY();
	}
	public boolean collideSprite(Sprite spr) {
		if(tileCover!=null&&spr.collidesWith(tileCover))
			return true;
		if(tileBarrier!=null&&spr.collidesWith(tileBarrier))
			return true;
		return false;
	}
}
