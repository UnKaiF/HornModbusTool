package com.basisdas.hornModbusTool.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.views.custom.CircleButton;
import com.basisdas.hornModbusTool.views.custom.EntityStateButton;
import com.basisdas.hornModbusTool.views.custom.ExpandButton;

public class MDOViewHolder  extends RecyclerView.ViewHolder
	{
	public EntityStateButton entityStateButton;
	public ExpandButton expandButton;
	public ViewGroup expandingArea;
	public CircleButton cbDeleteMDOButton;
	public TextView mdoProperties;
	public TextView mdoTransactionErrors;
	public TextView mdoNameNValue;

	MDOViewHolder(View itemView)
		{
		super(itemView);
		entityStateButton = itemView.findViewById(R.id.button_mdo_state);
		expandButton = itemView.findViewById(R.id.button_expand_mdo);
		expandingArea = itemView.findViewById(R.id.layout_mdo_expanding_area);
		cbDeleteMDOButton = itemView.findViewById(R.id.button_delete_mdo);
		mdoProperties = itemView.findViewById(R.id.text_view_mdo_parameters_block);
		mdoTransactionErrors = itemView.findViewById(R.id.text_view_mdo_errors_log);
		mdoNameNValue = itemView.findViewById(R.id.text_view_mdo_value);
		}

	}
