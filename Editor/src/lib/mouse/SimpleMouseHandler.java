package lib.mouse;

import java.util.function.BiConsumer;

import javafx.scene.input.MouseEvent;
import lib.misc.Vec2;

public class SimpleMouseHandler implements MouseHandler
{
	private final BiConsumer<MouseEvent, Vec2> mHandler;
	
	public SimpleMouseHandler(BiConsumer<MouseEvent, Vec2> h)
	{
		mHandler = h;
	}

	@Override
	public void onPressed(MouseEvent e, Vec2 p)
	{
		mHandler.accept(e, p);
	}

	@Override
	public void onDragged(MouseEvent e, Vec2 p)
	{
		mHandler.accept(e, p);
	}

	@Override
	public void onReleased(MouseEvent e)
	{
	}
}
