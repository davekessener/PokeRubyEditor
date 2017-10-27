package model;

import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Tileset implements JsonModel
{
	private int iSize;
	private String sSource;
	private final List<Tile> aTiles = new ArrayList<>();
	
	public int getSize() { return iSize; }
	public String getSource() { return sSource; }
	
	public void setSize(int s) { iSize = s; }
	public void setSource(String s) { sSource = s; }
	
	public int getTileCount() { return aTiles.size(); }
	public Tile getTile(int i) { return aTiles.get(i); }
	
	@Override
	public void load(JsonValue value)
	{
		JsonObject tag = value.asObject();
		
		iSize = tag.getInt("size", 0);
		sSource = tag.getString("source", null);
		
		aTiles.clear();
		for(JsonValue v : tag.get("tiles").asArray())
		{
			aTiles.add(loadTile(v.asObject()));
		}
	}

	@Override
	public JsonValue save()
	{
		JsonObject tag = new JsonObject();
		JsonArray tiles = new JsonArray();
		
		tag.add("size", iSize);
		tag.add("source", sSource);
		
		for(Tile t : aTiles)
		{
			tiles.add(t.save());
		}
		
		tag.add("tiles", tiles);
		
		return tag;
	}
	
	private static Tile loadTile(JsonObject tag)
	{
		Tile t = tag.get("at") == null ? new AnimatedTile() : new StaticTile();
		
		t.load(tag);
		
		return t;
	}
	
	public abstract static class Tile implements JsonModel
	{
		private String sID;
		
		public String getID() { return sID; }
		public void setID(String id) { sID = id; }
		
		public abstract Vec2 getPosition();
		
		@Override
		public void load(JsonValue value)
		{
			JsonObject tag = value.asObject();
			
			sID = tag.getString("id", null);
		}
		
		@Override
		public JsonValue save()
		{
			return new JsonObject().add("id", sID);
		}
	}
	
	public static class StaticTile extends Tile
	{
		private Vec2 vPosition;

		@Override
		public Vec2 getPosition()
		{
			return vPosition;
		}
		
		public void setPosition(Vec2 p)
		{
			vPosition = p;
		}
		
		@Override
		public void load(JsonValue value)
		{
			super.load(value);
			
			vPosition = new Vec2(0, 0);
			vPosition.load(value.asObject().get("at"));
		}
		
		@Override
		public JsonValue save()
		{
			return super.save().asObject().add("at", vPosition.save());
		}
	}
	
	public static class AnimatedTile extends Tile
	{
		private int iPeriod;
		private final List<Vec2> aFrames = new ArrayList<>();

		public int getPeriod() { return iPeriod; }
		public void setPeriod(int ms) { iPeriod = ms; }
		
		public int getFrameCount() { return aFrames.size(); }
		public Vec2 getFrame(int i) { return aFrames.get(i); }
		public void setFrame(int i, Vec2 v) { aFrames.set(i, v); }
		public void addFrame(int i, Vec2 v) { aFrames.add(i, v); }
		public void removeFrame(int i) { aFrames.remove(i); }
		
		@Override
		public Vec2 getPosition()
		{
			return aFrames.get(0);
		}
		
		@Override
		public void load(JsonValue value)
		{
			JsonObject tag = value.asObject();
			
			super.load(value);
			
			iPeriod = tag.getInt("period", 0);
			
			aFrames.clear();
			for(JsonValue position : tag.get("frames").asArray())
			{
				Vec2 v = new Vec2(0, 0);
				v.load(position.asObject().get("at"));
				aFrames.add(v);
			}
		}
		
		@Override
		public JsonValue save()
		{
			JsonObject tag = super.save().asObject();
			JsonArray frames = new JsonArray();
			
			tag.add("period", iPeriod);
			
			for(Vec2 v : aFrames)
			{
				frames.add(new JsonObject().add("at", v.save()));
			}
			
			tag.add("frames", frames);
			
			return tag;
		}
	}
}
