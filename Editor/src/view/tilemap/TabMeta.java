package view.tilemap;

import java.util.Map.Entry;

import controller.EditorController;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import lib.misc.Rect;
import lib.misc.Vec2;
import lib.mouse.MouseHandlerCollection;
import lib.mouse.SimpleMouseHandler;
import lib.tilemap.MetaCollection;
import model.layer.Layer;
import model.layer.StaticLayerManager;
import view.TilesetCanvas;
import view.UI;

public class TabMeta implements UI
{
	private BorderPane mRoot;
	private MetaCollection mMetas;
	private ListView<MetaField> mMetaList;
	private TilesetCanvas mTilemap;
	private TilesetCanvas mMetamap;
	private StaticLayerManager mMetaLayer;
	private TextField mNewMetaInput;
	private String mSelectedID;
	private MetaChangeHandler mCallback;
	
	public TabMeta(Layer metas, int ts, TilesetCanvas tm)
	{
		Vec2 s = metas.dimension();
		
		mRoot = new BorderPane();
		mMetas = new MetaCollection();
		mMetaList = new ListView<>();
		mMetaLayer = new StaticLayerManager(s);
		mTilemap = tm;
		
		for(Vec2 p : new Rect(s.getX(), s.getY()))
		{
			mMetas.addMeta(metas.get(p));
		}
		
		for(Entry<String, Color> e : mMetas.entrySet())
		{
			mMetaList.getItems().add(new MetaField(e.getKey(), e.getValue()));
		}
		
		mMetaList.setEditable(false);
		mMetaList.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> onSelected(n.getMetaID()));
		mMetaList.getSelectionModel().select(0);
		
		mMetamap = new TilesetCanvas(mMetaLayer, ts, (gc, id, x, y) -> {
			gc.setFill(mMetas.getColor(id).interpolate(Color.TRANSPARENT, 0.25));
			gc.fillRect(x * ts, y * ts, ts, ts);
		});
		mMetamap.drawGridProperty().bind(EditorController.Instance.getOptions().drawGridProperty());
		mTilemap.drawGridProperty().bind(EditorController.Instance.getOptions().drawGridProperty());
		mMetamap.setTrueClear(true);
		mMetamap.setActiveLayer(0);
		mMetaLayer.add(metas);
		
		mNewMetaInput = new TextField();
		ScrollPane sp = new ScrollPane();
		GridPane pane = new GridPane();
		Button addBtn = new Button("+");

		mMetaList.prefHeightProperty().bind(sp.heightProperty());
		pane.setVgap(5D);
		pane.setHgap(5D);
		pane.setPadding(new Insets(0D, 0D, 0D, 3D));
		
		sp.setContent(mMetaList);
		pane.add(sp, 0, 0, 2, 1);
		pane.add(mNewMetaInput, 0, 1);
		pane.add(addBtn, 1, 1);
		mRoot.setRight(pane);
		
		GridPane.setHgrow(mNewMetaInput, Priority.ALWAYS);
		GridPane.setHgrow(sp, Priority.ALWAYS);
		GridPane.setVgrow(sp, Priority.ALWAYS);
		
		sp = new ScrollPane();
		StackPane st = new StackPane();
		st.getChildren().addAll(mTilemap, mMetamap);
		sp.setContent(st);
		mRoot.setCenter(sp);
		
		mNewMetaInput.setOnKeyPressed(e -> {
			if(e.getCode().equals(KeyCode.ENTER))
			{
				mMetamap.requestFocus();
				addMeta(mNewMetaInput.getText());
			}
		});
		addBtn.setOnAction(e -> addMeta(mNewMetaInput.getText()));
		
		mMetamap.setMouseHandler((new MouseHandlerCollection())
			.add(e -> e.getButton().equals(MouseButton.PRIMARY),
					new SimpleMouseHandler((me, p) -> writeMeta(p.getX(), p.getY(), mSelectedID)))
			.add(e -> e.getButton().equals(MouseButton.SECONDARY), 
					new SimpleMouseHandler((me, p) -> mMetaLayer.get(0).get(p))));
		
		draw();
	}
	
	public void refreshMap(Layer metas)
	{
		mMetaLayer = new StaticLayerManager(metas.dimension());
		mMetamap.setManager(mMetaLayer);
		mMetaLayer.add(metas);
	}
	
	public void draw()
	{
		mTilemap.draw();
		mMetamap.draw();
	}
	
	private void addMeta(String meta)
	{
		mMetas.addMeta(meta);
		mMetaList.getItems().add(new MetaField(meta, mMetas.getColor(meta)));
		mMetaList.getSelectionModel().select(mMetaList.getItems().size() - 1);
		mNewMetaInput.setText("");
		
		draw();
	}
	
	public Color getColorOfMeta(String id) { return mMetas.getColor(id); }
	public void setOnChange(MetaChangeHandler cb) { mCallback = cb; }
	public void select(String meta)
	{
		for(int i = 0 ; i < mMetaList.getItems().size() ; ++i)
		{
			if(mMetaList.getItems().get(i).getMetaID().equals(meta))
			{
				mMetaList.getSelectionModel().select(i);
				return;
			}
		}
		
		addMeta(meta);
	}
	
	private void writeMeta(int x, int y, String id)
	{
		if(mCallback != null)
		{
			mCallback.onChange(x, y, id);
		}
	}
	
	private void onSelected(String meta)
	{
		mSelectedID = meta;
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	public static interface MetaChangeHandler { public abstract void onChange(int x, int y, String id); }
}
