package dl.study.bombsuper.game;

import dl.study.bombsuper.game.MapDataDecode.MapInfo;
import dl.study.bombsuper.main.Info;
import dl.study.tools.Tools;
import lbq.tools.game.engine.TiledLayer;

public class MapFactory {
	private static MapFactory instance ;
	private MapFactory(){}
	public static MapFactory getInstance(){
		if(instance == null){
			instance = new MapFactory();
		}
		return instance;
	}
	public Map makeMap(int niGate){
		MapInfo[] arrMapInfo= MapDataDecode.getInstance().decode(niGate);
		TiledLayer[] arrTile = new TiledLayer[arrMapInfo.length];
		for(int i=0,iLen=arrTile.length;i<iLen;i++)
		{
			arrTile[i] = new  TiledLayer(
					arrMapInfo[i].getCols(),
					arrMapInfo[i].getRows(),
					Tools.getBmpFromAssetManager(arrMapInfo[i].getStrBmpPath()), 
					arrMapInfo[i].getCellWidth(), 
					arrMapInfo[i].getCellHeight(), 
					Info.NI_SCREEN_WIDTH, Info.NI_SCREEN_HEIGHT);
			int []arrData = arrMapInfo[i].getData();
			for(int j=0,jLen=arrData.length;j<jLen;j++){
				arrTile[i].setCell(j%arrTile[i].getColumns(), j/arrTile[i].getColumns(), arrData[j]);
			}
		}
		return new Map(arrTile);
	}
}
