package lib.misc.record;

import lib.action.Action;

public class BackupRecorder implements Recordable
{
	private final Backupable mObject;
	private Object mBackup;
	
	public BackupRecorder(Backupable b)
	{
		mObject = b;
	}

	@Override
	public void start()
	{
		mBackup = mObject.backup();
	}

	@Override
	public Action stop()
	{
		return new BackupAction(mObject, mBackup, mObject.backup());
	}
}
