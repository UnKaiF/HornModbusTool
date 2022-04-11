package com.basisdas.hornModbusTool.misc;

public enum EntityState
	{
	INACTIVE (0),
	ACTIVE (1);


	private int value;
	private EntityState(int value)
		{
		this.value = value;
		}

	}
