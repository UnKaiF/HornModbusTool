package com.basisdas.hornModbusTool.views;


/*Note: all positions just in vewable tree lists */

public interface ISerialCommLineFragmentView
	{
	/*Toolbar clicks*/
	void clickAutoUpdate();
	void clickSettings();
	void clickDeflateAll();
	void clickSaveMBSlaveDeviceMap();
	void clickLoadMBSlaveDeviceMap();

	/*Serial interface related clicks*/
	void clickSerialCommLineActivate();
	void clickSerialCommLineChoosePort();
	void clickSerialCommLineDeflateInflate();
	void clickSerialCommLineSettings();
	void clickSerialCommLineAutoRenewPort();
	void clickSerialCommLineCreateSlaveDevice();

	/*Modbus slave device cell related clicks */
	void clickSlaveDeviceActivate(int position);
	void clickSlaveDeviceDeflateInflate(int position);
	void clickSlaveDeviceSettings(int position);
	void clickSlaveDeviceDelete(int position);
	void clickSlaveDeviceCreateMDO(int position);

	/*Modbus data object cell related clicks*/
	void clickMDOActivate(int slaveDevicePosition, int mdoPosition);
	void clickMDORead(int slaveDevicePosition, int mdoPosition);
	void clickMDOWrite(int slaveDevicePosition, int mdoPosition);
	void clickMDODeflateInflate(int slaveDevicePosition, int mdoPosition);
	void clickMDOSettings(int slaveDevicePosition, int mdoPosition);
	void clickMDODelete(int slaveDevicePosition, int mdoPosition);
	}
