package counterargue.leptoprosopic.scrupulum.todolistlight.mainapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import counterargue.leptoprosopic.scrupulum.todolistlight.R;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties.ToDoItem;

public class ToDoDialog extends DialogFragment {

    public static final String MSG_EXTRA = "MSG_EXTRA";
    private static final String POS = "POS";
    private static final String MSG = "MSG";
    private ToDoItem mToDoItem = new ToDoItem();
    private int pos;
    private Gson mGson;

    @BindView(R.id.dialog_edittxt)
    EditText mEditText;

    public static ToDoDialog newInstance(ToDoItem item) {
        Gson gson = new Gson();
        String str_item = gson.toJson(item);
        Bundle args = new Bundle();
        args.putString(MSG, str_item);
        ToDoDialog fragment = new ToDoDialog();
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_lay, null, false);
        ButterKnife.bind(this, view);

        if (mToDoItem.title != null){
            mEditText.setText(mToDoItem.title);
        }



        return new AlertDialog.Builder(getActivity())
                .setTitle("ToDo")
                .setView(view)
                .setPositiveButton(android.R.string.ok, onOkClickListener)
                .setNegativeButton(android.R.string.cancel, onCancelClickListener)
                .setCancelable(false)
                .create();
    }

    private DialogInterface.OnClickListener onOkClickListener = (dialogInterface, i) -> onOkClick();

    private void onOkClick() {
        sendResult(Activity.RESULT_OK);
        dismiss();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        String todo_title = mEditText.getText().toString();
        if (todo_title.length() == 0) {
            todo_title = "TODO";
        }

        mGson = new Gson();

        mToDoItem.category = "cat";
        mToDoItem.description = "desc";
        mToDoItem.status = "status";
        mToDoItem.title = todo_title;

        String str_updated_todo = mGson.toJson(mToDoItem);

        intent.putExtra(MSG_EXTRA, str_updated_todo);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private DialogInterface.OnClickListener onCancelClickListener = (dialogInterface, i) -> dismissDialog();

    private void dismissDialog() {
        this.dismiss();
    }
}
