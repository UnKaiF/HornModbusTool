package com.basisdas.hornModbusTool.viewmodels;

import com.basisdas.hornModbusTool.datamodels.MDOParameters;
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

	public MDOParameters getParams()
		{
		return mdo.getParams();
		}

	public String getParamsString()
		{
		MDOParameters p = mdo.getParams();
		return p.mdoArea.name() +
				"\r\nСтартовый адрес: " + p.startingAddress +
				"\r\nИнтерпретация: " + p.elementType.name() +
				"\r\nБитовый размер: " + p.elementBitSize.name() +
				"\r\nОбратный порядок регистров: " + p.registersSwapped +
				"\r\nОбратный порядок элементов: " + p.elementsReversed;
		}

	public void setParams(MDOParameters params)
		{
		mdo.setParams(params);
		}

	public String getValue()
		{
		return mdo.getValue();
		}

	public void setValue(String value)
		{
		mdo.setValue(value);
		}

	public String getName()
		{
		return mdo.getName();
		}

	public ModbusDataObject getMDO()
		{
		return mdo;
		}

	public void setMDO(ModbusDataObject mdo)
		{
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
