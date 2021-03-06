package view.create;

import com.eclipsesource.json.JsonValue;

import lib.EnterableTextField;
import lib.IDValidator;
import lib.misc.Vec2;
import model.Tilemap;
import model.layer.BasicLayer;

public class NewTilemapUI extends BasicNewUI
{
	public NewTilemapUI()
	{
		addField("tileset", "Tileset ID", new IDValidator("tileset"));
		addField("width", "Width", EnterableTextField.IS_POSITIVE_INT);
		addField("height", "Height", EnterableTextField.IS_POSITIVE_INT);
		addField("meta", "Default meta", "wall");
	}

	@Override
	public JsonValue getData()
	{
		Tilemap map = new Tilemap();
		int w = Integer.parseInt(getField("width"));
		int h = Integer.parseInt(getField("height"));
		BasicLayer meta = new BasicLayer(new Vec2(w, h));
		
		meta.fill(getField("meta"));
		
		map.setTilesetID(getField("tileset"));
		map.setWidth(w);
		map.setHeight(h);
		map.setMeta(meta);
		
		return map.save();
	}
}
