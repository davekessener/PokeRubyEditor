package lib.action;

public class BasicAction implements Action
{
	private final Runnable mUndo, mRedo;
	
	public BasicAction(Runnable undo, Runnable redo)
	{
		mUndo = undo;
		mRedo = redo;
	}

	@Override
	public void undo()
	{
		mUndo.run();
	}

	@Override
	public void redo()
	{
		mRedo.run();
	}
}
