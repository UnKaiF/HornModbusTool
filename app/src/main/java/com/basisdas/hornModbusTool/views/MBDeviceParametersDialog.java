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

import com.basisdas.filedialogs.utils.KeyboardUtils;
import com.basisdas.hornModbusTool.R;

public class MBDeviceParametersDialog extends DialogFragment implements Toolbar.OnMenuItemClickListener

	{

	public static final String DEVICE_NAME = "device_name";
	public static final String DEVICE_ID = "device_id";

	public static final String TITLE = "TITLE";
	private String mTitle = "";
	private int mToolbarTitleColor;

	protected Toolbar mToolbar;
	private String mDeviceName;
	private int mDeviceId;


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
				}
			}


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
		}

	protected void sendResult()
		{
		Fragment targetFragment = getParentFragment();

		if (targetFragment != null && targetFragment instanceof MBDeviceParametersUpatedListener)
			{
			((MBDeviceParametersUpatedListener) targetFragment).onMBDeviceParametersUpdated(this.mDeviceName, this.mDeviceId);
			}
		else
			{
			Activity activity = getActivity();
			if (activity != null && activity instanceof MBDeviceParametersUpatedListener)
				{
				((MBDeviceParametersUpatedListener) activity).onMBDeviceParametersUpdated(this.mDeviceName, this.mDeviceId);
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

	public interface MBDeviceParametersUpatedListener
		{
			void onMBDeviceParametersUpdated(String deviceName, int deviceId);
		}

	//TODO: check input before user can press "apply"

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


