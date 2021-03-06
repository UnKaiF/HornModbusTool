package com.basisdas.hornModbusTool.filedialogs.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class KeyboardUtils
	{

	public static void hideKeyboard(Activity activity)
		{

		InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(
				Context.INPUT_METHOD_SERVICE);

// check if no view has focus:
		View view = activity.getCurrentFocus();
		if (view != null)
			{
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}