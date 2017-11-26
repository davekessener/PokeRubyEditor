package view.map;

import java.util.function.Consumer;

import controller.EditorController;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import lib.observe.ObservableStore;
import lib.store.Store;
import view.UI;

public class TabEvents implements UI
{
	private final BorderPane mRoot, mEventView;
	private final Preview mPreview;
	private final Store<String> mEvents;
	private final ComboBox<String> mEventSelector;
	private final EventCanvas mOverview;
	private Runnable mOnAdd;
	private Consumer<String> mOnDelete;
	private Consumer<String> mOnSelectEvent;
	
	public TabEvents(ObservableStore<String> events, Preview preview)
	{
		mRoot = new BorderPane();
		mEventView = new BorderPane();
		mOverview = new EventCanvas(preview.getMapWidth(), preview.getMapHeight(), preview.getTileSize());
		mPreview = preview;
		mEvents = events;
		
		mPreview.drawGridProperty().bind(EditorController.Instance.getOptions().drawGridProperty());
		
		StackPane st = new StackPane();
		st.getChildren().addAll(mPreview, mOverview);
		
		ScrollPane sc = new ScrollPane();
		sc.setContent(st);
		
		mRoot.setCenter(sc);
		
		sc = new ScrollPane();
		
		GridPane gp = new GridPane();

		Button addBtn = new Button("+");
		Button delBtn = new Button("-");
		
		mEventSelector = new ComboBox<>();
		
		Runnable refreshIDList = () -> {
			mEventSelector.getItems().clear();
			events.forEach(id -> mEventSelector.getItems().add(id));
		};
		
		refreshIDList.run();
		
		addBtn.setPrefWidth(25D);
		delBtn.setPrefWidth(25D);
		mEventSelector.setMaxWidth(Double.MAX_VALUE);
		gp.addRow(0, delBtn, mEventSelector, addBtn);
		
		GridPane.setHgrow(mEventSelector, Priority.ALWAYS);
		
		mEventView.setTop(gp);
		
		sc.setContent(mEventView);
		
		mRoot.setRight(sc);
		
		mPreview.mousedOverTileProperty().addListener((ob, o, n) -> EditorController.Instance.getMenuManager().setStatus(n == null ? "" : n.toString()));
		
		addBtn.setOnAction(e -> addEvent());
		delBtn.setOnAction(e -> deleteEvent(mEventSelector.getSelectionModel().getSelectedIndex()));
		mEventSelector.valueProperty().addListener((ob, o, n) -> onSelectEvent(n));
		
		events.addObserver(o -> refreshIDList.run());
		
		mPreview.draw();
	}
	
	public void setOnAdd(Runnable cb) { mOnAdd = cb; }
	public void setOnDelete(Consumer<String> cb) { mOnDelete = cb; }
	public void setOnSelectEvent(Consumer<String> cb) { mOnSelectEvent = cb; }
	
	public EventCanvas getEventView() { return mOverview; }
	
	public void clearSelection()
	{
		mEventSelector.getSelectionModel().select(-1);
		mEventView.setCenter(null);
	}
	
	public void selectEvent(String id)
	{
		mEventSelector.getSelectionModel().select(id);
		onSelectEvent(id);
	}
	
	public String getSelectedEvent()
	{
		return mEventSelector.getSelectionModel().getSelectedItem();
	}
	
	public void setEventView(Node ev)
	{
		mEventView.setCenter(ev);
	}
	
	private void onSelectEvent(String id)
	{
		if(mOnSelectEvent != null)
		{
			mOnSelectEvent.accept(id);
		}
	}
	
	private void addEvent()
	{
		if(mOnAdd != null)
		{
			mOnAdd.run();
		}
	}
	
	private void deleteEvent(int idx)
	{
		if(idx >= 0 && mOnDelete != null)
		{
			mOnDelete.accept(mEvents.get(idx));
		}
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
