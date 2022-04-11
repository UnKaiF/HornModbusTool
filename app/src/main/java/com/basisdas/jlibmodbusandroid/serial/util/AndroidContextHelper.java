package com.basisdas.jlibmodbusandroid.serial.util;

import android.content.Context;
import android.hardware.usb.UsbManager;

public class AndroidContextHelper
	{

	private Context appcontext = null;

	public Context getAppContext()
		{
		return appcontext;
		}

	public void setAppContext(Context context)
		{
		this.appcontext = context;
		}

	public UsbManager getUsbManager()
		{
		return (UsbManager) appcontext.getSystemService(Context.USB_SERVICE);
		}

	private AndroidContextHelper() {}

	private static class SingletonHelper
		{
		private static final AndroidContextHelper INSTANCE = new AndroidContextHelper();
		}

	public static AndroidContextHelper getInstance()
		{
		return SingletonHelper.INSTANCE;
		}


	}