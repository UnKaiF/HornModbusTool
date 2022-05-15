package com.basisdas.hornModbusTool.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.datamodels.MDOParameters;
import com.basisdas.hornModbusTool.datamodels.ModbusDataObject;
import com.basisdas.hornModbusTool.datamodels.utils.JsonParser;
import com.basisdas.hornModbusTool.datamodels.utils.MDOParamConstructor;
import com.google.android.material.textfield.TextInputEditText;

public class MDOValueUpdateDialog extends DialogFragment implements Toolbar.OnMenuItemClickListener

	{

	public static final String MDO = "mdo";
	public static final String NEW_MDO_VALUE = "new_mdo_value";

	public static final String TITLE = "TITLE";
	private String mTitle = "";
	private int mToolbarTitleColor;

	protected Toolbar mToolbar;
	private TextView mMdoParametersTextView;
	private TextInputEditText mEditText;
	private ModbusDataObject mMDO;
	private String mNewValue;

	protected int getLayoutResourceId()
		{
		return R.layout.dialog_mdo_value;
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
			mNewValue = savedInstanceState.getString(NEW_MDO_VALUE);
			mTitle = savedInstanceState.getString(TITLE);
			}
		else
			{
			if (arguments != null)
				{
				if (arguments.containsKey(MDO))
					json = arguments.getString(MDO);
				if (arguments.containsKey(NEW_MDO_VALUE))
					mNewValue = arguments.getString(NEW_MDO_VALUE);
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
			mMDO.setValue("0.22");
			}

		mMdoParametersTextView = (TextView) view.findViewById(R.id.mdo_value_dialog_parameters);
		mMdoParametersTextView.setText(getMdoParametersSummary(mMDO));

		mEditText = (TextInputEditText) view.findViewById(R.id.dialog_mdo_value_edit);
		mEditText.setText(mMDO.getValue());

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

	private String getMdoParametersSummary(ModbusDataObject mdo)
		{
		StringBuilder sb = new StringBuilder();
		sb.append("Имя: " + mdo.getName() + "\r\n");
		sb.append("Пространство: " + mdo.getParams().mdoArea.name() + "\r\n");
		sb.append("Начальный адрес: " + mdo.getParams().startingAddress + "\r\n");
		sb.append("Тип интерпретации: " + mdo.getParams().elementType.name() + "\r\n");
		sb.append("Битовый размер: " + mdo.getParams().elementBitSize.name());
		return sb.toString();
		}


	@Override
	public void onSaveInstanceState(Bundle outState)
		{
		super.onSaveInstanceState(outState);
		outState.putString(NEW_MDO_VALUE, mNewValue);
		String json = JsonParser.getGsonParser().toJson(mMDO);
		outState.putString(MDO, json);
		outState.putString(TITLE, mTitle);
		}

	protected void sendResult()
		{
		Fragment targetFragment = getParentFragment();

		if (targetFragment != null && targetFragment instanceof MDOValueUpdateListener)
			{
			((MDOValueUpdateListener) targetFragment).onMDOValueUpdated(this.mMDO, this.mNewValue);
			}
		else
			{
			Activity activity = getActivity();
			if (activity != null && activity instanceof MDOValueUpdateListener)
				{
				((MDOValueUpdateListener) activity).onMDOValueUpdated(this.mMDO, this.mNewValue);
				}
			}

		dismiss();
		}

	@Override
	public void onDismiss(DialogInterface dialog)
		{
		super.onDismiss(dialog);
		}

	public interface MDOValueUpdateListener
		{
			void onMDOValueUpdated(ModbusDataObject mdo, String value);
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


