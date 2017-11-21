package controller.map;

import java.util.Map;
import java.util.Optional;

import controller.ContentController;
import controller.ModalDialog;
import javafx.scene.Node;
import lib.Utils;
import lib.observe.ObservableList;
import lib.observe.ObservableStore;
import model.Direction;
import model.Event;
import model.Map.Neighbor;
import model.Tilemap;
import view.TabbedUI;
import view.map.Preview;
import view.map.PreviewManager;
import view.map.TabData;
import view.map.TabEvents;
import view.map.TabNeighbors;
import view.map.create.NewEventUI;

public class MapController extends ContentController
{
	private final TabbedUI mUI;
	private final String mMapID;
	private final model.Map mMap;
	private final PreviewManager mPreviews;
	private final ObservableList<Event> mEvents;
	private final TabData mTabData;
	private final TabNeighbors mTabNeighbors;
	private final TabEvents mTabEvents;
	private final int mWidth, mHeight;
	
	public MapController(String id, model.Map map)
	{
		super(map);
		
		Tilemap tm = Utils.loadTilemap(map.getTilemapID());
		
		mUI = new TabbedUI();
		mMapID = id;
		mMap = map;
		mPreviews = new PreviewManager();
		mEvents = ObservableList.Instantiate(mMap.getEvents());
		mWidth = tm.getWidth();
		mHeight = tm.getHeight();
		
		mTabData = new TabData(mMap.getName(), mMap.getTilemapID(), mMap.getBorderID());
		mTabNeighbors = new TabNeighbors(mMapID, mPreviews, mMap.getNeighbors());
		mTabEvents = new TabEvents(createIDView(), Preview.Create(mMapID));
		
		mTabData.setOnNameChange(name -> { mMap.setName(name); change(); });
		mTabData.setOnTilemapIDChange(tid -> { mMap.setTilemapID(tid); change(); });
		mTabData.setOnBorderIDChange(tid -> { mMap.setBorderID(tid); change(); });
		
		mTabNeighbors.setOnNeighborChange((d, mid, o) -> setNeighbor(d, mid, o));
		
		mTabEvents.setOnAdd(() -> createNewEvent().ifPresent(e -> addEvent(e)));
		mTabEvents.setOnDelete(eid -> deleteEvent(eid));
		mTabEvents.setOnSelectEvent(eid -> selectEvent(eid));
		
		mEvents.addObserver(o -> mTabEvents.clearSelection());
		
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
	
	private void addEvent(Event e)
	{
		act(
			() -> { mEvents.add(e); mTabEvents.selectEvent(e.getID()); },
			() -> mEvents.remove(e));
	}
	
	private void deleteEvent(String id)
	{
		getEvent(id).ifPresent(e ->
		{
			act(
				() -> mEvents.remove(e),
				() -> mEvents.add(e));
		});
	}
	
	private void selectEvent(String id)
	{
		getEvent(id).ifPresent(e ->
		{
			EventController ec = new EventController(mWidth, mHeight, e.getID(), e.getLocation(), e.getArgument());
			mTabEvents.setEventView(ec.getUI());
		});
	}
	
	private Optional<Event> getEvent(String id)
	{
		return mEvents.stream().filter(e -> e.getID().equals(id)).findAny();
	}
	
	private Optional<Event> createNewEvent()
	{
		Event e = null;
		NewEventUI ui = new NewEventUI(mWidth, mHeight);
		ModalDialog dialog = new ModalDialog("Create new Event", ui);
		
		dialog.showAndWait();
		
		if(dialog.isSuccessful())
		{
			e = ui.createEvent();
		}
		
		return Optional.ofNullable(e);
	}
	
	private ObservableStore<String> createIDView()
	{
		return ObservableStore.Instantiate(mEvents, e -> e.getID());
	}

	@Override
	public Node getUI()
	{
		return mUI.getNode();
	}
}
