package cn.yshstudio.originalproduct.pages.view.swipemenu.interfaces;

import cn.yshstudio.originalproduct.pages.view.swipemenu.bean.SwipeMenu;
import cn.yshstudio.originalproduct.pages.view.swipemenu.view.SwipeMenuView;


public interface OnSwipeItemClickListener {
    void onItemClick(SwipeMenuView view, SwipeMenu menu, int index);
}