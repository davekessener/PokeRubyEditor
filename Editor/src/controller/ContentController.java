package controller;

import com.eclipsesource.json.JsonValue;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import lib.action.Action;
import lib.action.BasicAction;
import model.JsonModel;

public abstract class ContentController implements Controller
{
	private Property<Boolean> mChanged;
	private JsonModel mModel;
	
	public ContentController(JsonModel model)
	{
		mModel = model;
		mChanged = new SimpleBooleanProperty();
	}
	
	public Property<Boolean> needsSave() { return mChanged; }
	protected void change() { mChanged.setValue(true); }
	
	public JsonValue save()
	{
		mChanged.setValue(false);
		
		return mModel.save();
	}

	protected void act(Runnable redo, Runnable undo)
	{
		Action a = new ContentAction(this, undo, redo);
		EditorController.Instance.getActionManager().addAction(a);
		a.redo();
	}
	
	protected static class ContentAction extends BasicAction
	{
		public ContentAction(ContentController c, Runnable undo, Runnable redo)
		{
			super(() -> { undo.run(); c.change(); }, () -> { redo.run(); c.change(); });
		}
	}
}
