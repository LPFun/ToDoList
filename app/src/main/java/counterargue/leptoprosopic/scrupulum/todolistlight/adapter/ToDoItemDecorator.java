package counterargue.leptoprosopic.scrupulum.todolistlight.adapter;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ToDoItemDecorator extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        outRect.set(15, 15, 15, 0);
//        if (pos == state.getItemCount() - 1) {
//            outRect.set(15, 15, 15, 15);
//        } else if (pos == state.getItemCount() - 2){
//            outRect.set(15, 0, 15, 0);
//        }
    }
}
