package model.layer;

import java.util.List;

import lib.misc.Vec2;
import lib.observe.BasicObservable;
import lib.observe.Observer;
import model.Tilemap;

public class BasicLayerManager extends BasicObservable implements LayerManager
{
	private final Tilemap mTilemap;
	private final String mID;
	private final Observer mObserver;
	
	public BasicLayerManager(Tilemap tm, String id)
	{
		mTilemap = tm;
		mID = id;
		mObserver = o -> change();
	}
	
	private List<Layer> getLayers() { return mTilemap.getLayers(mID); }

	@Override
	public int size()
	{
		return getLayers().size();
	}

	@Override
	public Vec2 dimension()
	{
		return new Vec2(mTilemap.getWidth(), mTilemap.getHeight());
	}

	@Override
	public Layer get(int i)
	{
		Layer l = getLayers().get(i);
		
		l.addObserver(mObserver);
		
		return l;
	}

	@Override
	public void add(int i, Layer l)
	{
		getLayers().add(i, l);
		
		change();
	}

	@Override
	public void remove(int i)
	{
		getLayers().remove(i);
		
		change();
	}
}
