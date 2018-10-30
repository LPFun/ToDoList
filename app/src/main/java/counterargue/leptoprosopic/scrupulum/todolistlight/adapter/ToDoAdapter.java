package counterargue.leptoprosopic.scrupulum.todolistlight.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import counterargue.leptoprosopic.scrupulum.todolistlight.R;
import counterargue.leptoprosopic.scrupulum.todolistlight.database.entyties.ToDoItem;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ToDoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IAdapter {

    private Context mContext;
    private List<ToDoItem> mListData;
    private ToDoItem mRecentlyDeletedItem;
    private View view;
    private PublishSubject<Integer> mClickSubject = PublishSubject.create();
    private PublishSubject<SparseArray<ToDoItem>> mUpdateSubject = PublishSubject.create();
    private String TAG = this.getClass().getSimpleName();

    public ToDoAdapter(Context context) {
        mContext = context;
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
            int adapterPos = viewHolder.getLayoutPosition();
            if (adapterPos != RecyclerView.NO_POSITION)
                mClickSubject.onNext(viewHolder.getAdapterPosition());
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
        SparseArray sparseArray = new SparseArray();
        sparseArray.put(3, mRecentlyDeletedItem);
        mUpdateSubject.onNext(sparseArray);
    }

    public void deleteItem(int pos) {
        mListData.remove(pos);
        SparseArray sparseArray = new SparseArray();
        sparseArray.put(4, mRecentlyDeletedItem);
        mUpdateSubject.onNext(sparseArray);
        notifyItemRemoved(pos);
    }

    public Observable<Integer> getClickEvent(){
        return mClickSubject;
    }

    public Observable<SparseArray<ToDoItem>> getUpdateEvent(){
        return mUpdateSubject;
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
        Log.i(TAG, "onItemMove: from " + fromPosition + " to " + toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void addItem(ToDoItem item) {
        mListData.add(0, item);
        SparseArray sparseArray = new SparseArray();
        sparseArray.put(2, item);
        mUpdateSubject.onNext(sparseArray);
        notifyItemInserted(0);
    }

    public void changeItem(int position, ToDoItem item) {
        mListData.remove(position);
        mListData.add(position, item);
        SparseArray mapItem = new SparseArray();
        mapItem.put(1, item);
        mUpdateSubject.onNext(mapItem);
        notifyItemChanged(position);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public void setData(List<ToDoItem> data) {
        mListData = data;
    }

    public List<ToDoItem> getData() {
        return mListData;
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_txt)
        TextView item_txt;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(ToDoItem item) {
            Log.i(TAG, "bind: " + item.position);
            item_txt.setText(item.title);
        }
    }
}
