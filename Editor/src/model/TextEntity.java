package model;

import com.eclipsesource.json.JsonValue;

public class TextEntity extends Entity
{
	private String sArgument;
	
	public String getArgument() { return sArgument; }
	public void setArgument(String argument) { sArgument = argument; }
	
	@Override
	public void load(JsonValue value)
	{
		super.load(value);
		sArgument = value.asObject().getString("argument", null);
	}
	
	@Override
	public JsonValue save( )
	{
		return super.save().asObject().add("argument", sArgument);
	}
	
	@Override
	public String getType()
	{
		return "text";
	}
}
