package dl.study.bombsuper.game;

import dl.study.tools.Tools;

public class MapDataDecode {
	public  static final  String STR_SIGN_1 =",";
	public  static final  String STR_SIGN_2 ="#";
	private static MapInfo mapInfoBottom;
	private static MapInfo mapInfoBarrier;
	private static MapDataDecode instance;
	private MapDataDecode() {
		mapInfoBottom = getMapInfo("bottom.txt");
		mapInfoBarrier = getMapInfo("barrier.txt");
	}
	public static MapDataDecode getInstance(){
		if(instance==null)
			instance = new MapDataDecode();
		return instance;
	}
	public MapInfo[] decode(int gate){
		return new MapInfo[]{mapInfoBottom,mapInfoBarrier,getMapInfo("cover"+gate+".txt")};
	}
	private MapInfo getMapInfo(String fileName){
		MapInfo mapInfo = new MapInfo();
		String strOrail = Tools.readFileFromAsset("data/map/"+fileName, true);
		String[] arrSplitInfo = Tools.splitString(strOrail, STR_SIGN_2);
		//行，列，图片路径，图块宽，高
		String info[] = Tools.splitString(arrSplitInfo[0],STR_SIGN_1);
		mapInfo.setRows(Integer.parseInt(info[0]));
		mapInfo.setCols(Integer.parseInt(info[1]));
		mapInfo.setStrBmpPath(info[2]);
		mapInfo.setCellWidth(Integer.parseInt(info[3]));
		mapInfo.setCellHeight(Integer.parseInt(info[4]));
		mapInfo.setData(Tools.decodeStringToIntArray(Tools.splitString(arrSplitInfo[1],STR_SIGN_1)));
		return mapInfo;
		
	}
	public class MapInfo{
		private int rows;
		private int cols;
		private int cellWidth;
		private int cellHeight;
		private String strBmpPath;
		private int[] data;
		public MapInfo() {}

		public int getRows() {
			return rows;
		}
		public void setRows(int rows) {
			this.rows = rows;
		}
		public int getCols() {
			return cols;
		}
		public void setCols(int cols) {
			this.cols = cols;
		}
		public int getCellWidth() {
			return cellWidth;
		}
		public void setCellWidth(int cellWidth) {
			this.cellWidth = cellWidth;
		}
		public int getCellHeight() {
			return cellHeight;
		}
		public void setCellHeight(int cellHeight) {
			this.cellHeight = cellHeight;
		}
		public String getStrBmpPath() {
			return strBmpPath;
		}
		public void setStrBmpPath(String strBmpPath) {
			this.strBmpPath = strBmpPath;
		}
		public int[] getData() {
			return data;
		}
		public void setData(int[] data) {
			this.data = data;
		}
		
	}
}
