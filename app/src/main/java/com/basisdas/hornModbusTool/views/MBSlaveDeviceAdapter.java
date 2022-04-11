package com.basisdas.hornModbusTool.views;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.datamodels.MDOParameters;
import com.basisdas.hornModbusTool.datamodels.SlaveDevice;
import com.basisdas.hornModbusTool.datamodels.ModbusDataObject;
import com.basisdas.hornModbusTool.misc.InflateState;

public class MBSlaveDeviceAdapter extends RecyclerView.Adapter<MDOViewHolder>
	{

	private SlaveDevice slaveDevice;

	// Constuctor
	MBSlaveDeviceAdapter(SlaveDevice slaveDevice)
		{
		this.slaveDevice = slaveDevice;
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
		ModbusDataObject modbusDataObject = slaveDevice.getMDO(position);
		mdoViewHolder.mdoNameNValue.setText(modbusDataObject.getName() + ": " + modbusDataObject.getValue());

		mdoViewHolder.mdoTransactionErrors.setText("");
		mdoViewHolder.mdoProperties.setText(getMDOreadableParams(modbusDataObject));

		//fixme: нужно где-то хранить состояние, иначе будет всё время сворачитаться при прокрутке
		mdoViewHolder.expandingArea.setVisibility(
				mdoViewHolder.expandButton.getState() == InflateState.INFLATED ? View.VISIBLE : View.GONE);
		mdoViewHolder.expandButton.setClickListener(null);
		mdoViewHolder.expandButton.setClickListener(state ->
															mdoViewHolder.expandingArea.setVisibility((state == InflateState.DEFLATED) ? View.GONE : View.VISIBLE));


		}

	//fixme: temporary method. must be removed
	private String getMDOreadableParams(ModbusDataObject modbusDataObject)
		{
		final String rn = "\r\n";

		StringBuilder str = new StringBuilder();
		MDOParameters params = modbusDataObject.getParams();
		str.append(params.mdoArea.name());
		str.append(rn);
		str.append("Стартовый адрес: ");
		str.append(params.startingAddress);
		str.append(rn);
		str.append("Интерпретация: ");
		str.append(params.elementType.toString());
		str.append(rn);
		str.append("Битовый размер: ");
		str.append(params.elementBitSize.getBitSize());
		str.append(rn);
		str.append("Обратный порядок регистров: ");
		str.append(params.registersSwapped);
		str.append(rn);
		str.append("Обратный порядок элементов: ");
		str.append(params.elementsReversed);
		return str.toString();
		}

	@Override
	public int getItemCount()
		{
		return slaveDevice.getMDOCount();
		}

	}
