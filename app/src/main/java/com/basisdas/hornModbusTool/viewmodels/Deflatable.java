package com.basisdas.hornModbusTool.viewmodels;

import androidx.lifecycle.ViewModel;

import com.basisdas.hornModbusTool.misc.EntityState;
import com.basisdas.hornModbusTool.misc.EntitySubState;
import com.basisdas.hornModbusTool.misc.InflateState;

abstract public class Deflatable extends ViewModel
	{

	protected InflateState inflateState = InflateState.DEFLATED;
	protected EntityState state = EntityState.ACTIVE;
	protected EntitySubState subState = EntitySubState.UNKNOWN;

	abstract protected void deflateChilds();

	public void deflateAll()
		{
		deflate();
		deflateChilds();
		}

	public void deflate()
		{
		this.inflateState = InflateState.DEFLATED;
		}

	public void inflate()
		{
		this.inflateState = InflateState.INFLATED;
		}

	public InflateState getInflateState()
		{
		return this.inflateState;
		}

	public EntityState getState()
		{
		return this.state;
		}

	public void setState(EntityState state)
		{
		this.state = state;
		}

	public EntitySubState getEntitySubState()
		{
		return this.subState;
		}

	public void setEntitySubState(EntitySubState subState)
		{
		this.subState = subState;
		}

	}
