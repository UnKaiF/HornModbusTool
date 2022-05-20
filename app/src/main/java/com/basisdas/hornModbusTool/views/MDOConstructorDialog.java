package com.basisdas.hornModbusTool.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.datamodels.Enums.InterpretationBitSize;
import com.basisdas.hornModbusTool.datamodels.Enums.InterpretationType;
import com.basisdas.hornModbusTool.datamodels.Enums.MDOArea;
import com.basisdas.hornModbusTool.datamodels.MDOParameters;
import com.basisdas.hornModbusTool.datamodels.ModbusDataObject;
import com.basisdas.hornModbusTool.datamodels.utils.JsonParser;
import com.basisdas.hornModbusTool.datamodels.utils.MDOParamConstructor;
import com.basisdas.jlibmodbusandroid.Modbus;
import com.basisdas.jlibmodbusandroid.serial.SerialPort;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MDOConstructorDialog extends DialogFragment implements Toolbar.OnMenuItemClickListener, AdapterView.OnItemSelectedListener

	{

	public static final String TITLE = "TITLE";
	public static final String DEVICE_INDEX = "device_index";
	public static final String MDO_INDEX = "mdo_index";
	public static final String MDO = "mdo";

	private String mTitle = "";
	private int mToolbarTitleColor;

	private static final Boolean b[] = {false, true};
	private boolean isInUpdateState = false;
	private boolean isStartingAddressError = false;

	protected Toolbar mToolbar;
	private TextInputEditText mMdoNameEditText, mMdoStartingAddressEditText;
	private TextInputLayout mMdoStartingAddressEditTextLayout;
	private Spinner mMdoAreaSpinner, mMdoInterpretationTypeSpinner, mMdoBitSizeSpinner, mMdoElementOrderSpinner, mMdoRegisterOrderSpinner;

	private ModbusDataObject mMDO;
	private int mDeviceIndex = -1;
	private int mMDOIndex = -1;

	protected int getLayoutResourceId()
		{
		return R.layout.dialog_mdo_constructor;
		}

	@Override
	public void onAttach(Activity activity)
		{

		super.onAttach(activity);
		mToolbarTitleColor = activity.getApplicationContext().getColor(R.color.white);
		}


	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState)
		{
		return inflater.inflate(getLayoutResourceId(), container);
		}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
		{

		super.onViewCreated(view, savedInstanceState);

		Bundle arguments = getArguments();
		String json = null;

		if (savedInstanceState != null)
			{
			json = savedInstanceState.getString(MDO);
			mTitle = savedInstanceState.getString(TITLE);
			mDeviceIndex = savedInstanceState.getInt(DEVICE_INDEX, -1);
			mMDOIndex = savedInstanceState.getInt(MDO_INDEX, -1);
			}
		else
			{
			if (arguments != null)
				{
				if (arguments.containsKey(MDO))
					json = arguments.getString(MDO);
				if (arguments.containsKey(TITLE))
					mTitle = getArguments().getString(TITLE);
				if (arguments.containsKey(DEVICE_INDEX))
					mDeviceIndex = getArguments().getInt(DEVICE_INDEX);
				if (arguments.containsKey(MDO_INDEX))
					mMDOIndex = getArguments().getInt(MDO_INDEX);
				}
			}

		if (json != null)
			{
			mMDO = JsonParser.getGsonParser().fromJson(json, ModbusDataObject.class);
			}
		else
			{
			mMDO = new ModbusDataObject(MDOParamConstructor.getMDOParameters(), "??МДО??");
			//mMDO.setValue("0.22");
			}

		mMdoNameEditText = (TextInputEditText) view.findViewById(R.id.dialog_mdo_name_edit);
		mMdoNameEditText.setText(mMDO.getName());
		mMdoStartingAddressEditText = (TextInputEditText) view.findViewById(R.id.dialog_mdo_starting_address_edit);
		mMdoStartingAddressEditText.setText(Integer.toString(mMDO.getParams().startingAddress));
		mMdoStartingAddressEditTextLayout = (TextInputLayout) view.findViewById(R.id.dialog_mdo_starting_address_text_layout);
		mMdoStartingAddressEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)	{}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)	{}
			@Override
			public void afterTextChanged(Editable editable)
				{
				int address = -1;
				String addressString = editable.toString();
				try
					{
					address = Integer.parseUnsignedInt(addressString);
					}
				catch (NumberFormatException e) {}
				isStartingAddressError = address < Modbus.MIN_START_ADDRESS || address > Modbus.MAX_START_ADDRESS;
				mMdoStartingAddressEditTextLayout.setError(isStartingAddressError ? "Неверное значение" : null);
				if (! isStartingAddressError)
					{
					MDOParamConstructor.setMDOParameters(mMDO.getParams());
					MDOParamConstructor.setStartingAddress(address);
					mMDO.setParams(MDOParamConstructor.getMDOParameters());
					updateAllSpinnersProgrammatically();
					}
				}
		});

		mMdoAreaSpinner = (Spinner) view.findViewById(R.id.mdo_area_spinner);
		mMdoInterpretationTypeSpinner = (Spinner) view.findViewById(R.id.mdo_interpretation_type_spinner);
		mMdoBitSizeSpinner = (Spinner) view.findViewById(R.id.mdo_bit_size_spinner);
		mMdoElementOrderSpinner = (Spinner) view.findViewById(R.id.mdo_element_order_spinner);
		mMdoRegisterOrderSpinner = (Spinner) view.findViewById(R.id.mdo_registers_order_spinner);

		setSpinnerAdapter(mMdoAreaSpinner, MDOArea.names());
		setSpinnerAdapter(mMdoInterpretationTypeSpinner, InterpretationType.names());
		setSpinnerAdapter(mMdoBitSizeSpinner, InterpretationBitSize.values());
		setSpinnerAdapter(mMdoElementOrderSpinner, b);
		setSpinnerAdapter(mMdoRegisterOrderSpinner, b);

		updateAllSpinnersProgrammatically();

		mToolbar = (Toolbar) view.findViewById(R.id.dialog_save_file_toolbar);
		mToolbar.inflateMenu(R.menu.toolbar_menu_apply);
		mToolbar.setOnMenuItemClickListener(this);
		mToolbar.setTitleTextColor(mToolbarTitleColor);
		mToolbar.setTitle(mTitle);

		mToolbar.setNavigationOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
					{
					dismiss();
					}
			});

		}

	private void updateAllSpinnersProgrammatically()
		{
		isInUpdateState = true;
		mMdoAreaSpinner.setOnItemSelectedListener(null);
		mMdoInterpretationTypeSpinner.setOnItemSelectedListener(null);
		mMdoBitSizeSpinner.setOnItemSelectedListener(null);
		mMdoElementOrderSpinner.setOnItemSelectedListener(null);
		mMdoRegisterOrderSpinner.setOnItemSelectedListener(null);


		MDOParameters p = mMDO.getParams();
		mMdoAreaSpinner.setSelection(p.mdoArea.ordinal(), false);
		mMdoInterpretationTypeSpinner.setSelection(p.elementType.ordinal(), false);
		mMdoBitSizeSpinner.setSelection(p.elementBitSize.ordinal(), false);
		mMdoElementOrderSpinner.setSelection(p.elementsReversed ? 1 : 0, false);
		mMdoRegisterOrderSpinner.setSelection(p.registersSwapped ? 1 : 0, false);

		mMdoAreaSpinner.setOnItemSelectedListener(this);
		mMdoInterpretationTypeSpinner.setOnItemSelectedListener(this);
		mMdoBitSizeSpinner.setOnItemSelectedListener(this);
		mMdoElementOrderSpinner.setOnItemSelectedListener(this);
		mMdoRegisterOrderSpinner.setOnItemSelectedListener(this);
		isInUpdateState = false;
		}

	private <T> void  setSpinnerAdapter(Spinner spinner, T[] array)
		{
		ArrayAdapter<T> adapter = new ArrayAdapter<T>(this.getContext(), R.layout.spinner_item, array);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
		{
		if (isInUpdateState) return;
		MDOParamConstructor.setMDOParameters(mMDO.getParams());
		switch (adapterView.getId())
			{
			case R.id.mdo_area_spinner:
				MDOParamConstructor.setMDOArea(MDOArea.values()[(int)l]);
				break;
			case R.id.mdo_interpretation_type_spinner:
				MDOParamConstructor.setElementType(InterpretationType.values()[(int)l]);
				break;
			case R.id.mdo_bit_size_spinner:
				MDOParamConstructor.setElementBitSize(InterpretationBitSize.values()[(int)l]);
				break;
			case R.id.mdo_registers_order_spinner:
				MDOParamConstructor.setRegistersSwapped(b[(int)l]);
				break;
			case R.id.mdo_element_order_spinner:
				MDOParamConstructor.setElementsReversed(b[(int)l]);
			}
		mMDO.setParams(MDOParamConstructor.getMDOParameters());
		updateAllSpinnersProgrammatically();
		}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {}

	@Override
	public void onSaveInstanceState(Bundle outState)
		{
		super.onSaveInstanceState(outState);
		String json = JsonParser.getGsonParser().toJson(mMDO);
		outState.putString(MDO, json);
		outState.putString(TITLE, mTitle);
		outState.putInt(DEVICE_INDEX, mDeviceIndex);
		outState.putInt(MDO_INDEX, mMDOIndex);
		}

	protected void sendResult()
		{
		Fragment targetFragment = getParentFragment();

		if (targetFragment != null && targetFragment instanceof MDOConstructorDoneListener)
			{
			((MDOConstructorDoneListener) targetFragment).onMDOConstructed(this.mMDO, this.mDeviceIndex, this.mMDOIndex);
			}
		else
			{
			Activity activity = getActivity();
			if (activity != null && activity instanceof MDOConstructorDoneListener)
				{
				((MDOConstructorDoneListener) activity).onMDOConstructed(this.mMDO, this.mDeviceIndex, this.mMDOIndex);
				}
			}

		dismiss();
		}

	@Override
	public void onDismiss(DialogInterface dialog)
		{
		super.onDismiss(dialog);
		}

	public interface MDOConstructorDoneListener
		{
			void onMDOConstructed(ModbusDataObject mdo, int deviceIndex, int mdoIndex);
		}

	@Override
	public boolean onMenuItemClick(MenuItem menuItem)
		{
		if (menuItem.getItemId() == R.id.menu_apply && (!isStartingAddressError))
			{
			mMDO.setName(mMdoNameEditText.getEditableText().toString());
			sendResult();
			}
		return false;
		}


	}


