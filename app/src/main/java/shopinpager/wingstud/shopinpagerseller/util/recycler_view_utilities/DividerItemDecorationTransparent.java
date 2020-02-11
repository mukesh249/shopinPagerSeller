package shopinpager.wingstud.shopinpagerseller.util.recycler_view_utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import shopinpager.wingstud.shopinpagerseller.R;


/**
 * Created by Vishal on 11/10/2016.
 */

public class DividerItemDecorationTransparent extends RecyclerView.ItemDecoration {


    private Drawable mDivider;

    public DividerItemDecorationTransparent(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.rcy_item_devider_gray);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}

