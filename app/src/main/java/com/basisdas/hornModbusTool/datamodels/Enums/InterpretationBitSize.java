package com.basisdas.hornModbusTool.datamodels.Enums;

import android.util.Range;
import java.util.ArrayList;

public enum InterpretationBitSize
	{
	_1Bit	(1),
	_8Bit	(8),
	_16Bit	(16),
	_32Bit	(32),
	_64Bit	(64);

	private Integer size;

	private InterpretationBitSize(int size)
		{
		this.size = new Integer(size);
		}

	public Integer getBitSize()
		{
		return size;
		}

	public static String[] names()
		{
		String[] arr = new String[InterpretationBitSize.values().length];
		InterpretationBitSize[] interpretationBitSizes = InterpretationBitSize.values();
		for (int i=0 ; i < arr.length; i++)
			{
			arr[i] = interpretationBitSizes[i].toString();
			}
		return arr;
		}

	static public InterpretationBitSize[] getContainingInRange(Range<Integer> range)
		{
		ArrayList<InterpretationBitSize> arr = new ArrayList<>();
		for (InterpretationBitSize interpretationBitSize : InterpretationBitSize.values())
			{
			if (range.contains(interpretationBitSize.size))
				{
				arr.add(interpretationBitSize);
				}
			}
		InterpretationBitSize[] s = new InterpretationBitSize[arr.size()];
		return arr.toArray(s);
		}

	static public InterpretationBitSize getByName(String _name)
		{
		for (InterpretationBitSize interpretationBitSize : InterpretationBitSize.values())
			{
			if (interpretationBitSize.toString().equalsIgnoreCase(_name))
				{
				return interpretationBitSize;
				}
			}
		return null;
		}

	static public InterpretationBitSize getByBitSize(int _value)
		{
		for (InterpretationBitSize interpretationBitSize : InterpretationBitSize.values())
			{
			if (interpretationBitSize.size == _value)
				{
				return interpretationBitSize;
				}
			}
		return null;
		}


	@Override
	public String toString()
		{
		return this.name();
		}

	}
