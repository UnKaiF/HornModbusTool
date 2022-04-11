package com.basisdas.hornModbusTool.datamodels;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.basisdas.jlibmodbusandroid.master.ModbusMaster;
import com.basisdas.jlibmodbusandroid.master.ModbusMasterFactory;
import com.basisdas.jlibmodbusandroid.serial.SerialParameters;
import com.basisdas.jlibmodbusandroid.serial.SerialPortException;
import com.basisdas.jlibmodbusandroid.serial.SerialPortFactoryUSBSerialAndroid;
import com.basisdas.jlibmodbusandroid.serial.SerialUtils;
import com.basisdas.jlibmodbusandroid.serial.util.AndroidUSBSerialPortResolver;
import com.basisdas.hornModbusTool.datamodels.Enums.MBProtocolType;
import com.basisdas.hornModbusTool.datamodels.Enums.SerialCommunicationDeviceState;

import java.util.List;

public class SerialCommunicationDevice
	{
	public static final String ACTION_USB_PERMISSION = "ACTION.USB_PERMISSION";
	private final SerialParameters sp;
	private final MBProtocolType protocol;
	private final Context applicationContext;
	private final UsbManager manager;
	private UsbDevice usbDevice;
	private ModbusMaster modbusMaster;
	private SerialCommunicationDeviceState state = SerialCommunicationDeviceState.NOT_EXIST;

	public SerialCommunicationDevice(Context appContext, SerialParameters sp, MBProtocolType protocol)
		{
		this.applicationContext = appContext;
		this.sp = sp;
		this.protocol = protocol;
		SerialUtils.setSerialPortFactory(new SerialPortFactoryUSBSerialAndroid(), applicationContext);
		manager = (UsbManager) applicationContext.getSystemService(Context.USB_SERVICE);
		advanceState();
		}

	private void advanceState()
		{
		switch (state)
			{
			case NOT_EXIST:
				usbDevice = AndroidUSBSerialPortResolver.getUsbDevice(sp.getDevice(), applicationContext);
				if (usbDevice == null)
					return;
				state = SerialCommunicationDeviceState.NO_PERMISSION;
			case NO_PERMISSION:
				if (! manager.hasPermission(usbDevice))
					{
					PendingIntent permissionIntent = PendingIntent.getBroadcast(applicationContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
					manager.requestPermission(usbDevice, permissionIntent);
					return;
					}
				state = SerialCommunicationDeviceState.READY;
			case READY:
				try
					{
					switch (protocol)
						{
						case RTU:
							modbusMaster = ModbusMasterFactory.createModbusMasterRTU(sp);
						break;
						case ASCII:
							modbusMaster = ModbusMasterFactory.createModbusMasterASCII(sp);
						break;
						}
					if (modbusMaster == null)
						throw new SerialPortException("Some error while creating ModbusMaster instance");
					}
				catch (Exception e)
					{
					state = SerialCommunicationDeviceState.NOT_EXIST;
					modbusMaster = null;
					return;
					}
				try
					{
					modbusMaster.connect();
					}
				catch (Exception e)
					{
					state = SerialCommunicationDeviceState.NOT_EXIST;
					}
			default:
				state = SerialCommunicationDeviceState.CONNECTED;
			}
		}

	public SerialCommunicationDeviceState getState()
		{
		return state;
		}

	public void disconnect()
		{
		if (state == SerialCommunicationDeviceState.CONNECTED)
			{
			try
				{
				if (modbusMaster.isConnected())
					modbusMaster.disconnect();
				}
			catch (Exception e)
				{
				e.printStackTrace();
				}
			}
		state = SerialCommunicationDeviceState.NOT_EXIST;
		}


	private boolean isAvailibleInSystem()
		{
		return manager.getDeviceList().get(AndroidUSBSerialPortResolver.extractDeviceName(sp.getDevice())) != null;
		}


	public SerialParameters getSerialParameters()
		{
		return sp;
		}

	public MBProtocolType getMBProtocolType()
		{
		return protocol;
		}

	public String getMBSerialCommunicationDeviceName()
		{
		return sp.getDevice();
		}

	static public List<String> getAvailibleUSBSerialCommDevice(Context applicationContext)
		{
		return AndroidUSBSerialPortResolver.getAvailiblePortIdentifiers(applicationContext);
		}

	}
