package com.basisdas.hornModbusTool.views;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.datamodels.MDOParameters;
import com.basisdas.hornModbusTool.datamodels.ModbusDataObject;
import com.basisdas.hornModbusTool.misc.InflateState;
import com.basisdas.hornModbusTool.viewmodels.ModbusDataObjectViewModel;
import com.basisdas.hornModbusTool.viewmodels.SlaveDeviceViewModel;

public class MBSlaveDeviceAdapter extends RecyclerView.Adapter<MDOViewHolder>
	{

	private SlaveDeviceViewModel slaveDeviceViewModel;

	// Constuctor
	MBSlaveDeviceAdapter(SlaveDeviceViewModel slaveDeviceViewModel)
		{
		this.slaveDeviceViewModel = slaveDeviceViewModel;
		}

	@NonNull
	@Override
	public MDOViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
		{

		// Here we inflate the corresponding
		// layout of the child item
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mdo_item, viewGroup, false);

		return new MDOViewHolder(view);
		}

	@Override
	public void onBindViewHolder(@NonNull MDOViewHolder mdoViewHolder, int position)
		{

		// Create an instance of the ChildItem
		// class for the given position
		ModbusDataObjectViewModel modbusDataObjectViewModel = slaveDeviceViewModel.modbusDataObjectViewModels.get(position);
		mdoViewHolder.mdoNameNValue.setText(modbusDataObjectViewModel.getName() + ": " + modbusDataObjectViewModel.getValue());

		mdoViewHolder.mdoTransactionErrors.setText("");
		mdoViewHolder.mdoProperties.setText(modbusDataObjectViewModel.getParamsString());

		InflateState inflateState = modbusDataObjectViewModel.getInflateState();
		mdoViewHolder.expandingArea.setVisibility(inflateState == InflateState.INFLATED ? View.VISIBLE : View.GONE);
		mdoViewHolder.expandButton.setState(inflateState);
		mdoViewHolder.expandButton.setClickListener(null);
		mdoViewHolder.expandButton.setClickListener(state ->
														 {
														 mdoViewHolder.expandingArea.setVisibility((state == InflateState.DEFLATED) ? View.GONE : View.VISIBLE);
														 modbusDataObjectViewModel.setInflateState(state);
														 });
		}


	@Override
	public int getItemCount()
		{
		return slaveDeviceViewModel.modbusDataObjectViewModels.size();
		}

	}
