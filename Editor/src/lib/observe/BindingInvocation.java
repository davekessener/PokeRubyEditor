package lib.observe;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class BindingInvocation implements InvocationHandler
{
	private final IInvocationCollection mInvocations;
	private final InvocationHandler mOnUnknown;
	
	public BindingInvocation(IInvocationCollection invs) { this(invs, DEFAULT_ONUNKNOWN); }
	public BindingInvocation(IInvocationCollection invs, InvocationHandler unknown)
	{
		mInvocations = invs;
		mOnUnknown = unknown;
	}

	@Override
	public Object invoke(Object x, Method m, Object[] a) throws Throwable
	{
		InvocationHandler h = mInvocations.getHandler(x, m, a);
		
		if(h != null)
		{
			return h.invoke(x, m, a);
		}
		else
		{
			return mOnUnknown.invoke(x, m, a);
		}
	}
	
	private static final InvocationHandler DEFAULT_ONUNKNOWN = 
			(x, m, a) -> { throw new RuntimeException("Called method " + m.getName() + " on object of type " + x.getClass().getName()); };
}
