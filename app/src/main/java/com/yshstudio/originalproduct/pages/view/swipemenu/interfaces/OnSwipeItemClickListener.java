package com.yshstudio.originalproduct.pages.view.swipemenu.interfaces;

import com.yshstudio.originalproduct.pages.view.swipemenu.bean.SwipeMenu;
import com.yshstudio.originalproduct.pages.view.swipemenu.view.SwipeMenuView;


public interface OnSwipeItemClickListener {
    void onItemClick(SwipeMenuView view, SwipeMenu menu, int index);
}