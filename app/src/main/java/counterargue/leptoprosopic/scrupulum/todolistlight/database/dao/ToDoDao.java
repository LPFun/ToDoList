package counterargue.leptoprosopic.scrupulum.todolistlight.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties.ToDoItem;
import io.reactivex.Flowable;

@Dao
public interface ToDoDao {
    @Query("SELECT * FROM todoitems")
    Flowable<List<ToDoItem>> getAll();

    @Query("SELECT * FROM todoitems WHERE id = :id")
    ToDoItem getById(long id);

    @Insert
    void insert(ToDoItem toDoItem);

    @Update
    void update(ToDoItem toDoItem);

    @Delete
    void delete(ToDoItem toDoItem);

}
