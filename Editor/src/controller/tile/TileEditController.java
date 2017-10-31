package controller.tile;

import controller.Controller;
import javafx.beans.property.Property;

public interface TileEditController extends Controller
{
	public Property<Boolean> changed();
}
