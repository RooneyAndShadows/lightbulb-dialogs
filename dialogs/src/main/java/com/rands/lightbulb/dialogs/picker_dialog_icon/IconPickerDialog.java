package com.rands.lightbulb.dialogs.picker_dialog_icon;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.rands.lightbulb.commons.utils.ResourceUtils;
import com.rands.lightbulb.dialogs.base.BaseDialogFragment;
import com.rands.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints;
import com.rands.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder;
import com.rands.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("unused")
public class IconPickerDialog extends AdapterPickerDialog<IconPickerAdapter.IconModel> {
    private final static String ADAPTER_STATE_TAG = "ADAPTER_STATE_TAG";
    private final static int iconSize = ResourceUtils.dpToPx(50);
    private int spans;
    private int lastVisibleItemPosition = -1;
    private RecyclerView recyclerView;

    public static IconPickerDialog newInstance(
            String title, String message, BaseDialogFragment.DialogButtonConfiguration positive, BaseDialogFragment.DialogButtonConfiguration negative,
            boolean cancelable, BaseDialogFragment.DialogAnimationTypes animationType) {
        IconPickerDialog dialogFragment = new IconPickerDialog();
        Bundle args = new Bundle();
        dialogFragment.setArguments(new BaseDialogFragment.DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(BaseDialogFragment.DialogTypes.NORMAL)
                .withAnimation(animationType == null ? BaseDialogFragment.DialogAnimationTypes.NO_ANIMATION : animationType)
                .getBundle()
        );
        return dialogFragment;
    }

    @Override
    protected void create(Bundle dialogArguments, Bundle savedInstanceState) {
        super.create(dialogArguments, savedInstanceState);
        if (savedInstanceState != null)
            lastVisibleItemPosition = savedInstanceState.getInt("LAST_VISIBLE_ITEM");
    }

    @Override
    protected void saveInstanceState(Bundle outState) {
        super.saveInstanceState(outState);
        GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        if (manager != null)
            outState.putInt("LAST_VISIBLE_ITEM", manager.findFirstVisibleItemPosition());
    }

    @Override
    protected void configureRecyclerView(RecyclerView recyclerView) {
        super.configureRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        spans = Math.min(7, regularDialogConstraints.getMaxWidth() / iconSize);
        recyclerView.getLayoutParams().height = 1; //Fixes rendering all possible icons (later will be resized)
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spans, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(ResourceUtils.dpToPx(8), 0, ResourceUtils.dpToPx(8), 0);
        if (lastVisibleItemPosition != -1)
            recyclerView.scrollToPosition(lastVisibleItemPosition);
        /* iconListLayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position < listItems.size() && adapter.getItemViewType(position) == Item.TYPE_HEADER) {
                    return iconListLayout.getSpanCount();
                } else {
                    return 1;
                }
            }
        });*/
    }

    @Override
    protected RegularDialogConstraints getRegularConstraints() {
        int orientation = getResources().getConfiguration().orientation;
        int maxWidth = getPercentOfWindowWidth(orientation == Configuration.ORIENTATION_PORTRAIT ? 85 : 65);
        return new RegularDialogConstraintsBuilder(this)
                .Default()
                .withMaxWidth(maxWidth)
                .withMaxHeight(Math.min(getPercentOfWindowHeight(85), ResourceUtils.dpToPx(450)))
                .build();
    }

    @Override
    protected void setupRegularDialog(RegularDialogConstraints constraints, Window dialogWindow, View dialogLayout, Rect fgPadding) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        dialogLayout.measure(widthMeasureSpec, heightMeasureSpec);
        int horPadding = fgPadding.left + fgPadding.right;
        int verPadding = fgPadding.top + fgPadding.bottom;
        int items = adapter.getItems().size();
        int rows = (int) Math.ceil((double) items / spans);
        int dialogLayoutHeight = dialogLayout.getMeasuredHeight();
        int RecyclerRequiredHeight = (rows * iconSize) + recyclerView.getPaddingBottom() + recyclerView.getPaddingTop();
        int recyclerRequiredWidth = (spans * iconSize) + recyclerView.getPaddingRight() + recyclerView.getPaddingLeft();
        int recyclerWidth = recyclerView.getMeasuredWidth();
        int recyclerHeight = recyclerView.getMeasuredHeight();
        dialogLayoutHeight = dialogLayoutHeight - recyclerHeight;
        if (recyclerWidth > recyclerRequiredWidth) recyclerWidth = recyclerRequiredWidth;
        else if (recyclerWidth < recyclerRequiredWidth) recyclerWidth = spans * iconSize;
        if (recyclerHeight > RecyclerRequiredHeight) recyclerHeight = RecyclerRequiredHeight;
        else if (recyclerHeight < RecyclerRequiredHeight) recyclerHeight = iconSize * rows;
        int desiredWidth = Math.max(recyclerWidth, dialogLayout.getMeasuredWidth());
        int desiredHeight = dialogLayoutHeight + recyclerHeight;
        if (desiredHeight > constraints.getMaxHeight()) {
            desiredHeight -= recyclerHeight;
            recyclerHeight = constraints.getMaxHeight() - desiredHeight;
            desiredHeight += recyclerHeight;
        }
        int newWidth = constraints.resolveWidth(desiredWidth);
        int newHeight = constraints.resolveHeight(desiredHeight);
        recyclerView.getLayoutParams().height = recyclerHeight;
        dialogWindow.setLayout(newWidth + horPadding, newHeight + verPadding);
        //dialogLayout.setLayoutParams(new ViewGroup.LayoutParams(newWidth, newHeight));
    }

    private IconPickerAdapter.IconModel[] getAdapterIconsByExternalNames(List<String> externalNames) {
        List<IconPickerAdapter.IconModel> adapterItems = adapter.getItems();
        List<Integer> positionsInAdapter = new ArrayList<>();
        for (int adapterPosition = 0; adapterPosition < adapterItems.size(); adapterPosition++) {
            IconPickerAdapter.IconModel adapterIcon = adapterItems.get(adapterPosition);
            for (String externalName : externalNames)
                if (adapterIcon.getIconExternalName().equals(externalName))
                    positionsInAdapter.add(adapterPosition);
        }
        List<IconPickerAdapter.IconModel> items = adapter.getItems(positionsInAdapter);
        IconPickerAdapter.IconModel[] itemsArray = new IconPickerAdapter.IconModel[items.size()];
        for (int i = 0; i < items.size(); i++) itemsArray[i] = items.get(i);
        return itemsArray;
    }

    private IconPickerAdapter.IconModel[] getAdapterIconsByNames(List<String> names) {
        List<IconPickerAdapter.IconModel> adapterItems = adapter.getItems();
        List<Integer> positionsInAdapter = new ArrayList<>();
        for (int adapterPosition = 0; adapterPosition < adapterItems.size(); adapterPosition++) {
            IconPickerAdapter.IconModel adapterIcon = adapterItems.get(adapterPosition);
            for (String iconName : names)
                if (adapterIcon.getIconName().equals(iconName))
                    positionsInAdapter.add(adapterPosition);
        }
        List<IconPickerAdapter.IconModel> items = adapter.getItems(positionsInAdapter);
        IconPickerAdapter.IconModel[] itemsArray = new IconPickerAdapter.IconModel[items.size()];
        for (int i = 0; i < items.size(); i++) itemsArray[i] = items.get(i);
        return itemsArray;
    }

    /*private class IconLayoutManager extends GridLayoutManager {
        private final int iconSize;
        IconLayoutManager(Context context, int iconSize) {
            super(context, -1, RecyclerView.VERTICAL, false);
            this.iconSize = iconSize;
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            int width = getWidth();
            int height = getHeight();
            if (getSpanCount() == -1 && iconSize > 0 && width > 0 && height > 0) {
                // Adjust span count on available space and icon size
                int layoutWidth = width - getPaddingRight() - getPaddingLeft();
                setSpanCount(Math.max(1, layoutWidth / iconSize));
            }
            super.onLayoutChildren(recycler, state);
        }
    }*/
}