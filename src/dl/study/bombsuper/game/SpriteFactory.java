package dl.study.bombsuper.game;

import android.graphics.Bitmap;
import dl.study.tools.Tools;
import lbq.tools.game.engine.Sprite;

public class SpriteFactory {
	public static final int NI_SPRITE_TYPE_PLAYER1 = 0x000 ;
	public static final int NI_SPRITE_TYPE_PLAYER2 = 0x001 ;
	public static final int NI_SPRITE_TYPE_PLAYER3 = 0x002 ;
	public static final int NI_SPRITE_TYPE_PLAYER_NUM = 0x003 ;
	public static final int NI_SPRITE_TYPE_ENEMY1  =  0x003 ;
	public static final int NI_SPRITE_TYPE_ENEMY2  =  0x004 ;
	public static final int NI_SPRITE_TYPE_ENEMY_NUM = 0x002 ;
	private static SpriteFactory instance ;
	private SpriteFactory(){} ;
	public static SpriteFactory getInstance(){
		if(instance == null)
			instance = new SpriteFactory();
		return instance;
	}
	public Sprite make(int nbType){
		Sprite spr = null;
		Bitmap bmp= null; 
		switch(nbType){
		case NI_SPRITE_TYPE_PLAYER1:
			bmp = Tools.getBmpFromAssetManager("image/role/player/0.png");
			spr = new Sprite(bmp,bmp.getWidth()/4,bmp.getHeight()/4);
			break;
		case NI_SPRITE_TYPE_PLAYER2:
			bmp = Tools.getBmpFromAssetManager("image/role/player/1.png");
			spr = new Sprite(bmp,bmp.getWidth()/4,bmp.getHeight()/4);
			break;
		case NI_SPRITE_TYPE_PLAYER3:
			bmp = Tools.getBmpFromAssetManager("image/role/player/2.png");
			spr = new Sprite(bmp,bmp.getWidth()/4,bmp.getHeight()/4);
			break;
		case NI_SPRITE_TYPE_ENEMY1:
			 bmp = Tools.getBmpFromAssetManager("image/role/enemy/0.png");
			 spr = new Sprite(bmp,bmp.getWidth()/4,bmp.getHeight()/4);
			break;
		case NI_SPRITE_TYPE_ENEMY2:
			bmp = Tools.getBmpFromAssetManager("image/role/enemy/1.png");
			spr = new Sprite(bmp,bmp.getWidth()/4,bmp.getHeight()/4);
			break;
		}
		return spr;
	}
	public Bitmap getSpriteBitmap(int nbType){
		Bitmap bmp= null; 
		switch(nbType){
		case NI_SPRITE_TYPE_PLAYER1:
			 bmp = Tools.getBmpFromAssetManager("image/role/player/0.png");
			break;
		case NI_SPRITE_TYPE_PLAYER2:
			bmp = Tools.getBmpFromAssetManager("image/role/player/1.png");
			break;
		case NI_SPRITE_TYPE_PLAYER3:
			bmp = Tools.getBmpFromAssetManager("image/role/player/2.png");
			break;
		}
		return bmp;
	}
	public int getEnemyType(int id){
		switch(id){
		case 0:
			return NI_SPRITE_TYPE_ENEMY1;
		case 1:
			return NI_SPRITE_TYPE_ENEMY2;
		}
		return NI_SPRITE_TYPE_ENEMY1;
	}
}
