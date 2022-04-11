package com.basisdas.hornModbusTool.datamodels.utils;


import android.os.Build;

import com.basisdas.hornModbusTool.datamodels.Enums.InterpretationType;
import com.basisdas.hornModbusTool.datamodels.MDODataContainer;
import com.basisdas.hornModbusTool.datamodels.MDOParameters;

import java.math.BigInteger;

public class MDOInterpreter
	{

	private MDOInterpreter() {}

	private static class SingletonHelper
		{
		private static final MDOInterpreter INSTANCE = new MDOInterpreter();
		}

	public static MDOInterpreter getInstance()
		{
		return MDOInterpreter.SingletonHelper.INSTANCE;
		}

	private static final String space = " ";
	private static final String question = "?";
	private static final BigInteger TWO_64 = BigInteger.ONE.shiftLeft(64);

	public MDODataContainer fromStringValue(MDOParameters para, String value)
		{
		MDODataContainer container = new MDODataContainer();
		if (value == null || value.length() == 0)
			return container;

		int expectedElementsCount;
		if (para.elementBitSize.getBitSize() >= para.mdoArea.minBitsPerOperation())
			{
			//MDO занимает несколько регистров и элемент будет всего 1
			expectedElementsCount = 1;
			}
		else
			{
			expectedElementsCount = para.mdoArea.minBitsPerOperation() / para.elementBitSize.getBitSize();
			}

		value = value.trim().replaceAll(" +", space);
		value = value.replace(',','.');
		String[] values = value.split(space);
		if (values.length < expectedElementsCount)
			return container;
		long[] basicElements = new long[expectedElementsCount];
		for (int elIdx = 0, vaIdx = expectedElementsCount-1; elIdx < expectedElementsCount; elIdx++, vaIdx--)
			{
			try
				{
				basicElements[elIdx] = stringToBasic(values[vaIdx], para.elementType, para.elementBitSize.getBitSize());
				}
			catch (NumberFormatException e)
				{
				return container;
				}
			}
		container.setFromBasicElements(para.elementBitSize.getBitSize(), basicElements);
		return container;
		}

	public String toStringValue(MDOParameters para, MDODataContainer container)
		{
		if (container == null || container.isEmpty())
			return question;
		StringBuilder stringBuilder = new StringBuilder();
		long[] elements = container.getBasicElements(para.elementBitSize.getBitSize());
		for (int index = elements.length - 1; index >= 0; index--)
			{
			stringBuilder.append(basicToString(elements[index], para.elementType, para.elementBitSize.getBitSize()));
			if (index != 0)
				stringBuilder.append(space);
			}
		return stringBuilder.toString();
		}

	private long stringToBasic(String val, InterpretationType type, int bitsize) throws NumberFormatException
		{
		Long l = 0L;
		switch (type)
			{
			case Bool:
				l = Boolean.parseBoolean(val) ? 1L : 0L;
			break;

			case Binary:
				l = parseLong(val, 2);
			break;

			case Char:
			case WideChar:
				l = (long)val.charAt(0);
			break;

			case Hex:
				l = parseLong(val, 16);
			break;

			case Float:
				l =  (long) Float.floatToIntBits(Float.valueOf(val));
			break;

			case Double:
				l = Double.doubleToLongBits(Double.valueOf(val));
			break;

			case UnsignedDecimal:
				if (val.contains("-"))
					throw new NumberFormatException();
			case Decimal:
				l = parseLong(val, 10);
			}


		long mask = ((1L << bitsize) - 1L);
		mask = (mask == 0) ? -1L : mask;
		l &= mask;
		return l;
		}

	private String basicToString(Long val, InterpretationType type, int bitsize)
		{
		String str;
		switch (type)
			{
			case Bool:
				return Boolean.toString(val != 0);

			case Binary:
				return Long.toBinaryString(val);


			case Char:
				return Character.toString((char)(val & 0xffL));


			case WideChar:
				return Character.toString((char)(val & 0xffffL));

			case Hex:
				return Long.toHexString(val);

			case Float:
				str = String.format("%.3g", Float.intBitsToFloat((int)(val & 0xFFFFFFFFL)));
				return str.replace(',','.');

			case Double:
				str = String.format("%.3g", Double.longBitsToDouble(val));
				return str.replace(',','.');


			case Decimal:
				int shift = Long.SIZE - bitsize;
				val <<= shift;
				val >>= shift;
				return Long.toString(val);

			case UnsignedDecimal:
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
					{
					return Long.toUnsignedString(val);
					}
				else
					{
					return asUnsignedDecimalString(val);
					}

			default:
				return "";
			}
		}

	private long parseLong(String s, int base) throws NumberFormatException
		{
		return new BigInteger(s, base).longValue();
		}

	private String asUnsignedDecimalString(long l)
		{
		BigInteger b = BigInteger.valueOf(l);
		if (b.signum() < 0)
			{
			b = b.add(TWO_64);
			}
		return b.toString();
		}
	}
