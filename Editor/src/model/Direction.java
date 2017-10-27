package model;

public enum Direction
{
	LEFT("left", new Vec2(-1, 0)),
	RIGHT("right", new Vec2(1, 0)),
	UP("up", new Vec2(0, -1)),
	DOWN("down", new Vec2(0, 1));
	
	public final String literal;
	public final Vec2 distance;
	
	private Direction(String literal, Vec2 distance)
	{
		this.literal = literal;
		this.distance = distance;
	}
}
