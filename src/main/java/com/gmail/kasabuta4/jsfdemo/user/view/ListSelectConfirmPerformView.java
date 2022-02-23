package com.gmail.kasabuta4.jsfdemo.user.view;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.gmail.kasabuta4.jsfdemo.common.jsf.message.FacesMessageProducer;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.faces.context.FacesContext;

public abstract class ListSelectConfirmPerformView<K extends Serializable, V extends Serializable>
    implements Serializable {

  private static final long serialVersionUID = 1L;

  private final Function<V, K> keyFunction;
  private List<V> allItems;
  private Map<K, Boolean> selectionStatus = new LinkedHashMap<>();
  private List<V> selectedItems;
  private List<V> performedItems;

  protected ListSelectConfirmPerformView(Function<V, K> keyFunction) {
    this.keyFunction = keyFunction;
  }

  protected abstract List<V> listAllItems();

  protected abstract void doPerform(Collection<K> keyOfSelectedItems) throws Exception;

  public void list() {
    allItems = listAllItems();
  }

  public void updateSelectionStatus() {}

  public void confirm() {
    Collection<K> keysOfSelectedItems = keysOfSelectedItems();
    selectedItems =
        allItems.stream()
            .filter(v -> keysOfSelectedItems.contains(keyFunction.apply(v)))
            .collect(toList());
  }

  public void backToSelect() {
    selectedItems = null;
  }

  public void perform() {
    try {
      doPerform(keysOfSelectedItems());
      performedItems = selectedItems;
    } catch (Exception ex) {
      FacesContext.getCurrentInstance().addMessage(null, FacesMessageProducer.error(ex));
      list();
      selectionStatus.clear();
      selectedItems = null;
    }
  }

  public boolean isSelecting() {
    return !isConfirming() && !isPerformed();
  }

  public boolean isConfirming() {
    return !isPerformed() && selectedItems != null && !selectedItems.isEmpty();
  }

  public boolean isPerformed() {
    return performedItems != null && !performedItems.isEmpty();
  }

  public List<V> getAllItems() {
    return allItems;
  }

  public Map<K, Boolean> getSelectionStatus() {
    return selectionStatus;
  }

  public List<V> getSelectedItems() {
    return selectedItems;
  }

  public List<V> getPerformedItems() {
    return performedItems;
  }

  public boolean isNoItemSelected() {
    return !selectionStatus.entrySet().stream().anyMatch(Map.Entry::getValue);
  }

  private Collection<K> keysOfSelectedItems() {
    return selectionStatus.entrySet().stream()
        .filter(Map.Entry::getValue)
        .map(Map.Entry::getKey)
        .collect(toSet());
  }
}
