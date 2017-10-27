package controller;

import com.eclipsesource.json.JsonValue;

public interface ContentController extends Controller
{
	public abstract boolean needsSave();
	public abstract JsonValue save();
}
