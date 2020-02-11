package shopinpager.wingstud.shopinpagerseller.custome;

import android.content.Context;
import android.graphics.Canvas;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import shopinpager.wingstud.shopinpagerseller.AppInitialization;

/**
 * Created by Android1 on 1/9/2016.
 */
public class TextViewRegular extends AppCompatTextView {
    public TextViewRegular(Context context) {
        super(context);
        init();
    }
    private void init() {
        this.setTypeface(AppInitialization.getFontRegular());
    }
    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public TextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}