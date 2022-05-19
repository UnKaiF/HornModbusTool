package com.basisdas.hornModbusTool.datamodels;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SlaveDevice
	{
	private int slaveID;
	private String deviceName;

	public SlaveDevice(int slaveID, String deviceName)
		{
		this.slaveID = slaveID;
		this.deviceName = deviceName;
		}

	public SlaveDevice()
		{
		this.slaveID = 0;
		this.deviceName = "Устройство Modbus";
		}

	public String getDeviceName()
		{
		return deviceName;
		}

	public void setDeviceName(String deviceName)
		{
		this.deviceName = deviceName;
		}

	public int getSlaveID()
		{
		return slaveID;
		}

	public void setSlaveID(int slaveID)
		{
		this.slaveID = slaveID;
		}
	}
