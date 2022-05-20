package com.basisdas.hornModbusTool.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.basisdas.hornModbusTool.R;
import com.basisdas.hornModbusTool.misc.EntityState;
import com.basisdas.hornModbusTool.misc.EntitySubState;

public class EntityStateButton extends CircleButton
	{


	public interface ClickListener
		{
			void onStateChanged(EntityState state);
		}

	private EntityState mState;
	private EntitySubState mSubstate = EntitySubState.UNKNOWN;
	private ClickListener mClickListener;

	public EntityStateButton(Context context, AttributeSet attrs)
		{
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.EntityStateButton,
				0, 0);

		EntityState state;
		EntitySubState subState;
		try
			{
			state = EntityState.values()[a.getInteger(R.styleable.EntityStateButton_entityState, 1)];
			subState = EntitySubState.values()[a.getInteger(R.styleable.EntityStateButton_entitySubState, 0)];
			}
		finally { a.recycle(); }
		setState(state);
		setSubState(subState);
		}



	@Override
	public boolean performClick()
		{
		super.performClick();
		int next = ((mState.ordinal() + 1) % mState.values().length);
		setState(EntityState.values()[next]);
		performEntityStateButtonClick();
		return true;
		}


	private void performEntityStateButtonClick()
		{
		if (mClickListener == null) return;
		mClickListener.onStateChanged(mState);
		}

	private void createDrawableState()
		{
		switch (mState)
			{
			case ACTIVE:
				switch (mSubstate)
					{
					case UNKNOWN:
						setImageResource(R.drawable.circle_yellow_48);
					break;
					case ERROR:
						setImageResource(R.drawable.circle_red_48);
					break;
					case GOOD:
						setImageResource(R.drawable.circle_green_48);
					break;
					}
				break;
			case INACTIVE:
				setImageResource(R.drawable.circle_grey_48);
				break;
			}
		}


	public EntityState getState()
		{
		return mState;
		}

	public void setState(EntityState state)
		{
		if (state == null) return;
		/*if (mState == EntityState.ACTIVE && state == EntityState.INACTIVE)
			mSubstate = EntitySubState.UNKNOWN;*/
		this.mState = state;
		createDrawableState();
		}

	public void setSubState(EntitySubState substate)
		{
		if (substate == null) return;
		if (mState == EntityState.INACTIVE)
			{
			this.mSubstate = EntitySubState.UNKNOWN;
			return;
			}
		this.mSubstate = substate;
		createDrawableState();
		}

	public EntitySubState getSubstate()
		{
		return mSubstate;
		}

	public ClickListener getClickListener()
		{
		return mClickListener;
		}

	public void removeClickListener()
		{
		mClickListener = null;
		}

	public void setClickListener(ClickListener clickListener)
		{
		this.mClickListener = clickListener;
		}



	}