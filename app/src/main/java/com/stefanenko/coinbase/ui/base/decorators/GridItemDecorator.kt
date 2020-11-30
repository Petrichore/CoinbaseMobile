package com.stefanenko.coinbase.ui.base.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecorator(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemCount = parent.adapter!!.itemCount
        val viewPosition = parent.getChildAdapterPosition(view)

        if (itemCount > 2) {
            if ((itemCount % 2 == 0 && (viewPosition == itemCount - 1 || viewPosition == itemCount - 2)) ||
                (itemCount % 2 != 0 && viewPosition == itemCount - 1)
            ) {
                outRect.top = space
                outRect.bottom = space
            } else {
                outRect.top = space
            }
        } else {
            outRect.top = space
            outRect.bottom = space
        }
    }
}