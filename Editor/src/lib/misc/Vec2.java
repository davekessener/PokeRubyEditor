package lib.misc;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import model.JsonModel;

public class Vec2 implements JsonModel, Cloneable
{
	private int dx, dy;
	
	public Vec2(int dx, int dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	@Override
	public Vec2 clone()
	{
		return new Vec2(dx, dy);
	}
	
	public int getX() { return dx; }
	public int getY() { return dy; }
	
	public Vec2 add(Vec2 p) { return new Vec2(dx + p.dx, dy + p.dy); }
	public Vec2 sub(Vec2 p) { return new Vec2(dx - p.dx, dy - p.dy); }

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
	public String toString()
	{
		return "(" + dx + ", " + dy + ")";
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
	
	public static final Vec2 ORIGIN = new Vec2(0, 0);
}
