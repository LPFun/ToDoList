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
import android.view.ViewGroup;

import com.google.gson.Gson;

import counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties.ToDoItem;

public class DeleteDialogFragment extends DialogFragment {

    public static final String MSG_EXTRA = "MSG_EXTRA";
    private static final String POS = "POS";
    private static final String MSG = "MSG";
    private ToDoItem mToDoItem = new ToDoItem();
    private int pos;
    private Gson mGson;
    private String objInString;

    public static DeleteDialogFragment newInstance(ToDoItem item) {
        Gson gson = new Gson();
        String str_item = gson.toJson(item);
        Bundle args = new Bundle();
        args.putString(MSG, str_item);
        DeleteDialogFragment fragment = new DeleteDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mGson = new Gson();
            objInString = bundle.getString(MSG);
            mToDoItem = mGson.fromJson(objInString, ToDoItem.class);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        return new AlertDialog.Builder(getActivity())
                .setTitle(mToDoItem.title)
                .setMessage("Delete item?")
                .setPositiveButton(android.R.string.ok, onOkClickListener)
                .setNegativeButton(android.R.string.cancel, onCancelClickListener)
                .setCancelable(false)
                .setOnCancelListener(dialog -> {
                    sendResult(Activity.RESULT_CANCELED);
                })
                .create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCancelable(false);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
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
        intent.putExtra(MSG_EXTRA, objInString);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private DialogInterface.OnClickListener onCancelClickListener = (dialogInterface, i) -> {
        sendResult(Activity.RESULT_CANCELED);
        dismissDialog();
    };

    private void dismissDialog() {
        this.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
