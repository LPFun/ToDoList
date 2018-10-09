package counterargue.leptoprosopic.scrupulum.todolistlight.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

import counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties.ToDoItem;

public class ToDoDiffUtilCallback extends DiffUtil.Callback {

    private List<ToDoItem> oldList;
    private List<ToDoItem> newList;

    public ToDoDiffUtilCallback(List<ToDoItem> oldList, List<ToDoItem> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        ToDoItem oldItem = oldList.get(i);
        ToDoItem newItem = newList.get(i1);
        return oldItem.id == newItem.id;
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        ToDoItem oldToDoItem = oldList.get(i);
        ToDoItem newToDoItem = newList.get(i1);
        boolean isCategorySame = oldToDoItem.category.equals(newToDoItem.category);
        boolean isTitleSame = oldToDoItem.title.equals(newToDoItem.title);
        boolean isDescSame = oldToDoItem.description.equals(newToDoItem.description);
        boolean isStatusSame = oldToDoItem.status.equals(newToDoItem.status);
        boolean isPosSame = oldToDoItem.position == newToDoItem.position;
        return (isCategorySame && isTitleSame && isDescSame && isStatusSame && isPosSame);
    }
}
