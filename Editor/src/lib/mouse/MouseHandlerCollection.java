package lib.mouse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import lib.misc.Vec2;

public class MouseHandlerCollection implements MouseHandler
{
	private final List<Pair<Predicate<MouseEvent>, MouseHandler>> mHandlers;
	private final OnUnknownHandler mOnUnknown;
	private MouseHandler mCurrentHandler;
	
	public MouseHandlerCollection() { this(DEFAULT); }
	public MouseHandlerCollection(OnUnknownHandler h)
	{
		mHandlers = new ArrayList<>();
		mOnUnknown = h;
	}
	
	public MouseHandlerCollection add(Predicate<MouseEvent> f, MouseHandler h)
	{
		mHandlers.add(new Pair<>(f, h));
		
		return this;
	}

	@Override
	public void onPressed(MouseEvent me, Vec2 p)
	{
		if(mCurrentHandler != null) return;
		
		for(Pair<Predicate<MouseEvent>, MouseHandler> e : mHandlers)
		{
			if(e.getKey().test(me))
			{
				mCurrentHandler = e.getValue();
				
				break;
			}
		}
		
		if(mCurrentHandler != null)
		{
			mCurrentHandler.onPressed(me, p);
		}
		else
		{
			mOnUnknown.handle(me, p);
		}
	}

	@Override
	public void onDragged(MouseEvent e, Vec2 p)
	{
		if(mCurrentHandler != null)
		{
			mCurrentHandler.onDragged(e, p);
		}
	}

	@Override
	public void onReleased(MouseEvent e)
	{
		if(mCurrentHandler != null)
		{
			mCurrentHandler.onReleased(e);
			
			mCurrentHandler = null;
		}
	}
	
	public static interface OnUnknownHandler { public abstract void handle(MouseEvent e, Vec2 p); }
	
	public static final OnUnknownHandler DEFAULT = (e, p) -> { };
}
