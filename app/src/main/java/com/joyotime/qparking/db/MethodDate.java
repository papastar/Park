package com.joyotime.qparking.db;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;

public class MethodDate
{
	private SQLiteDatabase DATABASE;// =SQLiteDatabase.openOrCreateDatabase(DBHelpernew.DB_NAME,
									// null);
	private DBHelper DB_HELPER;
	Context context;

	public MethodDate(Context context)
	{
		this.context = context;
		DB_HELPER = new DBHelper(context);
	}

	public void writeLogTxt(String active, String strcontent)
	{
		Log.i("bbbbbbb", active + strcontent);

		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 鍒ゆ柇sd鍗℃槸鍚﹀瓨鍦�
		if (sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();// 鑾峰彇璺熺洰褰�
		}

		String filePath = sdDir.toString() + "/QParking/";
		File filefoder = new File(filePath);
		if (!filefoder.exists())
		{
			filefoder.mkdir();
		}

		File fileTxt = new File(filePath + "Log.txt");
		if (!fileTxt.exists())
		{
			try
			{
				fileTxt.createNewFile();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		// 每次写入时，都换行写
		String strContent = date + "\r\n" + active + ":" + strcontent + "\r\n\r\n";
		try
		{
			if (!fileTxt.exists())
			{
				fileTxt.getParentFile().mkdirs();
				fileTxt.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(fileTxt, "rwd");
			raf.seek(fileTxt.length());
			raf.write(strContent.getBytes());
			raf.close();
		}
		catch (Exception e)
		{

		}
	}

	// 方法类
	// 数据库连接
	// 全局变量

	/** 新增车锁信息
	 * 
	 * @param ID
	 * @param 名称
	 * @param 蓝牙地址
	 * @param 备注
	 * @param Key3
	 * @param S
	 *            /N
	 * @param Key2
	 * @param openid
	 * @param 是否主人
	 *            (0否、1是)
	 * @param 是否正在使用
	 *            (0否、1是)
	 * @param 经度
	 * @param 纬度
	 * @param 停车场内部位置
	 * @param 所在城市名称
	 * @param 所在停车场名称
	 * @param 所在停车场地址
	 * @return */
	public boolean InstarBlueDBLocal(String LockId, String bluename, String blueaddress, String notename, String key, String sn, String locktoken, String openid, String isowner, String isuse,
			String locklong, String locklat, String lockaddress, String cityname, String parkingname, String parkingaddress, String citycode, String betocellphone)
	{
		try
		{
			if (isuse.equals("1"))
			{
				DATABASE = DB_HELPER.getWritableDatabase();
				String sql = "UPDATE BlueData SET isuse='0' WHERE openid ='" + openid + "'";
				DATABASE.execSQL(sql);
				DATABASE.close();
			}

			int _ID = GetMaxKeyId("BlueData");
			DATABASE = DB_HELPER.getWritableDatabase();
			DATABASE.execSQL("insert into BlueData (id,lockid,bluename,blueaddress,notename,key,sn,locktoken,openid,isowner,isuse,locklong,locklat,lockaddress,cityname,parkingname,parkingaddress,citycode,betocellphone) values("
					+ _ID
					+ ",'"
					+ LockId
					+ "','"
					+ bluename
					+ "','"
					+ blueaddress
					+ "','"
					+ notename
					+ "','"
					+ key
					+ "','"
					+ sn
					+ "','"
					+ locktoken
					+ "','"
					+ openid
					+ "','"
					+ isowner
					+ "','"
					+ isuse
					+ "','" + locklong + "','" + locklat + "','" + lockaddress + "','" + cityname + "','" + parkingname + "','" + parkingaddress + "','" + citycode + "','" + betocellphone + "')");
			DATABASE.close();
			return true;
		}
		catch (Exception e)
		{
			String xx = e.toString();
			return false;
		}

	}

	/** 获取版本号
	 * 
	 * @return 当前应用的版本号 */
	public int getVersion()
	{
		try
		{
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			int version = info.versionCode;
			return version;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	/** 获取版本号
	 * 
	 * @return 当前应用的版本号 */
	public String getVersionName()
	{
		try
		{
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		}
		catch (Exception e)
		{
			e.getMessage();
			return "";
		}
	}
	/** 更新 */
	public boolean UpdateBlueDBLocal(String bluename, String notename, String key, String sn, String locktoken, String locklong, String locklat, String lockaddress, String cityname,
			String parkingname, String parkingaddress, String citycode, String betocellphone)
	{
		try
		{
			DATABASE = DB_HELPER.getWritableDatabase();
			if (key == null && locktoken == null)
			{
				DATABASE.execSQL("update BlueData set bluename='" + bluename + "',notename='" + notename + "',locklong='" + locklong + "',locklat='" + locklat + "',lockaddress='" + lockaddress
						+ "',cityname='" + cityname + "',parkingname='" + parkingname + "',parkingaddress='" + parkingaddress + "',citycode='" + citycode + "',betocellphone='" + betocellphone
						+ "' where sn='" + sn + "'");
			}
			else if (locktoken == null && key != null)
			{
				DATABASE.execSQL("update BlueData set bluename='" + bluename + "',notename='" + notename + "',key='" + key + "' ,locklong='" + locklong + "',locklat='" + locklat + "',lockaddress='"
						+ lockaddress + "',cityname='" + cityname + "',parkingname='" + parkingname + "',parkingaddress='" + parkingaddress + "',citycode='" + citycode + "',betocellphone='"
						+ betocellphone + "'  where sn='" + sn + "'");
			}
			else
			{
				DATABASE.execSQL("update BlueData set bluename='" + bluename + "',notename='" + notename + "',key='" + key + "',locktoken='" + locktoken + "',locklong='" + locklong + "',locklat='"
						+ locklat + "',lockaddress='" + lockaddress + "',cityname='" + cityname + "',parkingname='" + parkingname + "',parkingaddress='" + parkingaddress + "',citycode='" + citycode
						+ "',betocellphone='" + betocellphone + "'  where sn='" + sn + "'");
			}

			DATABASE.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}

	}
	/** 更新正在使用的锁
	 * 
	 * @param openid
	 * @param SN
	 * @return */
	public boolean UpdateIsUseLock(String openid, String SN)
	{
		try
		{

			DATABASE = DB_HELPER.getWritableDatabase();
			String sql = "UPDATE BlueData SET isuse='0' WHERE openid ='" + openid + "'";
			DATABASE.execSQL(sql);
			DATABASE.close();

			DATABASE = DB_HELPER.getWritableDatabase();
			String sql1 = "UPDATE BlueData SET isuse='1' WHERE openid ='" + openid + "' and sn='" + SN + "' ";
			DATABASE.execSQL(sql1);
			DATABASE.close();

			return true;
		}
		catch (Exception e)
		{
			return false;// PhoneNum
		}

	}
	/** 更新用户信息
	 * 
	 * @param openid
	 * @param username
	 * @param headimage
	 * @return */
	public boolean UpdateUserInfo(String openid, String username, String headimage, String gender, String province, String city)
	{
		try
		{
			DATABASE = DB_HELPER.getWritableDatabase();
			String sql1 = "UPDATE UserData SET username='" + username + "',headimage='" + headimage + "',gender='" + gender + "',province='" + province + "',city='" + city + "' WHERE openid ='"
					+ openid + "' ";
			DATABASE.execSQL(sql1);
			DATABASE.close();

			return true;
		}
		catch (Exception e)
		{
			return false;// PhoneNum
		}

	}

	/** 添加用户信息
	 * 
	 * @param openid
	 * @param username
	 * @param token
	 * @return 成功返回true */
	public boolean InstarUserDataLocal(String openid, String username, String token, String headimage, String phonenum, String gender, String province, String city)
	{
		try
		{

			DATABASE = DB_HELPER.getWritableDatabase();
			String sql = "UPDATE UserData SET insign='0' WHERE openid !='" + openid + "'";
			DATABASE.execSQL(sql);
			DATABASE.close();

			int _ID = GetMaxKeyId("UserData");
			DATABASE = DB_HELPER.getWritableDatabase();
			DATABASE.execSQL("insert into UserData (id,openid,username,token,headimage,insign,phonenum,gender,province,city) values(" + _ID + ",'" + openid + "','" + username + "','" + token + "','"
					+ headimage + "','1','" + phonenum + "','" + gender + "','" + province + "','" + city + "')");
			DATABASE.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}

	}

	/** 更改登陆
	 * 
	 * @param openid
	 * @param username
	 * @param token
	 * @param headimage
	 * @return */
	public boolean LogoUserDataLocal(String openid, String username, String token, String headimage)
	{
		try
		{

			DATABASE = DB_HELPER.getWritableDatabase();
			String sql = "UPDATE UserData SET insign='0' WHERE openid !='" + openid + "'";
			DATABASE.execSQL(sql);
			DATABASE.close();

			DATABASE = DB_HELPER.getWritableDatabase();
			String sql1 = "UPDATE UserData SET insign='1',username='" + username + "',token='" + token + "',headimage='" + headimage + "' WHERE openid ='" + openid + "'";
			DATABASE.execSQL(sql1);
			DATABASE.close();

			return true;
		}
		catch (Exception e)
		{
			return false;// PhoneNum
		}

	}

	/** 更新手机号码
	 * 
	 * @param openid
	 * @param PhoneNum
	 * @return */
	public boolean UpdatePhoneNum(String openid, String PhoneNum)
	{
		try
		{
			DATABASE = DB_HELPER.getWritableDatabase();
			String sql1 = "UPDATE UserData SET PhoneNum='" + PhoneNum + "'  WHERE openid ='" + openid + "'";
			DATABASE.execSQL(sql1);
			DATABASE.close();

			return true;
		}
		catch (Exception e)
		{
			return false;//
		}

	}

	/** 同步服务器版本并更新
	 * 
	 * @param hardwaresoftcode
	 * @param hardwarecode
	 * @return */
	public boolean UpdateVersion(String hardwaresoftcode, String hardwarecode)
	{
		try
		{
			DATABASE = DB_HELPER.getWritableDatabase();
			String sql = "UPDATE HardWare SET hardwaresoftcode='" + hardwaresoftcode + "' where hardwarecode='" + hardwarecode + "'  ";
			DATABASE.execSQL(sql);
			DATABASE.close();

			return true;
		}
		catch (Exception e)
		{
			return false;//
		}

	}

	/** 新增固件版本号
	 * 
	 * @param hardwarecode
	 * @param hardwaresoftcode
	 * @param openid
	 * @param sn
	 * @return */
	public boolean InstarVersion(String hardwarecode, String hardwaresoftcode, String openid, String sn)
	{
		try
		{
			int _ID = GetMaxKeyId("HardWare");
			DATABASE = DB_HELPER.getWritableDatabase();
			DATABASE.execSQL("insert into HardWare (id,hardwarecode,hardwaresoftcode,openid,sn) values(" + _ID + ",'" + hardwarecode + "','" + hardwaresoftcode + "','" + openid + "','" + sn + "')");
			DATABASE.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	public String GetAllVersion()
	{
		String hardCode = "";
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null;
		try
		{
			sql = "SELECT hardwarecode FROM HardWare GROUP by hardwarecode";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Cursor cursor = DATABASE.rawQuery(sql, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			int hardwarecodeColumn = cursor.getColumnIndex("hardwarecode");
			hardCode = cursor.getString(hardwarecodeColumn) + ",";
		}
		return hardCode;
	}

	/** 获取硬件固件版本号
	 * 
	 * @param hardwarecode
	 * @return */
	public int GetOneVersion(int hardwarecode)
	{
		String hardwaresoftcode = "";
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null;
		try
		{
			sql = "SELECT hardwaresoftcode FROM HardWare where hardwarecode='" + hardwarecode + "'";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Cursor cursor = DATABASE.rawQuery(sql, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			int hardwarecodeColumn = cursor.getColumnIndex("hardwaresoftcode");
			hardwaresoftcode = cursor.getString(hardwarecodeColumn);
		}
		return hardwaresoftcode == "" ? 0 : Integer.parseInt(hardwaresoftcode);
	}

	/** 获取表最大主键
	 * 
	 * @param 表名
	 * @return */
	public int GetMaxKeyId(String TableName)
	{
		try
		{
			DATABASE = DB_HELPER.getWritableDatabase();
			int count = 0;
			String sql = "SELECT MAX(id) FROM " + TableName + "";
			Cursor cursor = DATABASE.rawQuery(sql, null);
			while (cursor.moveToNext())
			{
				count = cursor.getInt(0); // 获取第一列的值,第一列的索引从0开始
			}
			cursor.close();
			DATABASE.close();
			count++;
			return count;
		}
		catch (Exception e)
		{
			return 100;
		}
	}

	public int GetTableCode(String TableName, String openid, String strwhere)
	{
		try
		{
			DATABASE = DB_HELPER.getWritableDatabase();
			int count = 0;
			String sql = null;
			if (strwhere != null && !strwhere.equals(""))
			{
				sql = "SELECT count(*) FROM " + TableName + " where openid='" + openid + "' and "+strwhere+" ";
			}
			else
			{
				sql = "SELECT count(*) FROM " + TableName + " where openid='" + openid + "'";
			}

			Cursor cursor = DATABASE.rawQuery(sql, null);
			while (cursor.moveToNext())
			{
				count = cursor.getInt(0); // 获取第一列的值,第一列的索引从0开始
			}
			cursor.close();
			DATABASE.close();
			return count;
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	/** 获取所有车锁设备
	 * 
	 * @return */
	public Cursor GetAllBlue(String openid)
	{
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null;
		try
		{
			sql = "SELECT ID as _id,lockid,bluename,blueaddress,sn,key,notename,locktoken,isowner,locklong,locklat,lockaddress,cityname,parkingname,parkingaddress,betocellphone FROM BlueData where openid='"
					+ openid + "'  ";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Cursor cursor = DATABASE.rawQuery(sql, null);

		return cursor;

	}
	/** 获取手机号
	 * 
	 * @param snString
	 * @return */
	public Cursor GetSharePhoneNum(String snString)
	{
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null;
		try
		{
			sql = "select a.betocellphone as phonenum from BlueData a where a.sn= '" + snString + "'  ";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Cursor cursor = DATABASE.rawQuery(sql, null);

		return cursor;

	}

	public Cursor GetIsUseBlue(String openid)
	{
		int counttable = GetTableCode("BlueData", openid,"");

		int nowcounttable = GetTableCode("BlueData", openid," isuse='1'  ");
		String sql = null,sql1=null;
		DATABASE = DB_HELPER.getWritableDatabase();
		if(counttable>0&&nowcounttable<=0)
		{
			sql1 = "update BlueData set isuse='1'  where sn in (select  sn from BlueData  where openid='" + openid + "'  order by id desc limit 1)";
			DATABASE.execSQL(sql1);
		}
		
		try
		{
			sql = "SELECT ID as _id,lockid,bluename,blueaddress,sn,key,notename,locktoken,isowner ,locklong,locklat,lockaddress,cityname,parkingname,parkingaddress,betocellphone FROM BlueData where openid='"
					+ openid + "' and isuse='1' ";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Cursor cursor = DATABASE.rawQuery(sql, null);

		return cursor;

	}

	/** 获取分享锁
	 * 
	 * @param openid
	 * @return */
	public Cursor GetShareBlue(String openid,String snString)
	{
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null;
		try
		{
			sql = "SELECT ID as _id,shareid,guest,sn FROM UserShare where openid='" + openid + "'  and sn='"+snString+"'";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Cursor cursor = DATABASE.rawQuery(sql, null);

		return cursor;

	}

	/** 获取指定车锁设备
	 * 
	 * @return */
	public Cursor GetonlyBlue(String sn)
	{
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null;
		try
		{
			sql = "SELECT ID as _id,lockid,bluename,blueaddress,sn,key,notename,isowner ,locklong,locklat,lockaddress,cityname,parkingname,parkingaddress,betocellphone FROM BlueData where sn='" + sn
					+ "'";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Cursor cursor = DATABASE.rawQuery(sql, null);

		return cursor;

	}

	/** 获取登陆用户
	 * 
	 * @return */
	public Cursor GetLoginUser()
	{
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null;
		try
		{
			sql = "SELECT ID as _id,openid,username,token,headimage,phonenum,gender,province,city  FROM  UserData  where insign='1' ";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cursor cursor = DATABASE.rawQuery(sql, null);
		return cursor;
	}

	/** 验证用户是否存在
	 * 
	 * @param openid
	 * @return 存在返回true */
	public Boolean ValidationUser(String openid)
	{
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null;
		try
		{
			sql = "SELECT ID as _id FROM UserData where  openid='" + openid + "'";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cursor dataCursor = DATABASE.rawQuery(sql, null);
		int count = 0;
		while (dataCursor.moveToNext())
		{
			count = dataCursor.getInt(0); // 获取第一列的值,第一列的索引从0开始
		}
		dataCursor.close();
		DATABASE.close();

		if (count > 0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	public Boolean ValidationBlue(String blueaddess, String isowner)
	{
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null;
		try
		{
			sql = "SELECT ID as _id FROM BlueData where  blueaddress='" + blueaddess + "' and isowner='" + isowner + "' ";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cursor dataCursor = DATABASE.rawQuery(sql, null);
		int count = 0;
		while (dataCursor.moveToNext())
		{
			count = dataCursor.getInt(0); // 获取第一列的值,第一列的索引从0开始
		}
		dataCursor.close();
		DATABASE.close();

		if (count > 0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	/** 检查已经分享的车锁
	 * 
	 * @param blueaddess
	 * @param guest
	 * @return */
	public Boolean ValidationShareBlue(String blueaddess, String guest)
	{
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null;
		try
		{
			sql = "SELECT ID as _id FROM UserShare where  bluetooth='" + blueaddess + "' and guest='" + guest + "'";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cursor dataCursor = DATABASE.rawQuery(sql, null);
		int count = 0;
		while (dataCursor.moveToNext())
		{
			count = dataCursor.getInt(0); // 获取第一列的值,第一列的索引从0开始
		}
		dataCursor.close();
		DATABASE.close();

		if (count > 0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}
	/** 新增分享锁
	 * 
	 * @param openid
	 * @param shareid
	 * @param guest
	 * @param usabledate
	 * @param sn
	 * @param bluetooth
	 * @return */
	public boolean InstarBlueDBShare(String openid, String shareid, String guest, String usabledate, String sn, String bluetooth)
	{
		try
		{
			int _ID = GetMaxKeyId("UserShare");
			DATABASE = DB_HELPER.getWritableDatabase();
			DATABASE.execSQL("insert into UserShare (id,openid,shareid,guest,usabledate,sn,bluetooth) values(" + _ID + ",'" + openid + "','" + shareid + "','" + guest + "','" + usabledate + "','"
					+ sn + "','" + bluetooth + "')");
			DATABASE.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}

	}

	/** 删除车锁设备
	 * 
	 * @param 车锁蓝牙地址
	 * @return */
	public boolean DetetelBlueDBLocal(String bluenote, Boolean IsUser, String openid)
	{
		/* try { */
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null, sql1 = null, sql2 = null, sql3 = null;
		try
		{
			sql = "DELETE FROM BlueData WHERE sn ='" + bluenote + "'";
			sql1 = "DELETE FROM UserShare WHERE sn ='" + bluenote + "'";
			sql2 = "DELETE FROM HardWare WHERE sn ='" + bluenote + "'";
			sql3 = "update BlueData set isuse='1'  where sn in (select  sn from BlueData  where openid='" + openid + "'  order by id desc limit 1)";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (IsUser)
		{

		}
		DATABASE.execSQL(sql);
		DATABASE.execSQL(sql1);
		DATABASE.execSQL(sql2);
		DATABASE.execSQL(sql3);
		DATABASE.close();
		return true;
		/* } catch (Exception e) { return false; } */
	}
	public boolean DetetelBlueShareToMe()
	{
		/* try { */
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null;
		try
		{
			sql = "DELETE FROM BlueData WHERE isowner ='0'";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DATABASE.execSQL(sql);
		DATABASE.close();
		return true;
		/* } catch (Exception e) { return false; } */
	}
	/** 删除
	 * 
	 * @param shareid
	 * @return */
	public boolean DetetelBlueShare(String shareid)
	{

		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null;
		try
		{
			sql = "DELETE FROM UserShare WHERE shareid ='" + shareid + "'";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DATABASE.execSQL(sql);
		DATABASE.close();
		return true;

		/* try { */

		/* } catch (Exception e) { return false; } */
	}

	/** 删除用户
	 * 
	 * @param openid
	 * @return */
	public boolean DetetelUserData(String openid)
	{
		/* try { */
		DATABASE = DB_HELPER.getWritableDatabase();
		String sql = null, sql1 = null, sql2 = null, sql3 = null;
		try
		{
			sql = "DELETE FROM UserData WHERE openid ='" + openid + "'";
			sql1 = "DELETE FROM BlueData WHERE openid ='" + openid + "'";
			sql2 = "DELETE FROM UserShare WHERE openid ='" + openid + "'";
			sql3 = "DELETE FROM HardWare WHERE openid ='" + openid + "'";
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DATABASE.execSQL(sql);
		DATABASE.execSQL(sql1);
		DATABASE.execSQL(sql2);
		DATABASE.execSQL(sql3);
		DATABASE.close();
		return true;
		/* } catch (Exception e) { return false; } */
	}
}
