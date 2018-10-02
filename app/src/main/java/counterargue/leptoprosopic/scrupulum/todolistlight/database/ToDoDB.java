package counterargue.leptoprosopic.scrupulum.todolistlight.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.dao.ToDoDao;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties.ToDoItem;

@Database(entities = {ToDoItem.class}, version = 1)
public abstract class ToDoDB extends RoomDatabase {
    public abstract ToDoDao mToDoDao();
}
