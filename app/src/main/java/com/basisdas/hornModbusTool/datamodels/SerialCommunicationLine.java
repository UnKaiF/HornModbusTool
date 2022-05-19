package com.basisdas.hornModbusTool.datamodels;

import android.content.Context;

import androidx.annotation.Nullable;

import com.basisdas.hornModbusTool.misc.EntityState;
import com.basisdas.hornModbusTool.misc.EntitySubState;
import com.basisdas.jlibmodbusandroid.serial.SerialParameters;
import com.basisdas.jlibmodbusandroid.serial.SerialPort;
import com.basisdas.hornModbusTool.datamodels.Enums.MBProtocolType;
import com.basisdas.jlibmodbusandroid.serial.util.AndroidUSBSerialPortResolver;

import java.util.ArrayList;
import java.util.List;

public class SerialCommunicationLine
	{

	private SerialParameters serialParameters;
	private int currentUSBSerialDeviceIndex = 0;

	private static class SingletonHelper
		{
		private static final SerialCommunicationLine INSTANCE = new SerialCommunicationLine();
		}

	public static SerialCommunicationLine getInstance()
		{
		return SerialCommunicationLine.SingletonHelper.INSTANCE;
		}

	private SerialCommunicationLine()
		{
		serialParameters = new SerialParameters("/dev/bus/usb/002/002/0", //port full path
												SerialPort.BaudRate.BAUD_RATE_19200,
												8, //Data bits
												1, //Stop bits
												SerialPort.Parity.NONE );
		}

	public SerialParameters getSerialParameters()
		{
		return serialParameters;
		}

	public void setSerialParameters(SerialParameters serialParameters)
		{
		this.serialParameters = serialParameters;
		}

	public boolean renewCommDevice(Context context)
		{
		List<String> devices = AndroidUSBSerialPortResolver.getAvailiblePortIdentifiers(context);
		if (devices.isEmpty())
			{
			serialParameters.setDevice("Нет подходящего интерфейса");
			return false;
			}
		if (++currentUSBSerialDeviceIndex > (devices.size() - 1))
			currentUSBSerialDeviceIndex = 0;
		serialParameters.setDevice(devices.get(currentUSBSerialDeviceIndex));
		//TODO: call Recreate Modbus Master method
		return true;
		}



	}
