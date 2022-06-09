package com.github.rooneyandshadows.lightbulb.dialogs.picker_dialog_adapter;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.rooneyandshadows.lightbulb.dialogs.base.BasePickerDialogFragment;
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.bottomsheet.BottomSheetDialogConstraints;
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints;
import com.github.rooneyandshadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder;
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyAdapterDataModel;
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.EasyRecyclerAdapter;
import com.github.rooneyandshadows.lightbulb.recycleradapters.abstraction.callbacks.EasyAdapterSelectionChangedListener;
import com.github.rooneyandshadows.lightbulb.dialogs.R;

import java.util.ArrayList;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPickerDialog<ItemType extends EasyAdapterDataModel> extends BasePickerDialogFragment<int[]> {
    private static final String ADAPTER_SELECTION_TAG = "ADAPTER_SELECTION_TAG";
    private static final String ADAPTER_SELECTION_DRAFT_TAG = "ADAPTER_SELECTION_DRAFT_TAG";
    protected RecyclerView recyclerView;
    private RecyclerView.ItemDecoration itemDecoration;
    protected EasyRecyclerAdapter<ItemType> adapter;
    private final EasyAdapterSelectionChangedListener selectionListener;

    public AdapterPickerDialog() {
        super(new AdapterPickerDialogSelection(null, null));
        selectionListener = newSelection -> {
            if (isDialogShown()) selection.setDraftSelection(newSelection, false);
            else selection.setCurrentSelection(newSelection, false);
        };
    }

    public static <ItemType extends EasyAdapterDataModel> AdapterPickerDialog<ItemType> newInstance(
            String title, String message, DialogButtonConfiguration positive, DialogButtonConfiguration negative,
            boolean cancelable, DialogTypes dialogType, DialogAnimationTypes animationType) {
        AdapterPickerDialog<ItemType> dialogFragment = new AdapterPickerDialog<>();
        dialogFragment.setArguments(
                new DialogBundleHelper()
                        .withTitle(title)
                        .withMessage(message)
                        .withPositiveButtonConfig(positive)
                        .withNegativeButtonConfig(negative)
                        .withCancelable(cancelable)
                        .withShowing(false)
                        .withDialogType(dialogType == null ? DialogTypes.NORMAL : dialogType)
                        .withAnimation(animationType == null ? DialogAnimationTypes.NO_ANIMATION : animationType)
                        .getBundle()
        );
        return dialogFragment;
    }

    @Override
    protected View setDialogLayout(LayoutInflater inflater) {
        return View.inflate(getContext(), R.layout.dialog_picker_normal, null);
    }

    @Override
    protected final void configureContent(View view, Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.dialogRecycler);
        recyclerView.setItemAnimator(null);
        adapter.addOrReplaceSelectionChangedListener(selectionListener);
        configureRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected RegularDialogConstraints getRegularConstraints() {
        int orientation = getResources().getConfiguration().orientation;
        return new RegularDialogConstraintsBuilder(this)
                .Default()
                .withMinWidth(getPercentOfWindowWidth(orientation == Configuration.ORIENTATION_PORTRAIT ? 70 : 60))
                .withMaxHeight(getPercentOfWindowHeight(orientation == Configuration.ORIENTATION_PORTRAIT ? 65 : 85))
                .build();
    }

    @Override
    protected void synchronizeSelectUi() {
        int[] newSelection = selection.hasDraftSelection() ? selection.getDraftSelection() : selection.getCurrentSelection();
        if (adapter != null)
            adapter.selectPositions(newSelection);
    }

    protected void configureRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (recyclerView.getItemDecorationCount() > 0)
            recyclerView.removeItemDecorationAt(0);
        if (itemDecoration != null)
            recyclerView.addItemDecoration(itemDecoration, 0);
    }

    @Override
    protected void setupRegularDialog(RegularDialogConstraints constraints, Window dialogWindow, View dialogLayout, Rect fgPadding) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(constraints.getMaxWidth(), View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec);
        int desiredHeightForRecycler = recyclerView.getMeasuredHeight();
        int horPadding = fgPadding.left + fgPadding.right;
        int verPadding = fgPadding.top + fgPadding.bottom;
        int desiredWidth = dialogLayout.getMeasuredWidth();
        int desiredHeight = dialogLayout.getMeasuredHeight();
        if (desiredHeight > constraints.getMaxHeight()) {
            desiredHeight -= desiredHeightForRecycler;
            int recyclerExactHeight = constraints.getMaxHeight() - desiredHeight;
            recyclerView.getLayoutParams().height = recyclerExactHeight;
            desiredHeight += recyclerExactHeight;
        }
        desiredWidth = constraints.resolveWidth(desiredWidth);
        desiredHeight = constraints.resolveHeight(desiredHeight);
        dialogWindow.setLayout(desiredWidth + horPadding, desiredHeight + verPadding);
    }

    @Override
    protected void setupFullScreenDialog(Window dialogWindow, View dialogLayout) {
        LinearLayoutCompat.LayoutParams parameters = (LinearLayoutCompat.LayoutParams) recyclerView.getLayoutParams();
        parameters.weight = 1;
        int maxHeight = getWindowHeight();
        int desiredHeight = dialogLayout.getMeasuredHeight();
        int desiredHeightForRecycler = recyclerView.getMeasuredHeight();
        if (desiredHeight > maxHeight) {
            desiredHeight -= desiredHeightForRecycler;
            recyclerView.getLayoutParams().height = maxHeight - desiredHeight;
        }
    }

    @Override
    protected void setupBottomSheetDialog(BottomSheetDialogConstraints constraints, Window dialogWindow, View dialogLayout) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getWindowWidth(), View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec);
        int desiredHeight = dialogLayout.getMeasuredHeight();
        int desiredHeightForRecycler = recyclerView.getMeasuredHeight();
        if (desiredHeight > constraints.getMaxHeight()) {
            desiredHeight -= desiredHeightForRecycler;
            int recyclerExactHeight = constraints.getMaxHeight() - desiredHeight;
            recyclerView.getLayoutParams().height = recyclerExactHeight;
            desiredHeight += recyclerExactHeight;
        }
        desiredHeight = constraints.resolveHeight(desiredHeight);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, desiredHeight);
    }

    @Override
    protected void create(Bundle dialogArguments, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            if (dialogArguments == null) throw new IllegalArgumentException("Bundle args required");
            if (hasSelection()) selection.setCurrentSelection(selection.getCurrentSelection());
            else selection.setCurrentSelection(dialogArguments.getIntArray(ADAPTER_SELECTION_TAG));
        } else {
            selection.setCurrentSelection(savedInstanceState.getIntArray(ADAPTER_SELECTION_TAG));
            selection.setDraftSelection(savedInstanceState.getIntArray(ADAPTER_SELECTION_DRAFT_TAG));
        }
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        super.saveInstanceState(outState);
        if (selection.getCurrentSelection() != null)
            outState.putIntArray(ADAPTER_SELECTION_TAG, selection.getCurrentSelection());
        if (selection.getDraftSelection() != null)
            outState.putIntArray(ADAPTER_SELECTION_DRAFT_TAG, selection.getDraftSelection());
    }

    public void setSelection(int newSelection) {
        selection.setCurrentSelection(new int[]{newSelection});
    }

    public void setAdapter(EasyRecyclerAdapter<ItemType> adapter) {
        this.adapter = adapter;
    }

    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        this.itemDecoration = itemDecoration;
    }

    public void setData(ArrayList<ItemType> data) {
        adapter.setCollection(data);
    }
}