package lib.store;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import lib.misc.Producer;

public class BasicStore<T> implements Store<T>
{
	private final Function<Integer, T> mAcquire;
	private final Producer<Integer> mSize;
	
	public BasicStore(Function<Integer, T> f, Producer<Integer> s)
	{
		mAcquire = f;
		mSize = s;
	}
	
	public static <T> Store<T> Instantiate(List<? extends T> c)
	{
		return new BasicStore<T>(idx -> c.get(idx), () -> c.size());
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return new StoreIterator();
	}

	@Override
	public int size()
	{
		return mSize.produce();
	}

	@Override
	public T get(int idx)
	{
		return mAcquire.apply(idx);
	}

	private class StoreIterator implements Iterator<T>
	{
		private int i = 0;

		@Override
		public boolean hasNext()
		{
			return i < mSize.produce();
		}

		@Override
		public T next()
		{
			return mAcquire.apply(i++);
		}
	}
}
