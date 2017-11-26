package lib.observe;

import lib.misc.Vec2;
import model.layer.Layer;

public class ObservableLayer extends BasicObservable implements Layer
{
	private final Layer mSuper;
	
	public ObservableLayer(Layer l)
	{
		mSuper = l;
	}

	@Override
	public Vec2 dimension()
	{
		return mSuper.dimension();
	}

	@Override
	public String get(Vec2 p)
	{
		return mSuper.get(p);
	}

	@Override
	public void set(Vec2 p, String id)
	{
		mSuper.set(p, id);
		
		change();
	}

	@Override
	public void resize(Vec2 v)
	{
		mSuper.resize(v);
		
		change();
	}
}
