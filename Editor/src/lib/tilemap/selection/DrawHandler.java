package lib.tilemap.selection;

import java.util.function.Consumer;

import javafx.scene.input.MouseEvent;
import lib.misc.Producer;
import lib.misc.Rect;
import lib.misc.Vec2;
import lib.mouse.MouseHandler;

public class DrawHandler implements MouseHandler
{
	private final Consumer<Vec2> mCallback;
	private final Producer<Vec2> mSize;
	private DataBag mData;
	
	public DrawHandler(Producer<Vec2> f, Consumer<Vec2> cb)
	{
		mCallback = cb;
		mSize = f;
	}

	@Override
	public void onPressed(MouseEvent e, Vec2 p)
	{
		Vec2 s = mSize.produce();
		
		if(s.getX() > 0 && s.getY() > 0)
		{
			mData = new DataBag();
			
			mData.area = new Rect(s.getX(), s.getY());
			mData.start = p.clone();
			
			mCallback.accept(p);
		}
	}

	@Override
	public void onDragged(MouseEvent e, Vec2 p)
	{
		if(mData != null)
		{
			apply(p);
		}
	}

	@Override
	public void onReleased(MouseEvent e)
	{
		mData = null;
	}
	
	private void apply(Vec2 p)
	{
		int dx = p.getX() - mData.start.getX();
		int dy = p.getY() - mData.start.getY();
		
		dx -= Math.floorMod(dx, mData.area.getWidth());
		dy -= Math.floorMod(dy, mData.area.getHeight());
		
		mCallback.accept(mData.start.add(new Vec2(dx, dy)));
	}
	
	private static class DataBag
	{
		public Rect area;
		public Vec2 start;
	}
}
