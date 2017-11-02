package view.tile;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lib.EnterableTextField;
import model.Vec2;
import view.TiledCanvas;
import view.UI;

public class AnimatedTileUI implements UI
{
	private BorderPane mRoot;
	private TilesetUI mTilesetUI;
	private Button mAddPrev, mAddNext, mDel;
	private ComboBox<String> mFrame;
	private Label mMaxFrames;
	private EnterableTextField mPeriod;
	
	public AnimatedTileUI(Image source, int ts)
	{
		mRoot = new BorderPane();
		mTilesetUI = new TilesetUI(source, ts);
		mAddPrev = new Button("<");
		mAddNext = new Button(">");
		mDel = new Button("-");
		mFrame = new ComboBox<>();
		mMaxFrames = new Label("/ 0");
		mPeriod = new EnterableTextField("0");

		Label ms = new Label("ms");
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(3D, 3D, 3D, 3D));
		hbox.getChildren().addAll(mDel, mFrame, mMaxFrames, mAddPrev, mAddNext, mPeriod, ms);
		
		ScrollPane spane = new ScrollPane();
		spane.setContent(mTilesetUI);
		
		mRoot.setTop(hbox);
		mRoot.setCenter(spane);

		mMaxFrames.setAlignment(Pos.CENTER_RIGHT);
		ms.setAlignment(Pos.CENTER_LEFT);
		
		mPeriod.addValidations(EnterableTextField.IS_POSITIVE_INT);
		mPeriod.setAlignment(Pos.CENTER_RIGHT);
	}
	
	public void setFrameCount(int m)
	{
		mFrame.getItems().clear();
		for(int i = 0 ; i < m ; ++i)
		{
			mFrame.getItems().add("Frame " + (i + 1));
		}
		mMaxFrames.setText(" / " + m + " ");
	}
	
	public void selectFrame(int i, Vec2 p)
	{
		mFrame.getSelectionModel().select(i);
		mTilesetUI.setSelected(p);
	}
	
	public int getSelectedFrame() { return mFrame.getSelectionModel().getSelectedIndex(); }
	public void setDeleteEnable(boolean v) { mDel.setDisable(!v); }
	public void setPeriod(int p) { mPeriod.setText("" + p); }
	
	public void setOnActionHandler(TiledCanvas.TileActivatedHandler h) { mTilesetUI.setOnTileActivated(h); }
	public void setOnAddFramePrev(Runnable r) { mAddPrev.setOnAction(e -> r.run()); }
	public void setOnAddFrameNext(Runnable r) { mAddNext.setOnAction(e -> r.run()); }
	public void setOnDeleteFrame(Runnable r) { mDel.setOnAction(e -> r.run()); }
	public void setOnSelectFrame(FrameSelect s) { mFrame.valueProperty().addListener((ob, o, n) -> s.select(mFrame.getSelectionModel().getSelectedIndex())); }
	public void setOnPeriodChange(PeriodChange c) { mPeriod.setCallback(s -> c.change(Integer.parseInt(s))); }

	@Override
	public Parent getNode()
	{
		return mRoot;
	}

	public static interface FrameSelect { public abstract void select(int f); }
	public static interface PeriodChange { public abstract void change(int f); }
}
