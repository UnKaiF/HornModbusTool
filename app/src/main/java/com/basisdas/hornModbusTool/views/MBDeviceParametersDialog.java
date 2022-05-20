package com.basisdas.hornModbusTool.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.basisdas.hornModbusTool.filedialogs.utils.KeyboardUtils;
import com.basisdas.hornModbusTool.R;
import com.google.android.material.textfield.TextInputEditText;

public class MBDeviceParametersDialog extends DialogFragment implements Toolbar.OnMenuItemClickListener

	{

	public static final String DEVICE_NAME = "device_name";
	public static final String DEVICE_ID = "device_id";
	public static final String INDEX = "item_index";

	public static final String TITLE = "TITLE";
	private String mTitle = "";
	private int mToolbarTitleColor;

	protected Toolbar mToolbar;
	private TextInputEditText mEditTextDeviceName, mEditTextDeviceID;
	private String mDeviceName = "";
	private int mDeviceId = -1;
	private int mIndex = -1;

	protected int getLayoutResourceId()
		{
		return R.layout.dialog_mbdevice_parameters;
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

		if (savedInstanceState != null)
			{
			mDeviceName = savedInstanceState.getString(DEVICE_NAME, "Устройство Modbus");
			mDeviceId = savedInstanceState.getInt(DEVICE_ID, 1);
			mTitle = savedInstanceState.getString(TITLE);
			mIndex = savedInstanceState.getInt(INDEX);
			}
		else
			{
			if (arguments != null)
				{
				if (arguments.containsKey(DEVICE_NAME))
					mDeviceName = arguments.getString(DEVICE_NAME);
				if (arguments.containsKey(DEVICE_ID))
					mDeviceId = arguments.getInt(DEVICE_ID);
				if (arguments.containsKey(TITLE))
					mTitle = getArguments().getString(TITLE);
				if (arguments.containsKey(INDEX))
					mIndex = getArguments().getInt(INDEX);
				}
			}


		mEditTextDeviceName = (TextInputEditText) view.findViewById(R.id.dialog_mbdevice_name);
		mEditTextDeviceName.setText(mDeviceName);
		mEditTextDeviceID =  (TextInputEditText) view.findViewById(R.id.dialog_mbdevice_id);
		if (mDeviceId > 0)
			mEditTextDeviceID.setText(Integer.toString(mDeviceId));


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


	@Override
	public void onSaveInstanceState(Bundle outState)
		{
		super.onSaveInstanceState(outState);
		outState.putString(DEVICE_NAME, mDeviceName);
		outState.putInt(DEVICE_ID, mDeviceId);
		outState.putString(TITLE, mTitle);
		outState.putInt(INDEX, mIndex);
		}

	protected void sendResult()
		{
		Fragment targetFragment = getParentFragment();

		if (targetFragment != null && targetFragment instanceof MBDeviceParametersCreatedListener)
			{
			((MBDeviceParametersCreatedListener) targetFragment).onMBDeviceParametersCreated(this.mDeviceName, this.mDeviceId, this.mIndex);
			}
		else
			{
			Activity activity = getActivity();
			if (activity != null && activity instanceof MBDeviceParametersCreatedListener)
				{
				((MBDeviceParametersCreatedListener) activity).onMBDeviceParametersCreated(this.mDeviceName, this.mDeviceId, this.mIndex);
				}
			}

		dismiss();
		}

	@Override
	public void onDismiss(DialogInterface dialog)
		{
		if (getActivity() != null)
			{
			KeyboardUtils.hideKeyboard(getActivity());
			}
		super.onDismiss(dialog);
		}

	public interface MBDeviceParametersCreatedListener
		{
			void onMBDeviceParametersCreated(String deviceName, int deviceId, int index);
		}


	@Override
	public boolean onMenuItemClick(MenuItem menuItem)
		{
		if (menuItem.getItemId() == R.id.menu_apply)
			{
			String deviceName = mEditTextDeviceName.getEditableText().toString();
			String deviceIDString = mEditTextDeviceID.getEditableText().toString();
			int deviceID = -1;
			try
				{
				deviceID = Integer.parseUnsignedInt(deviceIDString);
				}
			catch (NumberFormatException e)
				{
				deviceID = -1;
				}

			if (deviceName.isEmpty() || deviceIDString.isEmpty())	{ return false;	}

			if (deviceID < 1 || deviceID > 65535) return false;

			mDeviceId = deviceID;
			mDeviceName = deviceName;
			sendResult();
			}
		return false;
		}


	}


