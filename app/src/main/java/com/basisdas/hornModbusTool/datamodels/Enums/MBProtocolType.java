package com.basisdas.hornModbusTool.datamodels.Enums;

public enum MBProtocolType
	{
	ASCII,
	RTU;

	public static String[] names()
		{
		String[] arr = new String[MBProtocolType.values().length];
		MBProtocolType[] types = MBProtocolType.values();
		for (int i=0 ; i < arr.length; i++)
			{
			arr[i] = types[i].toString();
			}
		return arr;
		}
	}
