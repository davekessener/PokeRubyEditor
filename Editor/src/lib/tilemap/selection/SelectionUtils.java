package lib.tilemap.selection;

import lib.misc.Rect;
import lib.misc.Vec2;
import model.layer.Layer;
import model.layer.ReadOnlyLayer;

public class SelectionUtils
{
	public static void applyOnLayer(ReadOnlyLayer source, Layer destination, Vec2 offset)
	{
		Vec2 src = source.dimension(), dst = destination.dimension();
		Rect t = new Rect(dst.getX(), dst.getY());
		
		for(Vec2 v : new Rect(src.getX(), src.getY()))
		{
			Vec2 p = v.add(offset);
			
			if(t.contains(p))
			{
				destination.set(p, source.get(v));
			}
		}
	}
	
	private SelectionUtils() { }
}
