package lib.action;

public interface IActionManager
{
	public void addAction(Action a);
	public void clear();
	public boolean canUndo();
	public boolean canRedo();
	public void undo();
	public void redo();
}
