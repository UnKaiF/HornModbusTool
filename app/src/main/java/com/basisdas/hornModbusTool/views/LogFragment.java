package com.basisdas.hornModbusTool.views;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.basisdas.hornModbusTool.filedialogs.FileDialog;
import com.basisdas.hornModbusTool.filedialogs.OpenFileDialog;
import com.basisdas.hornModbusTool.filedialogs.SaveFileDialog;
import com.basisdas.hornModbusTool.R;
import com.basisdas.jlibmodbusandroid.exception.ModbusIOException;
import com.basisdas.jlibmodbusandroid.master.ModbusMaster;
import com.basisdas.jlibmodbusandroid.master.ModbusMasterFactory;
import com.basisdas.jlibmodbusandroid.serial.SerialParameters;
import com.basisdas.jlibmodbusandroid.serial.SerialPort;
import com.basisdas.jlibmodbusandroid.serial.SerialPortFactoryUSBSerialAndroid;
import com.basisdas.jlibmodbusandroid.serial.SerialUtils;
import com.basisdas.jlibmodbusandroid.serial.util.AndroidUSBSerialPortResolver;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogFragment extends Fragment implements Toolbar.OnMenuItemClickListener, FileDialog.OnFileSelectedListener
	{

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	@BindView(R.id.toolbar_log)
	androidx.appcompat.widget.Toolbar toolbar;
	@BindView(R.id.memo)
	TextView memo;
	@BindView(R.id.scroll_view)
	ScrollView scrollView;

	public LogFragment()
		{
		// Required empty public constructor
		}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment LogFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static LogFragment newInstance(String param1, String param2)
		{
		LogFragment fragment = new LogFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
		}

	@Override
	public void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
			{
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
			}
		}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
		{
		super.onViewCreated(view, savedInstanceState);
		toolbar.inflateMenu(R.menu.menu_log);
		toolbar.setOnMenuItemClickListener(this);
		appendMemoLine("Журнал чист");
		}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
		{
		View view = inflater.inflate(R.layout.fragment_log, container, false);
		ButterKnife.bind(this, view);
		return view;
		}


	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
		{
		inflater.inflate(R.menu.menu_log, menu);
		super.onCreateOptionsMenu(menu, inflater);
		}

	@Override
	public boolean onMenuItemClick(MenuItem item)
		{
		switch (item.getItemId())
			{
			case R.id.menu_load_log:
				showFileDialog(new OpenFileDialog(), OpenFileDialog.class.getName());
				break;
			case R.id.menu_save_log:
				showFileDialog(new SaveFileDialog(), SaveFileDialog.class.getName());
				break;
			case R.id.menu_support_log:
				break;
			case R.id.menu_help_log:
				break;
			}
		return false;
		}

	private void showFileDialog(FileDialog dialog, String tag)
		{
		Bundle args = new Bundle();
		args.putString(FileDialog.EXTENSION, "log");
		dialog.setArguments(args);
		dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_HornModbusTool);
		dialog.show(getChildFragmentManager(), tag);
		}

	@Override
	public void onFileSelected(FileDialog dialog, File file)
		{
		if (dialog.getClass().getName().equalsIgnoreCase(OpenFileDialog.class.getName()))
			{
			memo.setText("");
			try
				{
				appendMemoLine(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8));
				}
			catch (IOException e)
				{
				appendMemoLine("Не могу открыть файл\"" + file.getAbsolutePath() + "\"");
				appendMemoLine(e.getMessage());
				}
			}
		else
			{
			PrintWriter out = null;
			try
				{
				out = new PrintWriter(file.getAbsolutePath());
				out.println(memo.getText().toString());
				}
			catch (IOException e)
				{
				appendMemoLine("Не могу открыть файл\"" + file.getAbsolutePath() + "\"");
				appendMemoLine(e.getMessage());
				}
			finally
				{
				if (out != null)
					out.close();
				}

			}
		}


	private void appendMemoLine(String str)
		{
		String old = memo.getText().toString();
		String newtxt = old + str + '\n';
		memo.setText(newtxt);
		scrollView.scrollTo(0, memo.getHeight());
		}


	/**
	 * @return yyyy-MM-dd HH:mm:ss formate date as string
	 */
	public static String getCurrentTimeStamp()
		{
		try
			{

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDateTime = dateFormat.format(new Date()); // Find todays date

			return currentDateTime;
			}
		catch (Exception e)
			{
			e.printStackTrace();

			return null;
			}
		}
	}