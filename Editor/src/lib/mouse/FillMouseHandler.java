package lib.mouse;

import java.util.function.Consumer;
import java.util.function.Function;

import lib.Utils;
import lib.misc.Producer;
import lib.misc.Rect;
import lib.misc.Vec2;
import model.layer.ReadOnlyLayer;

public class FillMouseHandler extends SingleMouseHandler
{
	public FillMouseHandler(Producer<ReadOnlyLayer> l, Consumer<Vec2> f)
	{
		super((e, p) -> Fill(p, l, f));
	}
	
	private static void Fill(Vec2 p, Producer<ReadOnlyLayer> lf, Consumer<Vec2> f)
	{
		ReadOnlyLayer l = lf.produce();
		Rect r = new Rect(l.dimension());
		Function<Vec2, String> get = v -> r.contains(v) ? l.get(v) : null;
		
		Utils.findAllAdjacent(p, get).forEach(v -> f.accept(v));
	}
}
