package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.Vec2;

public class RenderUtils
{
	public static void RenderBox(GraphicsContext gc, Vec2 select, int s) { RenderBox(gc, select, s, false); }
	public static void RenderBox(GraphicsContext gc, Vec2 select, int s, boolean grid)
	{
		double inset = grid ? 1.5 : 0.5;
		gc.setStroke(Color.RED);
		gc.strokeRect(select.getX() * s + inset, select.getY() * s + inset, s - 0.5 - inset, s - 0.5 - inset);
	}
	
	public static void RenderTile(GraphicsContext gc, Image source, int ts, Vec2 s, Vec2 d)
	{
		gc.drawImage(source, s.getX() * ts, s.getY() * ts, ts, ts, d.getX() * ts, d.getY() * ts, ts, ts);
	}
	
	private RenderUtils() { }
}
