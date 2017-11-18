package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Map implements JsonModel
{
	private String sName;
	private String sTilemap;
	private String sBorder;
	private final java.util.Map<Direction, Neighbor> mNeighbors = new HashMap<>();
	private final List<Event> mEntities = new ArrayList<>();
	
	public String getName() { return sName; }
	public void setName(String name) { sName = name; }
	public String getTilemapID() { return sTilemap; }
	public void setTilemapID(String id) { sTilemap = id; }
	public String getBorderID() { return sBorder; }
	public void setBorderID(String id) { sBorder = id; }
	public java.util.Map<Direction, Neighbor> getNeighbors() { return mNeighbors; }
	public List<Event> getEvents() { return mEntities; }
	
	@Override
	public void load(JsonValue value)
	{
		JsonObject tag = value.asObject();
		JsonValue vneighbors = tag.get("neighbors");
		
		sName = tag.getString("name", null);
		sTilemap = tag.getString("map", null);
		sBorder = tag.getString("border", null);
		
		if(vneighbors != null)
		{
			JsonObject ns = vneighbors.asObject();
			
			for(Direction d : Direction.values())
			{
				JsonValue v = ns.get(d.literal);
				
				if(v != null)
				{
					Neighbor n = new Neighbor();
					n.load(v);
					mNeighbors.put(d, n);
				}
			}
		}
		
		mEntities.clear();
		JsonValue entities = tag.get("entities");
		if(entities != null) for(JsonValue v : entities.asArray())
		{
			Event e = new Event();
			e.load(v);
			mEntities.add(e);
		}
	}

	@Override
	public JsonValue save()
	{
		JsonObject tag = new JsonObject();
		
		tag.add("name", sName);
		tag.add("map", sTilemap);
		tag.add("border", sBorder);
		
		if(!mNeighbors.isEmpty())
		{
			JsonObject ns = new JsonObject();
			
			for(Entry<Direction, Neighbor> e : mNeighbors.entrySet())
			{
				ns.add(e.getKey().literal, e.getValue().save());
			}
			
			tag.add("neighbors", ns);
		}
		
		JsonArray entities = new JsonArray();
		for(final Event e : mEntities)
		{
			entities.add(e.save());
		}
		tag.add("entities", entities);
		
		return tag;
	}
	
	public static class Neighbor implements JsonModel
	{
		private String mMapID;
		private int mOffset;
		
		public Neighbor() { this(null, -1); }
		public Neighbor(String id, int o) { mMapID = id; mOffset = o; }
		
		public String getMapID() { return mMapID; }
		public int getOffset() { return mOffset; }

		@Override
		public void load(JsonValue value)
		{
			JsonObject tag = value.asObject();
			
			mMapID = tag.getString("id", null);
			mOffset = tag.getInt("offset", 0);
		}

		@Override
		public JsonValue save()
		{
			JsonObject tag = new JsonObject();
			
			tag.add("id", mMapID);
			tag.add("offset", mOffset);
			
			return tag;
		}
	}
}
