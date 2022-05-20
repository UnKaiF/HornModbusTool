package com.basisdas.hornModbusTool.views;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.datamodels.utils.JsonParser;
import com.basisdas.hornModbusTool.misc.EntityState;
import com.basisdas.hornModbusTool.misc.InflateState;
import com.basisdas.hornModbusTool.viewmodels.SerialCommLineViewModel;
import com.basisdas.hornModbusTool.viewmodels.SlaveDeviceViewModel;

public class SerialCommLineAdapter extends RecyclerView.Adapter<MBDeviceViewHolder>
	{

	// An object of RecyclerView.RecycledViewPool
	// is created to share the Views
	// between the child and
	// the parent RecyclerViews
	//private RecyclerView.RecycledViewPool viewPool	= new RecyclerView.RecycledViewPool();
	private SerialCommLineViewModel serialCommLineViewModel;
	private FragmentManager fragmentManager;

	SerialCommLineAdapter(SerialCommLineViewModel serialCommLineViewModel, FragmentManager fragmentManager)
		{
		this.serialCommLineViewModel = serialCommLineViewModel;
		this.fragmentManager = fragmentManager;
		}

	@NonNull
	@Override
	public MBDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
		{
		// Here we inflate the corresponding
		// layout of the parent item
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_item, viewGroup, false);
		return new MBDeviceViewHolder(view);
		}

	@Override
	public void onBindViewHolder(@NonNull MBDeviceViewHolder mbDeviceViewHolder, int position)
		{

		// Create an instance of the ParentItem
		// class for the given position
		SlaveDeviceViewModel deviceItem = serialCommLineViewModel.slaveDeviceViewModels.get(position);

		// For the created instance,
		// get the title and set it
		// as the text for the TextView
		mbDeviceViewHolder.tvDeviceName.setText(deviceItem.getDeviceName() + " ID:" + deviceItem.getSlaveID());

		InflateState inflateState = deviceItem.getInflateState();
		mbDeviceViewHolder.expandingArea.setVisibility(inflateState == InflateState.INFLATED ? View.VISIBLE : View.GONE);
		mbDeviceViewHolder.expandButton.setState(inflateState);
		mbDeviceViewHolder.expandButton.setClickListener(null);
		mbDeviceViewHolder.expandButton.setClickListener(state ->
			 {
			 mbDeviceViewHolder.expandingArea.setVisibility((state == InflateState.DEFLATED) ? View.GONE : View.VISIBLE);
			 serialCommLineViewModel.slaveDeviceViewModels.get(mbDeviceViewHolder.getAdapterPosition()).setInflateState(state);
			 });

		mbDeviceViewHolder.cbDeleteDeviceButton.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view)
				{
				int pos = mbDeviceViewHolder.getAdapterPosition();
				serialCommLineViewModel.slaveDeviceViewModels.remove(pos);
				notifyItemRemoved(pos);
				return true;
				}
		});

		mbDeviceViewHolder.cbDeviceProperties.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view)
				{
				int pos = mbDeviceViewHolder.getAdapterPosition();
				Bundle args = new Bundle();
				args.putString("TITLE", "Параметры устройства");
				args.putInt(MBDeviceParametersDialog.INDEX, pos);
				args.putString(MBDeviceParametersDialog.DEVICE_NAME, serialCommLineViewModel.slaveDeviceViewModels.get(pos).getDeviceName());
				args.putInt(MBDeviceParametersDialog.DEVICE_ID, serialCommLineViewModel.slaveDeviceViewModels.get(pos).getSlaveID());
				MBDeviceParametersDialog mbDialog = new MBDeviceParametersDialog();
				mbDialog.setArguments(args);
				mbDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_HornModbusTool);
				mbDialog.show(fragmentManager, MBDeviceParametersDialog.class.getName());
				return true;
				}
		});


		mbDeviceViewHolder.cbAddMDOButton.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view)
				{
				int pos = mbDeviceViewHolder.getAdapterPosition();
				Bundle args = new Bundle();
				args.putString("TITLE", "Новый ОДМ");
				args.putInt(MDOConstructorDialog.DEVICE_INDEX, pos);
				MDOConstructorDialog dialog = new MDOConstructorDialog();
				dialog.setArguments(args);
				dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_HornModbusTool);
				dialog.show(fragmentManager, MDOConstructorDialog.class.getName());
				return true;
				}
		});
		// Create a layout manager
		// to assign a layout
		// to the RecyclerView.

		// Here we have assigned the layout
		// as LinearLayout with vertical orientation
		LinearLayoutManager layoutManager= new LinearLayoutManager(
				mbDeviceViewHolder.rvMDOContainer.getContext(),
				LinearLayoutManager.VERTICAL,
				false);

		// Since this is a nested layout, so
		// to define how many child items
		// should be prefetched when the
		// child RecyclerView is nested
		// inside the parent RecyclerView,
		// we use the following method
		layoutManager.setInitialPrefetchItemCount(deviceItem.modbusDataObjectViewModels.size());

		// Create an instance of the child
		// item view adapter and set its
		// adapter, layout manager and RecyclerViewPool
		MBSlaveDeviceAdapter MBSlaveDeviceAdapter = new MBSlaveDeviceAdapter(deviceItem, mbDeviceViewHolder, fragmentManager);
		mbDeviceViewHolder.rvMDOContainer.setLayoutManager(layoutManager);
		mbDeviceViewHolder.rvMDOContainer.setAdapter(MBSlaveDeviceAdapter);
		//mbDeviceViewHolder.rvMDOContainer.setRecycledViewPool(viewPool);
		}


	@Override
	public int getItemCount()
		{
		return serialCommLineViewModel.slaveDeviceViewModels.size();
		}

	public interface SlaveDeviceParametersEditListener
		{
		void onMBDeviceParametersEdit(int deviceIndex, String deviceName, int deviceID);
		}

	}