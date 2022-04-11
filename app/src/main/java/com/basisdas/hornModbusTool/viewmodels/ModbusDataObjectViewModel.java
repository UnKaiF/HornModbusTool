package com.basisdas.hornModbusTool.viewmodels;

import com.basisdas.hornModbusTool.datamodels.ModbusDataObject;
import com.basisdas.hornModbusTool.datamodels.TransactionObject;

public class ModbusDataObjectViewModel extends Deflatable implements ITransactionFlowNode
	{

	private SlaveDeviceViewModel parent;
	private ModbusDataObject mdo;

	public ModbusDataObjectViewModel(SlaveDeviceViewModel parent, ModbusDataObject mdo)
		{
		this.parent = parent;
		this.mdo = mdo;
		}

	@Override
	public void deflateChilds() {}

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
