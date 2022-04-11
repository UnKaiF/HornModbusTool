package com.basisdas.hornModbusTool.viewmodels;

import android.content.Context;


import com.basisdas.hornModbusTool.datamodels.Enums.MBProtocolType;
import com.basisdas.hornModbusTool.datamodels.SerialCommunicationLine;
import com.basisdas.hornModbusTool.datamodels.TransactionObject;
import com.basisdas.hornModbusTool.misc.EntityState;
import com.basisdas.hornModbusTool.misc.EntitySubState;
import com.basisdas.jlibmodbusandroid.serial.SerialParameters;
import com.basisdas.jlibmodbusandroid.serial.util.AndroidUSBSerialPortResolver;

import java.util.ArrayList;
import java.util.List;

public class SerialCommLineViewModel extends Deflatable implements ITransactionFlowNode
	{

	private final ArrayList<SlaveDeviceViewModel> slaveDeviceViewModels;


	private final SerialCommunicationLine serialCommunicationLine;

	public SerialCommLineViewModel()
		{
		serialCommunicationLine = SerialCommunicationLine.getInstance();
		slaveDeviceViewModels = new ArrayList<>();
		}

	public String getCommDevicePath()
		{
		String fullPath = serialCommunicationLine.getSerialParameters().getDevice();
		if (fullPath == null) return "";
		return fullPath;
		}

	public void setCommDevicePath(String commDevicePath)
		{
		SerialParameters s = serialCommunicationLine.getSerialParameters();
		s.setDevice(commDevicePath);
		serialCommunicationLine.setSerialParameters(s);
		}

	public String getCommDeviceParameters()
		{
		return serialCommunicationLine.getSerialParameters().getParametersString() +
								 "\r\n" +
				serialCommunicationLine.getProtocolType().name();
		}

	public void setCommDeviceParameters(SerialParameters sp, MBProtocolType protocol)
		{
		sp.setDevice(serialCommunicationLine.getSerialParameters().getDevice());
		serialCommunicationLine.setSerialParameters(sp);
		serialCommunicationLine.setProtocolType(protocol);
		}

	public void renewCommDevice(Context context)
		{
		List<String> devices = AndroidUSBSerialPortResolver.getAvailiblePortIdentifiers(context);
		if (devices.isEmpty())
			{
			setCommDevicePath("Нет подходящего интерфейса");
			setState(EntityState.INACTIVE);
			}
		else
			{
			setCommDevicePath(devices.get(0));
			//TODO: message to communication thread to establish parameters
			setEntitySubState(EntitySubState.UNKNOWN);
			setState(EntityState.ACTIVE);
			}
		}

	@Override
	public void ariseTransaction(TransactionObject transactionObject)
		{

		}

	@Override
	public boolean plantTransaction(TransactionObject transactionObject)
		{
		return false;
		}

	@Override
	public void deflateChilds()
		{
		for (SlaveDeviceViewModel slaveDeviceVm: slaveDeviceViewModels)
			{
			slaveDeviceVm.deflateChilds();
			}
		}


	}
