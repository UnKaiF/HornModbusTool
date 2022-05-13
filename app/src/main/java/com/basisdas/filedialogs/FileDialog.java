package com.basisdas.filedialogs;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basisdas.filedialogs.utils.KeyboardUtils;
import com.basisdas.hornModbusTool.R;

import java.io.File;
import java.io.FileFilter;


public abstract class FileDialog extends DialogFragment implements FileListAdapter.OnFileSelectedListener
	{

	public static final String ROOT_DIRECTORY = "root_directory";
	public static final String START_DIRECTORY = "start_directory";
	public static final String EXTENSION = "extension";

	private static final String OUT_STATE_CURRENT_DIRECTORY = "out_state_current_dir";
	private static final String EXTERNAL_ROOT_PATH = Environment.getExternalStorageDirectory().getPath();

	protected File mCurrentDir;
	protected File mRootDir;
	protected FileFilter mFilesFilter;

	protected Toolbar mToolbar;
	protected ProgressBar mProgress;
	protected RecyclerView mRecyclerView;
	protected int mIconColor;
	protected int mToolbarTitleColor;

	private UpdateFilesTask mUpdateFilesTask;
	protected String mExtension;

	private String mRootPathDisplayName; // todo: create an argument for this


	@Override
	public void onAttach(Activity activity)
		{

		super.onAttach(activity);
		mToolbarTitleColor = activity.getApplicationContext().getColor(R.color.white);
		mIconColor = activity.getApplicationContext().getColor(R.color.menu_text_color);
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

		mToolbar.setTitleTextColor(mToolbarTitleColor);

		mToolbar.setNavigationOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
					{

					if (mCurrentDir.getPath().equalsIgnoreCase(EXTERNAL_ROOT_PATH))
						{
						dismiss();
						}
					else
						{
						mCurrentDir = mCurrentDir.getParentFile();
						refresh();
						}
					}
			});

		extractArguments(savedInstanceState);

		initList();
		}

	protected void extractArguments(Bundle savedInstanceState)
		{

		Bundle arguments = getArguments();

		mRootDir = arguments != null ? (File) arguments.getSerializable(ROOT_DIRECTORY) : null;

		if (savedInstanceState != null)
			{
			mCurrentDir = (File) savedInstanceState.getSerializable(OUT_STATE_CURRENT_DIRECTORY);
			}
		else
			{
			mCurrentDir = (File) (arguments != null ? arguments.getSerializable(START_DIRECTORY) : null);
			}

		if (mCurrentDir == null)
			{
			mCurrentDir = new File(EXTERNAL_ROOT_PATH);
			}

		if (mRootDir == null)
			{
			mRootDir = mCurrentDir;
			}

		if (arguments != null && arguments.containsKey(EXTENSION))
			{
			mExtension = arguments.getString(EXTENSION);
			mFilesFilter = new ExtensionFilter(mExtension);
			}
		}

	private void initList()
		{

		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		}

	@Override
	public void onStart()
		{

		super.onStart();

		refresh();
		}

	public void refresh()
		{

		if (mUpdateFilesTask == null || mUpdateFilesTask.getStatus() == AsyncTask.Status.FINISHED)
			{

			mUpdateFilesTask = new UpdateFilesTask();
			mUpdateFilesTask.execute(mCurrentDir);
			}
		}

	@Override
	public void onStop()
		{

		super.onStop();
		mUpdateFilesTask.cancel(false);
		}

	@Override
	public void onFileSelected(File file)
		{

		if (file.isDirectory())
			{
			mCurrentDir = file;
			refresh();
			}
		}

	@Override
	public void onSaveInstanceState(Bundle outState)
		{

		super.onSaveInstanceState(outState);

		outState.putSerializable(OUT_STATE_CURRENT_DIRECTORY, mCurrentDir);
		}

	protected void sendResult(File file)
		{

		Fragment targetFragment = getParentFragment();

		if (targetFragment != null && targetFragment instanceof OnFileSelectedListener)
			{
			((OnFileSelectedListener) targetFragment).onFileSelected(this, file);
			}
		else
			{
			Activity activity = getActivity();
			if (activity != null && activity instanceof OnFileSelectedListener)
				{
				((OnFileSelectedListener) activity).onFileSelected(this, file);
				}
			}

		dismiss();
		}

	protected abstract int getLayoutResourceId();

	private class UpdateFilesTask extends AsyncTask<File, Void, File[]>
		{

			private File[] mFileArray;
			private File mDirectory;

			private UpdateFilesTask()
				{

				}

			@Override
			protected void onPreExecute()
				{

				super.onPreExecute();

				mProgress.setVisibility(View.VISIBLE);
				}

			@Override
			protected File[] doInBackground(File... files)
				{

				mDirectory = files[0];
				mFileArray = files[0].listFiles(mFilesFilter);

				return mFileArray;
				}

			@Override
			protected void onPostExecute(File[] localFiles)
				{

				super.onPostExecute(localFiles);

				if (!isCancelled() && getActivity() != null)
					{

					Drawable navIcon;

					if (mDirectory.getPath().equalsIgnoreCase(EXTERNAL_ROOT_PATH))
						{

						mToolbar.setTitle(mRootPathDisplayName);
						navIcon = getResources().getDrawable(R.drawable.baseline_close_48);
						}
					else
						{
						mToolbar.setTitle(mCurrentDir.getName());
						navIcon = getResources().getDrawable(R.drawable.go_back_48px);
						}

					navIcon.setColorFilter(mIconColor, PorterDuff.Mode.SRC_IN);
					mToolbar.setNavigationIcon(navIcon);

					mRecyclerView.setAdapter(new FileListAdapter(getActivity(), localFiles, FileDialog.this));

					mProgress.setVisibility(View.GONE);
					}
				}
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

	public interface OnFileSelectedListener
		{

			void onFileSelected(FileDialog dialog, File file);
		}
	}
