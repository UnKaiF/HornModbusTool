package com.basisdas.hornModbusTool.datamodels;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class MDODataContainer
	{
	//В библиотеке jLibModbus используется только младшие 16 бит в каждом int (32 bit) из массивов-резултатов транзакций
	private final static int BITS_USED_IN_INT = 16;
	private transient boolean[] container;

	public void setFromJlibModbusArray(@NotNull int... jint)
		{
		container = new boolean[jint.length * BITS_USED_IN_INT];
		int containerIndex = 0;
		for (int element: jint)
			{
			container[containerIndex++] = ((element & 1) != 0);
			container[containerIndex++] = ((element & 2) != 0);
			container[containerIndex++] = ((element & 4) != 0);
			container[containerIndex++] = ((element & 8) != 0);
			container[containerIndex++] = ((element & 16) != 0);
			container[containerIndex++] = ((element & 32) != 0);
			container[containerIndex++] = ((element & 64) != 0);
			container[containerIndex++] = ((element & 128) != 0);
			container[containerIndex++] = ((element & 256) != 0);
			container[containerIndex++] = ((element & 512) != 0);
			container[containerIndex++] = ((element & 1024) != 0);
			container[containerIndex++] = ((element & 2048) != 0);
			container[containerIndex++] = ((element & 4096) != 0);
			container[containerIndex++] = ((element & 8192) != 0);
			container[containerIndex++] = ((element & 16384) != 0);
			container[containerIndex++] = ((element & 32768) != 0);
			}

		}

	public void setFromJlibModbusArray(@NotNull boolean... jbool)
		{
		container = jbool;
		}

	public int[] getJlibModbusIntArray()
		{
		checkContainerEmpty();
		checkContainerAliquot(BITS_USED_IN_INT);
		int shortSize = container.length / BITS_USED_IN_INT;
		int[] array = new int[shortSize];
		int containerIndex = 0;
		for (int arrindex = 0; arrindex < shortSize; arrindex++)
			{
			int temp = 0;
			temp |= container[containerIndex++] ? 1 : 0;
			temp |= container[containerIndex++] ? 2 : 0;
			temp |= container[containerIndex++] ? 4 : 0;
			temp |= container[containerIndex++] ? 8 : 0;
			temp |= container[containerIndex++] ? 16 : 0;
			temp |= container[containerIndex++] ? 32 : 0;
			temp |= container[containerIndex++] ? 64 : 0;
			temp |= container[containerIndex++] ? 128 : 0;
			temp |= container[containerIndex++] ? 256 : 0;
			temp |= container[containerIndex++] ? 512 : 0;
			temp |= container[containerIndex++] ? 1024 : 0;
			temp |= container[containerIndex++] ? 2048 : 0;
			temp |= container[containerIndex++] ? 4096 : 0;
			temp |= container[containerIndex++] ? 8192 : 0;
			temp |= container[containerIndex++] ? 16384 : 0;
			temp |= container[containerIndex++] ? 32768 : 0;
			array[arrindex] = temp;
			}
		return array;
		}

	public boolean[] getJlibModbusBoolArray()
		{
		checkContainerEmpty();
		return container;
		}

	public void setFromBasicElements(int elementBitSize, @NotNull long... elements )
		{
		container = new boolean[elementBitSize * elements.length];
		int containerIndex = 0;
		for (long element: elements)
			{
			long mask = 1L;
			for (int bitcount = 0; bitcount < elementBitSize; bitcount++)
				{
				container[containerIndex++] = (((element & mask) != 0L) ? true : false);
				mask <<= 1;
				}
			}
		}

	public long[] getBasicElements(int elementBitSize)
		{
		checkContainerEmpty();
		checkContainerAliquot(elementBitSize);
		int size = container.length / elementBitSize;
		long[] elements = new long[size];
		int containerIndex = 0;
		for (int elementIndex = 0; elementIndex < size; elementIndex++)
			{
			long mask = 1L;
			for (int bitcount = 0; bitcount < elementBitSize; bitcount++)
				{
				elements[elementIndex] |= container[containerIndex++] ? mask : 0L;
				mask <<= 1;
				}
			}

		return elements;
		}

	public void reverseBasicElements(int elementBitSize)
		{
		long[] elements = this.getBasicElements(elementBitSize);
		long temp;
		for (int head = elements.length - 1, tail = 0 ; head > tail; head--, tail++)
			{
			temp = elements[tail];
			elements[tail] = elements[head];
			elements[head] = temp;
			}
		this.setFromBasicElements(elementBitSize, elements);
		}

	public void swapRegisters(int elementBitSize)
		{
		if (elementBitSize <= 16)
			return;
		long[] elements = this.getBasicElements(elementBitSize);
		for (int elementIndex = 0; elementIndex < elements.length; elementIndex++)
			{
			long l0, l1, l2, l3;
			long element = elements[elementIndex];
			l0 =  (element & 0xFFFFL) << 16;
			l1 =  (element & (0xFFFFL << 16)) >>> 16;
			l2 =  (element & (0xFFFFL << 32)) << 16;
			l3 =  ((element & (0xFFFFL << 48)) >>> 16);
			elements[elementIndex] = l0 | l1 | l2 | l3;
			}
		this.setFromBasicElements(elementBitSize, elements);
		}

	private void checkContainerAliquot(int elementBitSize)
		{
		if (container.length % elementBitSize != 0)
			throw new NoSuchElementException();
		}

	private void checkContainerEmpty()
		{
		if (isEmpty())
			throw new NoSuchElementException();
		}

	public boolean isEmpty()
		{
		return (container == null || container.length == 0);
		}

	public void clear()
		{
		container = null;
		}

	public MDODataContainer() {};

	//Deep copy
	public MDODataContainer(MDODataContainer another)
		{
		if (another == null || another.isEmpty())
			this.container = null;
		else
			this.container = another.container.clone();
		}

	}
