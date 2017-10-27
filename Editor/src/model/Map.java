package model;

import java.util.HashMap;
import java.util.Map.Entry;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Map implements JsonModel
{
	private String sID;
	private String sTilemap;
	private String sBorder;
	private final Neighbors mNeighbors = new Neighbors();
	
	@Override
	public void load(JsonValue value)
	{
		JsonObject tag = value.asObject();
		sID = tag.getString("id", null);
		sTilemap = tag.getString("map", null);
		sBorder = tag.getString("border", null);
		mNeighbors.load(tag.get("neighbors"));
	}

	@Override
	public JsonValue save()
	{
		JsonObject tag = new JsonObject();
		
		tag.add("id", sID);
		tag.add("map", sTilemap);
		tag.add("border", sBorder);
		tag.add("neighbors", mNeighbors.save());
		
		return tag;
	}
	
	public static class Neighbors implements JsonModel
	{
		private final java.util.Map<Direction, String> map_ = new HashMap<>();
		
		public void setNeighbor(Direction d, String id)
		{
			map_.put(d, id);
		}
		
		public String getNeighbor(Direction d)
		{
			return map_.get(d);
		}

		@Override
		public void load(JsonValue value)
		{
			map_.clear();
			
			for(Direction d : Direction.values())
			{
				JsonValue v = value.asObject().get(d.literal);
				
				if(v != null)
				{
					setNeighbor(d, v.asString());
				}
			}
		}

		@Override
		public JsonValue save()
		{
			JsonObject tag = new JsonObject();
			
			for(Entry<Direction, String> e : map_.entrySet())
			{
				tag.add(e.getKey().literal, e.getValue());
			}
			
			return tag;
		}
	}
}
