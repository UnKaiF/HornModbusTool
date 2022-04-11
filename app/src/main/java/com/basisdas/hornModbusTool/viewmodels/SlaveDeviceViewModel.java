package com.basisdas.hornModbusTool.viewmodels;

import com.basisdas.hornModbusTool.datamodels.SlaveDevice;
import com.basisdas.hornModbusTool.datamodels.TransactionObject;

import java.util.ArrayList;

public class SlaveDeviceViewModel extends Deflatable implements ITransactionFlowNode
	{

	private SerialCommLineViewModel parent;
	private SlaveDevice slaveDevice;
	private ArrayList<ModbusDataObjectViewModel> modbusDataObjectViewModels;

	public SlaveDeviceViewModel(SerialCommLineViewModel parent, SlaveDevice slaveDevice)
		{
		this.parent = parent;
		this.slaveDevice = slaveDevice;
		this.modbusDataObjectViewModels = new ArrayList<>();
		}

	@Override
	public void deflateChilds()
		{
		for (ModbusDataObjectViewModel modbusDataObjectViewModel: modbusDataObjectViewModels)
			{
			modbusDataObjectViewModel.deflateChilds();
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
