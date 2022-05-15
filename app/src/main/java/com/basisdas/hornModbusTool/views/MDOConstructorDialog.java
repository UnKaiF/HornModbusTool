package com.basisdas.hornModbusTool.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.basisdas.hornModbusTool.datamodels.ModbusDataObject;
import com.basisdas.hornModbusTool.datamodels.utils.JsonParser;
import com.basisdas.hornModbusTool.datamodels.utils.MDOParamConstructor;
import com.basisdas.jlibmodbusandroid.serial.SerialPort;
import com.google.android.material.textfield.TextInputEditText;

public class MDOConstructorDialog extends DialogFragment implements Toolbar.OnMenuItemClickListener

	{

	public static final String TITLE = "TITLE";
	private String mTitle = "";
	private int mToolbarTitleColor;

	public static final String MDO = "mdo";
	private static final Boolean b[] = {false, true};

	protected Toolbar mToolbar;
	private TextInputEditText mMdoNameEditText, mMdoStartingAddressEditText;
	private Spinner mMdoAreaSpinner, mMdoInterpretationTypeSpinner, mMdoBitSizeSpinner, mMdoElementOrderSpinner, mMdoRegisterOrderSpinner;
	private ModbusDataObject mMDO;


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
			}
		else
			{
			if (arguments != null)
				{
				if (arguments.containsKey(MDO))
					json = arguments.getString(MDO);
				if (arguments.containsKey(TITLE))
					mTitle = getArguments().getString(TITLE);
				}
			}

		if (json != null)
			{
			mMDO = JsonParser.getGsonParser().fromJson(json, ModbusDataObject.class);
			}
		else
			{
			mMDO = new ModbusDataObject(MDOParamConstructor.getMDOParameters(), "Коррекция t");
			//mMDO.setValue("0.22");
			}

		mMdoNameEditText = (TextInputEditText) view.findViewById(R.id.dialog_mdo_name_edit);
		mMdoNameEditText.setText(mMDO.getName());

		mMdoStartingAddressEditText = (TextInputEditText) view.findViewById(R.id.dialog_mdo_starting_address_edit);
		mMdoStartingAddressEditText.setText(Integer.toString(mMDO.getParams().startingAddress));

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

	private <T> void  setSpinnerAdapter(Spinner spinner, T[] array)
		{
		ArrayAdapter<T> adapter = new ArrayAdapter<T>(this.getContext(), R.layout.spinner_item, array);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		}

	@Override
	public void onSaveInstanceState(Bundle outState)
		{
		super.onSaveInstanceState(outState);
		String json = JsonParser.getGsonParser().toJson(mMDO);
		outState.putString(MDO, json);
		outState.putString(TITLE, mTitle);
		}

	protected void sendResult()
		{
		Fragment targetFragment = getParentFragment();

		if (targetFragment != null && targetFragment instanceof MDOConstructorDoneListener)
			{
			((MDOConstructorDoneListener) targetFragment).onMDOConstructed(this.mMDO);
			}
		else
			{
			Activity activity = getActivity();
			if (activity != null && activity instanceof MDOConstructorDoneListener)
				{
				((MDOConstructorDoneListener) activity).onMDOConstructed(this.mMDO);
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
			void onMDOConstructed(ModbusDataObject mdo);
		}

	@Override
	public boolean onMenuItemClick(MenuItem menuItem)
		{
		if (menuItem.getItemId() == R.id.menu_apply)
			{
			sendResult();
			}
		return false;
		}


	}


