package lib.mouse;

import javafx.scene.input.MouseEvent;
import lib.misc.Producer;
import lib.misc.Vec2;

public class ConditionalWrapper implements MouseHandler
{
	private final MouseHandler mSuper;
	private final Producer<Boolean> mCondition;
	private boolean mRunning;
	
	public ConditionalWrapper(MouseHandler m, Producer<Boolean> f)
	{
		mSuper = m;
		mCondition = f;
	}

	@Override
	public void onPressed(MouseEvent e, Vec2 p)
	{
		if(mRunning = mCondition.produce())
		{
			mSuper.onPressed(e, p);
		}
	}

	@Override
	public void onDragged(MouseEvent e, Vec2 p)
	{
		if(mRunning)
		{
			mSuper.onDragged(e, p);
		}
	}

	@Override
	public void onReleased(MouseEvent e)
	{
		if(mRunning)
		{
			mSuper.onReleased(e);
			
			mRunning = false;
		}
	}
}
