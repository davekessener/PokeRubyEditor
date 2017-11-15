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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import lib.MetaCollection;
import lib.tilemap.StaticLayerManager;
import model.ILayer;
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
	
	public TabMeta(ILayer metas, int ts, TilesetCanvas tm)
	{
		int w = metas.getWidth();
		int h = metas.getHeight();
		
		mRoot = new BorderPane();
		mMetas = new MetaCollection();
		mMetaList = new ListView<>();
		mMetaLayer = new StaticLayerManager(w, h);
		mTilemap = tm;
		
		for(int y = 0 ; y < h ; ++y)
		{
			for(int x = 0 ; x < w ; ++x)
			{
				mMetas.addMeta(metas.get(x, y));
			}
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
		mMetaLayer.addLayer(metas);
		
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
		
		mMetamap.setOnLeftClick((x, y) -> writeMeta(x, y, mSelectedID));
		mMetamap.setOnRightClick(id -> select(id));
		
		redraw();
	}
	
	public void refreshMap(ILayer metas)
	{
		mMetaLayer = new StaticLayerManager(metas.getWidth(), metas.getHeight());
		mMetamap.setManager(mMetaLayer);
		mMetaLayer.addLayer(metas);
	}
	
	public void redraw()
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
		
		redraw();
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
