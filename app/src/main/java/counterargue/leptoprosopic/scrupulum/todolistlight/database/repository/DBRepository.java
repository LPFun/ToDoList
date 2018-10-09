package counterargue.leptoprosopic.scrupulum.todolistlight.database.repository;

import java.util.List;

import counterargue.leptoprosopic.scrupulum.todolistlight.database.dao.ToDoDao;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties.ToDoItem;
import io.reactivex.Flowable;

public class DBRepository implements IRepository {
    ToDoDao mToDoDao;

    public DBRepository(ToDoDao toDoDao) {
        mToDoDao = toDoDao;
    }

    @Override
    public Flowable<List<ToDoItem>> getAll() {
        return mToDoDao.getAll();
    }

    @Override
    public ToDoItem getById(long id) {
        return mToDoDao.getById(id);
    }

    @Override
    public void insert(ToDoItem toDoItem) {
        mToDoDao.insert(toDoItem);
    }

    @Override
    public void update(ToDoItem toDoItem) {
        mToDoDao.update(toDoItem);
    }

    @Override
    public void delete(ToDoItem toDoItem) {
        mToDoDao.delete(toDoItem);
    }

    @Override
    public void deleteAll() {
        mToDoDao.deleteAll();
    }

    @Override
    public void updateItems(List<ToDoItem> toDoItems) {
        mToDoDao.updateAll(toDoItems);
    }

    @Override
    public void insertAll(List<ToDoItem> items) {
        mToDoDao.insertAll(items);
    }
}
