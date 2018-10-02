package counterargue.leptoprosopic.scrupulum.todolistlight.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import counterargue.leptoprosopic.scrupulum.todolistlight.R;

public class ToDoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IAdapter {

    private Context mContext;
    private List<String> mListData;
    private OnListItemClick mOnListItemClick;
    private String mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private View view;

    public ToDoAdapter(Context context, OnListItemClick onListItemClick) {
        mContext = context;
        mOnListItemClick = onListItemClick;
        mListData = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        RecyclerView.ViewHolder viewHolder;
        view = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        viewHolder = new ToDoViewHolder(view);
        view.setOnClickListener(view -> {
            int adapterPos = viewHolder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION)
                mOnListItemClick.OnItemClick(viewHolder.getAdapterPosition(), ((ToDoViewHolder) viewHolder).item_txt.getText().toString());
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ((ToDoViewHolder) viewHolder).bind(mListData.get(i));
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    @Override
    public void onItemDismiss(int pos) {
        mRecentlyDeletedItem = mListData.get(pos);
        mRecentlyDeletedItemPosition = pos;
        mListData.remove(pos);
        notifyItemRemoved(pos);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(view, "Undo?", Snackbar.LENGTH_SHORT);
        snackbar.setAction("Yes", view -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        mListData.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mListData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mListData, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void addItem(String item) {
        mListData.add(0, item);
        notifyItemInserted(0);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public void setData(List<String> data) {
        mListData = data;
    }

    public void changeItem(int position, String msg) {
        mListData.remove(position);
        mListData.add(position, msg);
        notifyItemChanged(position);
    }

    public List<String> getData() {
        return mListData;
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_txt)
        TextView item_txt;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String item) {
            item_txt.setText(item);
        }
    }
}
