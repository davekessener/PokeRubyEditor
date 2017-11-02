package view.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import controller.EditorController;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import lib.EnterableTextField;
import lib.EnterableTextField.EmptyAcceptor;
import lib.IDValidator;
import model.Direction;
import model.Loader;
import model.Map.Neighbor;
import model.Tilemap;
import model.Tileset;
import model.Vec2;
import view.UI;

public class TabNeighbors implements UI
{
	private final BorderPane mRoot;
	private final String mMapID;
	private final PreviewManager mPreviewManager;
	private final Map<Direction, Neighbor> mNeighbors;
	private final ScrollPane mPreviewContainer;
	private int mTileSize;
	private NeighborChangeHandler mCallback;
	
	public TabNeighbors(String id, PreviewManager m, Map<Direction, Neighbor> neighbors)
	{
		mMapID = id;
		mRoot = new BorderPane();
		mNeighbors = neighbors;
		mPreviewContainer = new ScrollPane();
		mPreviewManager = m;
		mTileSize = loadTileSize(mMapID);
		
		GridPane grid = new GridPane();
		Vec2 start = new Vec2(1, 1);
		
		for(Direction d : Direction.values())
		{
			EnterableTextField tfID = new EnterableTextField("");
			EnterableTextField tfOff = new EnterableTextField("");
			Label lblID = new Label("ID");
			Label lblOff = new Label("Offset");
			Neighbor n = neighbors.get(d);
			GridPane p = new GridPane();
			
			if(n != null)
			{
				tfID.setText(n.getMapID());
				tfOff.setText("" + n.getOffset());
			}
			
			tfID.setCallback(s -> setNeighbor(d, s, tfOff));
			tfOff.setCallback(s -> setNeighbor(d, tfID.getText(), tfOff));
			
			tfID.addValidations(new EmptyAcceptor(new IDValidator("map")));
			tfOff.addValidations(new EmptyAcceptor(EnterableTextField.IS_INT));

			GridPane.setValignment(lblID, VPos.CENTER);
			GridPane.setValignment(lblOff, VPos.CENTER);
			GridPane.setHalignment(lblID, HPos.RIGHT);
			GridPane.setHalignment(lblOff, HPos.RIGHT);

			p.setHgap(3D);
			p.setVgap(2D);
			
			p.addColumn(0, lblID, lblOff);
			p.addColumn(2, tfID, tfOff);
			
			Vec2 at = start.add(d.distance);
			grid.add(p, at.getX(), at.getY());
		}
		
		ScrollPane sp = new ScrollPane();
		grid.setPadding(new Insets(10D, 10D, 10D, 10D));
		sp.setContent(grid);
		mRoot.setTop(sp);
		mRoot.setCenter(mPreviewContainer);
		
		refreshPreview();
	}
	
	public void setOnNeighborChange(NeighborChangeHandler cb) { mCallback = cb; }
	
	public void redraw()
	{
		refreshPreview();
	}
	
	private void setNeighbor(Direction d, String id, TextField tfOff)
	{
		int o = -1;
		
		if(id.equals(""))
		{
			tfOff.setText("");
			id = null;
		}
		else
		{
			o = 0;
			
			if(tfOff.getText().equals(""))
			{
				tfOff.setText("0");
			}
			else
			{
				o = Integer.parseInt(tfOff.getText());
			}
		}
		
		if(mCallback != null)
		{
			mCallback.onNeighborChange(d, id, o);
		}
	}
	
	private void refreshPreview()
	{
		mPreviewContainer.setContent(new NeighborGrid());
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
	
	private static int loadTileSize(String id)
	{
		Loader l = EditorController.Instance.getLoader();
		model.Map map = new model.Map();
		Tilemap tm = new Tilemap();
		Tileset ts = new Tileset();
		
		map.load(l.loadData("map", id));
		tm.load(l.loadData("tilemap", map.getTilemapID()));
		ts.load(l.loadData("tileset", tm.getTilesetID()));
		
		return ts.getSize();
	}
	
	public static interface NeighborChangeHandler { public abstract void onNeighborChange(Direction d, String id, int offset); }
	
	private class NeighborGrid extends GridPane
	{
		private final Map<String, Vec2> mCoords;
		private final Preview mMain;
		
		public NeighborGrid()
		{
			int w = 0, h = 0;
			
			mCoords = new HashMap<>();
			
			mCoords.put(mMapID, new Vec2(0, 0));
			mMain = mPreviewManager.getPreview(mMapID);
			
			for(Entry<Direction, Neighbor> e : mNeighbors.entrySet())
			{
				addNeighbor(e.getKey(), e.getValue());
			}

			normalizeCoordinates();
			
			for(Entry<String, Vec2> e : mCoords.entrySet())
			{
				Preview p = mPreviewManager.getPreview(e.getKey());
				Vec2 v = e.getValue();
				
				this.add(p, v.getX(), v.getY(), p.getMapWidth(), p.getMapHeight());
				
				w = Math.max(w, v.getX() + p.getMapWidth());
				h = Math.max(h, v.getY() + p.getMapHeight());
			}
			
			adjustSizes(w, h);
		}
		
		private void normalizeCoordinates()
		{
			Vec2 o = new Vec2(0, 0);
			
			for(Vec2 v : mCoords.values())
			{
				if(v.getX() < o.getX()) o.setX(v.getX());
				if(v.getY() < o.getY()) o.setY(v.getY());
			}
			
			o.setX(-o.getX());
			o.setY(-o.getY());
			
			for(String k : mCoords.keySet())
			{
				mCoords.put(k, mCoords.get(k).add(o));
			}
		}
		
		private void adjustSizes(int w, int h)
		{
			for(int x = 0 ; x < w ; ++x)
			{
				this.getColumnConstraints().add(new ColumnConstraints(mTileSize));
			}
			
			for(int y = 0 ; y < h ; ++y)
			{
				this.getRowConstraints().add(new RowConstraints(mTileSize));
			}
		}
		
		private void addNeighbor(Direction d, Neighbor n)
		{
			Vec2 o = null;
			Preview p = mPreviewManager.getPreview(n.getMapID());
			
			switch(d)
			{
				case LEFT:
					o = new Vec2(-p.getMapWidth(), n.getOffset());
					break;
				case RIGHT:
					o = new Vec2(mMain.getMapWidth(), n.getOffset());
					break;
				case UP:
					o = new Vec2(n.getOffset(), -p.getMapHeight());
					break;
				case DOWN:
					o = new Vec2(n.getOffset(), mMain.getMapHeight());
					break;
			}
			
			if(o != null)
			{
				mCoords.put(n.getMapID(), o);
			}
		}
	}
}
