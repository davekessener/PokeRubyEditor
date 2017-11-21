package view.map;

import view.UI;

public abstract class ArgumentView implements UI
{
	private final String mType;
	
	protected ArgumentView(String type)
	{
		mType = type;
	}
	
	public String getType()
	{
		return mType;
	}
}
