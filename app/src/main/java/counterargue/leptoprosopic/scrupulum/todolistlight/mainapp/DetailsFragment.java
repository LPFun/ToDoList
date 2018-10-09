package counterargue.leptoprosopic.scrupulum.todolistlight.mainapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import counterargue.leptoprosopic.scrupulum.todolistlight.R;

public class DetailsFragment extends Fragment {

    @BindView(R.id.details_title_edit)
    EditText mTitleEditTxt;
    @BindView(R.id.details_cat_edit)
    EditText mCategoryEditTxt;
    @BindView(R.id.details_desc_edit)
    EditText mDescEditTxt;

    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    @OnClick(R.id.details_accept_btn)
    public void onAcceptBtnClick() {
        sendResult();
    }

    private void sendResult() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
