package lib.tilemap.selection;

import java.util.function.Consumer;

import javafx.scene.input.MouseEvent;
import lib.Utils;
import lib.misc.Rect;
import lib.misc.Vec2;
import lib.mouse.MouseHandler;

public class AreaSelector implements MouseHandler
{
	private final Consumer<Rect> mSelector;
	private Vec2 mStart, mStop;
	
	public AreaSelector(Consumer<Rect> f)
	{
		mSelector = f;
	}
	
	@Override
	public void onPressed(MouseEvent e, Vec2 p)
	{
		mStart = mStop = p;
		
		trigger();
	}

	@Override
	public void onDragged(MouseEvent e, Vec2 p)
	{
		mStop = p;
		
		trigger();
	}

	@Override
	public void onReleased(MouseEvent e)
	{
		mStart = mStop = null;
	}
	
	private void trigger()
	{
		mSelector.accept(Utils.transform(new Rect(mStart, mStop), r -> new Rect(r.getStart(), r.getWidth() + 1, r.getHeight() + 1)));
	}
}
