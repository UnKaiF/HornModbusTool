package com.basisdas.hornModbusTool.viewmodels;

import android.app.Application;
import android.content.Context;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.basisdas.hornModbusTool.datamodels.*;
import com.basisdas.hornModbusTool.datamodels.TransactionObject;
import com.basisdas.hornModbusTool.datamodels.utils.MDOInterpreter;
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


	public void performTransaction(int deviceIndex, int mdoIndex, String value)
		{
		boolean isReadTransaction = (value == null);
		SlaveDeviceViewModel slaveVM = slaveDeviceViewModels.get(deviceIndex);
		ModbusDataObjectViewModel mdoVm = slaveVM.modbusDataObjectViewModels.get(mdoIndex);

		if (slaveVM.getState() == EntityState.INACTIVE || mdoVm.getState() == EntityState.INACTIVE)
			return;

		MDOParameters para = mdoVm.getParams();
		MDODataContainer container = MDOInterpreter.getInstance().fromStringValue(para, value);
		TransactionObject tro = new TransactionObject(isReadTransaction, para, container);

		tro.slaveID = slaveVM.getSlaveID();
		tro = serialCommunicationLine.getTransaction(tro);
		mdoVm.setEntitySubState(tro.exception == null ? EntitySubState.GOOD : EntitySubState.ERROR);
		if (tro.parameters.registersSwapped)
			tro.container.swapRegisters(tro.parameters.elementBitSize.getBitSize());
		if (tro.parameters.elementsReversed)
			tro.container.reverseBasicElements(tro.parameters.elementBitSize.getBitSize());
 		mdoVm.setValue(MDOInterpreter.getInstance().toStringValue(para, tro.container));
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
