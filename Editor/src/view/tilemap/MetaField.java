package view.tilemap;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MetaField extends HBox
{
	private Rectangle mColorView;
	private Label mIDView;
	private String mID;
	private Color mColor;
	
	public MetaField(String id, Color c)
	{
		mColorView = new Rectangle(16, 16);
		mIDView = new Label(id);
		
		mColorView.setFill(c);
		
		this.setPadding(new Insets(3D, 3D, 3D, 3D));
		this.setSpacing(15D);
		
		this.getChildren().addAll(mColorView, mIDView);
	}
	
	public String getMetaID() { return mID; }
	public Color getMetaColor() { return mColor; }
}
