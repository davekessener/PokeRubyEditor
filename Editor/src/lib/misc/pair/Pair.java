package lib.misc.pair;

public interface Pair<T1, T2> extends ReadOnlyPair<T1, T2>
{
	public abstract void setFirst(T1 v);
	public abstract void setSecond(T2 v);
}
