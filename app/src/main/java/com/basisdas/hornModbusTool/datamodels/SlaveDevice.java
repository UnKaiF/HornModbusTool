package com.basisdas.hornModbusTool.datamodels;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SlaveDevice
	{

	private int slaveID;
	private String deviceName;
	private final ArrayList<ModbusDataObject> modbusDataObjects = new ArrayList<>();

	public SlaveDevice() {};

	public SlaveDevice(int slaveID, String name)
		{
		this.setDeviceName(name);
		this.setSlaveID(slaveID);
		}

	public int getSlaveID()
		{
		return slaveID;
		}

	public void setSlaveID(int slaveID)
		{
		this.slaveID = slaveID;
		}

	public String getDeviceName()
		{
		return deviceName;
		}

	public void setDeviceName(String name)
		{
		this.deviceName = name;
		}

	public ModbusDataObject getMDO(int index)
		{
		return modbusDataObjects.get(index);
		}

	public int getMDOCount()
		{
		return modbusDataObjects.size();
		}

	public int indexOf(@Nullable Object o)
		{
		if (o == null)
			return -1;
		MDOParameters parameters = null;
		if (o.getClass() == ModbusDataObject.class)
			parameters = ((ModbusDataObject) o).getParams();
		if (o.getClass() == MDOParameters.class)
			parameters = (MDOParameters) o;
		if (parameters == null)
			return -1;
		for (int i = 0; i < modbusDataObjects.size(); i++)
			{
			if (modbusDataObjects.get(i).getParams().equals(parameters))
				return i;
			}
		return -1;
		}


	public int deleteMDO(@Nullable Object o)
		{
		int index = indexOf(o);
		if (index != -1) modbusDataObjects.remove(index);
		return index;
		}

	public int deleteMDO(int index)
		{
		modbusDataObjects.remove(index);
		return index;
		}


	public int addMDO(ModbusDataObject modbusDataObject)
		{
		if (indexOf(modbusDataObject) == -1)
			{
			modbusDataObjects.add(modbusDataObject);
			return modbusDataObjects.size() -1;
			}
		return -1;
		}


	public int addMDO(int index, ModbusDataObject element)
		{
		if (indexOf(element) == -1)
			{
			modbusDataObjects.add(index, element);
			return index;
			}
		return -1;
		}

	}
