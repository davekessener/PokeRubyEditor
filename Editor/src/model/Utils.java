package model;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class Utils
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
				a[x][y] = row.get(x).asString();
			}
		}
		
		return a;
	}
	
	public static JsonValue SaveStringMatrix(int w, int h, String[][] a)
	{
		JsonArray v = new JsonArray();
		
		for(int y = 0 ; y < h ; ++y)
		{
			JsonArray row = new JsonArray();
			
			for(int x = 0 ; x < w ; ++x)
			{
				row.add(a[x][y]);
			}
			
			v.add(row);
		}
		
		return v;
	}
	
	private Utils( ) { }
}
