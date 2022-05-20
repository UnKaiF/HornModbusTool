package com.basisdas.hornModbusTool.viewmodels;

import android.app.Application;
import android.content.Context;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.basisdas.hornModbusTool.datamodels.*;
import com.basisdas.hornModbusTool.datamodels.TransactionObject;
import com.basisdas.hornModbusTool.misc.EntityState;
import com.basisdas.hornModbusTool.misc.EntitySubState;
import com.basisdas.jlibmodbusandroid.serial.SerialParameters;
import com.basisdas.jlibmodbusandroid.serial.util.AndroidUSBSerialPortResolver;

import java.util.ArrayList;
import java.util.List;

public class SerialCommLineViewModel extends Deflatable implements ITransactionFlowNode
	{

	public final ArrayList<SlaveDeviceViewModel> slaveDeviceViewModels;
	private final SerialCommunicationLine serialCommunicationLine;

	public SerialCommLineViewModel()
		{
		serialCommunicationLine = SerialCommunicationLine.getInstance();
		slaveDeviceViewModels = new ArrayList<>();
		}

	public String getCommDevicePath()
		{
		String fullPath = serialCommunicationLine.getSerialParameters().getDevice();
		if (fullPath == null) return "?";
		return fullPath;
		}


	public String getCommDeviceParametersString()
		{
		return serialCommunicationLine.getSerialParameters().getParametersString() +
								 "\r\n";
		}

	public SerialParameters getCommDeviceParameters()
		{
		return serialCommunicationLine.getSerialParameters();
		}

	public void setCommDeviceParameters(SerialParameters sp)
		{
		sp.setDevice(serialCommunicationLine.getSerialParameters().getDevice());
		serialCommunicationLine.setSerialParameters(sp);
		}

	public void renewCommDevice(Context context)
		{
		if (serialCommunicationLine.renewCommDevice(context))
			{
			setEntitySubState(EntitySubState.GOOD);
			setState(EntityState.ACTIVE);
			}
		else
			{
			setState(EntityState.ACTIVE);
			setEntitySubState(EntitySubState.ERROR);
			}
		}


/*
	public int addDevice(SlaveDeviceViewModel slaveDeviceViewModel)
		{
		if (indexOf(slaveDeviceViewModel) == -1)
			{
			slaveDeviceViewModels.add(slaveDeviceViewModel);
			return slaveDeviceViewModels.size() - 1;
			}
		return -1;
		}

	public int addDevice(int index, SlaveDeviceViewModel slaveDeviceViewModel)
		{
		if (indexOf(slaveDeviceViewModel) == -1)
			{
			slaveDeviceViewModels.add(index, slaveDeviceViewModel);
			return index;
			}
		return -1;
		}


	public int indexOf(@Nullable SlaveDeviceViewModel slaveDeviceViewModel)
		{
		if (slaveDeviceViewModel == null)
			return -1;
		return indexOf(slaveDeviceViewModel.getSlaveID());
		}

	public int indexOf(int slaveID)
		{
		for (int i = 0; i < slaveDeviceViewModels.size(); i++)
			{
			if (slaveDeviceViewModels.get(i).getSlaveID() == slaveID)
				return i;
			}
		return -1;
		}


	public int deleteDevice(int slaveID)
		{
		int index = indexOf(slaveID);
		slaveDeviceViewModels.remove(index);
		return index;
		}

	public int deleteDevice(SlaveDeviceViewModel slaveDevice)
		{
		return deleteDevice(slaveDevice.getSlaveID());
		}

*/


	@Override
	public void ariseTransaction(TransactionObject transactionObject)
		{

		}

	@Override
	public boolean plantTransaction(TransactionObject transactionObject)
		{
		return false;
		}

	@Override
	public void deflateChilds()
		{
		this.deflate();
		for (SlaveDeviceViewModel slaveDeviceVm: slaveDeviceViewModels)
			{
			slaveDeviceVm.deflateChilds();
			}
		}


	}
