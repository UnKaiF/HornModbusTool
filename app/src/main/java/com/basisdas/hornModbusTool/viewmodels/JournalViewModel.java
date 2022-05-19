package com.basisdas.hornModbusTool.viewmodels;

import androidx.lifecycle.ViewModel;

import com.basisdas.hornModbusTool.datamodels.SerialCommunicationLine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JournalViewModel extends ViewModel
	{

	private StringBuilder stringBuilder;
	private static final String endl = "\r\n";

	public JournalViewModel()
		{
		stringBuilder = new StringBuilder();
		stringBuilder.append("Журнал начат ");
		stringBuilder.append(getCurrentTimeStamp());
		stringBuilder.append(endl);
		}

	public void appendLine(String line)
		{
		stringBuilder.append(line);
		stringBuilder.append(endl);
		}

	public String getAllLines()
		{
		return stringBuilder.toString();
		}

	public static String getCurrentTimeStamp()
		{
		try
			{

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy:mm:ss");
			String currentDateTime = dateFormat.format(new Date()); // Find todays date

			return currentDateTime;
			}
		catch (Exception e)
			{
			e.printStackTrace();

			return null;
			}
		}

/*	private static class SingletonHelper
		{
			private static final JournalViewModel INSTANCE = new JournalViewModel();
		}

	public static JournalViewModel getInstance()
		{
		return JournalViewModel.SingletonHelper.INSTANCE;
		}
*/
	}
