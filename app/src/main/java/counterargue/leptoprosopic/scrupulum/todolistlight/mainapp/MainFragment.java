package counterargue.leptoprosopic.scrupulum.todolistlight.mainapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import counterargue.leptoprosopic.scrupulum.todolistlight.App;
import counterargue.leptoprosopic.scrupulum.todolistlight.R;
import counterargue.leptoprosopic.scrupulum.todolistlight.adapter.ItemTouchHelperCallback;
import counterargue.leptoprosopic.scrupulum.todolistlight.adapter.ToDoAdapter;
import counterargue.leptoprosopic.scrupulum.todolistlight.adapter.ToDoItemDecorator;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.ToDoDB;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties.ToDoItem;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.repository.DBRepository;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.repository.IRepository;
import counterargue.leptoprosopic.scrupulum.todolistlight.utils.ToDoDiffUtilCallback;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Main fragment shows list of todoitems.
 *
 * @author Aleks Dark
 * @version 1.1
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";
    /**
     * Adapter for recyclerview
     */
    private ToDoAdapter mAdapter;
    private final static int REQUEST_CODE = 0;
    private final static int CHANGE_CODE = 1;
    private final String DIALOG_ADD = "DIALOG_ADD";
    private final String DIALOG_CHANGE = "DIALOG_CHANGE";
    private int position;
    private static Disposable mToDoDbDisposable, mOnItemClick, mOnItemUpdate, mUpdateDB;
    private IRepository mToDoDao;
    private List<ToDoItem> mToDoItems;

    @BindView(R.id.recycle_list)
    RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initDB();
        initAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);

        initView();

        initRecycler();

        return view;
    }

    private void initView() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null && !actionBar.isShowing()) {
            actionBar.show();
        }
    }

    /**
     * Method that goto todoitem add fragment
     */
    @OnClick(R.id.toolbar_add_btn)
    public void onToolbarAddBtnClick() {
        if (mAdapter.getData().size() < 20) {
            ToDoDialog doDialog = new ToDoDialog();
            doDialog.setTargetFragment(MainFragment.this, REQUEST_CODE);
            doDialog.show(getFragmentManager(), DIALOG_ADD);
        } else {
            Toast.makeText(getActivity(), "To mutch TODOS, delete one", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method that initializing recyclerview
     */
    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
        mRecyclerView.addItemDecoration(new ToDoItemDecorator());
        mRecyclerView.setAdapter(mAdapter);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * Method that initializing adapter
     */
    private void initAdapter() {
        mAdapter = new ToDoAdapter(getActivity());

        setupRecyclerOnItemClickListener();

        mToDoDbDisposable = mToDoDao.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(toDoItems -> {
                            mToDoItems = new ArrayList<>();
                            if (toDoItems.size() != 0) {
                                mToDoItems = toDoItems;
                                Collections.reverse(mToDoItems);

                                ToDoDiffUtilCallback toDoDiffUtilCallback = new ToDoDiffUtilCallback(mAdapter.getData(), mToDoItems);
                                DiffUtil.DiffResult toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtilCallback);

                                mAdapter.setData(mToDoItems);
                                toDoDiffResult.dispatchUpdatesTo(mAdapter);

                                Log.i(TAG, "initAdapter: ");

                            } else {
                                Toast.makeText(getActivity(), "Add TODO", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                        }
                );
    }

    /**
     * Method that initializing DB
     */
    private void initDB() {
        ToDoDB db = App.getInstance().getDataBase();
        mToDoDao = new DBRepository(db.mToDoDao());
    }

    /**
     * Method that handles user's interactions with list of items
     */
    private void setupRecyclerOnItemClickListener() {
        mOnItemClick = mAdapter
                .getClickEvent()
                .subscribe(this::handleOnRecyclerItemClick);
        mOnItemUpdate = mAdapter
                .getUpdateEvent()
                .subscribe(item -> {
                    switch (item.keyAt(0)) {
                        case 1:
                            changeDB(() -> mToDoDao.update(item.get(1)));
                            break;
                        case 2:
                            changeDB(() -> mToDoDao.insert(item.get(2)));
                            break;
                        case 3:
                            changeDB(() -> mToDoDao.delete(item.get(3)));
                            break;
                    }
                });
    }

    /**
     * Method that handles user's on item click
     * @param pos - item position in list
     */
    private void handleOnRecyclerItemClick(int pos) {
        ToDoDialog doDialog = ToDoDialog.newInstance(mToDoItems.get(pos));
        doDialog.setTargetFragment(MainFragment.this, CHANGE_CODE);
        position = pos;
        doDialog.show(getFragmentManager(), DIALOG_CHANGE);
//        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new DetailsFragment()).addToBackStack(null).commit();
    }


    /**
     * Method that handles callback from FragmentDialog
     * @param requestCode - determines user's action
     * @param resultCode - determines result code
     * @param data - changed data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Gson gson = new Gson();
        if (requestCode == REQUEST_CODE) {
            String msg = data.getStringExtra(ToDoDialog.MSG_EXTRA);
            ToDoItem toDoItem1 = gson.fromJson(msg, ToDoItem.class);
            mAdapter.addItem(toDoItem1);
//            changeDB(() -> mToDoDao.insert(toDoItem1));
            Log.i(TAG, "onActivityResult: " + msg);
        } else if (requestCode == CHANGE_CODE) {
            String msg = data.getStringExtra(ToDoDialog.MSG_EXTRA);
            ToDoItem toDoItem2 = gson.fromJson(msg, ToDoItem.class);
            Log.i(TAG, "onActivityResult: " + msg);
            ;
            changeDB(() -> mToDoDao.update(toDoItem2));
            mAdapter.changeItem(position, toDoItem2);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        updateListInDb();
        dispose(mToDoDbDisposable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispose(mOnItemClick);
        dispose(mUpdateDB);
        dispose(mOnItemUpdate);
    }

    /**
     * Method that updates list in DB
     */
    private void updateListInDb() {
        changeDB(() -> mToDoDao.updateItems(mAdapter.getData()));
    }

    /**
     * Method that sends changes to DB
     * @param changeDB - functional interface
     */
    private void changeDB(IChangeDB changeDB) {
        mUpdateDB = Completable.fromAction(changeDB::changeDB)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, throwable -> {
                    Log.e(TAG, "changeDB: ", throwable);
                });
    }

    /**
     * Method that dispose all disposables
     */
    private void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * Functional interface
     */
    public interface IChangeDB {
        void changeDB();
    }
}
