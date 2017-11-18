package controller;

import java.util.Map;

import javafx.scene.Node;
import lib.observe.IObservableList;
import lib.observe.ObservableList;
import model.Direction;
import model.Event;
import model.Map.Neighbor;
import view.TabbedUI;
import view.map.Preview;
import view.map.PreviewManager;
import view.map.TabData;
import view.map.TabEvents;
import view.map.TabNeighbors;

public class MapController extends ContentController
{
	private final TabbedUI mUI;
	private final String mMapID;
	private final model.Map mMap;
	private final PreviewManager mPreviews;
	private final IObservableList<Event> mEvents;
	private final TabData mTabData;
	private final TabNeighbors mTabNeighbors;
	private final TabEvents mTabEvents;
	
	public MapController(String id, model.Map map)
	{
		super(map);
		mUI = new TabbedUI();
		mMapID = id;
		mMap = map;
		mPreviews = new PreviewManager();
		mEvents = ObservableList.Instantiate(mMap.getEvents());
		
		mTabData = new TabData(mMap.getName(), mMap.getTilemapID(), mMap.getBorderID());
		mTabNeighbors = new TabNeighbors(mMapID, mPreviews, mMap.getNeighbors());
		mTabEvents = new TabEvents(mEvents, Preview.Create(mMapID));
		
		mTabData.setOnNameChange(name -> { mMap.setName(name); change(); });
		mTabData.setOnTilemapIDChange(tid -> { mMap.setTilemapID(tid); change(); });
		mTabData.setOnBorderIDChange(tid -> { mMap.setBorderID(tid); change(); });
		mTabNeighbors.setOnNeighborChange((d, mid, o) -> setNeighbor(d, mid, o));
		
		mUI.addTab("Data", mTabData).addTab("Events", mTabEvents).addTab("Neighbors", mTabNeighbors);
	}
	
	private void setNeighbor(Direction d, String id, int o)
	{
		Map<Direction, Neighbor> neighbors = mMap.getNeighbors();
		
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
