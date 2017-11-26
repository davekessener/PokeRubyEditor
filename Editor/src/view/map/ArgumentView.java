package view.map;

import java.util.function.Consumer;

import lib.action.Action;
import lib.action.BasicAction;
import view.UI;

public abstract class ArgumentView implements UI
{
	private final String mType;
	private final Consumer<Action> mOnAction;
	
	protected ArgumentView(String type, Consumer<Action> onaction)
	{
		mType = type;
		mOnAction = onaction;
	}
	
	protected void act(Runnable redo, Runnable undo)
	{
		act(new BasicAction(undo, redo));
	}
	
	protected void act(Action a)
	{
		mOnAction.accept(a);
	}
	
	public String getType()
	{
		return mType;
	}
}
