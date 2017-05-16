package cn.com.shequnew.pages.view.swipemenu.interfaces;

import cn.com.shequnew.pages.view.swipemenu.bean.SwipeMenu;
import cn.com.shequnew.pages.view.swipemenu.view.SwipeMenuView;


public interface OnSwipeItemClickListener {
    void onItemClick(SwipeMenuView view, SwipeMenu menu, int index);
}