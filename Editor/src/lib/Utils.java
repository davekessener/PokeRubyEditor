package lib;

import java.util.function.Consumer;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import controller.EditorController;
import model.JsonModel;
import model.Map;
import model.Tilemap;
import model.Tileset;

public class Utils
{
	public static <T> T with(T o, Consumer<T> f)
	{
		f.accept(o);
		
		return o;
	}
	
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
	
	public static Map loadMap(String id)
	{
		return loadJSON(new Map(), "map", id);
	}
	
	public static Tilemap loadTilemap(String id)
	{
		return loadJSON(new Tilemap(), "tilemap", id);
	}
	
	public static Tileset loadTileset(String id)
	{
		return loadJSON(new Tileset(), "tileset", id);
	}
	
	public static <T extends JsonModel> T loadJSON(T json, String type, String id)
	{
		json.load(EditorController.Instance.getLoader().loadData(type, id));
		
		return json;
	}
	
	public static String capitalize(String s)
	{
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	private Utils() { }
}
