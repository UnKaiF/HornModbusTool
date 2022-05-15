package com.basisdas.hornModbusTool.views;

import android.app.Activity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.basisdas.hornModbusTool.R;

public class TimesDialog extends DialogFragment implements Toolbar.OnMenuItemClickListener, SeekBar.OnSeekBarChangeListener

	{

	public static final String CURRENT_TIMEOUT = "current_timeout";
	public static final String CURRENT_PERIOD = "current_period";

	public static final String TITLE = "TITLE";
	private String mTitle = "";
	private int mToolbarTitleColor;

	private Toolbar mToolbar;
	private TextView mPeriodHeaderTV, mTimeoutHeaderTV;
	private SeekBar mPeriodSeekBar, mTimeoutSeekBar;

	private int mTimeout;
	private int mPeriod;

	protected int getLayoutResourceId()
		{
		return R.layout.dialog_times_settings;
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
			mTimeout = savedInstanceState.getInt(CURRENT_TIMEOUT, 5000);
			mPeriod = savedInstanceState.getInt(CURRENT_PERIOD, 1000);
			mTitle = savedInstanceState.getString(TITLE);
			}
		else
			{
			if (arguments != null)
				{
				if (arguments.containsKey(CURRENT_TIMEOUT))
					mTimeout = arguments.getInt(CURRENT_TIMEOUT);
				if (arguments.containsKey(CURRENT_PERIOD))
					mPeriod = arguments.getInt(CURRENT_PERIOD);
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

		mPeriodHeaderTV = (TextView) view.findViewById(R.id.period_dialog_header);
		mTimeoutHeaderTV = (TextView) view.findViewById(R.id.timeout_dialog_header);
		mPeriodSeekBar = (SeekBar) view.findViewById(R.id.period_dialog_seekbar);
		mTimeoutSeekBar = (SeekBar) view.findViewById(R.id.timeout_dialog_seekbar);

		mTimeoutSeekBar.setProgress(mTimeout);
		mPeriodSeekBar.setProgress(mPeriod);
		mPeriodHeaderTV.setText("Интервал опроса " + mPeriod + " мс");
		mTimeoutHeaderTV.setText("Таймаут ответа " + mTimeout + " мс");
		mPeriodSeekBar.setOnSeekBarChangeListener(this);
		mTimeoutSeekBar.setOnSeekBarChangeListener(this);

		}

	@Override
	public void onProgressChanged(SeekBar seekBar, int i, boolean b)
		{
		if (!b) return;
		if (seekBar.getId() == mPeriodSeekBar.getId())
			{
			mPeriod = i;
			mPeriodHeaderTV.setText("Интервал опроса " + i + " мс");
			}
		else
			{
			mTimeout = i;
			mTimeoutHeaderTV.setText("Таймаут ответа " + i + " мс");
			}
		}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) 	{}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)	{}


	@Override
	public void onSaveInstanceState(Bundle outState)
		{
		super.onSaveInstanceState(outState);
		outState.putInt(CURRENT_TIMEOUT, mTimeout);
		outState.putInt(CURRENT_PERIOD, mPeriod);
		outState.putString(TITLE, mTitle);
		}

	protected void sendResult(int period, int timeout)
		{
		Fragment targetFragment = getParentFragment();

		if (targetFragment != null && targetFragment instanceof TimesDialog.OnTimesUpdatedListener)
			{
			((TimesDialog.OnTimesUpdatedListener) targetFragment).onTimesUpdated(this.mPeriod, this.mTimeout);
			}
		else
			{
			Activity activity = getActivity();
			if (activity != null && activity instanceof TimesDialog.OnTimesUpdatedListener)
				{
				((TimesDialog.OnTimesUpdatedListener) activity).onTimesUpdated(this.mPeriod, this.mTimeout);
				}
			}

		dismiss();
		}


	public interface OnTimesUpdatedListener
		{
			void onTimesUpdated(int period, int timeout);
		}

	@Override
	public boolean onMenuItemClick(MenuItem menuItem)
		{
		if (menuItem.getItemId() == R.id.menu_apply)
			{
			sendResult(mPeriod, mTimeout);
			}
		return false;
		}


	}


