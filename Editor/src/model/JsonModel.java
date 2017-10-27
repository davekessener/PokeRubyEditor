package model;

import com.eclipsesource.json.JsonValue;

public interface JsonModel
{
	public abstract void load(JsonValue value);
	public abstract JsonValue save( );
}
