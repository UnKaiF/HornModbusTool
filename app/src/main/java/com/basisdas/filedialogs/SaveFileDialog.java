package com.basisdas.filedialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.basisdas.hornModbusTool.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;


public class SaveFileDialog extends FileDialog implements Toolbar.OnMenuItemClickListener
	{


	protected TextInputEditText mFileNameText;
	private FileNameValidator fileNameValidator;

	@Override
	protected int getLayoutResourceId()
		{
		return R.layout.dialog_save_file;
		}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
		{
		mToolbar = (Toolbar) view.findViewById(R.id.dialog_save_file_toolbar);
		mProgress = (ProgressBar) view.findViewById(R.id.dialog_save_file_progress);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.dialog_save_file_rv_files);

		super.onViewCreated(view, savedInstanceState);
		fileNameValidator = new FileNameValidator(getString(R.string.error_invalid_file_name),
												  getString(R.string.error_empty_field));

		mFileNameText = (TextInputEditText) view.findViewById(R.id.dialog_save_file_et_filename);

		mToolbar.inflateMenu(R.menu.toolbar_menu_apply);
		mToolbar.getMenu().findItem(R.id.menu_apply).getIcon().setColorFilter(mIconColor, PorterDuff.Mode.SRC_IN);
		mToolbar.setOnMenuItemClickListener(this);

		}

	@Override
	public void onFileSelected(final File file)
		{

		if (file.isFile())
			{

			confirmOverwrite(file);
			}
		else
			{
			super.onFileSelected(file);
			}
		}

	private void confirmOverwrite(final File file)
		{

		new AlertDialog.Builder(getActivity())
				.setMessage(R.string.confirm_overwrite_file)
				.setPositiveButton(R.string.label_button_overwrite, new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which)
							{

							sendResult(file);
							}
					})
				.setNegativeButton(R.string.label_button_cancel, null)
				.create().show();
		}


	@Override
	public boolean onMenuItemClick(MenuItem menuItem)
		{

		if (menuItem.getItemId() == R.id.menu_apply && fileNameValidator.isValid(mFileNameText.getText(), false))
			{

			File result = new File(mCurrentDir, mFileNameText.getText() + "." + (mExtension != null ? mExtension : ""));

			if (result.exists())
				{
				confirmOverwrite(result);
				}
			else
				{
				sendResult(result);
				}
			}

		return false;
		}
	}
