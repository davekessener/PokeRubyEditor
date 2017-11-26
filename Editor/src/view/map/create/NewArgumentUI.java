package view.map.create;

import java.util.HashMap;
import java.util.Map;

import lib.misc.Producer;
import model.Event.Argument;
import view.DialogUI;

public abstract class NewArgumentUI implements DialogUI
{
	public abstract Argument create();
	
	public static NewArgumentUI Instantiate(String id)
	{
		return TABLE.get(id).produce();
	}
	
	private static final Map<String, Producer<NewArgumentUI>> TABLE = new HashMap<>();
	
	static
	{
		TABLE.put("text", () -> new NewTextArgumentUI());
		TABLE.put("npc", () -> new NewNPCArgumentUI());
		TABLE.put("warp", () -> new NewWarpArgumentUI());
	}
}
