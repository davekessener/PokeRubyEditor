package view.create;

import com.eclipsesource.json.JsonValue;

import view.DialogUI;

public interface NewUI extends DialogUI
{
	public abstract String getID();
	public abstract JsonValue getData();
}
