package lib.mouse;

import java.util.function.Consumer;

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
		
		Utils.findAllAdjacent(p, new Rect(l.dimension()), v -> l.get(v)).forEach(v -> f.accept(v));
	}
}
