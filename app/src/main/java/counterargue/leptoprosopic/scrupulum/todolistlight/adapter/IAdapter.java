package counterargue.leptoprosopic.scrupulum.todolistlight.adapter;

import android.content.Context;

public interface IAdapter {
    void onItemDismiss(int position);

    boolean onItemMove(int fromPosition, int toPosition);

    Context getContext();
}
