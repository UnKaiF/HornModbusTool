package com.basisdas.hornModbusTool.datamodels;

import androidx.annotation.Nullable;

import com.basisdas.jlibmodbusandroid.serial.SerialParameters;
import com.basisdas.jlibmodbusandroid.serial.SerialPort;
import com.basisdas.hornModbusTool.datamodels.Enums.MBProtocolType;

import java.util.ArrayList;

public class SerialCommunicationLine
	{

	private SerialParameters serialParameters;
	private MBProtocolType protocolType;
	private final ArrayList<SlaveDevice> modbusSlaveDevices;

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
		protocolType = MBProtocolType.RTU;
		modbusSlaveDevices = new ArrayList<SlaveDevice>();
		}

	public SerialParameters getSerialParameters()
		{
		return serialParameters;
		}

	public void setSerialParameters(SerialParameters serialParameters)
		{
		this.serialParameters = serialParameters;
		}

	public MBProtocolType getProtocolType()
		{
		return protocolType;
		}

	public void setProtocolType(MBProtocolType protocolType)
		{
		this.protocolType = protocolType;
		}

	public int getDevicesCount()
		{
		return modbusSlaveDevices.size();
		}


	public int addDevice(SlaveDevice slaveDevice)
		{
		if (indexOf(slaveDevice) == -1)
			{
			modbusSlaveDevices.add(slaveDevice);
			return modbusSlaveDevices.size() - 1;
			}
		return -1;
		}

	public int addDevice(int index, SlaveDevice slaveDevice)
		{
		if (indexOf(slaveDevice) == -1)
			{
			modbusSlaveDevices.add(index, slaveDevice);
			return index;
			}
		return -1;
		}


	public int indexOf(@Nullable SlaveDevice device)
		{
		if (device == null)
			return -1;
		return indexOf(device.getSlaveID());
		}

	public int indexOf(int slaveID)
		{
		for (int i = 0; i < modbusSlaveDevices.size(); i++)
			{
			if (modbusSlaveDevices.get(i).getSlaveID() == slaveID)
				return i;
			}
		return -1;
		}


	public int deleteDevice(int slaveID)
		{
		int index = indexOf(slaveID);
		modbusSlaveDevices.remove(index);
		return index;
		}

	public int deleteDevice(SlaveDevice slaveDevice)
		{
		return deleteDevice(slaveDevice.getSlaveID());
		}


	public SlaveDevice getDevice(int index)
		{
		return modbusSlaveDevices.get(index);
		}


	}
