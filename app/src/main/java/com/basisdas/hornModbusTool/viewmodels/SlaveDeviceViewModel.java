package com.basisdas.hornModbusTool.viewmodels;

import androidx.annotation.Nullable;

import com.basisdas.hornModbusTool.datamodels.MDOParameters;
import com.basisdas.hornModbusTool.datamodels.SlaveDevice;
import com.basisdas.hornModbusTool.datamodels.TransactionObject;

import java.util.ArrayList;

public class SlaveDeviceViewModel extends Deflatable implements ITransactionFlowNode
	{

	private SerialCommLineViewModel parent;

	private SlaveDevice slaveDevice;

	public ArrayList<ModbusDataObjectViewModel> modbusDataObjectViewModels;

	public SlaveDeviceViewModel(SerialCommLineViewModel parent, SlaveDevice slaveDevice)
		{
		this.parent = parent;
		this.slaveDevice = slaveDevice;
		this.modbusDataObjectViewModels = new ArrayList<>();
		}

	public SlaveDeviceViewModel(int slaveID, String name)
		{
		slaveDevice = new SlaveDevice();
		}

	public int getSlaveID()
		{
		return slaveDevice.getSlaveID();
		}

	public void setSlaveID(int slaveID)
		{
		slaveDevice.setSlaveID(slaveID);
		}

	public String getDeviceName()
		{
		return slaveDevice.getDeviceName();
		}

	public void setDeviceName(String name)
		{
		slaveDevice.setDeviceName(name);
		}




/*
	//поиск в коллекции ОДМ объекта с совпадающими параметрами
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


	public int deleteMDOViewModel(@Nullable Object o)
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

*/

	@Override
	public void deflateChilds()
		{
		this.deflate();
		for (ModbusDataObjectViewModel modbusDataObjectViewModel: modbusDataObjectViewModels)
			{
			modbusDataObjectViewModel.deflate();
			}
		}

	@Override
	public void ariseTransaction(TransactionObject transactionObject)
		{

		}

	@Override
	public boolean plantTransaction(TransactionObject transactionObject)
		{
		return false;
		}
	}
