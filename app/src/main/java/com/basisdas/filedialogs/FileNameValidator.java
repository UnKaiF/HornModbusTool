package com.basisdas.filedialogs;

import androidx.annotation.NonNull;
import com.basisdas.filedialogs.utils.TextUtils;

public class FileNameValidator
	{

	private String errorMessage;
	private final String mEmptyMessage;
	private final String mInvalidMessage;

	public FileNameValidator(String invalidMessage, @NonNull String emptyMessage)
		{
		errorMessage = emptyMessage;
		mInvalidMessage = invalidMessage;
		mEmptyMessage = emptyMessage;

		}

	public void setErrorMessage(@NonNull String errorMessage)
		{
		this.errorMessage = errorMessage;
		}

	@NonNull
	public String getErrorMessage()
		{
		return this.errorMessage;
		}

	public boolean isValid(@NonNull CharSequence charSequence, boolean isEmpty)
		{

		if (isEmpty || TextUtils.isEmpty(charSequence.toString()))
			{
			errorMessage = mEmptyMessage;
			return false;
			}

		if (charSequence.toString().matches("^[^A-Za-z_\\-\\s0-9\\.]+$"))
			{
			errorMessage = mInvalidMessage;
			return false;
			}

		return true;
		}
	}