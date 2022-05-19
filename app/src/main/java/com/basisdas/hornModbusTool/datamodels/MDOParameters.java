package com.basisdas.hornModbusTool.datamodels;

import androidx.annotation.Nullable;

import com.basisdas.hornModbusTool.datamodels.Enums.InterpretationBitSize;
import com.basisdas.hornModbusTool.datamodels.Enums.InterpretationType;
import com.basisdas.hornModbusTool.datamodels.Enums.MDOArea;
import org.jetbrains.annotations.NotNull;

public class MDOParameters
	{
	public int startingAddress;
	public MDOArea mdoArea;
	public InterpretationBitSize elementBitSize;
	public InterpretationType elementType;
	public boolean elementsReversed;
	public boolean registersSwapped;

	public MDOParameters() {}


	//@Deep copy
	public MDOParameters(@NotNull MDOParameters p)
		{
		this.startingAddress = p.startingAddress;
		this.mdoArea = p.mdoArea;
		this.elementBitSize = p.elementBitSize;
		this.elementType = p.elementType;
		this.elementsReversed = p.elementsReversed;
		this.registersSwapped = p.registersSwapped;
		}

	@Override
	public boolean equals(@Nullable Object obj)
		{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass())
			return false;
		MDOParameters another = (MDOParameters) obj;
		return  ((this.startingAddress == another.startingAddress)
				&& (this.mdoArea == another.mdoArea)
				&& (this.elementBitSize == another.elementBitSize)
				&& (this.elementsReversed == another.elementsReversed)
				&& (this.registersSwapped == another.registersSwapped));
		}

	}
