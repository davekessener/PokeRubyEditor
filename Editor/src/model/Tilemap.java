package model;

import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Tilemap implements JsonModel
{
	private String sTileset;
	private int iWidth;
	private int iHeight;
	private List<Layer> aLayers;
	private String[][] mMeta;
	
	public String getTilesetID() { return sTileset; }
	public int getWidth() { return iWidth; }
	public int getHeight() { return iHeight; }

	public void setTilesetID(String id) { sTileset = id; }
	public void setWidth(int w) { iWidth = w; }
	public void setHeight(int h) { iHeight = h; }
	
	public String getMeta(int x, int y) { return mMeta[x][y]; }
	public void setMeta(int x, int y, String m) { mMeta[x][y] = m; }
	
	public int getLayerCount() { return aLayers.size(); }
	public Layer getLayer(int i) { return aLayers.get(i); }
	public void removeLayer(int i) { aLayers.remove(i); }

	@Override
	public void load(JsonValue value)
	{
		JsonObject tag = value.asObject();
		
		sTileset = tag.getString("tileset", null);
		iWidth = tag.getInt("width", 0);
		iHeight = tag.getInt("height", 0);
		
		aLayers = new ArrayList<>();
		
		for(JsonValue v : tag.get("map").asArray())
		{
			Layer layer = new Layer();
			
			layer.load(v);
			
			aLayers.add(layer);
		}
		
		mMeta = Utils.LoadStringMatrix(iWidth, iHeight, tag.get("meta"));
	}

	@Override
	public JsonValue save()
	{
		JsonObject tag = new JsonObject();
		
		tag.add("tileset", sTileset);
		tag.add("width", iWidth);
		tag.add("height", iHeight);
		tag.add("meta", Utils.SaveStringMatrix(iWidth, iHeight, mMeta));
		
		return tag;
	}
	
	public class Layer implements JsonModel
	{
		private String sOrder;
		private String[][] mTiles;
		
		public String getTile(int x, int y) { return mTiles[x][y]; }
		public void setTile(int x, int y, String t) { mTiles[x][y] = t; }
		
		@Override
		public void load(JsonValue value)
		{
			JsonObject tag = value.asObject();

			sOrder = tag.getString("order", null);
			mTiles = Utils.LoadStringMatrix(iWidth, iHeight, tag.get("map"));
		}

		@Override
		public JsonValue save()
		{
			JsonObject tag = new JsonObject();
			
			tag.add("order", sOrder);
			tag.add("map", Utils.SaveStringMatrix(iWidth, iHeight, mTiles));
			
			return tag;
		}
	}
}
