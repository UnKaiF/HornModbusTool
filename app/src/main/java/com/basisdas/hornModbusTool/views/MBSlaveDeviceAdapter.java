package com.basisdas.hornModbusTool.views;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.datamodels.MDOParameters;
import com.basisdas.hornModbusTool.datamodels.ModbusDataObject;
import com.basisdas.hornModbusTool.datamodels.utils.JsonParser;
import com.basisdas.hornModbusTool.misc.EntityState;
import com.basisdas.hornModbusTool.misc.InflateState;
import com.basisdas.hornModbusTool.viewmodels.ModbusDataObjectViewModel;
import com.basisdas.hornModbusTool.viewmodels.SlaveDeviceViewModel;
import com.basisdas.hornModbusTool.views.custom.CircleButton;
import com.basisdas.hornModbusTool.views.custom.EntityStateButton;

public class MBSlaveDeviceAdapter extends RecyclerView.Adapter<MDOViewHolder>
	{

	private SlaveDeviceViewModel slaveDeviceViewModel;
	private FragmentManager fragmentManager;
	private MBDeviceViewHolder deviceViewHolder;

	// Constuctor
	MBSlaveDeviceAdapter(SlaveDeviceViewModel slaveDeviceViewModel, MBDeviceViewHolder deviceViewHolder, FragmentManager fragmentManager)
		{
		this.slaveDeviceViewModel = slaveDeviceViewModel;
		this.fragmentManager = fragmentManager;
		this.deviceViewHolder = deviceViewHolder;
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
		mdoViewHolder.mdoProperties.setOnLongClickListener( new View.OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View view)
					{
					int pos = mdoViewHolder.getAdapterPosition();
					Bundle args = new Bundle();
					args.putString("TITLE", "Параметры ОДМ");
					String json = JsonParser.getGsonParser().toJson(slaveDeviceViewModel.modbusDataObjectViewModels.get(mdoViewHolder.getAdapterPosition()).getMDO());
					args.putString(MDOConstructorDialog.MDO, json);
					args.putInt(MDOConstructorDialog.DEVICE_INDEX, deviceViewHolder.getAdapterPosition());
					args.putInt(MDOConstructorDialog.MDO_INDEX, pos);
					MDOConstructorDialog dialog = new MDOConstructorDialog();
					dialog.setArguments(args);
					dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_HornModbusTool);
					dialog.show(fragmentManager, MDOConstructorDialog.class.getName());
					return true;
					}
			}
		);

		InflateState inflateState = modbusDataObjectViewModel.getInflateState();
		mdoViewHolder.expandingArea.setVisibility(inflateState == InflateState.INFLATED ? View.VISIBLE : View.GONE);
		mdoViewHolder.expandButton.setState(inflateState);
		mdoViewHolder.expandButton.setClickListener(null);
		mdoViewHolder.expandButton.setClickListener(state ->
														 {
														 mdoViewHolder.expandingArea.setVisibility((state == InflateState.DEFLATED) ? View.GONE : View.VISIBLE);
														 slaveDeviceViewModel.modbusDataObjectViewModels.get(mdoViewHolder.getAdapterPosition()).setInflateState(state);
														 });
		//State & substate  clicklisterer
		mdoViewHolder.entityStateButton.setState(slaveDeviceViewModel.modbusDataObjectViewModels.get(position).getState());
		mdoViewHolder.entityStateButton.setSubState(slaveDeviceViewModel.modbusDataObjectViewModels.get(position).getEntitySubState());
		mdoViewHolder.entityStateButton.setClickListener(new EntityStateButton.ClickListener()
			{
				@Override
				public void onStateChanged(EntityState state)
					{
					int pos = mdoViewHolder.getAdapterPosition();
					slaveDeviceViewModel.modbusDataObjectViewModels.get(pos).setState(state);
					}
			});

		//Delete MDO button clicklistener
		mdoViewHolder.cbDeleteMDOButton.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view)
				{
				int pos = mdoViewHolder.getAdapterPosition();
				slaveDeviceViewModel.modbusDataObjectViewModels.remove(pos);
				notifyItemRemoved(pos);
				return true;
				}
			});



		}


	@Override
	public int getItemCount()
		{
		return slaveDeviceViewModel.modbusDataObjectViewModels.size();
		}

	}
