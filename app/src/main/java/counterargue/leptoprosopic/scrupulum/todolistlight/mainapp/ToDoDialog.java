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

import butterknife.BindView;
import butterknife.ButterKnife;
import counterargue.leptoprosopic.scrupulum.todolistlight.R;

public class ToDoDialog extends DialogFragment {

    public static final String MSG_EXTRA = "MSG_EXTRA";
    private static final String POS = "POS";
    private static final String MSG = "MSG";
    private String msg;
    private int pos;

    @BindView(R.id.dialog_edittxt)
    EditText mEditText;

    public static ToDoDialog newInstance(String msg) {
        Bundle args = new Bundle();
        args.putString(MSG, msg);
        ToDoDialog fragment = new ToDoDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            msg = bundle.getString(MSG);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_lay, null, false);
        ButterKnife.bind(this, view);

        mEditText.setText(msg);

        return new AlertDialog.Builder(getActivity())
                .setTitle("ToDo")
                .setView(view)
                .setPositiveButton(android.R.string.ok, onOkClickListener)
                .setNegativeButton(android.R.string.cancel, onCancelClickListener)
                .setCancelable(false)
                .create();

    }

    private DialogInterface.OnClickListener onOkClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            onOkClick();
        }
    };

    private void onOkClick() {
        sendResult(Activity.RESULT_OK);
        dismiss();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        String todo_str = mEditText.getText().toString();

        if (todo_str.length() == 0) {
            todo_str = "TODO";
        }

        intent.putExtra(MSG_EXTRA, todo_str);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private DialogInterface.OnClickListener onCancelClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dismissDialog();
        }
    };

    private void dismissDialog() {
        this.dismiss();
    }
}
