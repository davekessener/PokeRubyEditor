package controller.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import model.Event.Argument;
import model.Event.TextArgument;
import model.Vec2;
import view.map.ArgumentTextView;
import view.map.ArgumentView;
import view.map.EventView;

public class EventController
{
	private final EventView mUI;
	private ArgumentView mArgUI;
	private Argument mArgument;
	
	public EventController(int w, int h, String id, Vec2 pos, Argument arg)
	{
		mArgument = arg;
		mArgUI = instantiateArgumentUI();
		
		mUI = new EventView(id, w, h, pos, mArgUI);
	}
	
	private ArgumentView instantiateArgumentUI()
	{
		return UIS.get(mArgument.getType()).apply(mArgument);
	}
	
	public EventView getUI()
	{
		return mUI;
	}
	
	private static final Map<String, Function<Argument, ArgumentView>> UIS = new HashMap<>();
	
	static
	{
		UIS.put("text", a -> new ArgumentTextView(((TextArgument) a).getText()));
	}
}
