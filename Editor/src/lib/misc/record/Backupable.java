package lib.misc.record;

public interface Backupable
{
	public Object backup();
	public void restore(Object o);
}
