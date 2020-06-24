package com.example.notekeeper;

import android.view.Gravity;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;

public class toolBarNavigationIconListener implements View.OnClickListener {
    private DrawerLayout storeDrawer;

    @Override
    public void onClick(View v) {
        if(!storeDrawer.isDrawerOpen(Gravity.LEFT)){
            storeDrawer.openDrawer(Gravity.LEFT);
        }else if(storeDrawer.isDrawerOpen(Gravity.LEFT)){
            storeDrawer.closeDrawer(Gravity.LEFT);
        }
    }
}
