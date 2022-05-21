package com.basisdas.hornModbusTool.datamodels;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.basisdas.hornModbusTool.datamodels.Enums.MDOArea;
import com.basisdas.hornModbusTool.datamodels.Exceptions.TransactionException;
import com.basisdas.hornModbusTool.misc.EntityState;
import com.basisdas.hornModbusTool.misc.EntitySubState;
import com.basisdas.hornModbusTool.viewmodels.JournalViewModel;
import com.basisdas.jlibmodbusandroid.exception.ModbusIOException;
import com.basisdas.jlibmodbusandroid.master.ModbusMaster;
import com.basisdas.jlibmodbusandroid.master.ModbusMasterFactory;
import com.basisdas.jlibmodbusandroid.serial.SerialParameters;
import com.basisdas.jlibmodbusandroid.serial.SerialPort;
import com.basisdas.hornModbusTool.datamodels.Enums.MBProtocolType;
import com.basisdas.jlibmodbusandroid.serial.SerialPortFactoryUSBSerialAndroid;
import com.basisdas.jlibmodbusandroid.serial.SerialUtils;
import com.basisdas.jlibmodbusandroid.serial.util.AndroidUSBSerialPortResolver;

import java.util.ArrayList;
import java.util.List;

public class SerialCommunicationLine
	{

	private SerialParameters serialParameters;
	private int currentUSBSerialDeviceIndex = 0;
	private SerialPortFactoryUSBSerialAndroid serialPortFactoryUSBSerialAndroid;
	private ModbusMaster master;

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
												SerialPort.Parity.NONE);
		serialPortFactoryUSBSerialAndroid = new SerialPortFactoryUSBSerialAndroid();
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
		boolean ret = true;
		List<String> devices = AndroidUSBSerialPortResolver.getAvailiblePortIdentifiers(context);
		if (devices.isEmpty())
			{
			serialParameters.setDevice("Нет подходящего интерфейса");
			return false;
			}
		if (++currentUSBSerialDeviceIndex > (devices.size() - 1))
			currentUSBSerialDeviceIndex = 0;

		serialParameters.setDevice(devices.get(currentUSBSerialDeviceIndex));
		SerialUtils.setSerialPortFactory(serialPortFactoryUSBSerialAndroid, context);
		UsbDevice device = AndroidUSBSerialPortResolver.getUsbDevice(serialParameters.getDevice(), context);
		UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

		if (device == null) return false;

		if (!manager.hasPermission(device))
			{
			PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent("ACTION.USB_PERMISSION"), 0);
			manager.requestPermission(device, permissionIntent);
			return false;
			}


		try
			{
			master = ModbusMasterFactory.createModbusMasterRTU(serialParameters);
			//валиться на connect() при отсутствии устройства
			master.connect();
			}
		catch (Exception e)
			{
			ret = false;
			master = null;
			}
		return ret;
		}

	public TransactionObject getTransaction(TransactionObject tro)
		{
		if (tro.isReadTransaction)
			return readTransaction(tro);
		else
			return writeTransaction(tro);
		}

	private TransactionObject readTransaction(TransactionObject tro)
		{
		int quantity = Math.max(tro.parameters.elementBitSize.getBitSize(),  tro.parameters.mdoArea.minBitsPerOperation());
		tro.container = new MDODataContainer();
		try
			{
			switch (tro.parameters.mdoArea)
				{
				case Coil_multiWrite:
				case Coil_singleWrite:
					tro.container.setFromJlibModbusArray(master.readCoils(tro.slaveID, tro.parameters.startingAddress, quantity));
					break;
				case DiscreteInput:
					tro.container.setFromJlibModbusArray(master.readDiscreteInputs(tro.slaveID, tro.parameters.startingAddress, quantity));
					break;
				case InputRegister:
					tro.container.setFromJlibModbusArray(master.readInputRegisters(tro.slaveID, tro.parameters.startingAddress, quantity>>4));
					break;
				case HoldingRegister_singleWrite:
				case HoldingRegister_multiWrite:
					tro.container.setFromJlibModbusArray(master.readHoldingRegisters(tro.slaveID, tro.parameters.startingAddress, quantity>>4));
					break;
				}
			}
		catch (RuntimeException e)
			{
			e.printStackTrace();
			tro.exception = e.getMessage();
			}
		catch (ModbusIOException e)
			{
			e.printStackTrace();
			tro.exception = e.getMessage();
			}
		catch (Exception e)
			{
			e.printStackTrace();
			tro.exception = e.getMessage();
			}
		return tro;
		}

	private TransactionObject writeTransaction(TransactionObject tro)
		{
		switch (tro.parameters.mdoArea)
			{
			case Coil_multiWrite:
				break;
			case Coil_singleWrite:
				break;
			case DiscreteInput:
				//error
				break;
			case InputRegister:
				//error
				break;
			case HoldingRegister_singleWrite:
				break;
			case HoldingRegister_multiWrite:
				break;
			}
		return tro;
		}


	}


/*
			try
				{
				master = ModbusMasterFactory.createModbusMasterRTU(serialParameters);
				//валиться на connect() при отсутствии устройства
				master.connect();
				try
					{
					int[] HWversionRegisterValues = master.readHoldingRegisters(0x02, 0x10, 2);
					int HWvervion = HWversionRegisterValues[0] << 16 | HWversionRegisterValues[1];
					}
				catch (RuntimeException e)
					{
					throw e;
					}
				finally
					{
					try
						{
						master.disconnect();
						}
					catch (ModbusIOException e1)
						{
						e1.printStackTrace();
						}
					}
				}
			catch (Exception e)
				{//Timeout handled here.. now

				}

 */

