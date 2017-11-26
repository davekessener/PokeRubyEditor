package lib;

import java.util.function.Consumer;

public class MemoryConsumer<T> implements Consumer<T>
{
	private final Consumer<T> mSuper;
	private T mOld;
	
	public MemoryConsumer(Consumer<T> s)
	{
		mSuper = s;
	}

	@Override
	public void accept(T t)
	{
		if(mOld == null || !mOld.equals(t))
		{
			mSuper.accept(mOld = t);
		}
	}
}
