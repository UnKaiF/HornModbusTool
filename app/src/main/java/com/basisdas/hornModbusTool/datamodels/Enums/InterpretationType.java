package com.basisdas.hornModbusTool.datamodels.Enums;

import android.util.Range;

import java.util.ArrayList;


public enum InterpretationType
	{
	Bool			(new Range<Integer>(1,1)),
	Char			(new Range<Integer>(8,8)),
	WideChar		(new Range<Integer>(16,16)),
	Binary			(new Range<Integer>(1,64)),
	UnsignedDecimal	(new Range<Integer>(8,64)),
	Decimal			(new Range<Integer>(8,64)),
	Hex				(new Range<Integer>(8,64)),
	Float			(new Range<Integer>(32,32)),
	Double			(new Range<Integer>(64,64));

	private final Range<Integer> bitSizeRange;

	private InterpretationType(Range<Integer> sizesRange)
		{
		this.bitSizeRange = sizesRange;
		}

	public Range<Integer> getBitSizeRange()
		{
		return this.bitSizeRange;
		}

	public static String[] names()
		{
		String[] arr = new String[InterpretationType.values().length];
		InterpretationType[] interpretationTypes = InterpretationType.values();
		for (int i=0 ; i < arr.length; i++)
			{
			arr[i] = interpretationTypes[i].toString();
			}
		return arr;
		}


	static public InterpretationType[] getContainingInRange(Range<Integer> range)
		{
		ArrayList<InterpretationType> arr = new ArrayList<>();
		for (InterpretationType interpretationType : InterpretationType.values())
			{
			if (range.contains(interpretationType.bitSizeRange))
				{
				arr.add(interpretationType);
				}
			}
		InterpretationType[] a = new InterpretationType[arr.size()];
		return arr.toArray(a);
		}


	static public InterpretationType[] getCompatibleByBitSize(Integer bitSize)
		{
		ArrayList<InterpretationType> arr = new ArrayList<>();
		for (InterpretationType interpretationType : InterpretationType.values())
			{
			if (interpretationType.bitSizeRange.contains(bitSize))
				{
				arr.add(interpretationType);
				}
			}
		InterpretationType[] a = new InterpretationType[arr.size()];
		return arr.toArray(a);
		}


	static public InterpretationType getByName(String _name)
		{
		for (InterpretationType interpretationType : InterpretationType.values())
			{
			if (interpretationType.toString().equalsIgnoreCase(_name))
				{
				return interpretationType;
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
