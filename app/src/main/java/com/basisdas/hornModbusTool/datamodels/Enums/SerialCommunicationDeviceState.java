package com.basisdas.hornModbusTool.datamodels.Enums;

public enum SerialCommunicationDeviceState
	{
	NOT_EXIST,
	NO_PERMISSION,
	READY,
	CONNECTED;

	public static String[] names()
		{
		String[] arr = new String[SerialCommunicationDeviceState.values().length];
		SerialCommunicationDeviceState[] states = SerialCommunicationDeviceState.values();
		for (int i=0 ; i < arr.length; i++)
			{
			arr[i] = states[i].toString();
			}
		return arr;
		}


	}
