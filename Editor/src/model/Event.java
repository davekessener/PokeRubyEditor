package model;

import java.util.HashMap;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

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
			mArgument = ARGUMENTS.get(tag.getString("type", null)).newInstance();
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
		tag.add("type", mArgument.getType());
		tag.add("argument", mArgument.save());
		
		return tag;
	}
	
	public static final java.util.Map<String, Class<? extends Argument>> ARGUMENTS = new HashMap<>();
	
	public static abstract class Argument implements JsonModel
	{
		public abstract String getType();
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
		public String getType()
		{
			return "text";
		}
	}
	
	public static class WarpArgument extends Argument
	{
		private String mMap;
		private String mTarget;
		
		@Override
		public void load(JsonValue value)
		{
			JsonObject tag = value.asObject();
			
			mMap = tag.getString("map", null);
			mTarget = tag.getString("target", null);
		}

		@Override
		public JsonValue save()
		{
			JsonObject tag = new JsonObject();
			
			tag.add("map", mMap);
			tag.add("target", mTarget);
			
			return tag;
		}
		
		@Override
		public String getType()
		{
			return "warp";
		}
	}
	
	public static class NPCArgument extends Argument
	{
		private String mSprite;
		private String mText;
		private String mAI;
		
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
		public String getType()
		{
			return "npc";
		}
	}
	
	static
	{
		ARGUMENTS.put("text", TextArgument.class);
		ARGUMENTS.put("warp", WarpArgument.class);
		ARGUMENTS.put("npc", NPCArgument.class);
	}
}
