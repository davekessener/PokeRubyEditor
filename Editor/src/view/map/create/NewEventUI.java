package view.map.create;

import java.util.Arrays;

import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.EnterableTextField;
import lib.misc.Vec2;
import model.Event;
import view.BasicDialogUI;

public class NewEventUI extends BasicDialogUI
{
	private final GridPane mRoot;
	private final BorderPane mArgumentContainer;
	private final TextField mID, mX, mY;
	private NewArgumentUI mArgumentUI;
	
	public NewEventUI(int w, int h)
	{
		mRoot = new GridPane();
		mArgumentContainer = new BorderPane();
		
		ComboBox<String> cb = new ComboBox<>();
		EnterableTextField tfID = new EnterableTextField("");
		EnterableTextField tfX = new EnterableTextField("0");
		EnterableTextField tfY = new EnterableTextField("0");
		
		mRoot.add(new Label("ID"), 0, 0);
		mRoot.add(tfID, 1, 0, 3, 1);
		mRoot.addRow(1, new Label("X"), tfX, new Label("Y"), tfY);
		mRoot.add(new Label("Type"), 0, 2);
		mRoot.add(cb, 1, 2, 3, 1);
		mRoot.add(mArgumentContainer, 0, 3, 4, 1);

		mRoot.setHgap(5D);
		mRoot.setVgap(3D);
		GridPane.setHgrow(tfID, Priority.ALWAYS);
		GridPane.setHgrow(tfX, Priority.ALWAYS);
		GridPane.setHgrow(tfY, Priority.ALWAYS);
		GridPane.setHgrow(cb, Priority.ALWAYS);
		GridPane.setHgrow(mArgumentContainer, Priority.ALWAYS);
		
		Arrays.stream(Event.Type.values()).forEachOrdered(t -> cb.getItems().add(t.toString()));
		
		cb.setMaxWidth(Double.MAX_VALUE);
		cb.getSelectionModel().selectFirst();
		cb.valueProperty().addListener((ob, o, n) -> selectArgumentType(n));
		
		tfID.addValidations(EnterableTextField.IS_NOT_EMPTY);
		tfX.addValidations(EnterableTextField.IS_POSITIVE_INT.lessThan(w));
		tfY.addValidations(EnterableTextField.IS_POSITIVE_INT.lessThan(h));
		
		mID = tfID;
		mX = tfX;
		mY = tfY;
		
		selectArgumentType(cb.getSelectionModel().getSelectedItem().toLowerCase());
	}
	
	public Event createEvent()
	{
		Event e = new Event();
		
		e.setID(mID.getText());
		e.setLocation(new Vec2(Integer.parseInt(mX.getText()), Integer.parseInt(mY.getText())));
		e.setArgument(mArgumentUI.create());
		
		return e;
	}
	
	private void selectArgumentType(String t)
	{
		mArgumentUI = NewArgumentUI.Instantiate(t);
		
		mArgumentContainer.setCenter(mArgumentUI.getNode());
		
		if(this.hasStage())
		{
			this.getStage().sizeToScene();
		}
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}

	@Override
	public boolean canCreate()
	{
		return !mID.getText().isEmpty() && mArgumentUI.canCreate();
	}
}
