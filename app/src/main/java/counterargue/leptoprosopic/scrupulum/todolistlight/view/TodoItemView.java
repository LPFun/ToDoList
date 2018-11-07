package counterargue.leptoprosopic.scrupulum.todolistlight.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TodoItemView extends View {

    private Paint mTitlePaint, mCategoryPaint;
    private String mTitle = "Text", mCategory = "Category";
    int fontSizeTitle = 40, fontSizeCategory = 35, view_width, view_height;
    float widths[], width;

    public TodoItemView(Context context) {
        super(context);
        init(context);
    }

    public TodoItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mCategoryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void initPaint(Paint paint, String text, int fontSize) {
        paint.setColor(Color.BLACK);
        paint.setTextSize(fontSize);
        paint.setStyle(Paint.Style.STROKE);
        width = paint.measureText(text);
        widths = new float[text.length()];
        paint.getTextWidths(text, widths);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        view_width = MeasureSpec.getSize(widthMeasureSpec);
        view_height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mTitle, 20, view_height / 4 + fontSizeTitle / 2, mTitlePaint);
        canvas.drawText(mCategory, 20, (3 * view_height) / 4 + fontSizeCategory / 4, mCategoryPaint);
    }

    public void setTitle(String title) {
        mTitle = title;
        initPaint(mTitlePaint, mTitle, fontSizeTitle);
    }

    public void setCategory(String category) {
        mCategory = category;
        initPaint(mCategoryPaint, mCategory, fontSizeCategory);
    }
}
