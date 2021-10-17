package com.github.RooneyAndShadows.lightbulb.dialogs.picker_dialog_color;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.github.RooneyAndShadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraints;
import com.github.RooneyAndShadows.lightbulb.dialogs.base.constraints.regular.RegularDialogConstraintsBuilder;
import com.github.RooneyAndShadows.lightbulb.dialogs.picker_dialog_adapter.AdapterPickerDialog;
import com.github.RooneyAndShadows.lightbulb.commons.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("unused")
public class ColorPickerDialog extends AdapterPickerDialog<ColorPickerAdapter.ColorModel> {
    private final static String ADAPTER_STATE_TAG = "ADAPTER_STATE_TAG";
    private final static int iconSize = ResourceUtils.dpToPx(50);
    private int spans;
    private int lastVisibleItemPosition = -1;
    private RecyclerView recyclerView;

    public ColorPickerDialog() {
        super();
    }

    public static ColorPickerDialog newInstance(
            String title, String message, DialogButtonConfiguration positive, DialogButtonConfiguration negative,
            boolean cancelable, DialogAnimationTypes animationType) {
        ColorPickerDialog dialogFragment = new ColorPickerDialog();
        dialogFragment.setArguments(new DialogBundleHelper()
                .withTitle(title)
                .withMessage(message)
                .withPositiveButtonConfig(positive)
                .withNegativeButtonConfig(negative)
                .withCancelable(cancelable)
                .withShowing(false)
                .withDialogType(DialogTypes.NORMAL)
                .withAnimation(animationType == null ? DialogAnimationTypes.NO_ANIMATION : animationType)
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
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(ResourceUtils.dpToPx(8), 0, ResourceUtils.dpToPx(8), 0);
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
        if (lastVisibleItemPosition != -1)
            recyclerView.scrollToPosition(lastVisibleItemPosition);
    }

    private ColorPickerAdapter.ColorModel[] getAdapterIconsByExternalNames(List<String> externalNames) {
        List<ColorPickerAdapter.ColorModel> adapterItems = adapter.getItems();
        List<Integer> positionsInAdapter = new ArrayList<>();
        for (int adapterPosition = 0; adapterPosition < adapterItems.size(); adapterPosition++) {
            ColorPickerAdapter.ColorModel adapterIcon = adapterItems.get(adapterPosition);
            for (String externalName : externalNames) {
                if (adapterIcon.getColorExternalName().equals(externalName))
                    positionsInAdapter.add(adapterPosition);
            }
        }
        List<ColorPickerAdapter.ColorModel> items = adapter.getItems(positionsInAdapter);
        ColorPickerAdapter.ColorModel[] itemsArray = new ColorPickerAdapter.ColorModel[items.size()];
        for (int i = 0; i < items.size(); i++) itemsArray[i] = items.get(i);
        return itemsArray;
    }

    private ColorPickerAdapter.ColorModel[] getAdapterIconsByHexCodes(List<String> hexCodes) {
        List<ColorPickerAdapter.ColorModel> adapterItems = adapter.getItems();
        List<Integer> positionsInAdapter = new ArrayList<>();
        for (int adapterPosition = 0; adapterPosition < adapterItems.size(); adapterPosition++) {
            ColorPickerAdapter.ColorModel adapterIcon = adapterItems.get(adapterPosition);
            for (String hex : hexCodes) {
                if (adapterIcon.getColorHex().equals(hex))
                    positionsInAdapter.add(adapterPosition);
            }
        }
        List<ColorPickerAdapter.ColorModel> items = adapter.getItems(positionsInAdapter);
        ColorPickerAdapter.ColorModel[] itemsArray = new ColorPickerAdapter.ColorModel[items.size()];
        for (int i = 0; i < items.size(); i++) itemsArray[i] = items.get(i);
        return itemsArray;
    }
}