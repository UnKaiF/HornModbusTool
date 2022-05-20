package com.basisdas.hornModbusTool.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basisdas.hornModbusTool.datamodels.SlaveDevice;
import com.basisdas.hornModbusTool.filedialogs.FileDialog;
import com.basisdas.hornModbusTool.filedialogs.OpenFileDialog;
import com.basisdas.hornModbusTool.filedialogs.SaveFileDialog;
import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.datamodels.ModbusDataObject;
import com.basisdas.hornModbusTool.misc.EntityState;
import com.basisdas.hornModbusTool.misc.InflateState;
import com.basisdas.hornModbusTool.viewmodels.SerialCommLineViewModel;
import com.basisdas.hornModbusTool.viewmodels.SlaveDeviceViewModel;
import com.basisdas.hornModbusTool.views.custom.CircleButton;
import com.basisdas.hornModbusTool.views.custom.EntityStateButton;
import com.basisdas.hornModbusTool.views.custom.ExpandButton;
import com.basisdas.jlibmodbusandroid.serial.SerialParameters;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SerialCommLineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SerialCommLineFragment extends Fragment implements
		Toolbar.OnMenuItemClickListener,
		TimesDialog.OnTimesUpdatedListener,
		FileDialog.OnFileSelectedListener,
		SerialParametersDialog.SerialParametersUpdateListener,
		MBDeviceParametersDialog.MBDeviceParametersCreatedListener,
		MDOValueUpdateDialog.MDOValueUpdateListener,
		MDOConstructorDialog.MDOConstructorDoneListener
	{

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private int mPeriod = 1000;
	private int mTimeout = 5000;

	private ViewModelProvider viewModelProvider;
	private SerialCommLineViewModel serialCommLineViewModel;
	SerialCommLineAdapter serialCommLineAdapter;

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	@BindView(R.id.layout_serial_int_expanding_area)  LinearLayout serialInterfaceToolBoxLayout;
	@BindView(R.id.button_expand_serial_int)  ExpandButton expandSerialIntarfaceButton;
	@BindView(R.id.recycler_view_devices_container)  RecyclerView rv_devices;
	@BindView(R.id.toolbar_main) androidx.appcompat.widget.Toolbar toolbar;
	@BindView(R.id.text_view_serial_path) TextView tv_serialCommDevice;
	@BindView(R.id.text_view_serial_parameters_block) TextView tv_serialParametersBlock;
	@BindView(R.id.button_renew_serial_interface) CircleButton btn_renewCommInterface;
	@BindView(R.id.button_serial_int_state)	EntityStateButton btn_SerialInterfaceState;
	@BindView(R.id.button_add_device) CircleButton btn_AddSlaveDevice;

	public SerialCommLineFragment()
		{
		// Required empty public constructor
		}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment MainFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static SerialCommLineFragment newInstance(String param1, String param2)
		{
		SerialCommLineFragment fragment = new SerialCommLineFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
		}

	@Override
	public void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
			{
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
			}
		setHasOptionsMenu(true);
		viewModelProvider = new ViewModelProvider(requireActivity());
		}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
		{
		View view = inflater.inflate(R.layout.fragment_serial_comm_line, container, false);
		ButterKnife.bind(this, view);
		serialCommLineViewModel = viewModelProvider.get(SerialCommLineViewModel.class);
		Setup();
		return view;
		}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
		{
		inflater.inflate(R.menu.menu_main, menu);
		super.onCreateOptionsMenu(menu, inflater);
		}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
		{
		super.onViewCreated(view, savedInstanceState);
		toolbar.inflateMenu(R.menu.menu_main);
		toolbar.setOnMenuItemClickListener(this);

		}

	@Override
	public boolean onMenuItemClick(MenuItem item)
		{
		Bundle args = new Bundle();
		switch (item.getItemId())
			{
			case R.id.menu_settings:
				TimesDialog dialog = new TimesDialog();
				args.putInt(TimesDialog.CURRENT_PERIOD, mPeriod);
				args.putInt(TimesDialog.CURRENT_TIMEOUT, mTimeout);
				dialog.setArguments(args);
				dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_HornModbusTool);
				dialog.show(getChildFragmentManager(), TimesDialog.class.getName());
				break;
			case R.id.menu_auto_query:
/*
				args.putString("TITLE","Параметры коммуникации");
				SerialParametersDialog spDialog = new SerialParametersDialog();
				spDialog.setArguments(args);
				spDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_HornModbusTool);
				spDialog.show(getChildFragmentManager(), SerialParametersDialog.class.getName());
*/
				args.putString("TITLE", "Запись ОДМ !");
				MDOValueUpdateDialog mbDialog = new MDOValueUpdateDialog();
				mbDialog.setArguments(args);
				mbDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_HornModbusTool);
				mbDialog.show(getChildFragmentManager(), MDOValueUpdateDialog.class.getName());

				break;
			case R.id.menu_deflate_all:
				/*
				args.putString("TITLE", "Новый ОДМ");
				MDOConstructorDialog mdoDialog = new MDOConstructorDialog();
				mdoDialog.setArguments(args);
				mdoDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_HornModbusTool);
				mdoDialog.show(getChildFragmentManager(), MDOConstructorDialog.class.getName());
				 */
				serialCommLineViewModel.deflateAll();
				expandSerialIntarfaceButton.setState(InflateState.DEFLATED);
				serialInterfaceToolBoxLayout.setVisibility(View.GONE);
				serialCommLineAdapter.notifyDataSetChanged();
				break;
			case R.id.menu_load_map:
				showFileDialog(new OpenFileDialog(), OpenFileDialog.class.getName());
				break;
			case R.id.menu_save_map:
				showFileDialog(new SaveFileDialog(), OpenFileDialog.class.getName());
				break;
			}
		return false;
		}

	private void showFileDialog(FileDialog dialog, String tag)
		{
		Bundle args = new Bundle();
		args.putString(FileDialog.EXTENSION, "mdojson");
		dialog.setArguments(args);
		dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_HornModbusTool);
		dialog.show(getChildFragmentManager(), tag);
		}

	@Override
	public void onTimesUpdated(int period, int timeout)
		{
		mPeriod = period;
		mTimeout = timeout;
		}


	@Override
	public void onFileSelected(FileDialog dialog, File file)
		{
		if (dialog.getClass().getName().equalsIgnoreCase(OpenFileDialog.class.getName()))
			{
			}
		else
			{
			}
		}

	@Override
	public void onSerialParametersUpdated(SerialParameters sp)
		{

		}

	@Override
	public void onMBDeviceParametersCreated(String deviceName, int deviceId, int index)
		{
		if (index < 0)
			{
			serialCommLineViewModel.slaveDeviceViewModels.add(0, new SlaveDeviceViewModel(serialCommLineViewModel, new SlaveDevice(deviceId, deviceName)));
			serialCommLineAdapter.notifyDataSetChanged();
			}
		else
			{
			serialCommLineViewModel.slaveDeviceViewModels.get(index).setDeviceName(deviceName);
			serialCommLineViewModel.slaveDeviceViewModels.get(index).setSlaveID(deviceId);
			serialCommLineAdapter.notifyItemChanged(index);
			}
		}


	@Override
	public void onMDOValueUpdated(ModbusDataObject mdo, String value)
		{

		}

	@Override
	public void onMDOConstructed(ModbusDataObject mdo)
		{

		}

	private void Setup()
		{
		InflateState inflateState = serialCommLineViewModel.getInflateState();
		expandSerialIntarfaceButton.setState(inflateState);
		serialInterfaceToolBoxLayout.setVisibility(
				expandSerialIntarfaceButton.getState() == InflateState.INFLATED ? View.VISIBLE : View.GONE);

		expandSerialIntarfaceButton.setClickListener(state ->
				 {
				 serialInterfaceToolBoxLayout.setVisibility((state == InflateState.DEFLATED) ? View.GONE : View.VISIBLE);
				 serialCommLineViewModel.setInflateState(state);
				 });

		//Кнопка "Найти другой usb-serial интерфейс"
		btn_renewCommInterface.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
				{
				serialCommLineViewModel.renewCommDevice(getContext().getApplicationContext());
				tv_serialCommDevice.setText(serialCommLineViewModel.getCommDevicePath());
				tv_serialParametersBlock.setText(serialCommLineViewModel.getCommDeviceParametersString());
				btn_SerialInterfaceState.setState(serialCommLineViewModel.getState());
				btn_SerialInterfaceState.setSubState(serialCommLineViewModel.getEntitySubState());
				}
		});

		//Кнопка активности серийного интерфейса
		btn_SerialInterfaceState.setState(serialCommLineViewModel.getState());
		btn_SerialInterfaceState.setSubState(serialCommLineViewModel.getEntitySubState());
		btn_SerialInterfaceState.setClickListener(new EntityStateButton.ClickListener() {
			@Override
			public void onStateChanged(EntityState state)
				{
				serialCommLineViewModel.setState(state);
				}
		});

		//Кнопка "Добавить Slave Device"
		btn_AddSlaveDevice.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view)
				{
				Bundle args = new Bundle();
				args.putString("TITLE", "Новое устройство");
				MBDeviceParametersDialog mbDialog = new MBDeviceParametersDialog();
				mbDialog.setArguments(args);
				mbDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_HornModbusTool);
				mbDialog.show(getChildFragmentManager(), MBDeviceParametersDialog.class.getName());
				return true;
				}
		});

		// Initialise the Linear layout manager
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

		// Pass the arguments
		// to the parentItemAdapter.
		// These arguments are passed
		// using a method ParentItemList()
		serialCommLineAdapter = new SerialCommLineAdapter(serialCommLineViewModel, getChildFragmentManager());

		// Set the layout manager
		// and adapter for items
		// of the parent recyclerview
		rv_devices.setAdapter(serialCommLineAdapter);
		rv_devices.setLayoutManager(layoutManager);


		tv_serialCommDevice.setText(serialCommLineViewModel.getCommDevicePath());
		tv_serialParametersBlock.setText(serialCommLineViewModel.getCommDeviceParametersString());


		}

	}