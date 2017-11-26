package lib.action;

import java.util.LinkedList;

import lib.Utils;

public class ActionBuilder
{
	private LinkedList<Action> mActions;
	
	public ActionBuilder()
	{
		mActions = new LinkedList<>();
	}
	
	public void add(Action a)
	{
		mActions.add(a);
	}
	
	public void clear()
	{
		mActions.clear();
	}
	
	public Action build()
	{
		return Utils.with(new ConstructedAction(mActions), a -> mActions = new LinkedList<>());
	}
	
	private static class ConstructedAction implements Action
	{
		private final LinkedList<Action> mActions;
		
		public ConstructedAction(LinkedList<Action> a)
		{
			mActions = a;
		}

		@Override
		public void undo()
		{
			mActions.descendingIterator().forEachRemaining(a -> a.undo());
		}

		@Override
		public void redo()
		{
			mActions.forEach(a -> a.redo());
		}
	}
}
