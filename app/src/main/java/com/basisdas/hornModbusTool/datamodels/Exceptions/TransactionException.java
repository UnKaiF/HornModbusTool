package com.basisdas.hornModbusTool.datamodels.Exceptions;

public abstract class TransactionException extends Exception
	{
	public TransactionException(String message, Throwable err)
		{
		super(message, err);
		}
	}
