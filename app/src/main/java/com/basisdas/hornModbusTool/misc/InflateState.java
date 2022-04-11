package com.basisdas.hornModbusTool.misc;

public enum InflateState
	{
	DEFLATED(0),
	INFLATED(1);

	private int value;
	private InflateState(int value)
		{
		this.value = value;
		}
	}
