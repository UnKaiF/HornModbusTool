package com.basisdas.hornModbusTool.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.os.Bundle;

import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.datamodels.Enums.InterpretationType;
import com.basisdas.hornModbusTool.datamodels.Enums.MDOArea;
import com.basisdas.hornModbusTool.datamodels.MDOParameters;
import com.basisdas.hornModbusTool.datamodels.ModbusDataObject;
import com.basisdas.hornModbusTool.datamodels.SlaveDevice;
import com.basisdas.hornModbusTool.datamodels.utils.MDOParamConstructor;
import com.basisdas.hornModbusTool.viewmodels.JournalViewModel;
import com.basisdas.hornModbusTool.viewmodels.ModbusDataObjectViewModel;
import com.basisdas.hornModbusTool.viewmodels.SerialCommLineViewModel;
import com.basisdas.hornModbusTool.viewmodels.SlaveDeviceViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
	{

	@BindView(R.id.viewPager)
	ViewPager2 viewPager;

	ViewModelProvider viewModelProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		//Динамический запрос привелегий на доcтуп к носителю
		this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

		viewPager.setAdapter(new ViewPagerFragmentStateAdapter(this.getSupportFragmentManager(), this.getLifecycle()));
		viewModelProvider = new ViewModelProvider(this);
		initViewModels(savedInstanceState);
		}

	private void initViewModels(Bundle savedInstanceState)
		{
		if (savedInstanceState == null)
			{
			SerialCommLineViewModel serialCommLineViewModel = viewModelProvider.get(SerialCommLineViewModel.class);
			JournalViewModel journalViewModel = viewModelProvider.get(JournalViewModel.class);
			journalViewModel.appendLine("Из активити");

			serialCommLineViewModel.renewCommDevice(getApplicationContext());

			serialCommLineViewModel.slaveDeviceViewModels.add(new SlaveDeviceViewModel(serialCommLineViewModel, new SlaveDevice(0x03, "Некий Датчик")));
			MDOParameters params = MDOParamConstructor.getMDOParameters();
			ModbusDataObject mdo = new ModbusDataObject(params, "Тангаж");
			mdo.setValue("21");
			serialCommLineViewModel.slaveDeviceViewModels.get(0).modbusDataObjectViewModels.add(new ModbusDataObjectViewModel(serialCommLineViewModel.slaveDeviceViewModels.get(0), mdo));
			MDOParamConstructor.setMDOArea(MDOArea.Coil_singleWrite);
			MDOParamConstructor.setElementType(InterpretationType.Float);
			MDOParamConstructor.setStartingAddress(0xAAAA);
			mdo = new ModbusDataObject(MDOParamConstructor.getMDOParameters(),  "Крен");
			mdo.setValue("10.3");
			serialCommLineViewModel.slaveDeviceViewModels.get(0).modbusDataObjectViewModels.add(new ModbusDataObjectViewModel(serialCommLineViewModel.slaveDeviceViewModels.get(0),  mdo));

			serialCommLineViewModel.slaveDeviceViewModels.add(new SlaveDeviceViewModel(serialCommLineViewModel, new SlaveDevice(99, "Адаптер ETS.USA")));
			MDOParamConstructor.setElementType(InterpretationType.Decimal);
			MDOParamConstructor.setMDOArea(MDOArea.HoldingRegister_singleWrite);
			MDOParamConstructor.setStartingAddress(3);
			mdo = new ModbusDataObject(MDOParamConstructor.getMDOParameters(),  "Контроль тока");
			mdo.setValue("57");
			serialCommLineViewModel.slaveDeviceViewModels.get(1).modbusDataObjectViewModels.add(new ModbusDataObjectViewModel(serialCommLineViewModel.slaveDeviceViewModels.get(1), mdo));

			}
		}

/*

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