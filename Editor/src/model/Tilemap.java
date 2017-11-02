package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Tilemap implements JsonModel
{
	private String sTileset;
	private int iWidth;
	private int iHeight;
	private final java.util.Map<String, List<Layer>> mLayers = new HashMap<>();
	private Layer mMeta;
	
	public String getTilesetID() { return sTileset; }
	public int getWidth() { return iWidth; }
	public int getHeight() { return iHeight; }
	public Layer getMeta() { return mMeta; }

	public void setTilesetID(String id) { sTileset = id; }
	public void setWidth(int w) { iWidth = w; }
	public void setHeight(int h) { iHeight = h; }
	public void setMeta(Layer l) { mMeta = l; }
	
	public List<Layer> getLayers(String id)
	{
		List<Layer> l = mLayers.get(id);
		
		if(l == null)
		{
			mLayers.put(id, l = new ArrayList<>());
		}
		
		return l;
	}
	
	public void resize(int w, int h)
	{
		iWidth = w;
		iHeight = h;
		
		for(List<Layer> ls : mLayers.values())
		{
			for(Layer l : ls)
			{
				l.resize(w, h);
			}
		}
		
		mMeta.resize(w, h);
		
		for(int y = 0 ; y < h ; ++y)
		{
			for(int x = 0 ; x < w ; ++x)
			{
				if(mMeta.get(x, y) == null)
				{
					mMeta.set(x, y, "wall");
				}
			}
		}
	}
	
	@Override
	public void load(JsonValue value)
	{
		JsonObject tag = value.asObject();
		JsonObject map = tag.get("map").asObject();
		
		sTileset = tag.getString("tileset", null);
		iWidth = tag.getInt("width", 0);
		iHeight = tag.getInt("height", 0);
		
		mLayers.clear();
		
		for(String d : LAYERS)
		{
			if(map.get(d) != null)
			{
				List<Layer> ls = new ArrayList<>();
				
				for(JsonValue v : map.get(d).asArray())
				{
					ls.add(loadLayer(v));
				}
				
				mLayers.put(d, ls);
			}
		}
		
		mMeta = loadLayer(tag.get("meta"));
	}

	@Override
	public JsonValue save()
	{
		JsonObject tag = new JsonObject();
		JsonObject map = new JsonObject();
		
		tag.add("tileset", sTileset);
		tag.add("width", iWidth);
		tag.add("height", iHeight);
		
		for(Entry<String, List<Layer>> e : mLayers.entrySet())
		{
			JsonArray layer = new JsonArray();
			
			for(Layer l : e.getValue())
			{
				layer.add(l.save());
			}
			
			if(!e.getValue().isEmpty())
			{
				map.add(e.getKey(), layer);
			}
		}
		
		tag.add("map", map);
		tag.add("meta", mMeta.save());
		
		return tag;
	}
	
	private Layer loadLayer(JsonValue v)
	{
		Layer l = new Layer(iWidth, iHeight);
		
		l.load(v);
		
		return l;
	}
	
	public static final String[] LAYERS = new String[] { "bottom", "top" };
}
