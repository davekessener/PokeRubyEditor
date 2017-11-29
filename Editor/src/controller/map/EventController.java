package controller.map;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import controller.ModalDialog;
import lib.Utils;
import lib.action.Action;
import lib.misc.Vec2;
import model.Event;
import model.Event.Argument;
import model.Event.Type;
import view.map.ArgumentTextView;
import view.map.ArgumentView;
import view.map.ArgumentWarpView;
import view.map.EventView;
import view.map.create.NewArgumentUI;

public class EventController
{
	private final EventView mUI;
	private final Consumer<Action> mOnChange;
	private final Event mEvent;
	private ArgumentView mArgUI;
	
	public EventController(Consumer<Action> onchange, int w, int h, Event e)
	{
		mEvent = e;
		mOnChange = onchange;
		mArgUI = instantiateArgumentUI();
		
		mUI = new EventView(e.getID(), w, h, e.getLocation(), mArgUI);
		
		mUI.setOnChangeArgumentType(id -> newArgument(id));
		mUI.idProperty().addListener((ob, o, n) -> setID(n));
		mUI.xProperty().addListener((ob, o, n) -> setPosition(new Vec2(n.intValue(), mUI.yProperty().getValue().intValue())));
		mUI.yProperty().addListener((ob, o, n) -> setPosition(new Vec2(mUI.yProperty().getValue().intValue(), n.intValue())));
	}
	
	private void setID(String id)
	{
		mOnChange.accept(Utils.ReversableAction(mEvent, "ID", id));
	}
	
	private void setPosition(Vec2 p)
	{
		mOnChange.accept(Utils.ReversableAction(mEvent, "Location", p));
	}
	
	private ArgumentView instantiateArgumentUI()
	{
		Argument a = mEvent.getArgument();
		
		return Instantiate(UIS.get(a.getType()), a, mOnChange);
	}
	
	private void newArgument(String id)
	{
		if(id.equals(mEvent.getArgument().getType())) return;
		
		NewArgumentUI ui = NewArgumentUI.Instantiate(id);
		ModalDialog dialog = new ModalDialog("New argument", ui);
		
		dialog.showAndWait();
		
		if(dialog.isSuccessful())
		{
			mOnChange.accept(Utils.ReversableAction(mEvent, "Argument", ui.create()));
		}
	}
	
	public EventView getUI()
	{
		return mUI;
	}
	
	private static ArgumentView Instantiate(Class<? extends ArgumentView> c, Argument a, Consumer<Action> on)
	{
		try
		{
			return c.getConstructor(Consumer.class, Argument.class).newInstance(on, a);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private static final Map<Type, Class<? extends ArgumentView>> UIS = new HashMap<>();
	
	static
	{
		UIS.put(Type.TEXT, ArgumentTextView.class);
		UIS.put(Type.WARP, ArgumentWarpView.class);
	}
}
