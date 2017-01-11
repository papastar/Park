package com.joyotime.qparking.model;

public class BlueMsg
{
	private int icon_id;
	private String title;
	private String msg;
	private String keyid;


	public BlueMsg(String title, String msg,String keyid)
	{
		this.title = title;
		this.msg = msg;
		this.keyid=keyid;
	}

	public int getIcon_id()
	{
		return icon_id;
	}
	public void setIcon_id(int icon_id)
	{
		this.icon_id = icon_id;
	}
	public String getTitle()
	{
		return title;
	}
	public String getKeyId()
	{
		return keyid;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public void setKeyId(String keyid)
	{
		this.keyid = keyid;
	}
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}


}
