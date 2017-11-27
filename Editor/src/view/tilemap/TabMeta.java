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
import lib.mouse.FillMouseHandler;
import lib.mouse.MouseHandlerCollection;
import lib.mouse.OnReleaseWrapper;
import lib.mouse.SimpleMouseHandler;
import lib.tilemap.MetaCollection;
import model.layer.Layer;
import model.layer.SingleLayerManager;
import view.TilesetCanvas;
import view.UI;

public class TabMeta implements UI
{
	private final BorderPane mRoot;
	private final MetaCollection mMetas;
	private final ListView<MetaField> mMetaList;
	private final TilesetCanvas mTilemap;
	private final TilesetCanvas mMetamap;
	private final SingleLayerManager mLayerManager;
	private final TextField mNewMetaInput;
	private final Runnable mSave;
	private final Layer mMetaLayer;
	private String mSelectedID;
	
	public TabMeta(Layer metas, int ts, TilesetCanvas tm, Runnable save)
	{
		Vec2 s = metas.dimension();
		
		mRoot = new BorderPane();
		mMetas = new MetaCollection();
		mMetaList = new ListView<>();
		mLayerManager = new SingleLayerManager(metas);
		mTilemap = tm;
		mMetaLayer = metas;
		mSave = save;
		
		for(Vec2 p : new Rect(s.getX(), s.getY()))
		{
			mMetas.addMeta(metas.get(p));
		}
		
		for(Entry<String, Color> e : mMetas.entrySet())
		{
			mMetaList.getItems().add(new MetaField(e.getKey(), e.getValue()));
		}
		
		mMetaList.setEditable(false);
		mMetaList.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> selectMeta(n.getMetaID()));
		mMetaList.getSelectionModel().select(0);
		
		mMetamap = new TilesetCanvas(mLayerManager, ts, (gc, id, x, y) -> {
			gc.setFill(mMetas.getColor(id).interpolate(Color.TRANSPARENT, 0.25));
			gc.fillRect(x * ts, y * ts, ts, ts);
		});
		mMetamap.drawGridProperty().bind(EditorController.Instance.getOptions().drawGridProperty());
		mTilemap.drawGridProperty().bind(EditorController.Instance.getOptions().drawGridProperty());
		mMetamap.setTrueClear(true);
		mMetamap.setActiveLayer(0);
		
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
					new OnReleaseWrapper(new SimpleMouseHandler((me, p) -> writeMeta(p, mSelectedID)), () -> mSave.run()))
			.add(e -> e.getButton().equals(MouseButton.SECONDARY), 
					new SimpleMouseHandler((me, p) -> selectMeta(mLayerManager.get(0).get(p))))
			.add(e -> e.getButton().equals(MouseButton.MIDDLE),
					new OnReleaseWrapper(new FillMouseHandler(() -> mMetaLayer, p -> mMetaLayer.set(p, mSelectedID)), () -> mSave.run())));
		
		mMetaLayer.addObserver(o -> draw());
		
		draw();
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
	}
	
	public Color getColorOfMeta(String id) { return mMetas.getColor(id); }
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
	
	private void writeMeta(Vec2 p, String id)
	{
		mMetaLayer.set(p, id);
	}
	
	private void selectMeta(String meta)
	{
		mSelectedID = meta;
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
