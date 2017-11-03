package view.create;

import com.eclipsesource.json.JsonValue;

import controller.EditorController;
import lib.EnterableTextField;
import lib.EnterableTextField.FileValidator;
import model.Tileset;
import model.Tileset.StaticTile;

public class NewTilesetUI extends BasicNewUI
{
	public NewTilesetUI()
	{
		addField("source", "Source", new FileValidator(s -> EditorController.Instance.getLoader().getFile("media", "tileset", s)));
		addField("size", "Tile Size", EnterableTextField.IS_POSITIVE_INT);
	}

	@Override
	public JsonValue getData()
	{
		Tileset ts = new Tileset();
		
		ts.setSource(getField("source"));
		ts.setSize(Integer.parseInt(getField("size")));
		ts.addTile("default", new StaticTile());
		
		return ts.save();
	}
}