package com.basisdas.hornModbusTool.datamodels;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MDODataContainerTest
	{

	MDODataContainer mdoDataContainer = new MDODataContainer();
	private static final int[] pattern = {0xA07F, 0xABC9, 0xB7B9, 0xF674,
										0xA828, 0xE2D7, 0x298F, 0x3B74,
										0xD076, 0x53AA, 0xE2F2, 0x9DF8,
										0x14C4, 0xAC2D, 0x9CA7, 0x0562};
	private static final long[] longPattern = {0xA07F, 0xABC9, 0xB7B9, 0xF674,
											0xA828, 0xE2D7, 0x298F, 0x3B74,
											0xD076, 0x53AA, 0xE2F2, 0x9DF8,
											0x14C4, 0xAC2D, 0x9CA7, 0x0562};

	private static final boolean[] boolPattern = {true, true, true, true, true, true, true, false,
												false, false, false, false, false, true, false, true,
												true, false, false, true, false, false, true, true,
												true, true, false, true, false, true, false, true,
												true, false, false, true, true, true, false, true,
												true, true, true, false, true, true, false, true,
												false, false, true, false, true, true, true, false,
												false, true, true, false, true, true, true, true,
												false, false, false, true, false, true, false, false,
												false, false, false, true, false, true, false, true,
												true, true, true, false, true, false, true, true,
												false, true, false, false, false, true, true, true,
												true, true, true, true, false, false, false, true,
												true, false, false, true, false, true, false, false,
												false, false, true, false, true, true, true, false,
												true, true, false, true, true, true, false, false,
												false, true, true, false, true, true, true, false,
												false, false, false, false, true, false, true, true,
												false, true, false, true, false, true, false, true,
												true, true, false, false, true, false, true, false,
												false, true, false, false, true, true, true, true,
												false, true, false, false, false, true, true, true,
												false, false, false, true, true, true, true, true,
												true, false, true, true, true, false, false, true,
												false, false, true, false, false, false, true, true,
												false, false, true, false, true, false, false, false,
												true, false, true, true, false, true, false, false,
												false, false, true, true, false, true, false, true,
												true, true, true, false, false, true, false, true,
												false, false, true, true, true, false, false, true,
												false, true, false, false, false, true, true, false,
												true, false, true, false, false, false, false, false};

	@Before
	public void setup()
		{
		mdoDataContainer.clear();
		}

	@Test
	public void getJlibModbusIntArray()
		{
		mdoDataContainer.setFromJlibModbusArray(pattern);
		assertArrayEquals(pattern, mdoDataContainer.getJlibModbusIntArray());
		}

	@Test
	public void getJlibModbusBoolArray()
		{
		mdoDataContainer.setFromJlibModbusArray(pattern);
		assertArrayEquals(boolPattern, mdoDataContainer.getJlibModbusBoolArray());
		}

	@Test
	public void setFromBasicElements()
		{
		mdoDataContainer.setFromBasicElements(16, longPattern);
		assertArrayEquals(pattern, mdoDataContainer.getJlibModbusIntArray());
		}

	@Test
	public void getBasicElements()
		{
		mdoDataContainer.setFromJlibModbusArray(pattern);
		assertArrayEquals(longPattern, mdoDataContainer.getBasicElements(16));
		}

	@Test
	public void reverseBasicElements()
		{
		mdoDataContainer.setFromJlibModbusArray(boolPattern);
		mdoDataContainer.reverseBasicElements(1);
		boolean[] result = mdoDataContainer.getJlibModbusBoolArray();
		for (int j = 0, i = result.length - 1; i > 0 ; j++, i--)
			assertEquals(boolPattern[i], result[j]);
		}

	@Test
	public void swapRegisters()
		{
		mdoDataContainer.setFromJlibModbusArray(pattern);
		mdoDataContainer.swapRegisters(32);
		int[] result = mdoDataContainer.getJlibModbusIntArray();
		for (int k = 1, i = 0; i < result.length; i++, k = -k)
			assertEquals(pattern[i], result[i + k]);
		}

	@Test
	public void isEmpty()
		{
		assertTrue(mdoDataContainer.isEmpty());
		mdoDataContainer.setFromJlibModbusArray(boolPattern);
		assertFalse(mdoDataContainer.isEmpty());
		}

	}