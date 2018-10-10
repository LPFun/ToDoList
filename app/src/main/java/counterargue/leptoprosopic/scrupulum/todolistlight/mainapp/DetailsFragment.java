package counterargue.leptoprosopic.scrupulum.todolistlight.mainapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import counterargue.leptoprosopic.scrupulum.todolistlight.R;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties.ToDoItem;

public class DetailsFragment extends Fragment {

    private static final String MSG = "MSG";
    public static final String MSG_EXTRA = "MSG_EXTRA";
    private Gson mGson;
    private ToDoItem mToDoItem = new ToDoItem();

    @BindView(R.id.details_title_edit)
    EditText mTitleEditTxt;
    @BindView(R.id.details_cat_edit)
    EditText mCategoryEditTxt;
    @BindView(R.id.details_desc_edit)
    EditText mDescEditTxt;

    private Unbinder mUnbinder;

    public static DetailsFragment newInstance(ToDoItem item) {
        Gson gson = new Gson();
        String str_item = gson.toJson(item);
        Bundle args = new Bundle();
        args.putString(MSG, str_item);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mGson = new Gson();
            String str = bundle.getString(MSG);
            mToDoItem = mGson.fromJson(str, ToDoItem.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
        if (mToDoItem.title != null) {
            mTitleEditTxt.setText(mToDoItem.title);
        }
        if (mToDoItem.category != null) {
            mCategoryEditTxt.setText(mToDoItem.category);
        }
        if (mToDoItem.description != null) {
            mDescEditTxt.setText(mToDoItem.description);
        }
    }

    @OnClick(R.id.details_accept_btn)
    public void onAcceptBtnClick() {
        sendResult();
        goBack();
    }

    private void sendResult() {
        if (getTargetFragment() == null) {
            return;
        }

        mGson = new Gson();

        Intent intent = new Intent();
        String title = mTitleEditTxt.getText().toString();
        if (title.length() == 0) {
            title = "ToDo";
        }
        mToDoItem.title = title;
        mToDoItem.description = mDescEditTxt.getText().toString();
        mToDoItem.category = mCategoryEditTxt.getText().toString();
        mToDoItem.status = null;

        String str_new_todo = mGson.toJson(mToDoItem);

        intent.putExtra(MSG_EXTRA, str_new_todo);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void goBack() {
        getFragmentManager().popBackStack();
    }
}
