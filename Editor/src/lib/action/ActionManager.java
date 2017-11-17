package lib.action;

import java.util.ArrayDeque;
import java.util.Deque;

public class ActionManager implements IActionManager
{
	private final Deque<Action> mUndoStack, mRedoStack;
	
	public ActionManager()
	{
		mUndoStack = new ArrayDeque<>();
		mRedoStack = new ArrayDeque<>();
	}
	
	@Override
	public void addAction(Action a)
	{
		mUndoStack.push(a);
		mRedoStack.clear();
	}
	
	@Override
	public void clear()
	{
		mUndoStack.clear();
		mRedoStack.clear();
	}
	
	@Override
	public boolean canUndo()
	{
		return !mUndoStack.isEmpty();
	}
	
	@Override
	public boolean canRedo()
	{
		return !mRedoStack.isEmpty();
	}
	
	@Override
	public void undo()
	{
		act(mUndoStack, mRedoStack).undo();
	}
	
	@Override
	public void redo()
	{
		act(mRedoStack, mUndoStack).redo();
	}
	
	private Action act(Deque<Action> actor, Deque<Action> actee)
	{
		Action a = actor.pop();
		
		actee.push(a);
		
		return a;
	}
}
