package counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "todoitems")
public class ToDoItem {
    @PrimaryKey(autoGenerate = true)
    public long id = 0;

    public int position;

    public String title;

    public String description;

    public String category;

    public String status;
}
