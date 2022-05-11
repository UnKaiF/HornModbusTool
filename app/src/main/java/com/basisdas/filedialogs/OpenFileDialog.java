package com.basisdas.filedialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.basisdas.hornModbusTool.R;

import java.io.File;


public class OpenFileDialog extends FileDialog
	{

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
		{
		mToolbar = (Toolbar) view.findViewById(R.id.dialog_open_file_toolbar);
		mProgress = (ProgressBar) view.findViewById(R.id.dialog_open_file_progress);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.dialog_open_file_rv_files);
		super.onViewCreated(view, savedInstanceState);
		}

	@Override
	protected int getLayoutResourceId()
		{
		return R.layout.dialog_open_file;
		}

	@Override
	public void onFileSelected(File file)
		{

		if (file.isFile())
			{
			sendResult(file);
			}
		else
			{
			super.onFileSelected(file);
			}
		}
	}