package model;

import java.util.function.Function;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import lib.misc.Rect;
import lib.misc.Vec2;

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
	
	public static JsonValue SaveStringMatrix(int w, int h, String[][] s)
	{
		return SaveStringMatrix(new Rect(w, h), p -> s[p.getX()][p.getY()]);
	}
	
	public static JsonValue SaveStringMatrix(Rect r, Function<Vec2, String> f)
	{
		JsonArray tag = new JsonArray();
		
		for(int y : r.Y())
		{
			JsonArray row = new JsonArray();
			
			for(int x : r.X())
			{
				row.add(f.apply(new Vec2(x, y)));
			}
			
			tag.add(row);
		}
		
		return tag;
	}
	
	private JsonUtils() { }
}
