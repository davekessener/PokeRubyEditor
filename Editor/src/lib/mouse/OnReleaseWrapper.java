package lib.mouse;

import javafx.scene.input.MouseEvent;
import lib.misc.Vec2;

public class OnReleaseWrapper implements MouseHandler
{
	private final MouseHandler mSuper;
	private final Runnable mOnReleased;
	
	public OnReleaseWrapper(MouseHandler m, Runnable r)
	{
		mSuper = m;
		mOnReleased = r;
	}

	@Override
	public void onPressed(MouseEvent e, Vec2 p)
	{
		mSuper.onPressed(e, p);
	}

	@Override
	public void onDragged(MouseEvent e, Vec2 p)
	{
		mSuper.onDragged(e, p);
	}

	@Override
	public void onReleased(MouseEvent e)
	{
		mSuper.onReleased(e);
		
		mOnReleased.run();
	}
}
