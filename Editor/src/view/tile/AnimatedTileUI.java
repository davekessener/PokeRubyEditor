package view.tile;

import java.util.List;
import java.util.function.Consumer;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lib.EnterableTextField;
import lib.misc.Vec2;
import lib.observe.ObservableList;
import model.Tileset.Type;

import static javafx.beans.property.ReadOnlyIntegerProperty.readOnlyIntegerProperty;

public class AnimatedTileUI extends BasicTileUI
{
	private final GridPane mRoot;
	private List<Vec2> mFrames;
	private TextField mCurrentFrame;
	private Consumer<Integer> mOnAddFrame, mOnRemoveFrame;
	private Consumer<Integer> mOnPeriodChange;
	private final Property<Number> mSelectedFrame;

	public AnimatedTileUI(ObservableList<Vec2> frames, int period, String anims)
	{
		super(anims);
		
		mRoot = new GridPane();
		mFrames = frames;
		mSelectedFrame = new SimpleIntegerProperty();
		
		mRoot.setHgap(3D);
		
		EnterableTextField tfFrame, tfPeriod;
		tfFrame = new EnterableTextField("1");
		tfPeriod = new EnterableTextField("" + period);
		
		tfFrame.addValidations(EnterableTextField.IS_POSITIVE_INT.lessThan(() -> mFrames.size() + 1));
		tfPeriod.addValidations(EnterableTextField.IS_POSITIVE_INT);
		
		Node root = super.getRoot();
		Label lblMax = new Label("/ " + mFrames.size());
		Label lblMs = new Label("ms");
		Button addPrev = new Button("V");
		Button addNext = new Button("^");
		Button deleteBtn = new Button("-");
		Button btnPrev = new Button("<");
		Button btnNext = new Button(">");
		lblMax.setAlignment(Pos.CENTER_LEFT);
		lblMs.setAlignment(Pos.CENTER_LEFT);
		mRoot.setAlignment(Pos.CENTER);
		
		mRoot.add(root, 0, 0, 1, 2);
		mRoot.add(addNext, 1, 0);
		mRoot.add(addPrev, 1, 1);
		mRoot.add(btnPrev, 2, 0, 1, 2);
		mRoot.add(tfFrame, 3, 0, 1, 2);
		mRoot.add(lblMax, 4, 0, 1, 2);
		mRoot.add(btnNext, 5, 0, 1, 2);
		mRoot.add(deleteBtn, 6, 0, 1, 2);
		mRoot.add(tfPeriod, 7, 0, 1, 2);
		mRoot.add(lblMs, 8, 0, 1, 2);

		GridPane.setValignment(root, VPos.CENTER);
		GridPane.setValignment(btnPrev, VPos.CENTER);
		GridPane.setValignment(tfFrame, VPos.CENTER);
		GridPane.setValignment(lblMax, VPos.CENTER);
		GridPane.setValignment(btnNext, VPos.CENTER);
		GridPane.setValignment(deleteBtn, VPos.CENTER);
		GridPane.setValignment(tfPeriod, VPos.CENTER);
		GridPane.setValignment(lblMs, VPos.CENTER);

		tfFrame.setCallback(s -> mSelectedFrame.setValue(Integer.parseInt(s) - 1));
		
		mCurrentFrame = tfFrame;
		
		deleteBtn.disableProperty().bind(readOnlyIntegerProperty(frames.sizeProperty()).lessThan(2));
		btnPrev.disableProperty().bind(readOnlyIntegerProperty(mSelectedFrame).isEqualTo(0));
		btnNext.disableProperty().bind(readOnlyIntegerProperty(mSelectedFrame).isEqualTo(readOnlyIntegerProperty(frames.sizeProperty()).subtract(1)));
		frames.addObserver(o -> lblMax.setText("/ " + frames.size()));

		btnPrev.setOnAction(e -> setSelectedFrame(getSelectedFrame() - 1));
		btnNext.setOnAction(e -> setSelectedFrame(getSelectedFrame() + 1));
		
		addPrev.setOnAction(e -> addFrame(getSelectedFrame()));
		addNext.setOnAction(e -> addFrame(getSelectedFrame() + 1));
		deleteBtn.setOnAction(e -> removeFrame(getSelectedFrame()));
		tfPeriod.setCallback(s -> changePeriod(Integer.parseInt(s)));
		
		mSelectedFrame.addListener(o -> this.selectTile(mFrames.get(getSelectedFrame())));
		mSelectedFrame.addListener(o -> mCurrentFrame.setText("" + (mSelectedFrame.getValue().intValue() + 1)));
	}
	
	public int getSelectedFrame() { return (int) mSelectedFrame.getValue(); }
	public void setSelectedFrame(int idx) { mSelectedFrame.setValue(idx); }
	
	public void setOnAddFrame(Consumer<Integer> cb) { mOnAddFrame = cb; }
	public void setOnRemoveFrame(Consumer<Integer> cb) { mOnRemoveFrame = cb; }
	public void setOnPeriodChange(Consumer<Integer> cb) { mOnPeriodChange = cb; }
	
	private void addFrame(int idx)
	{
		if(mOnAddFrame != null)
		{
			mOnAddFrame.accept(idx);
		}
	}
	
	private void removeFrame(int idx)
	{
		if(mOnRemoveFrame != null)
		{
			mOnRemoveFrame.accept(idx);
		}
	}
	
	private void changePeriod(int p)
	{
		if(mOnPeriodChange != null)
		{
			mOnPeriodChange.accept(p);
		}
	}
	
	@Override
	protected void onBind()
	{
		this.selectTile(mFrames.get(getSelectedFrame()));
	}

	@Override
	public Type getType()
	{
		return Type.ANIMATED;
	}

	@Override
	public Parent getNode()
	{
		return mRoot;
	}
}
