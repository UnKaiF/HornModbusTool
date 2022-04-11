package com.basisdas.hornModbusTool.datamodels;

import com.basisdas.hornModbusTool.datamodels.Exceptions.TransactionException;

public class TransactionObject
	{
	public boolean isReadTransaction;
	public int slaveID;
	public MDOParameters parameters;
	public MDODataContainer container;
	public TransactionException exception = null;

	public TransactionObject(boolean isReadTransaction, MDOParameters parameters, MDODataContainer container)
		{
		this.isReadTransaction = isReadTransaction;
		this.parameters = parameters;
		this.container = container;
		}

	}
