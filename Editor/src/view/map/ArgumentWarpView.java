package view.map;

import java.util.Arrays;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lib.EnterableTextField;
import lib.IDValidator;
import lib.Utils;
import lib.action.Action;
import model.Event;
import model.Event.Argument;
import model.Event.WarpArgument;

public class ArgumentWarpView extends ArgumentView
{
	private final GridPane mRoot;
	private final WarpArgument mArgument;
	
	public ArgumentWarpView(Consumer<Action> onchange, Argument a)
	{
		super(a.getType().toString(), onchange);
		
		mRoot = new GridPane();
		mArgument = (WarpArgument) a;
		
		EnterableTextField tfMap = new EnterableTextField(mArgument.getMapID());
		EnterableTextField tfTarget = new EnterableTextField(mArgument.getTargetID());
		ComboBox<String> cbDirection = new ComboBox<>();
		
		Arrays.stream(Event.WarpArgument.Direction.values()).forEachOrdered(d -> cbDirection.getItems().add(d.toString().toLowerCase()));
		
		cbDirection.getSelectionModel().select(mArgument.getDirection());
		cbDirection.valueProperty().addListener((ob, o, n) -> act(Utils.ReversableAction(mArgument, "Direction", n)));
		cbDirection.setMaxWidth(Double.MAX_VALUE);
		
		tfMap.addValidations(new IDValidator("map"));
		tfTarget.addValidations(EnterableTextField.IS_NOT_EMPTY);
		
		tfMap.setCallback(s -> act(Utils.ReversableAction(mArgument, "MapID", s)));
		tfTarget.setCallback(s -> act(Utils.ReversableAction(mArgument, "TargetID", s)));
		
		mRoot.addRow(0, new Label("Map"), tfMap);
		mRoot.addRow(1, new Label("Target"), tfTarget);
		mRoot.addRow(2, new Label("Direction"), cbDirection);

		mRoot.setVgap(3D);
		mRoot.setHgap(5D);
		mRoot.setPadding(new Insets(5D, 0D, 0D, 0D));
		GridPane.setHgrow(tfMap, Priority.ALWAYS);
		GridPane.setHgrow(tfTarget, Priority.ALWAYS);
		GridPane.setHgrow(cbDirection, Priority.ALWAYS);
	}
	
	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
