package controller.map;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import controller.ContentController;
import controller.ModalDialog;
import javafx.scene.Node;
import lib.Utils;
import lib.observe.ObservableList;
import lib.observe.ObservableStore;
import lib.action.Action;
import lib.misc.Vec2;
import model.Direction;
import model.Event;
import model.Map.Neighbor;
import model.Tilemap;
import view.TabbedUI;
import view.map.EventCanvas;
import view.map.Preview;
import view.map.PreviewManager;
import view.map.TabData;
import view.map.TabEvents;
import view.map.TabNeighbors;
import view.map.create.NewEventUI;

import static lib.Utils.ReversableAction;
import static lib.Utils.Append;

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
		
		mTabData = initDataTab();
		mTabNeighbors = initNeighborsTab();
		mTabEvents = initEventsTab();
		
		mEvents.addObserver(o -> refreshEventView());
		
		refreshEventView();
		
		mUI.addTab("Data", mTabData).addTab("Events", mTabEvents).addTab("Neighbors", mTabNeighbors);
	}
	
	private TabData initDataTab()
	{
		return Utils.with(new TabData(mMap.getName(), mMap.getTilemapID(), mMap.getBorderID()), tab -> {
			tab.setOnNameChange(name -> { mMap.setName(name); change(); });
			tab.setOnTilemapIDChange(tid -> { mMap.setTilemapID(tid); change(); });
			tab.setOnBorderIDChange(tid -> { mMap.setBorderID(tid); change(); });
		});
	}
	
	private TabNeighbors initNeighborsTab()
	{
		return Utils.with(new TabNeighbors(mMapID, mPreviews, mMap.getNeighbors()), tab -> {
			tab.setOnNeighborChange((d, mid, o) -> setNeighbor(d, mid, o));
		});
	}
	
	private TabEvents initEventsTab()
	{
		return Utils.with(new TabEvents(createIDView(), Preview.Create(mMapID)), tab -> {
			tab.setOnAdd(() -> createNewEvent().ifPresent(e -> addEvent(e)));
			tab.setOnDelete(eid -> deleteEvent(eid));
			tab.setOnSelectEvent(eid -> selectEvent(eid));
			
			tab.getEventView().setOnDrag(id -> tab.selectEvent(id));
			tab.getEventView().setOnDrop(p -> moveCurrentEvent(p));
		});
	}
	
	private void refreshEventView()
	{
		EventCanvas view = mTabEvents.getEventView();
		
		mTabEvents.clearSelection();
		
		view.clear();
		mEvents.stream().forEach(e -> view.add(e));
	}
	
	private void moveCurrentEvent(Vec2 p)
	{
		getEvent(mTabEvents.getSelectedEvent()).ifPresent(event -> {
			if(!event.getLocation().equals(p) && !mEvents.stream().anyMatch(e -> e.getLocation().equals(p)))
			{
				act(Append(ReversableAction(event, "Location", p), () -> mTabEvents.selectEvent(event.getID())));
			}
		});
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
			Consumer<Action> handler = a -> {
				act(Utils.Append(a, () -> {
					mEvents.change();
					mTabEvents.selectEvent(e.getID());
				}));
			};
			
			EventController ec = new EventController(handler, mWidth, mHeight, e);
			
			mTabEvents.setEventView(ec.getUI().getNode());
			mTabEvents.getEventView().add(e);
			mTabEvents.getEventView().setSelected(e.getLocation());
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
