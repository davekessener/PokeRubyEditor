package model;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class Vec2 implements JsonModel
{
	private int dx, dy;
	
	public Vec2(int dx, int dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	public int getX() { return dx; }
	public int getY() { return dy; }
	
	public void setX(int dx) { this.dx = dx; }
	public void setY(int dy) { this.dy = dy; }

	@Override
	public void load(JsonValue value)
	{
		dx = value.asArray().get(0).asInt();
		dy = value.asArray().get(1).asInt();
	}

	@Override
	public JsonValue save()
	{
		return new JsonArray().add(dx).add(dy);
	}
	
	@Override
	public int hashCode()
	{
		return Integer.valueOf(dx).hashCode() * Integer.valueOf(dy).hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		return (o != null && o instanceof Vec2) ? (((Vec2) o).dx == dx && ((Vec2) o).dy == dy) : false;
	}
}
