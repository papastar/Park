package com.joyotime.qparking.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.preference.PreferenceManager;

public class getJson
{

	public static String getAddressByLocal(double longitude, double latitude)
	{
		String back = "";
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
		try
		{
			String content = getUrlContent("http://api.map.baidu.com/geocoder?location=" + df.format(latitude) + "," + df.format(longitude)
					+ "&output=json&key=E3041FEDFA4A24627A4B76539E07658B0FE44A5D", 5000);

			try
			{
				System.out.println(content);
				JSONObject obj = new JSONObject(content);

				if (obj != null)
				{
					String state = obj.getString("status");
					if (state.equals("OK"))
					{
						String address = new JSONObject(obj.getString("result")).getString("addressComponent");
						JSONObject add = new JSONObject(address);
						String stress = add.getString("street");
						String stressNo = add.getString("street_number");
						back = stress + stressNo;
					}
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{

		}

		return back;
	}
	private static void SaveBegin(Activity act, double lang, double lant)
	{
		PreferenceManager.getDefaultSharedPreferences(act.getApplicationContext()).edit().putString("lang", String.valueOf(lang)).putString("lant", String.valueOf(lant)).commit();
	}

	private static void SaveCity(Activity act, String city)
	{
		PreferenceManager.getDefaultSharedPreferences(act.getApplicationContext()).edit().putString("city", city).commit();
	}

	public static String getAddress(double lang, double lant, Activity act)
	{
		String back = "";
		try
		{
			String content = getUrlContent(String.format("http://api.map.baidu.com/geocoder?location=%1$s,%2$s&output=json&key=E3041FEDFA4A24627A4B76539E07658B0FE44A5D", lang, lant), 5000);
			try
			{
				JSONObject obj = new JSONObject(content);

				if (obj.getString("status").equals("OK"))
				{
					JSONObject resu = new JSONObject(obj.getString("result"));
					JSONObject add = new JSONObject(resu.getString("addressComponent"));

					back = add.getString("street") + add.getString("street_number");

					String city = add.getString("city");
					System.out.println("city:" + city);

					SaveBegin(act, lang, lant);
					SaveCity(act, city);
				}

			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{

		}
		return back;
	}

	private static String getUrlContent(String url, int timeOut)
	{
		String str = "";
		try
		{

			URL aURL = null;
			try
			{
				aURL = new URL(url);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}

			URLConnection conn = aURL.openConnection();
			conn.connect();
			StringBuilder b = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			String line;
			while ((line = reader.readLine()) != null)
			{
				b.append(line);
			}
			reader.close();
			str = b.toString();
		}
		catch (IOException e)
		{
		}
		catch (Exception e)
		{
		}
		return str;
	}

}
