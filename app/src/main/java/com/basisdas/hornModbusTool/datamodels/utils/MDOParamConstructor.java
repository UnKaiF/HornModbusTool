package com.basisdas.hornModbusTool.datamodels.utils;

import android.util.Range;

import com.basisdas.hornModbusTool.datamodels.MDOParameters;
import com.basisdas.jlibmodbusandroid.Modbus;
import com.basisdas.hornModbusTool.datamodels.Enums.InterpretationBitSize;
import com.basisdas.hornModbusTool.datamodels.Enums.InterpretationType;
import com.basisdas.hornModbusTool.datamodels.Enums.MDOArea;

//Позволяет построить непротиворечивые валидные параметры MDO (MDOParameters)
public class MDOParamConstructor
	{

	private static  MDOParameters params;

	//Параметры по умолчанию (непротиворечивые)
	static
		{
		params = new MDOParameters();
		params.mdoArea = MDOArea.HoldingRegister_singleWrite;
		params.elementType = InterpretationType.UnsignedDecimal;
		params.elementBitSize = InterpretationBitSize._16Bit;
		params.startingAddress = Modbus.MIN_START_ADDRESS;
		params.elementsReversed = false;
		params.registersSwapped = false;
		}

	public static void setMDOParameters(MDOParameters params)
		{
		setMDOArea(params.mdoArea);
		setStartingAddress(params.startingAddress);
		setElementBitSize(params.elementBitSize);
		setElementType(params.elementType);
		setElementsReversed(params.elementsReversed);
		setRegistersSwapped(params.registersSwapped);
		}

	//return deep copy
	public static MDOParameters getMDOParameters()
		{
		return new MDOParameters(params);
		}


	public static MDOArea getMDOArea()
		{
		return params.mdoArea;
		}

	public static void setMDOArea(MDOArea mdoArea)
		{
		params.mdoArea = mdoArea;
		if (	! isHasEnoughBits(params.elementBitSize)	)
			params.elementBitSize = InterpretationBitSize.getContainingInRange(new Range<Integer>(1, bitsLeftToAddressSpaceBound()))[0];
		if ( ! isCompatibleInterpretation(params.elementBitSize))
			fixInterpretationType();
		}

	public static InterpretationType getElementType()
		{
		return params.elementType;
		}

	public static void setElementType(InterpretationType elementType)
		{
		if (isHasEnoughBits(elementType))
			{
			params.elementType = elementType;
			if (!isCompatibleBitSize(elementType))
				fixBitSize();
			}
		}

	private static void fixBitSize()
		{
		params.elementBitSize = InterpretationBitSize.getContainingInRange(params.elementType.getBitSizeRange())[0];
		}

	private static boolean isCompatibleBitSize(InterpretationType type)
		{
		return type.getBitSizeRange().contains(params.elementBitSize.getBitSize());
		}

	public static InterpretationBitSize getElementBitSize()
		{
		return params.elementBitSize;
		}

	public static void setElementBitSize(InterpretationBitSize elementBitSize)
		{
		if (isHasEnoughBits(elementBitSize))
			{
			params.elementBitSize = elementBitSize;
			if (!isCompatibleInterpretation(elementBitSize))
				fixInterpretationType();
			}
		}

	private static void fixInterpretationType()
		{
		params.elementType = InterpretationType.getCompatibleByBitSize(params.elementBitSize.getBitSize())[0];
		}

	private static boolean isCompatibleInterpretation(InterpretationBitSize bs)
		{
		return params.elementType.getBitSizeRange().contains(bs.getBitSize());
		}

	private static boolean isHasEnoughBits(InterpretationBitSize bs)
		{
		return bitsLeftToAddressSpaceBound() >= bs.getBitSize();
		}

	private static boolean isHasEnoughBits(InterpretationType type)
		{
		return bitsLeftToAddressSpaceBound() >= type.getBitSizeRange().getLower();
		}

	public static int getStartingAddress()
		{
		return params.startingAddress;
		}

	public static void setStartingAddress(int startingAddress)
		{
		if (startingAddress < Modbus.MIN_START_ADDRESS || startingAddress > Modbus.MAX_START_ADDRESS)
			return;
		params.startingAddress = startingAddress;
		if (	! isHasEnoughBits(params.elementBitSize)	)
			params.elementBitSize = InterpretationBitSize.getContainingInRange(new Range<Integer>(1, bitsLeftToAddressSpaceBound()))[0];
		if ( ! isCompatibleInterpretation(params.elementBitSize))
			fixInterpretationType();
		}

	private static int bitsLeftToAddressSpaceBound()
		{
		return (Modbus.MAX_START_ADDRESS - params.startingAddress + 1) * params.mdoArea.minBitsPerOperation();
		}

	public static boolean getElementsReversed()
		{
		return params.elementsReversed;
		}

	public static void setElementsReversed(boolean elementsReversed)
		{
		params.elementsReversed = elementsReversed;
		}

	public static boolean getRegistersSwapped()
		{
		return params.registersSwapped;
		}

	public static void setRegistersSwapped(boolean registersSwapped)
		{
		params.registersSwapped = registersSwapped;
		}

	}
