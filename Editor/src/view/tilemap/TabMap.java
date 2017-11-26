package view.tilemap;

import java.util.function.Consumer;

import controller.EditorController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lib.MemoryConsumer;
import lib.Utils;
import lib.action.Action;
import lib.misc.Producer;
import lib.misc.Rect;
import lib.misc.Vec2;
import lib.mouse.MouseHandlerCollection;
import lib.mouse.SimpleMouseHandler;
import lib.tilemap.selection.AreaSelector;
import lib.tilemap.selection.DrawHandler;
import lib.tilemap.selection.MapSelection;
import lib.tilemap.selection.Selection;
import lib.tilemap.selection.SingleLayerSelection;
import lib.tilemap.selection.SingleTileSelection;
import model.Tilemap;
import model.layer.ReadOnlyLayerManager;
import model.layer.EmptyLayerManager;
import model.layer.LayerUtils;
import model.layer.LayerWrapper;
import model.layer.MapLayerManager;
import model.layer.ReadOnlyLayer;
import model.layer.StaticLayerManager;
import view.TilesetCanvas;
import view.UI;

public class TabMap implements UI
{
	private final BorderPane mRoot;
	private final Tilemap mTilemap;
	private final TilesetCanvas mMapCanvas;
	private final TilesetCanvas mAreaSelection;
	private final TilesetView mTileset;
	private final MapLayerManager mManager;
	private final ComboBox<String> mLayerType;
	private final ComboBox<String> mLayer;
	private final Label mMaxLayerLabel;
	private final Button mDeleteButton;
	private Selection mSelection;
	private String mCurrentLayerType;
	private int mLayerIndex;
	private Consumer<Action> mActionReceiver;
	
	public TabMap(Tilemap map, MapLayerManager lm, int ts, TilesetRenderer r)
	{
		mRoot = new BorderPane();
		mTilemap = map;
		mMapCanvas = new TilesetCanvas(lm, ts, r);
		mAreaSelection = new TilesetCanvas(new EmptyLayerManager(), ts, r);
		mSelection = new SingleTileSelection(null);
		mTileset = new TilesetView(r, 8);
		mManager = lm;
		mCurrentLayerType = null;
		mLayerIndex = -1;
		
		EditorController editor = EditorController.Instance;
		
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
		sc.setContent(mMapCanvas);
		bp.setCenter(sc);
		
		sc = new ScrollPane();
		sc.setContent(mTileset);
		sc.prefWidthProperty().bind(mTileset.widthProperty().add(16));
		sc.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		
		ScrollPane ssc = new ScrollPane();
		ssc.setContent(mAreaSelection);
		ssc.setMinWidth(ts * 8 + 16);
		ssc.setMinHeight(ts * 8 + 16);
		ssc.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		ssc.setHbarPolicy(ScrollBarPolicy.ALWAYS);

		GridPane gp = new GridPane();
		gp.setVgap(3D);
		gp.setPadding(new Insets(0D, 0D, 0D, 5D));
		gp.addColumn(0, sc, ssc);
		GridPane.setVgrow(sc, Priority.ALWAYS);
		
		mRoot.setRight(gp);
		mRoot.setCenter(bp);
		BorderPane.setMargin(sc, new Insets(0D, 0D, 0D, 5D));
		
		mLayerType.getItems().addAll(Tilemap.TYPES);
		mLayerType.valueProperty().addListener((ob, o, n) -> selectLayer(n, mLayerIndex));
		mLayerType.getSelectionModel().select("bottom");
		mLayer.valueProperty().addListener((ob, o, n) -> selectLayer(mCurrentLayerType, mLayer.getSelectionModel().getSelectedIndex()));
		
		mMapCanvas.setMouseHandler((new MouseHandlerCollection())
				.add(e -> e.getButton().equals(MouseButton.PRIMARY), 
						new DrawHandlerImpl(() -> getSelectionSize(), new MemoryConsumer<>(p -> drawAt(p))))
				.add(e -> e.getButton().equals(MouseButton.SECONDARY), 
						(new MouseHandlerCollection())
					.add(e -> e.isShiftDown() && e.isControlDown(), 
							new AreaSelector(new MemoryConsumer<>(t -> selectWholeMapArea(t))))
					.add(e -> e.isControlDown(), 
							new AreaSelector(new MemoryConsumer<>(t -> selectMapArea(t))))
					.add(e -> true, 
							new SimpleMouseHandler((e, p) -> selectSingleTile(mManager.getLayers(mCurrentLayerType).get(mLayerIndex).get(p))))));
		
		mMapCanvas.drawGridProperty().bind(editor.getOptions().drawGridProperty());
		mMapCanvas.drawOverlayProperty().bind(greyOut.selectedProperty());
		mMapCanvas.drawUpperProperty().bind(renderTop.selectedProperty());
		mMapCanvas.mousedOverTileProperty().addListener((ob, o, n) -> editor.getMenuManager().setStatus(n == null ? "" : n.toString()));
		mMapCanvas.setOnMouseExited(e -> editor.getMenuManager().setStatus(""));
		
		mTileset.setMouseHandler((new MouseHandlerCollection())
				.add(e -> e.getButton().equals(MouseButton.PRIMARY), 
						new SimpleMouseHandler((e, p) -> selectSingleTile(mTileset.getID(p))))
				.add(e -> e.getButton().equals(MouseButton.SECONDARY),
						new AreaSelector(new MemoryConsumer<>(t -> selectTilesetArea(t)))));
		
		for(String id : Tilemap.TYPES)
		{
			if(mTilemap.getLayers(id).size() > 0)
			{
				mLayerType.getSelectionModel().select(id);
				break;
			}
		}
		
		mMapCanvas.draw();
		mTileset.draw();
	}
	
	public void setOnAction(Consumer<Action> cb) { mActionReceiver = cb; }
	
	public Vec2 getSelectionSize() { return mSelection == null ? Vec2.ORIGIN : mSelection.dimension(); }
	
	public void drawAt(Vec2 p)
	{
		if(mSelection != null)
		{
			mSelection.apply(mManager, mLayerIndex, p);
		}
	}
	
	private void selectSingleTile(String id)
	{
		mTileset.selectID(id);
		
		select(new SingleTileSelection(id), new EmptyLayerManager());
	}
	
	private void selectTilesetArea(Rect r)
	{
		selectSingleLayer(new LayerWrapper(r.getWidth(), r.getHeight(), p -> mTileset.getID(p.sub(r.getOffset()))));
	}
	
	private void selectWholeMapArea(Rect r)
	{
		mTileset.setSelected(null);
		
		ReadOnlyLayerManager m = LayerUtils.subManager(mManager, r);
		
		select(new MapSelection(m), m);
	}
	
	private void selectMapArea(Rect r)
	{
		selectSingleLayer(LayerUtils.subLayer(mManager.getLayers(mCurrentLayerType).get(mLayerIndex), r));
	}
	
	private void selectSingleLayer(ReadOnlyLayer l)
	{
		mTileset.setSelected(null);
		
		select(new SingleLayerSelection(l), Utils.with(new StaticLayerManager(l.dimension()), m -> m.add(l)));
	}
	
	private void select(Selection s, ReadOnlyLayerManager m)
	{
		mSelection = s;
		mAreaSelection.setManager(m);
	}
	
	private void addLayer(int i)
	{
		if(mCurrentLayerType != null)
		{
			i = mManager.createLayer(mCurrentLayerType, i);
			reloadLayerBox();
			mLayer.getSelectionModel().select(i);
		}
	}
	
	private void deleteLayer(int i)
	{
		i = mManager.deleteLayer(mCurrentLayerType, i);
		reloadLayerBox();
		mLayer.getSelectionModel().select(i);
	}
	
	public void draw()
	{
		mMapCanvas.draw();
	}
	
	private void selectLayer(String id, int idx)
	{
		boolean changedLayer = mCurrentLayerType == null ? id != null : !mCurrentLayerType.equals(id);
		
		mCurrentLayerType = id;
		mLayerIndex = idx;
		
		if(changedLayer)
		{
			reloadLayerBox();
		}
		
		selectActiveLayer();
	}
	
	private void selectActiveLayer()
	{
		mMapCanvas.setActiveLayer(mManager.getAbsoluteIndex(mCurrentLayerType, mLayerIndex));
	}

	private void reloadLayerBox()
	{
		mLayerIndex = -1;
		mLayer.getSelectionModel().clearSelection();
		mLayer.getItems().clear();
	
		for(int i = 0 ; i < mTilemap.getLayers(mCurrentLayerType).size() ; ++i)
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
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	private class DrawHandlerImpl extends DrawHandler
	{
		public DrawHandlerImpl(Producer<Vec2> f, Consumer<Vec2> cb) { super(f, cb); }

		@Override
		public void onReleased(MouseEvent e)
		{
		}
	}
}
