package com.basisdas.jlibmodbusandroid.serial;

import com.basisdas.jlibmodbusandroid.serial.util.AndroidContextHelper;
import com.basisdas.jlibmodbusandroid.serial.util.AndroidUSBSerialPortResolver;

import java.util.List;


public class SerialPortFactoryUSBSerialAndroid extends SerialPortAbstractFactory
	{

	public SerialPortFactoryUSBSerialAndroid() {}

	@Override
	public SerialPort createSerialImpl(SerialParameters sp)
		{
		return new SerialPortUSBSerialAndroid(sp);
		}

	@Override
	public List<String> getPortIdentifiersImpl()
		{
		return AndroidUSBSerialPortResolver
				.getAvailiblePortIdentifiers(AndroidContextHelper.getInstance().getAppContext());
		}
	}
