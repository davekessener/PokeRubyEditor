package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import lib.misc.Rect;
import lib.misc.Vec2;
import model.layer.Layer;

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
	
	public void resize(Vec2 v)
	{
		iWidth = v.getX();
		iHeight = v.getY();
		
		for(List<Layer> ls : mLayers.values())
		{
			for(Layer l : ls)
			{
				l.resize(v);
			}
		}
		
		mMeta.resize(v);
		
		for(Vec2 p : new Rect(iWidth, iHeight))
		{
			if(mMeta.get(p) == null)
			{
				mMeta.set(p, "wall");
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
		
		for(String d : TYPES)
		{
			if(map.get(d) != null)
			{
				List<Layer> ls = new ArrayList<>();
				
				for(JsonValue v : map.get(d).asArray())
				{
					ls.add(JsonUtils.LoadLayer(iWidth, iHeight, v));
				}
				
				mLayers.put(d, ls);
			}
		}
		
		mMeta = JsonUtils.LoadLayer(iWidth, iHeight, tag.get("meta"));
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
				layer.add(JsonUtils.SaveLayer(l));
			}
			
			if(!e.getValue().isEmpty())
			{
				map.add(e.getKey(), layer);
			}
		}
		
		tag.add("map", map);
		tag.add("meta", JsonUtils.SaveLayer(mMeta));
		
		return tag;
	}
	
	public static final String[] TYPES = new String[] { "bottom", "top" };
}
