package com.basisdas.hornModbusTool.datamodels;


public class ModbusDataObject
	{

	private String name = "<МДО>";
	private MDOParameters params;
	private String Value = "?";

	public ModbusDataObject()	{}

	public ModbusDataObject(MDOParameters params, String name)
		{
		this.params = params;
		this.name = name;
		}


	public MDOParameters getParams()
		{
		return params;
		}

	public void setParams(MDOParameters params)
		{
		this.params = params;
		}

	public void setValue(String newValue)
		{
		this.Value = newValue;
		}

	public String getValue()
		{
		return Value;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}
	}
