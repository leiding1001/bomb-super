package dl.study.tools;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import dl.study.bombsuper.main.Info;
import dl.study.bombsuper.main.Main;
import dl.study.engine.SoundMonitor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

public final class Tools {
	private static Random random = new Random(); 
	public static final SoundMonitor SOUND = new SoundMonitor(Main.getAudioManager());
	private Tools(){};
	
	public static int getRandom(int niMin,int niMax){
		return random.nextInt(niMax-niMin)+niMin;
	}
	public static void testInfo(String info){
		Log.i("sysout", info);
	}
	/**
	 * 读取指定的图片文件
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBmpFromAssetManager(String fileName){
		Bitmap bmp = null;
		InputStream is = null;
		try {
			is = Main.getAssetManager().open(fileName);
			bmp = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			testInfo("加载文件["+fileName+"]错误");
		}
		return bmp;
	}
	/**
	 * 读取文件夹中所用的图片文件
	 * @param fileDir
	 * @return
	 */
	public static Bitmap[] getBitmapFileFromAsset(String fileDir){
		String[] arrFileName = null ;
		try {
			arrFileName = Main.getAssetManager().list(fileDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(arrFileName==null||arrFileName.length==0)
			return null;
		Bitmap[] bmp=new Bitmap[arrFileName.length];
		for(int i=0;i<bmp.length;i++)
			bmp[i] = getBmpFromAssetManager(fileDir+"/"+arrFileName[i]);
		return bmp;
	}
	public static boolean pointCollideWithRect(float niX, float niY, Rect rect) {
		if(niX>=rect.left&&niX<=rect.right&&niY>=rect.top&&niY<=rect.bottom)
			return true;
		return false;
	}
	public static boolean pointCollideWithRect(int niX, int niY, Rect rect) {
		if(niX>=rect.left&&niX<=rect.right&&niY>=rect.top&&niY<=rect.bottom)
			return true;
		return false;
	}
	/**
	 * 通过给定方向进行反转
	 * @param nbDir
	 * @return
	 */
	public static final byte translateDirection(byte nbDir)
	{
		switch(nbDir){
		case Info.NB_DIR_UP : return Info.NB_DIR_DOWN;
		case Info.NB_DIR_DOWN :return Info.NB_DIR_UP;
		case Info.NB_DIR_LEFT :return Info.NB_DIR_RIGHT;
		case Info.NB_DIR_RIGHT :return Info.NB_DIR_LEFT;
		}
		return Info.NB_DIR_NULL;
	}
	public static final String readFileFromAsset(String fileName,boolean isFormat){
		String strOrial =readDataFromAsset(fileName);
		if(isFormat)
			strOrial = StringFormat(strOrial);
		int index = strOrial.indexOf('#');
		if(index==-1)
			return strOrial;
		strOrial = strOrial.substring(index+1);
		return strOrial;
	}
	public static final String readDataFromAsset(String fileName){
		String strOrial = null;
		InputStream is=null;
		ByteArrayOutputStream outStream = null ;
		byte[] buffer;
		try {
			outStream = new ByteArrayOutputStream();
			is = Main.getAssetManager().open(fileName);
			buffer = new byte[1024];
			int len =0;
			while((len=is.read(buffer))!=-1)
			{
				outStream.write(buffer,0,len);
			}
			strOrial = new String(outStream.toByteArray(),"utf-8");
		} catch (IOException e) {
			testInfo("读取"+fileName+"文件数据错误");
		}finally{
			if(is!=null)
				try {is.close();} catch (IOException e) {}
			if(outStream!=null)
				try {outStream.close();} catch (IOException e) {}
			is=null;
			outStream = null;
		}
		return strOrial;
	}
	public static final String StringFormat(String strOrial){
		StringBuffer strBuf=new StringBuffer();
		for(int i=0;i<strOrial.length();i++){
			char c = strOrial.charAt(i);
			if(c == '<'){
				i=strOrial.indexOf('>', i);
				continue;
			}
			else
//				回车，换行，空格符
				if(c == 10 || c== 13 || c==32)
					continue;
			strBuf.append(c);
		}
		return strBuf.toString();
	}
	public static final String[] splitString(String strData,String strSign){
		String[] arrStr = null;
		if(strData!=null&&strData.length()>0&&strData.contains(strSign))
			arrStr = strData.split(strSign);
		return arrStr;
	}	
	public static final int[] decodeStringToIntArray(String arrStrData[]){
		int[] arrData = null;
		if(arrStrData!=null&&arrStrData.length>0)
			arrData = new int[arrStrData.length];
		for(int i=0;i<arrData.length;i++){
			arrData[i] = Integer.parseInt(arrStrData[i]);
		}
		return arrData;
	}
	//存储rom
	public static final void saveFile(FileOutputStream fos ,String strContent){
		try {
			fos.write(strContent.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fos != null)
				try {fos.close();	} catch (IOException e) {}
			fos=null;
		}
	}
	//读取Rom
	public static final String loadRom(FileInputStream fis)
	{
		byte[] arrNBBuffer = null;
			try {
				int niLen = fis.available();
				if(niLen == 0)
					return null;
				arrNBBuffer = new byte[niLen];
				fis.read(arrNBBuffer);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(fis != null)
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					fis = null;
			}
			if(arrNBBuffer == null)
				return null;
			return new String(arrNBBuffer);
		}
}
