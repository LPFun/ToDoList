package counterargue.leptoprosopic.scrupulum.todolistlight;

import android.app.Application;
import android.arch.persistence.room.Room;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.ToDoDB;

public class App extends Application {
    public static App instance;

    public ToDoDB dataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dataBase = Room.databaseBuilder(this, ToDoDB.class, "tododb")
                .build();
    }

    public static App getInstance(){
        return instance;
    }

    public ToDoDB getDataBase(){
        return dataBase;
    }

}
