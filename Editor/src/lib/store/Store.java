package lib.store;

public interface Store<T> extends Iterable<T>
{
	public abstract int size();
	public abstract T get(int idx);
}
