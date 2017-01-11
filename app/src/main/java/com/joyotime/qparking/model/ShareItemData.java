package com.joyotime.qparking.model;

public class ShareItemData
{
	private int icon_id;
	private String lockname;
	private String lockparking;
	private String lockaddress;
	private String lockusetime;
	private String keyid;

	private String mlong;
	private String mlat;

	public ShareItemData(String lockname, String lockparking,String lockaddress,String lockusetime,String keyid,String mlong,String mlat)
	{
		this.lockname = lockname;
		this.lockparking = lockparking;
		this.lockaddress = lockaddress;
		this.lockusetime = lockusetime;
		this.keyid=keyid;
		this.mlong = mlong;
		this.mlat = mlat;
	}

	public int getIcon_id()
	{
		return icon_id;
	}
	public void setIcon_id(int icon_id)
	{
		this.icon_id = icon_id;
	}
	public String getLockname()
	{
		return lockname;
	}
	public void setLockname(String Lockname)
	{
		this.lockname = Lockname;
	}
	public String getLockparking()
	{
		return lockparking;
	}
	public void setLockparking(String lockparking)
	{
		this.lockparking = lockparking;
	}
	
	public String getKeyId()
	{
		return keyid;
	}
	
	public String getLockaddress()
	{
		return lockaddress;
	}
	public void setLockaddress(String lockaddress)
	{
		this.lockaddress = lockaddress;
	}
	
	public String getLockusetime()
	{
		return lockusetime;
	}
	public void setLockusetime(String lockusetime)
	{
		this.lockusetime = lockusetime;
	}

	public void setKeyId(String keyid)
	{
		this.keyid = keyid;
	}
	
	public String getmLong()
	{
		return mlong;
	}
	public void setmLong(String mLong)
	{
		this.mlong = mLong;
	}

	public String getmLat()
	{
		return mlat;
	}
	public void setmLat(String mLat)
	{
		this.mlat = mLat;
	}

}
