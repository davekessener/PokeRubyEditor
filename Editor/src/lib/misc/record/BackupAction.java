package lib.misc.record;

import lib.action.Action;

public class BackupAction implements Action
{
	private final Backupable mObject;
	private final Object mBackup, mFinal;
	
	public BackupAction(Backupable o, Object b, Object f)
	{
		mObject = o;
		mBackup = b;
		mFinal = f;
	}

	@Override
	public void undo()
	{
		mObject.restore(mBackup);
	}

	@Override
	public void redo()
	{
		mObject.restore(mFinal);
	}
}
