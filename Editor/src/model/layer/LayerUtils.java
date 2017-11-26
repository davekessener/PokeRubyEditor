package model.layer;

import lib.misc.Rect;
import lib.misc.Vec2;

public final class LayerUtils
{
	public static ReadOnlyLayerManager subManager(ReadOnlyLayerManager m, Rect t)
	{
		StaticLayerManager r = new StaticLayerManager(t.dimension());
		
		for(int i = 0 ; i < m.size() ; ++i)
		{
			r.add(subLayer(m.get(i), t));
		}
		
		return r;
	}
	
	public static Layer subLayer(ReadOnlyLayer l, Rect t)
	{
		Layer r = new BasicLayer(t.dimension());
		
		for(Vec2 p : t)
		{
			r.set(p.add(t.getOffset()), l.get(p));
		}
		
		return r;
	}
	
	private LayerUtils() { }
}
