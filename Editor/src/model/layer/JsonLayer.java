package model.layer;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import lib.misc.Rect;
import lib.misc.Vec2;
import model.JsonModel;
import model.JsonUtils;

public class JsonLayer extends BasicLayer implements JsonModel
{
	public JsonLayer() { this(Vec2.ORIGIN); }
	public JsonLayer(Vec2 s)
	{
		super(s);
	}

	@Override
	public void load(JsonValue value)
	{
		suspend();
		
		int w = dimension().getX(), h = dimension().getY();
		String[][] d = JsonUtils.LoadStringMatrix(w, h, value);
		
		for(Vec2 p : new Rect(w, h))
		{
			set(p, d[p.getX()][p.getY()]);
		}
		
		resume();
		change();
	}

	@Override
	public JsonValue save()
	{
		Rect r = new Rect(dimension().getX(), dimension().getY());
		JsonArray tag = new JsonArray();
		
		for(int y : r.Y())
		{
			JsonArray row = new JsonArray();
			
			for(int x : r.X())
			{
				row.add(get(new Vec2(x, y)));
			}
			
			tag.add(row);
		}
		
		return tag;
	}
}
