package com.joyotime.qparking.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{

	private static final int VERSION = 15;
	private static final String DB_NAME = "record.db";
	public DBHelper(Context context)
	{
		super(context, DB_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		String sqlUserData = "CREATE TABLE BlueData(id int identity(1,1) primary key NOT NULL,bluename nvarcher(100) NULL,lockid nvarchar(100) NULL,blueaddress nvarchar(100) NULL,notename nvarchar(100) NULL,key nvarchar(100) NULL,sn  nvarchar(50) NULL,locktoken  nvarchar(500) NULL,";
		sqlUserData += "openid nvarcher(100) NULL,isowner nvarcher(2) NULL,isuse nvarcher(2) NULL,locklong nvarchar(50) NULL,locklat nvarchar(50) NULL,lockaddress nvarchar(50) NULL,citycode nvarchar(50) NULL, cityname  nvarchar(50) NULL,parkingname nvarchar(200) NULL,parkingaddress nvarchar(200) NULL ,betocellphone nvarchar(20) NULL)";
		String sqlUserInfo = "CREATE TABLE UserData(id int identity(1,1) primary key NOT NULL,openid nvarcher(100) NULL,username nvarchar(100) NULL,token nvarchar(500) NULL,headimage nvarchar(500) NULL,phonenum nvarchar(15) NULL,insign nvarchar(2) NULL,gender nvarchar(50) NULL,province nvarchar(50) NULL,city nvarchar(50) NULL)";
		String sqlUserShare = "CREATE TABLE UserShare(id int identity(1,1) primary key NOT NULL,openid nvarcher(100) NULL,shareid nvarchar(100) NULL,guest nvarchar(20) NULL,usabledate nvarchar(50) NULL,sn nvarchar(15) NULL,bluetooth nvarchar(100) NULL)";
		String sqlHardware = "CREATE TABLE HardWare(id int identity(1,1) primary key NOT NULL,hardwarecode nvarcher(10) NULL,hardwaresoftcode nvarchar(10) NULL,openid nvarcher(100) NULL,sn nvarchar(15) NULL)";
		db.execSQL(sqlUserData);
		db.execSQL(sqlUserInfo);
		db.execSQL(sqlUserShare);
		db.execSQL(sqlHardware);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
		try
		{
			db.execSQL("DROP TABLE BlueData");
			db.execSQL("DROP TABLE UserData");
			db.execSQL("DROP TABLE UserShare");
			db.execSQL("DROP TABLE HardWare");	
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		

		String sqlUserData = "CREATE TABLE BlueData(id int identity(1,1) primary key NOT NULL,bluename nvarcher(100) NULL,lockid nvarchar(100) NULL,blueaddress nvarchar(100) NULL,notename nvarchar(100) NULL,key nvarchar(100) NULL,sn  nvarchar(50) NULL,locktoken  nvarchar(500) NULL,";
		sqlUserData += "openid nvarcher(100) NULL,isowner nvarcher(2) NULL,isuse nvarcher(2) NULL,locklong nvarchar(50) NULL,locklat nvarchar(50) NULL,lockaddress nvarchar(50) NULL,citycode nvarchar(50) NULL,cityname nvarchar(50) NULL,parkingname nvarchar(200) NULL,parkingaddress nvarchar(200) NULL,betocellphone nvarchar(20) NULL)";
		String sqlUserInfo = "CREATE TABLE UserData(id int identity(1,1) primary key NOT NULL,openid nvarcher(100) NULL,username nvarchar(100) NULL,token nvarchar(500) NULL,headimage nvarchar(500) NULL,phonenum nvarchar(15) NULL,insign nvarchar(2) NULL,gender nvarchar(50) NULL,province nvarchar(50) NULL,city nvarchar(50) NULL)";
		String sqlUserShare = "CREATE TABLE UserShare(id int identity(1,1) primary key NOT NULL,openid nvarcher(100) NULL,shareid nvarchar(100) NULL,guest nvarchar(20) NULL,usabledate nvarchar(50) NULL,sn nvarchar(15) NULL,bluetooth nvarchar(100) NULL)";
		String sqlHardware = "CREATE TABLE HardWare(id int identity(1,1) primary key NOT NULL,hardwarecode nvarcher(10) NULL,hardwaresoftcode nvarchar(10) NULL,openid nvarcher(100) NULL,sn nvarchar(15) NULL)";
		db.execSQL(sqlUserData);
		db.execSQL(sqlUserInfo);
		db.execSQL(sqlUserShare);
		db.execSQL(sqlHardware);
	}

}
