package model;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import lib.misc.Vec2;

public abstract class Entity implements JsonModel
{
	private String sID;
	private Vec2 vPosition;
	
	public String getID() { return sID; }
	public Vec2 getPosition() { return vPosition; }
	
	public void setID(String id) { sID = id; }
	public void setPosition(Vec2 p) { vPosition = p; }
	
	@Override
	public void load(JsonValue value)
	{
		JsonObject tag = value.asObject();
		
		sID = tag.getString("id", null);
		vPosition = new Vec2(0, 0);
		vPosition.load(tag.get("at"));
	}

	@Override
	public JsonValue save()
	{
		return new JsonObject().add("id", sID).add("at", vPosition.save()).add("type", getType());
	}
	
	public abstract String getType();
}
