package model;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import lib.misc.Vec2;
import model.layer.BasicLayer;
import model.layer.Layer;
import model.layer.ReadOnlyLayer;

public class JsonUtils
{
	public static String[][] LoadStringMatrix(int w, int h, JsonValue json)
	{
		JsonArray tiles = json.asArray();
		String[][] a = new String[w][h];
		
		for(int y = 0 ; y < h ; ++y)
		{
			JsonArray row = tiles.get(y).asArray();
			
			for(int x = 0 ; x < w ; ++x)
			{
				JsonValue v = row.get(x);
				a[x][y] = v.isNull() ? null : v.asString();
			}
		}
		
		return a;
	}
	
	public static Layer LoadLayer(int w, int h, JsonValue json)
	{
		return new BasicLayer(new Vec2(w, h), LoadStringMatrix(w, h, json));
	}
	
	public static JsonValue SaveLayer(ReadOnlyLayer l)
	{
		JsonArray v = new JsonArray();
		Vec2 s = l.dimension();
		
		for(int y = 0 ; y < s.getY() ; ++y)
		{
			JsonArray row = new JsonArray();
			
			for(int x = 0 ; x < s.getX() ; ++x)
			{
				row.add(l.get(new Vec2(x, y)));
			}
			
			v.add(row);
		}
		
		return v;
	}
	
	private JsonUtils() { }
}
