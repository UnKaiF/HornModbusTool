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
import com.basisdas.hornModbusTool.datamodels.utils.JsonParser;
import com.basisdas.jlibmodbusandroid.serial.SerialParameters;
import com.basisdas.jlibmodbusandroid.serial.SerialPort;


public class SerialParametersDialog extends DialogFragment implements Toolbar.OnMenuItemClickListener

	{

	public static final String SERIAL_PARAMETERS = "serial_parameters";
	protected Toolbar mToolbar;
	private SerialParameters mSp;
	private static final Integer databits[] = {7, 8};
	private static final Integer stopbits[] = {1, 2};
	private Spinner mBaudRateSpinner, mDataBitsSpinner, mStopBitsSpinner, mParityBitsSpinner;

	protected int getLayoutResourceId()
		{
		return R.layout.dialog_serial_parameters;
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
			json = savedInstanceState.getString(SERIAL_PARAMETERS);
			}
		else
			{
			if (arguments != null && arguments.containsKey(SERIAL_PARAMETERS))
					json = arguments.getString(SERIAL_PARAMETERS);
			}

		if (json != null)
			{
			mSp = JsonParser.getGsonParser().fromJson(json, SerialParameters.class);
			}
		else
			{
			mSp = new SerialParameters();
			}

		mDataBitsSpinner = (Spinner) view.findViewById(R.id.data_bits_spinner);
		mBaudRateSpinner = (Spinner) view.findViewById(R.id.baudrate_spinner);
		mStopBitsSpinner = (Spinner) view.findViewById(R.id.stop_bit_spinner);
		mParityBitsSpinner = (Spinner) view.findViewById(R.id.parity_bit_spinner);

		ArrayAdapter<SerialPort.BaudRate> baudRateAdapter = new ArrayAdapter<SerialPort.BaudRate>(this.getContext(), R.layout.spinner_item, SerialPort.BaudRate.values());
		baudRateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mBaudRateSpinner.setAdapter(baudRateAdapter);

		ArrayAdapter<SerialPort.Parity> parityBitsAdapter = new ArrayAdapter<SerialPort.Parity>(this.getContext(), R.layout.spinner_item, SerialPort.Parity.values());
		parityBitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mParityBitsSpinner.setAdapter(parityBitsAdapter);

		ArrayAdapter<Integer> dataBitsAdapter = new ArrayAdapter<Integer>(this.getContext(), R.layout.spinner_item, databits);
		dataBitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDataBitsSpinner.setAdapter(dataBitsAdapter);

		ArrayAdapter<Integer> stopBitsAdapter = new ArrayAdapter<Integer>(this.getContext(), R.layout.spinner_item, stopbits);
		stopBitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mStopBitsSpinner.setAdapter(stopBitsAdapter);

		//TODO: Spinner state restore from local SerialParameters (mSp)

		mToolbar = (Toolbar) view.findViewById(R.id.dialog_save_file_toolbar);
		mToolbar.inflateMenu(R.menu.toolbar_menu_apply);
		mToolbar.setOnMenuItemClickListener(this);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
					{
					dismiss();
					}
			});

		}


	@Override
	public void onSaveInstanceState(Bundle outState)
		{
		super.onSaveInstanceState(outState);
		String json = JsonParser.getGsonParser().toJson(mSp);
		outState.putString(SERIAL_PARAMETERS, json);
		}

	protected void sendResult()
		{
		Fragment targetFragment = getParentFragment();

		if (targetFragment != null && targetFragment instanceof SerialParametersUpdateListener)
			{
			((SerialParametersUpdateListener) targetFragment).onSerialParametersUpdated(mSp);
			}
		else
			{
			Activity activity = getActivity();
			if (activity != null && activity instanceof SerialParametersUpdateListener)
				{
				((SerialParametersUpdateListener) activity).onSerialParametersUpdated(mSp);
				}
			}

		dismiss();
		}

	@Override
	public void onDismiss(DialogInterface dialog)
		{
		super.onDismiss(dialog);
		}

	public interface SerialParametersUpdateListener
		{
			void onSerialParametersUpdated(SerialParameters sp);
		}
	//TODO: actualisation of lockal SerialParameters (mSp) by spinners state
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


