package view.create;

import com.eclipsesource.json.JsonValue;

import lib.IDValidator;
import model.Map;;

public class NewMapUI extends BasicNewUI
{
	public NewMapUI()
	{
		addField("name", "Name");
		addField("tilemap", "Tilemap ID", new IDValidator("tilemap"));
		addField("border", "Bordermap ID", new IDValidator("tilemap"));
	}

	@Override
	public JsonValue getData()
	{
		Map map = new Map();
		
		map.setName(getField("name"));
		map.setTilemapID(getField("tilemap"));
		map.setBorderID(getField("border"));
		
		return map.save();
	}
}
