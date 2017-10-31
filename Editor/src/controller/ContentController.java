package controller;

import com.eclipsesource.json.JsonValue;

import javafx.beans.property.Property;

public interface ContentController extends Controller
{
	public abstract Property<Boolean> needsSave();
	public abstract JsonValue save();
}
