package com.basisdas.jlibmodbusandroid.serial;

import android.content.Context;

import com.basisdas.jlibmodbusandroid.Modbus;
import com.basisdas.jlibmodbusandroid.serial.util.AndroidContextHelper;

import java.util.*;

public class SerialUtils
	{

	static private Set<ValidatorSerialPortFactory> validatorSet = new TreeSet<ValidatorSerialPortFactory>();

	static
		{
		registerSerialPortFactory("com.fazecast.jSerialComm.SerialPort", "com.basisdas.jlibmodbusandroid.serial.SerialPortFactoryJSerialComm");
		registerSerialPortFactory("com.google.android.things.AndroidThings", "com.basisdas.jlibmodbusandroid.serial.SerialPortFactoryAT");
		registerSerialPortFactory("com.hoho.android.usbserial.driver.UsbSerialPort", "com.basisdas.jlibmodbusandroid.serial.SerialPortFactoryUSBSerialAndroid");
		}

	static private SerialPortAbstractFactory factory = null;


	static public void registerSerialPortFactory(String connectorClassname, String factoryClassname)
		{
		if (!validatorSet.add(new ValidatorSerialPortFactory(connectorClassname, factoryClassname)))
			{
			Modbus.log().warning("The factory is already registered, skipping: " + factoryClassname);
			}
		}


	static public SerialPort createSerial(SerialParameters sp) throws SerialPortException
		{
		return SerialUtils.factory.createSerial(sp);
		}

	static public List<String> getPortIdentifiers() throws SerialPortException
		{
		return SerialUtils.factory.getPortIdentifiers();
		}

	/**
	 * @param factory - a concrete serial port factory instance
	 * @since 1.2.1
	 */
	static public void setSerialPortFactory(SerialPortAbstractFactory factory, Context appContext)
		{
		AndroidContextHelper.getInstance().setAppContext(appContext);
		SerialUtils.factory = factory;
		}

		//SerialUtils.factory = new SerialPortFactoryJSerialComm();

	}
