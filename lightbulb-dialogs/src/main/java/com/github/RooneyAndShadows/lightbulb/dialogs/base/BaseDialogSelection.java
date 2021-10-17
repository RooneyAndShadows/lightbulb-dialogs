package com.github.RooneyAndShadows.lightbulb.dialogs.base;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class BaseDialogSelection<SelectionType> {
    private final static String SELECTION_CURRENT = "SELECTION_CURRENT";
    private final static String SELECTION_DRAFT = "SELECTION_DRAFT";
    private final HashMap<String, SelectionType> selection = new HashMap<>();
    private final ArrayList<PickerSelectionListeners<SelectionType>> selectionListeners = new ArrayList<>();

    public BaseDialogSelection(SelectionType current, SelectionType draft) {
        selection.put(SELECTION_CURRENT, current);
        selection.put(SELECTION_DRAFT, draft);
    }

    public abstract boolean compareValues(SelectionType v1, SelectionType v2);

    public abstract boolean hasCurrentSelection();

    public abstract boolean hasDraftSelection();

    public SelectionType getCurrentSelection() {
        return selection.get(SELECTION_CURRENT);
    }

    public SelectionType getDraftSelection() {
        return selection.get(SELECTION_DRAFT);
    }

    public void addSelectionListeners(PickerSelectionListeners<SelectionType> selectionListeners) {
        this.selectionListeners.add(selectionListeners);
    }

    public void setCurrentSelection(SelectionType newValue) {
        setCurrentSelection(newValue, true);
    }

    public void setDraftSelection(SelectionType newValue) {
        setDraftSelection(newValue, true);
    }

    public void setCurrentSelection(SelectionType newValue, boolean notify) {
        SelectionType current = selection.get(SELECTION_CURRENT);
        if (compareValues(current, newValue))
            return;
        selection.remove(SELECTION_CURRENT);
        selection.put(SELECTION_CURRENT, newValue);
        if (notify)
            for (PickerSelectionListeners<SelectionType> listener : selectionListeners)
                listener.onCurrentSelectionChangedListener(newValue, current);
    }

    public void setDraftSelection(SelectionType newValue, boolean notify) {
        SelectionType draft = selection.get(SELECTION_DRAFT);
        if (compareValues(draft, newValue))
            return;
        selection.remove(SELECTION_DRAFT);
        selection.put(SELECTION_DRAFT, newValue);
        if (notify)
            for (PickerSelectionListeners<SelectionType> listener : selectionListeners)
                listener.onDraftSelectionChangedListener(newValue);
    }

    public void clearDraft() {
        setDraftSelection(null, false);
    }

    public void startDraft() {
        SelectionType current = getCurrentSelection();
        setDraftSelection(current, false);
    }

    public void commitDraft() {
        SelectionType beforeCommit = getCurrentSelection();
        SelectionType draft = getDraftSelection();
        setCurrentSelection(draft, false);
        clearDraft();
        for (PickerSelectionListeners<SelectionType> listener : selectionListeners)
            listener.onDraftCommit(draft, beforeCommit);
    }

    public void revertDraft() {
        clearDraft();
        for (PickerSelectionListeners<SelectionType> listener : selectionListeners)
            listener.onDraftReverted();
    }

    public interface PickerSelectionListeners<SelectionType> {

        void onCurrentSelectionChangedListener(SelectionType newValue, SelectionType oldValue);

        void onDraftSelectionChangedListener(SelectionType newValue);

        void onDraftCommit(SelectionType newValue, SelectionType beforeCommit);

        void onDraftReverted();
    }
}
