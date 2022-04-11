package com.basisdas.hornModbusTool.viewmodels;

import com.basisdas.hornModbusTool.datamodels.TransactionObject;

public interface ITransactionFlowNode
	{
	void ariseTransaction(TransactionObject transactionObject);
	boolean plantTransaction(TransactionObject transactionObject);
	}
