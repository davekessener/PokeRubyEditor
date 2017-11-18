package view.map;

import java.util.function.Function;
import java.util.stream.Collectors;

import controller.EditorController;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lib.observe.IObservableList;
import model.Event;
import view.UI;

public class TabEvents implements UI
{
	private final BorderPane mRoot;
	private final Preview mPreview;
	private final IObservableList<Event> mEvents;
	private Runnable mOnAdd;
	private Function<Integer, Void> mOnDelete;
	
	public TabEvents(IObservableList<Event> events, Preview preview)
	{
		mRoot = new BorderPane();
		mPreview = preview;
		mEvents = events;
		
		mPreview.drawGridProperty().bind(EditorController.Instance.getOptions().drawGridProperty());
		
		StackPane st = new StackPane();
		st.getChildren().add(mPreview);
		
		ScrollPane sc = new ScrollPane();
		sc.setContent(st);
		
		VBox vb = new VBox();
		HBox hb = new HBox();

		Button addBtn = new Button("+");
		Button delBtn = new Button("-");
		
		ComboBox<String> eventIDs = new ComboBox<>();
		
		Runnable refreshIDList = () -> eventIDs.getItems().addAll(mEvents.stream().map(e -> e.getID()).collect(Collectors.toList()));
		
		refreshIDList.run();
		
		hb.setPadding(new Insets(0D, 0D, 5D, 3D));
		hb.getChildren().addAll(delBtn, eventIDs, addBtn);
		
		vb.getChildren().add(hb);
		
		mRoot.setCenter(sc);
		mRoot.setRight(vb);
		
		EditorController.Instance.getMenuManager().setStatus(mPreview.mousedOverTileProperty());
		
		addBtn.setOnAction(e -> addEvent());
		delBtn.setOnAction(e -> deleteEvent(eventIDs.getSelectionModel().getSelectedIndex()));
		
		mEvents.addObserver(o -> refreshIDList.run());
		
		mPreview.draw();
	}
	
	public void setOnAdd(Runnable cb) { mOnAdd = cb; }
	public void setOnDelete(Function<Integer, Void> cb) { mOnDelete = cb; }
	
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
			mOnDelete.apply(idx);
		}
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
