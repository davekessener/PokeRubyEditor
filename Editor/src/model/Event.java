package model;

import java.util.Arrays;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import lib.misc.Vec2;

public class Event implements JsonModel
{
	private String mID;
	private Vec2 mLocation;
	private Argument mArgument;
	
	public Event()
	{
		mLocation = new Vec2(0, 0);
	}
	
	public String getID() { return mID; }
	public void setID(String id) { mID = id; }
	public Vec2 getLocation() { return mLocation; }
	public void setLocation(Vec2 p) { mLocation = p; }
	public Argument getArgument() { return mArgument; }
	public void setArgument(Argument a) { mArgument = a; }

	@Override
	public void load(JsonValue value)
	{
		JsonObject tag = value.asObject();
		
		mID = tag.getString("id", null);
		mLocation.load(tag.get("at"));
		
		try
		{
			mArgument = Type.valueOf(tag.getString("type", null).toUpperCase()).represent().newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		
		mArgument.load(tag.get("argument"));
	}

	@Override
	public JsonValue save()
	{
		JsonObject tag = new JsonObject();
		
		tag.add("id", mID);
		tag.add("at", mLocation.save());
		tag.add("type", mArgument.getType().toString());
		tag.add("argument", mArgument.save());
		
		return tag;
	}
	
	public static enum Type
	{
		TEXT,
		WARP,
		NPC;
		
		@SuppressWarnings("unchecked")
		public final Class<? extends Argument> represent()
		{
			return (Class<? extends Argument>) Arrays.stream(Event.class.getDeclaredClasses()).filter(
					c -> c.getSimpleName().equalsIgnoreCase(this.toString() + "Argument")
					     && Argument.class.isAssignableFrom(c)).findAny().get();
		}
		
		@Override
		public String toString()
		{
			return this.name().toLowerCase();
		}
	}
	
	public static abstract class Argument implements JsonModel
	{
		public abstract Type getType();
	}
	
	public static class TextArgument extends Argument
	{
		private String mText;
		
		public String getText() { return mText; }
		public void setText(String s) { mText = s; }
		
		@Override
		public void load(JsonValue value)
		{
			mText = value.asString();
		}

		@Override
		public JsonValue save()
		{
			return Json.value(mText);
		}

		@Override
		public Type getType()
		{
			return Type.TEXT;
		}
	}
	
	public static class WarpArgument extends Argument
	{
		private String mMap;
		private String mTarget;
		private String mDirection;
		
		public String getMapID() { return mMap; }
		public String getTargetID() { return mTarget; }
		public String getDirection() { return mDirection; }
		public void setMapID(String id) { mMap = id; }
		public void setTargetID(String id) { mTarget = id; }
		public void setDirection(String d) { mDirection = d; }
		
		@Override
		public void load(JsonValue value)
		{
			JsonObject tag = value.asObject();
			
			mMap = tag.getString("map", null);
			mTarget = tag.getString("target", null);
			mDirection = tag.getString("direction", "any");
		}

		@Override
		public JsonValue save()
		{
			JsonObject tag = new JsonObject();
			
			tag.add("map", mMap);
			tag.add("target", mTarget);
			tag.add("direction", mDirection);
			
			return tag;
		}
		
		@Override
		public Type getType()
		{
			return Type.WARP;
		}
		
		public static enum Direction
		{
			UP,
			DOWN,
			LEFT,
			RIGHT,
			ANY,
			NONE
		}
	}
	
	public static class NPCArgument extends Argument
	{
		private String mSprite;
		private String mText;
		private String mAI;
		
		public String getSpriteID() { return mSprite; }
		public String getTextID() { return mText; }
		public String getAI() { return mAI; }
		public void setSpriteID(String id) { mSprite = id; }
		public void setTextID(String id) { mText = id; }
		public void setAI(String ai) { mAI = ai; }
		
		@Override
		public void load(JsonValue value)
		{
			JsonObject tag = value.asObject();
			
			mSprite = tag.getString("sprite", null);
			mText = tag.getString("text", null);
			mAI = tag.getString("ai", null);
		}
		
		@Override
		public JsonValue save()
		{
			JsonObject tag = new JsonObject();
			
			tag.add("sprite", mSprite);
			tag.add("text", mText);
			tag.add("ai", mAI);
			
			return tag;
		}
		
		@Override
		public Type getType()
		{
			return Type.NPC;
		}
	}
}
