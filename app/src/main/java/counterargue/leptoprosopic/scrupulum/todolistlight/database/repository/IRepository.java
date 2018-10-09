package counterargue.leptoprosopic.scrupulum.todolistlight.database.repository;

import java.util.List;

import counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties.ToDoItem;
import io.reactivex.Flowable;

public interface IRepository {
    Flowable<List<ToDoItem>> getAll();

    ToDoItem getById(long id);

    void insert(ToDoItem toDoItem);

    void update(ToDoItem toDoItem);

    void delete(ToDoItem toDoItem);

    void deleteAll();

    void updateItems(List<ToDoItem> toDoItems);

    void insertAll(List<ToDoItem> items);
}
