package com.basisdas.hornModbusTool.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.datamodels.Enums.InterpretationType;
import com.basisdas.hornModbusTool.datamodels.SlaveDevice;
import com.basisdas.hornModbusTool.datamodels.SerialCommunicationLine;
import com.basisdas.hornModbusTool.datamodels.Enums.MDOArea;
import com.basisdas.hornModbusTool.datamodels.utils.MDOParamConstructor;
import com.basisdas.hornModbusTool.datamodels.MDOParameters;
import com.basisdas.hornModbusTool.datamodels.ModbusDataObject;
import com.basisdas.hornModbusTool.misc.InflateState;
import com.basisdas.hornModbusTool.viewmodels.SerialCommLineViewModel;
import com.basisdas.hornModbusTool.views.custom.ExpandButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SerialCommLineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SerialCommLineFragment extends Fragment
	{

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	@BindView(R.id.layout_serial_int_expanding_area)  LinearLayout serialInterfaceToolBoxLayout;
	@BindView(R.id.button_expand_serial_int)  ExpandButton expandSerialIntarfaceButton;
	@BindView(R.id.recycler_view_devices_container)  RecyclerView rv_devices;
	@BindView(R.id.toolbar_main) androidx.appcompat.widget.Toolbar toolbar;
	@BindView(R.id.text_view_serial_path) TextView tv_serialCommDevice;
	@BindView(R.id.text_view_serial_parameters_block) TextView tv_serialParametersBlock;

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
		}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
		{
		View view = inflater.inflate(R.layout.fragment_serial_comm_line, container, false);
		ButterKnife.bind(this, view);
		//SerialCommLineViewModel viewModel = new ViewModelProvider(getActivity()).get(SerialCommLineViewModel.class);
		Some();
		return view;
		}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
		{
		super.onViewCreated(view, savedInstanceState);
		toolbar.inflateMenu(R.menu.menu_main);
		}

	private void Some()
		{
		serialInterfaceToolBoxLayout.setVisibility(
				expandSerialIntarfaceButton.getState() == InflateState.INFLATED ? View.VISIBLE : View.GONE);

		expandSerialIntarfaceButton.setClickListener(state ->
															 serialInterfaceToolBoxLayout.setVisibility((state == InflateState.DEFLATED) ? View.GONE : View.VISIBLE));

		SerialCommunicationLine pool = SerialCommunicationLine.getInstance();

		SlaveDevice device = new SlaveDevice(0x02, "Датчик угла наклона");

		MDOParameters params = MDOParamConstructor.getMDOParameters();
		ModbusDataObject mdo = new ModbusDataObject(params, "Тангаж");
		mdo.setValue("21");
		device.addMDO(mdo);

		MDOParamConstructor.setMDOArea(MDOArea.Coil_singleWrite);
		MDOParamConstructor.setElementType(InterpretationType.Float);
		MDOParamConstructor.setStartingAddress(0xAAAA);

		mdo = new ModbusDataObject(MDOParamConstructor.getMDOParameters(),  "Крен");
		mdo.setValue("10.3");
		device.addMDO(mdo);

		pool.addDevice(device);

		device = new SlaveDevice(99, "Адаптер ETS.USA");

		MDOParamConstructor.setElementType(InterpretationType.Float);
		MDOParamConstructor.setMDOArea(MDOArea.HoldingRegister_singleWrite);
		MDOParamConstructor.setStartingAddress(3);
		mdo = new ModbusDataObject(MDOParamConstructor.getMDOParameters(),  "Контроль тока");
		mdo.setValue("57.47");
		device.addMDO(mdo);

		pool.addDevice(device);




		// Initialise the Linear layout manager
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

		// Pass the arguments
		// to the parentItemAdapter.
		// These arguments are passed
		// using a method ParentItemList()
		MBSlaveDevicePoolAdapter mbSlaveDevicePoolAdapter = new MBSlaveDevicePoolAdapter(pool);

		// Set the layout manager
		// and adapter for items
		// of the parent recyclerview
		rv_devices.setAdapter(mbSlaveDevicePoolAdapter);
		rv_devices.setLayoutManager(layoutManager);



		tv_serialCommDevice.setText(pool.getSerialParameters().getDevice());
		tv_serialParametersBlock.setText(pool.getSerialParameters().getParametersString());

		}

	}