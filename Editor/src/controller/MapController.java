package controller;

import javafx.scene.Node;
import model.Direction;
import model.Map;
import model.Map.Neighbor;
import view.TabbedUI;
import view.map.PreviewManager;
import view.map.TabData;
import view.map.TabNeighbors;

public class MapController extends ContentController
{
	private TabbedUI mUI;
	private String mMapID;
	private Map mMap;
	private PreviewManager mPreviews;
	private TabData mTabData;
	private TabNeighbors mTabNeighbors;
	
	public MapController(String id, Map map)
	{
		super(map);
		mUI = new TabbedUI();
		mMapID = id;
		mMap = map;
		mPreviews = new PreviewManager();
		
		mTabData = new TabData(mMap.getName(), mMap.getTilemapID(), mMap.getBorderID());
		mTabNeighbors = new TabNeighbors(mMapID, mPreviews, mMap.getNeighbors());
		
		mTabData.setOnNameChange(name -> { mMap.setName(name); change(); });
		mTabData.setOnTilemapIDChange(tid -> { mMap.setTilemapID(tid); change(); });
		mTabData.setOnBorderIDChange(tid -> { mMap.setBorderID(tid); change(); });
		mTabNeighbors.setOnNeighborChange((d, mid, o) -> setNeighbor(d, mid, o));
		
		mUI.addTab("Data", mTabData).addTab("Neighbors", mTabNeighbors);
	}
	
	private void setNeighbor(Direction d, String id, int o)
	{
		java.util.Map<Direction, Neighbor> neighbors = mMap.getNeighbors();
		
		if(id == null)
		{
			neighbors.remove(d);
		}
		else
		{
			neighbors.put(d, new Neighbor(id, o));
		}
		
		mTabNeighbors.redraw();
		change();
	}

	@Override
	public Node getUI()
	{
		return mUI.getNode();
	}
}
