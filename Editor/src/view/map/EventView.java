package view.map;

import java.util.Arrays;
import java.util.function.Consumer;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.EnterableTextField;
import lib.misc.Vec2;
import model.Event;
import view.UI;

public class EventView implements UI
{
	private final GridPane mRoot;
	private final Property<String> mID;
	private final Property<Number> mX, mY;
	private final ComboBox<String> mTypeSelector;
	private Consumer<String> mOnChangeType;
	private ArgumentView mArgumentUI;
	
	public EventView(String id, int w, int h, Vec2 pos, ArgumentView arg)
	{
		mRoot = new GridPane();
		mArgumentUI = arg;
		mID = new SimpleStringProperty();
		mX = new SimpleIntegerProperty();
		mY = new SimpleIntegerProperty();
		mTypeSelector = new ComboBox<>();
		
		mID.setValue(id);
		mX.setValue(pos.getX());
		mY.setValue(pos.getY());
		
		Label lblID = new Label("ID");
		Label lblX = new Label("X");
		Label lblY = new Label("Y");
		Label lblType = new Label("Type");
		
		Node argument = mArgumentUI.getNode();
		EnterableTextField tfID = new EnterableTextField(id);
		EnterableTextField tfX = new EnterableTextField("" + pos.getX());
		EnterableTextField tfY = new EnterableTextField("" + pos.getY());
		
		mTypeSelector.setMaxWidth(Double.MAX_VALUE);
		
		mRoot.add(lblID, 0, 0);
		mRoot.add(tfID, 1, 0, 3, 1);
		mRoot.add(lblX, 0, 1);
		mRoot.add(tfX, 1, 1);
		mRoot.add(lblY, 2, 1);
		mRoot.add(tfY, 3, 1);
		mRoot.add(lblType, 0, 2);
		mRoot.add(mTypeSelector, 1, 2, 3, 1);
		mRoot.add(argument, 0, 3, 4, 1);
		
		Arrays.stream(Event.Type.values()).forEachOrdered(t -> mTypeSelector.getItems().add(t.toString()));
		mTypeSelector.getSelectionModel().select(mArgumentUI.getType());
		
		GridPane.setHgrow(argument, Priority.ALWAYS);
		
		mRoot.setPadding(new Insets(5D, 5D, 0D, 5D));
		mRoot.setHgap(5D);
		mRoot.setVgap(5D);
		
		tfID.addValidations(EnterableTextField.IS_NOT_EMPTY);
		tfX.addValidations(EnterableTextField.IS_POSITIVE_INT.lessThan(w));
		tfY.addValidations(EnterableTextField.IS_POSITIVE_INT.lessThan(h));
		
		tfID.setCallback(s -> mID.setValue(s));
		tfX.setCallback(s -> mX.setValue(Integer.parseInt(s)));
		tfY.setCallback(s -> mY.setValue(Integer.parseInt(s)));
		
		mTypeSelector.valueProperty().addListener((ob, o, n) -> changeArgument(n));
	}
	
	public void setOnChangeArgumentType(Consumer<String> cb) { mOnChangeType = cb; }
	
	public Property<String> idProperty() { return mID; }
	public Property<Number> xProperty() { return mX; }
	public Property<Number> yProperty() { return mY; }

	public void setArgumentView(ArgumentView ui)
	{
		Node node = ui.getNode();
		
		mRoot.getChildren().remove(mArgumentUI);
		mRoot.add(node, 0, 3, 4, 1);
		GridPane.setHgrow(node, Priority.ALWAYS);
		
		mArgumentUI = ui;
	}
	
	private void changeArgument(String t)
	{
		if(mOnChangeType != null)
		{
			mOnChangeType.accept(t);
			
			mTypeSelector.getSelectionModel().select(mArgumentUI.getType());
		}
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
