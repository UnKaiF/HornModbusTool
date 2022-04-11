package com.basisdas.jlibmodbusandroid.serial.util;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.util.ArrayList;
import java.util.List;

/**
 *  fullPath to serial port consist of Usbfs path + '/' + port number
 */
public class AndroidUSBSerialPortResolver
	{

	static public List<String> getAvailiblePortIdentifiers(Context context)
		{
		List<String> fullPaths = new ArrayList<>();
		UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
		for (UsbSerialDriver driver: availableDrivers)
			{
			String deviceName = driver.getDevice().getDeviceName();
			for (UsbSerialPort port : driver.getPorts())
				{
				fullPaths.add(deviceName + "/" + port.getPortNumber());
				}
			}
		return fullPaths;
		}

	static public boolean checkFullPathValid(String fullPath)
		{
		if (fullPath == null)
			return false;
		int separatorIndex = fullPath.lastIndexOf('/');
		return (fullPath.length() >= 3) && (separatorIndex != -1);
		}

	static public String extractDeviceName(String fullPath)
		{
		if (checkFullPathValid(fullPath))
			{
			int separatorIndex = fullPath.lastIndexOf('/');
			return fullPath.substring(0, separatorIndex);
			}
		return null;
		}

	static public String extractPortNumber(String fullPath)
		{
		if (checkFullPathValid(fullPath))
			{
			int separatorIndex = fullPath.lastIndexOf('/');
			return fullPath.substring(separatorIndex + 1);
			}
		return null;
		}

	static public int getPortNumber(String fullPath)
		{
		if (fullPath == null)
			return 0;
		return Integer.parseInt(extractPortNumber(fullPath));
		}

	static public UsbSerialDriver getUsbSerialDriver(String fullPath, Context context)
		{
		UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		return UsbSerialProber
					.getDefaultProber()
					.probeDevice(manager.getDeviceList().get(extractDeviceName(fullPath)));
		}

	static public UsbDevice getUsbDevice(String fullPath, Context context)
		{
		return ((UsbManager) context.getSystemService(Context.USB_SERVICE))
				.getDeviceList().get(extractDeviceName(fullPath));
		}

	}
