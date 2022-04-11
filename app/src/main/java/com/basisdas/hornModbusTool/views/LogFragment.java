package com.basisdas.hornModbusTool.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.basisdas.hornModbusTool.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogFragment extends Fragment
	{

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	@BindView(R.id.toolbar_log)	androidx.appcompat.widget.Toolbar toolbar;
	@BindView(R.id.memo) TextView memo;
	@BindView(R.id.scroll_view)	ScrollView scrollView;

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


	private void appendMemoLine(String str)
		{
		String old = memo.getText().toString();
		String newtxt = old + str + '\n';
		memo.setText(newtxt);
		scrollView.scrollTo(0, memo.getHeight());
		}

	}