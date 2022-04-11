package com.basisdas.hornModbusTool.viewmodels;

import com.basisdas.hornModbusTool.misc.EntityState;
import com.basisdas.hornModbusTool.misc.EntitySubState;
import com.basisdas.hornModbusTool.misc.InflateState;
import com.basisdas.hornModbusTool.views.ISerialCommLineView;

/*Interface that ViewModel provides to View */

public interface ISerialCommLineViewModel
	{

	void bindView(ISerialCommLineView view);
	void unbindView();

	void deflateAll();

	void deflate();
	void inflate();
	InflateState getInflateState();

	EntityState getState();
	void setState(EntityState state);

	EntitySubState getEntitySubState();
	void setEntitySubState(EntitySubState subState);

	}
