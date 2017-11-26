package lib.mouse;

import javafx.scene.input.MouseEvent;
import lib.misc.Vec2;

public interface MouseHandler
{
	public abstract void onPressed(MouseEvent e, Vec2 p);
	public abstract void onDragged(MouseEvent e, Vec2 p);
	public abstract void onReleased(MouseEvent e);
}
