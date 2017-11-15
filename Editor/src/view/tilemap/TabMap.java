package view.tilemap;

import controller.EditorController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lib.tilemap.MapLayerManager;
import model.Tilemap;
import model.Vec2;
import view.TilesetCanvas;
import view.UI;

public class TabMap implements UI
{
	private BorderPane mRoot;
	private TilesetCanvas mCanvas;
	private Tilemap mTilemap;
	private TilesetView mTileset;
	private ComboBox<String> mLayerType;
	private ComboBox<String> mLayer;
	private Label mMaxLayerLabel;
	private Button mDeleteButton;
	private String mSelectedTile;
	private String mSelectedLayer;
	private MapLayerManager mManager;
	private int mLayerIndex;
	private ClickHandler mCallback;
	
	public TabMap(Tilemap map, MapLayerManager lm, int ts, TilesetRenderer r)
	{
		mRoot = new BorderPane();
		mTilemap = map;
		mCanvas = new TilesetCanvas(lm, ts, r);
		mTileset = new TilesetView(r, 8);
		mManager = lm;
		mSelectedLayer = null;
		mLayerIndex = -1;
		
		Label lblPlane = new Label("Plane");
		Label lblLayer = new Label("Layer");
		mLayerType = new ComboBox<>();
		mLayer = new ComboBox<>();
		mMaxLayerLabel = new Label();
		Button addPrev = new Button("<");
		Button addNext = new Button(">");
		mDeleteButton = new Button("-");
		ToggleButton greyOut = new ToggleButton("1");
		ToggleButton renderTop = new ToggleButton("2");
		
		renderTop.setSelected(true);
		addPrev.setOnAction(e -> addLayer(mLayerIndex));
		addNext.setOnAction(e -> addLayer(mLayerIndex + 1));
		mDeleteButton.setOnAction(e -> deleteLayer(mLayerIndex));
		mDeleteButton.disableProperty().bind(mLayer.getSelectionModel().selectedIndexProperty().lessThan(0));
		
		BorderPane bp = new BorderPane();
		HBox hb = new HBox();
		hb.setSpacing(3D);
		hb.setAlignment(Pos.CENTER_LEFT);
		hb.getChildren().addAll(lblPlane, mLayerType, lblLayer, mLayer, mMaxLayerLabel, addPrev, addNext, mDeleteButton, greyOut, renderTop);
		hb.setPadding(new Insets(0, 0, 5D, 0));
		bp.setTop(hb);
		ScrollPane sc = new ScrollPane();
		sc.setContent(mCanvas);
		bp.setCenter(sc);
		
		sc = new ScrollPane();
		sc.setContent(mTileset);
		mRoot.setRight(sc);
		mRoot.setCenter(bp);
		
		mLayerType.getItems().addAll(Tilemap.LAYERS);
		mLayerType.valueProperty().addListener((ob, o, n) -> selectLayer(n, mLayerIndex));
		mLayerType.getSelectionModel().select("bottom");
		mLayer.valueProperty().addListener((ob, o, n) -> selectLayer(mSelectedLayer, mLayer.getSelectionModel().getSelectedIndex()));
		mCanvas.setOnRightClick(id -> { mTileset.setSelected(id); selectTile(id); });
		mCanvas.setOnLeftClick((x, y) -> clickTile(x, y));
		mCanvas.drawGridProperty().bind(EditorController.Instance.getOptions().drawGridProperty());
		mCanvas.drawOverlayProperty().bind(greyOut.selectedProperty());
		mCanvas.drawUpperProperty().bind(renderTop.selectedProperty());
		EditorController.Instance.getMenuManager().setStatus(mCanvas.mousedOverTileProperty());
		
		mSelectedTile = mTileset.getID(0);
		mTileset.setSelected(new Vec2(0, 0));
		mTileset.setOnSelect(id -> selectTile(id));
		
		for(String id : Tilemap.LAYERS)
		{
			if(mTilemap.getLayers(id).size() > 0)
			{
				mLayerType.getSelectionModel().select(id);
				break;
			}
		}
		
		mCanvas.draw();
		mTileset.draw();
	}
	
	public void setOnClick(ClickHandler cb) { mCallback = cb; }
	
	private void addLayer(int i)
	{
		if(mSelectedLayer != null)
		{
			i = mManager.addLayer(mSelectedLayer, i);
			reloadLayerBox();
			mLayer.getSelectionModel().select(i);
		}
	}
	
	private void deleteLayer(int i)
	{
		i = mManager.deleteLayer(mSelectedLayer, i);
		reloadLayerBox();
		mLayer.getSelectionModel().select(i);
	}
	
	public void redraw()
	{
		mCanvas.draw();
	}
	
	private void clickTile(int x, int y)
	{
		if(mCallback != null)
		{
			mCallback.onClick(x, y, mSelectedLayer, mLayerIndex, mSelectedTile);
		}
	}
	
	private void selectLayer(String id, int idx)
	{
		boolean changedLayer = mSelectedLayer == null ? id != null : !mSelectedLayer.equals(id);
		
		mSelectedLayer = id;
		mLayerIndex = idx;
		
		if(changedLayer)
		{
			reloadLayerBox();
		}
		
		selectActiveLayer();
	}
	
	private void selectActiveLayer()
	{
		mCanvas.setActiveLayer(mManager.getAbsoluteIndex(mSelectedLayer, mLayerIndex));
	}

	private void reloadLayerBox()
	{
		mLayerIndex = -1;
		mLayer.getSelectionModel().clearSelection();
		mLayer.getItems().clear();
	
		for(int i = 0 ; i < mTilemap.getLayers(mSelectedLayer).size() ; ++i)
		{
			mLayer.getItems().add("Layer " + (i + 1));
		}
		
		if(!mLayer.getItems().isEmpty())
		{
			mLayer.getSelectionModel().select(0);
			mLayerIndex = 0;
		}
		
		mMaxLayerLabel.setText("/ " + mLayer.getItems().size());
	}
	
	private void selectTile(String tileID)
	{
		mSelectedTile = tileID;
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	public static interface ClickHandler { public void onClick(int x, int y, String lid, int i, String tile); }
}
