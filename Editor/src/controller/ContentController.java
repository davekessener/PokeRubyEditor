package controller;

import com.eclipsesource.json.JsonValue;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
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
}
