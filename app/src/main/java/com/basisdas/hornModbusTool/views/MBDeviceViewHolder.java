package com.basisdas.hornModbusTool.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.views.custom.CircleButton;
import com.basisdas.hornModbusTool.views.custom.EntityStateButton;
import com.basisdas.hornModbusTool.views.custom.ExpandButton;

class MBDeviceViewHolder extends RecyclerView.ViewHolder
	{
	public EntityStateButton entityStateButton;
	public TextView tvDeviceName;
	public ExpandButton expandButton;
	public ViewGroup expandingArea;
	public CircleButton cbDeleteDeviceButton;
	public CircleButton cbDeviceProperties;
	public CircleButton cbAddMDOButton;
	public RecyclerView rvMDOContainer;

	MBDeviceViewHolder(final View itemView)
		{
		super(itemView);
		entityStateButton = itemView.findViewById(R.id.button_device_state);
		tvDeviceName = itemView.findViewById(R.id.text_view_device_name);
		expandButton = itemView.findViewById(R.id.button_expand_device);
		expandingArea = itemView.findViewById(R.id.layout_device_expanding_area);
		cbDeleteDeviceButton = itemView.findViewById(R.id.button_delete_device);
		cbDeviceProperties = itemView.findViewById(R.id.button_device_properties);
		cbAddMDOButton = itemView.findViewById(R.id.button_add_mdo);
		rvMDOContainer = itemView.findViewById(R.id.recycler_view_mdo_container);
		}
	}

