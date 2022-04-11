package com.basisdas.hornModbusTool.views;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.datamodels.SlaveDevice;
import com.basisdas.hornModbusTool.datamodels.SerialCommunicationLine;
import com.basisdas.hornModbusTool.misc.InflateState;
import com.basisdas.hornModbusTool.views.custom.ExpandButton;

public class MBSlaveDevicePoolAdapter extends RecyclerView.Adapter<MBDeviceViewHolder>
	{

	// An object of RecyclerView.RecycledViewPool
	// is created to share the Views
	// between the child and
	// the parent RecyclerViews
	//private RecyclerView.RecycledViewPool viewPool	= new RecyclerView.RecycledViewPool();
	private SerialCommunicationLine slaveDevicePool;

	MBSlaveDevicePoolAdapter(SerialCommunicationLine slaveDevicePool)
		{
		this.slaveDevicePool = slaveDevicePool;
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
		SlaveDevice deviceItem = slaveDevicePool.getDevice(position);

		// For the created instance,
		// get the title and set it
		// as the text for the TextView
		mbDeviceViewHolder.tvDeviceName.setText(deviceItem.getDeviceName() + " ID:" + deviceItem.getSlaveID());
		mbDeviceViewHolder.expandingArea.setVisibility(
				mbDeviceViewHolder.expandButton.getState() == InflateState.INFLATED ? View.VISIBLE : View.GONE);
		mbDeviceViewHolder.expandButton.setClickListener(null);


		mbDeviceViewHolder.expandButton.setClickListener(state ->
			mbDeviceViewHolder.expandingArea.setVisibility((state == InflateState.DEFLATED) ? View.GONE : View.VISIBLE));


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
		layoutManager.setInitialPrefetchItemCount(deviceItem.getMDOCount());

		// Create an instance of the child
		// item view adapter and set its
		// adapter, layout manager and RecyclerViewPool
		MBSlaveDeviceAdapter MBSlaveDeviceAdapter = new MBSlaveDeviceAdapter(deviceItem);
		mbDeviceViewHolder.rvMDOContainer.setLayoutManager(layoutManager);
		mbDeviceViewHolder.rvMDOContainer.setAdapter(MBSlaveDeviceAdapter);
		//mbDeviceViewHolder.rvMDOContainer.setRecycledViewPool(viewPool);
		}


	@Override
	public int getItemCount()
		{
		return slaveDevicePool.getDevicesCount();
		}

	}