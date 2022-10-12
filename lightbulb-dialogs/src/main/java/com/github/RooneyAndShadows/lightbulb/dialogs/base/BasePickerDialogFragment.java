package com.github.rooneyandshadows.lightbulb.dialogs.base;

import androidx.lifecycle.LifecycleOwner;

public abstract class BasePickerDialogFragment<SelectionType> extends BaseDialogFragment {
    protected final BaseDialogSelection<SelectionType> selection;
    private SelectionChangedListener<SelectionType> onSelectionChangedListener;
    private final boolean synchronizeUiOnDraftChange;

    protected BasePickerDialogFragment(BaseDialogSelection<SelectionType> selection) {
        this(selection, true);
    }

    public BasePickerDialogFragment(BaseDialogSelection<SelectionType> selection, boolean synchronizeUiOnDraftChange) {
        this.synchronizeUiOnDraftChange = synchronizeUiOnDraftChange;
        this.selection = selection;
        addOnShowListener(dialogFragment -> selection.startDraft());
        addOnCancelListener(dialogFragment -> selection.revertDraft());
        addOnNegativeClickListeners((view, dialogFragment) -> selection.revertDraft());
        addOnPositiveClickListener((view, dialogFragment) -> selection.commitDraft());
        selection.addSelectionListeners(new BaseDialogSelection.PickerSelectionListeners<SelectionType>() {
            @Override
            public void onCurrentSelectionChangedListener(SelectionType newValue, SelectionType oldValue) {
                synchronizeSelectUi();
                dispatchSelectionChangedEvent(oldValue, newValue);
            }

            @Override
            public void onDraftSelectionChangedListener(SelectionType newValue) {
                if (synchronizeUiOnDraftChange)
                    synchronizeSelectUi();
            }

            @Override
            public void onDraftCommit(SelectionType newValue, SelectionType beforeCommit) {
                synchronizeSelectUi();
                dispatchSelectionChangedEvent(beforeCommit, newValue);
            }

            @Override
            public void onDraftReverted() {
                synchronizeSelectUi();
            }
        });
    }

    @Override
    protected boolean canShowDialog(LifecycleOwner dialogLifecycleOwner) {
        return true;
    }

    protected abstract void synchronizeSelectUi();

    public void setOnSelectionChangedListener(SelectionChangedListener<SelectionType> onSelectionChangedListener) {
        this.onSelectionChangedListener = onSelectionChangedListener;
    }

    public void setSelection(SelectionType newSelection) {
        selection.setCurrentSelection(newSelection);
    }

    public SelectionType getSelection() {
        return selection.getCurrentSelection();
    }

    public final boolean hasSelection() {
        return selection.hasCurrentSelection();
    }

    protected void dispatchSelectionChangedEvent(SelectionType oldValue, SelectionType newValue) {
        if (onSelectionChangedListener != null)
            onSelectionChangedListener.onSelectionChanged(oldValue, newValue);
    }

    public interface SelectionChangedListener<SType> {
        void onSelectionChanged(SType oldValue, SType newValue);
    }
}
