package com.basisdas.hornModbusTool.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.viewmodels.SerialCommLineViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
	{

	@BindView(R.id.viewPager) ViewPager2 viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		viewPager.setAdapter( new ViewPagerFragmentStateAdapter(this.getSupportFragmentManager(), this.getLifecycle()));
		SerialCommLineViewModel viewModel = new ViewModelProvider(this).get(SerialCommLineViewModel.class);
		}


/*
		MBSerialSlaveDevice device = new MBSerialSlaveDevice();
		device.name = "Device";
		device.slaveID = 0x02;

		MDOParameters params = MDOParamConstructor.getMDOParameters();

		device.addMDO(new ModbusDataObject(params,  "MDO 1"));

		MDOParamConstructor.setObjectKind(MDOKind.Coil_singleWrite);
		MDOParamConstructor.setElementType(InterpretationType.Bool);
		MDOParamConstructor.setStartingAddress(0xAAAA);

		device.addMDO(new ModbusDataObject(MDOParamConstructor.getMDOParameters(), new MDODataContainer(), "MDO 2"));


		Gson gson = new Gson();
		String str = gson.toJson(device);

		appendMemoLine(str);

		MBSerialSlaveDevice device1 =  gson.fromJson(str, MBSerialSlaveDevice.class);

		appendMemoLine("device1 name: " + device1.name);
		appendMemoLine("devive1 slave id" + device1.slaveID);




		//fixme: usbfs: при каждом отключении и подключении устройства увеличивает номер устройства
		List<String> ports = AndroidUSBSerialPortResolver.getAvailiblePortIdentifiers(getApplicationContext());
		if (!ports.isEmpty())
			{
			//appendMemoLine("Found usb serial ports:");
			//ports.forEach(this::appendMemoLine);
			}

		sp = new SerialParameters("/dev/bus/usb/002/002/0", //port full path
								  SerialPort.BaudRate.BAUD_RATE_19200,
								  8, //Data bits
								  1, //Stop bits
								  SerialPort.Parity.NONE );
		//comm = new MBSerialCommunicationDevice(getApplicationContext(), sp, MBProtocolType.RTU);

		}



	@OnClick(R.id.button_do_job)
	public void dojob(View view)
		{


			UsbDevice device = AndroidUSBSerialPortResolver.getUsbDevice(sp.getDevice(), getApplicationContext());
			UsbManager manager = (UsbManager) getApplicationContext().getSystemService(Context.USB_SERVICE);
			//TODO handle usb device disconnection
			if (device == null)
				{
				appendMemoLine("Device disconnected !");
				return;
				}
			if (manager.hasPermission(device) )
				{
				try
					{
					ModbusMaster master = ModbusMasterFactory.createModbusMasterRTU(sp);
					//валиться на connect() при отсутствии устройства
					master.connect();
					try
						{
						appendMemoLine("Trying to read ETS.TILT HWversion...");
						int[] HWversionRegisterValues = master.readHoldingRegisters(0x02, 0x10, 2);
						int HWvervion = HWversionRegisterValues[0]<<16 | HWversionRegisterValues[1];
						appendMemoLine("HWversion = " + HWvervion);
						appendMemoLine("Trying to read ETS.TILT temperature sensor...");
						int[] temperature = master.readHoldingRegisters(0x02, 0x23, 1);
						appendMemoLine("Temperature = " + temperature[0]);
						//Useless: appendMemoLine("Transaction Id: " + master.getTransactionId());
						//master.readDiscreteInputs()
						}
					catch (RuntimeException e)
						{
						throw e;
						}
					finally
						{
						try
							{
							master.disconnect();
							}
						catch (ModbusIOException e1)
							{
							e1.printStackTrace();
							}
						}
					}
				catch (Exception e)
					{//Timeout handled here.. now
					appendMemoLine("Exception " + e.getMessage());
					}
				}
			else
				{
				appendMemoLine("No such permissions to open port: " + sp.getDevice());
				PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
				manager.requestPermission(device, permissionIntent);
				}

 */




	}