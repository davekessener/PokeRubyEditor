package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import lib.misc.Vec2;
import lib.observe.ObservableMap;

public class Tileset implements JsonModel
{
	private int iSize;
	private String sSource;
	private final ObservableMap<String, Tile> mTiles = ObservableMap.Instantiate(new LinkedHashMap<>());
	
	public int getSize() { return iSize; }
	public String getSource() { return sSource; }
	
	public void setSize(int s) { iSize = s; }
	public void setSource(String s) { sSource = s; }
	
	public ObservableMap<String, Tile> getTiles() { return mTiles; }
	public void addTile(Tile t) { mTiles.put(t.getID(), t); }
	
	@Override
	public void load(JsonValue value)
	{
		JsonObject tag = value.asObject();
		
		iSize = tag.getInt("size", 0);
		sSource = tag.getString("source", null);
		
		mTiles.clear();
		for(JsonValue v : tag.get("tiles").asArray())
		{
			addTile(loadTile(v.asObject()));
		}
	}

	@Override
	public JsonValue save()
	{
		JsonObject tag = new JsonObject();
		JsonArray tiles = new JsonArray();
		
		tag.add("size", iSize);
		tag.add("source", sSource);
		
		for(Tile t : mTiles.values())
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
		public abstract Type getType();
		
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
		private String mAnimators;
		
		public StaticTile(String id, Vec2 p, String a)
		{
			setID(id);
			setPosition(p);
			setAnimators(a);
		}
		
		public StaticTile()
		{
			vPosition = new Vec2(0, 0);
		}

		@Override
		public Vec2 getPosition()
		{
			return vPosition;
		}
		
		@Override
		public Type getType()
		{
			return Type.STATIC;
		}
		
		public void setPosition(Vec2 p)
		{
			vPosition = p;
		}
		
		public String getAnimators() { return mAnimators; }
		public void setAnimators(String id) { mAnimators = id == null ? "" : id; }
		
		@Override
		public void load(JsonValue value)
		{
			super.load(value);
			
			JsonObject tag = value.asObject();
			
			vPosition.load(tag.get("at"));
			mAnimators = tag.getString("animation", "");
		}
		
		@Override
		public JsonValue save()
		{
			JsonObject tag = super.save().asObject();
			
			tag.add("at", vPosition.save());
			
			if(!mAnimators.isEmpty())
			{
				tag.add("animation", mAnimators);
			}
			
			return tag;
		}
	}
	
	public static class AnimatedTile extends Tile
	{
		private int iPeriod;
		private final List<Vec2> aFrames;
		
		public AnimatedTile() { this(null); }
		public AnimatedTile(String id)
		{
			setID(id);
			aFrames = new ArrayList<>();
			aFrames.add(new Vec2(0, 0));
			iPeriod = 1;
		}

		public int getPeriod() { return iPeriod; }
		public void setPeriod(int ms) { iPeriod = ms; }

		public List<Vec2> getFrames() { return aFrames; }
		
		@Override
		public Vec2 getPosition()
		{
			return aFrames.get(0);
		}
		
		@Override
		public Type getType()
		{
			return Type.ANIMATED;
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
	
	public static enum Type
	{
		STATIC("static"),
		ANIMATED("animated");
		
		public final String id;
		
		private Type(String id)
		{
			this.id = id;
		}
		
		@Override
		public String toString()
		{
			return id;
		}
	}
}
