package counterargue.leptoprosopic.scrupulum.todolistlight.mainapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import counterargue.leptoprosopic.scrupulum.todolistlight.App;
import counterargue.leptoprosopic.scrupulum.todolistlight.R;
import counterargue.leptoprosopic.scrupulum.todolistlight.adapter.OnListItemClick;
import counterargue.leptoprosopic.scrupulum.todolistlight.adapter.ItemTouchHelperCallback;
import counterargue.leptoprosopic.scrupulum.todolistlight.adapter.ToDoAdapter;
import counterargue.leptoprosopic.scrupulum.todolistlight.adapter.ToDoItemDecorator;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.ToDoDB;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.dao.ToDoDao;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties.ToDoItem;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";
    private ToDoAdapter mAdapter;
    private final static int REQUEST_CODE = 0;
    private final static int CHANGE_CODE = 1;
    private final String DIALOG_ADD = "DIALOG_ADD";
    private final String DIALOG_CHANGE = "DIALOG_CHANGE";
    private int position;
    private SharedPreferences mPreferences;
    private String TODOS_COUNT = "TODOS_COUNT";
    private Disposable todo_get;

    @BindView(R.id.recycle_list)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_list, container, false);
        ButterKnife.bind(this, view);

        ToDoDB db = App.getInstance().getDataBase();

        ToDoDao toDoDao = db.mToDoDao();

        mAdapter = new ToDoAdapter(getActivity(), mOnListItemClick);
//        List<String> data = new ArrayList<>();
//        todo_get = toDoDao.getAll()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(toDoItems -> {
//                    if (toDoItems != null) {
//                        for (ToDoItem item : toDoItems) {
//                            data.add(item.title);
//                        }
//                    } else {
//                        Toast.makeText(getActivity(), "Add TODO", Toast.LENGTH_SHORT).show();
//                    }
//                });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<String> data = getData();
        if (data.size() == 0)
            Toast.makeText(getActivity(), "Add TODO", Toast.LENGTH_SHORT).show();

        mAdapter.setData(data);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
        mRecyclerView.addItemDecoration(new ToDoItemDecorator());
        mRecyclerView.setAdapter(mAdapter);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        setHasOptionsMenu(true);
        return view;
    }

    private OnListItemClick mOnListItemClick = new OnListItemClick() {
        @Override
        public void OnItemClick(int pos, String msg) {
            ToDoDialog doDialog = ToDoDialog.newInstance(msg);
            doDialog.setTargetFragment(MainFragment.this, CHANGE_CODE);
            position = pos;
            doDialog.show(getFragmentManager(), DIALOG_CHANGE);
        }
    };

    private List<String> getData() {
        mPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int count = mPreferences.getInt(TODOS_COUNT, 0);
        List<String> todoList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            todoList.add(mPreferences.getString("todo" + i, "TODO"));
        }
        return todoList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_items, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE) {
            String msg = data.getStringExtra(ToDoDialog.MSG_EXTRA);
            mAdapter.addItem(msg);
            Log.i(TAG, "onActivityResult: " + msg);
        } else if (requestCode == CHANGE_CODE) {
            String msg = data.getStringExtra(ToDoDialog.MSG_EXTRA);
            Log.i(TAG, "onActivityResult: " + msg);
            mAdapter.changeItem(position, msg);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            if (mAdapter.getData().size() < 20){
                ToDoDialog doDialog = new ToDoDialog();
                doDialog.setTargetFragment(MainFragment.this, REQUEST_CODE);
                doDialog.show(getFragmentManager(), DIALOG_ADD);
            } else {
                Toast.makeText(getActivity(), "To mutch TODOS, delete one", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SharedPreferences.Editor editor = mPreferences.edit();
        List<String> todoList = mAdapter.getData();
        editor.putInt(TODOS_COUNT, todoList.size());
        editor.apply();
        for (int i = 0; i < todoList.size(); i++){
            editor.putString("todo" + i, todoList.get(i));
            editor.apply();
        }

        if (todo_get != null && todo_get.isDisposed()){
            todo_get.dispose();
        }

    }
}
