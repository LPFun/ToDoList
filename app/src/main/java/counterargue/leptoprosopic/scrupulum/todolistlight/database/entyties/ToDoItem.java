package counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Entity for Data Base
 *
 * @author Aleks Dark
 * @version 1.0
 */
@Entity(tableName = "todoitems")
public class ToDoItem {
    @PrimaryKey(autoGenerate = true)
    /** Id for todoitem in DB */
    public long id = 0;
    /** Position in user's list */
    public int position;
    /** Item's title */
    public String title;
    /** Item's description */
    public String description;
    /** Item's category */
    public String category;
    /** Item's status */
    public String status;
}
