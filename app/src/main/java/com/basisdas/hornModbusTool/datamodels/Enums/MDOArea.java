package com.basisdas.hornModbusTool.datamodels.Enums;

public enum MDOArea
	{
	Coil_singleWrite
		{
		@Override
		public int maxBitsPerWriteOperation() {	return 1; }
		@Override
		public int maxBitsPerReadOperation() { return 2000;	}
		@Override
		public int minBitsPerOperation() {	return 1; }
		},

	Coil_multiWrite
		{
		@Override
		public int maxBitsPerWriteOperation() {	return 1968; }
		@Override
		public int maxBitsPerReadOperation() { return 2000;	}
		@Override
		public int minBitsPerOperation() {	return 1; }
		},

	DiscreteInput
		{
		@Override
		public int maxBitsPerWriteOperation() {	return 0; }
		@Override
		public int maxBitsPerReadOperation() { return 2000;	}
		@Override
		public int minBitsPerOperation() {	return 1; }
		},

	InputRegister
		{
		@Override
		public int maxBitsPerWriteOperation() {	return 0; }
		@Override
		public int maxBitsPerReadOperation() { return 2000;	}
		@Override
		public int minBitsPerOperation() {	return 16; }
		},

	HoldingRegister_singleWrite
		{
		@Override
		public int maxBitsPerWriteOperation() {	return 16; }
		@Override
		public int maxBitsPerReadOperation() { return 2000;	}
		@Override
		public int minBitsPerOperation() {	return 16; }
		},

	HoldingRegister_multiWrite
		{
		@Override
		public int maxBitsPerWriteOperation() {	return 1968; }
		@Override
		public int maxBitsPerReadOperation() { return 2000;	}
		@Override
		public int minBitsPerOperation() {	return 16; }
		};

	public static String[] names()
		{
		String[] arr = new String[MDOArea.values().length];
		MDOArea[] mBDataObjectKinds = MDOArea.values();
		for (int i=0 ; i < arr.length; i++)
			{
			arr[i] = mBDataObjectKinds[i].toString();
			}
		return arr;
		}


	abstract public int maxBitsPerWriteOperation();
	abstract public int maxBitsPerReadOperation();
	abstract public int minBitsPerOperation();

	}
