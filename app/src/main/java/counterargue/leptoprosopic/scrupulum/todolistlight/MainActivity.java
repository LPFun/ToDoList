package counterargue.leptoprosopic.scrupulum.todolistlight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import counterargue.leptoprosopic.scrupulum.todolistlight.mainapp.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();

    }
}
