package lib.mouse;

import java.util.function.BiConsumer;

import javafx.scene.input.MouseEvent;
import lib.misc.Vec2;

public class SingleMouseHandler implements MouseHandler
{
	private final BiConsumer<MouseEvent, Vec2> mCallback;
	
	public SingleMouseHandler(BiConsumer<MouseEvent, Vec2> cb)
	{
		mCallback = cb;
	}
	
	@Override
	public void onPressed(MouseEvent e, Vec2 p)
	{
		mCallback.accept(e, p);
	}

	@Override
	public void onDragged(MouseEvent e, Vec2 p)
	{
	}

	@Override
	public void onReleased(MouseEvent e)
	{
	}
}
