package com.basisdas.hornModbusTool.misc;

public enum EntitySubState
	{
	UNKNOWN (0),
	ERROR (1),
	GOOD (2);

	private int value;
	private EntitySubState(int value)
		{
		this.value = value;
		}

	}
