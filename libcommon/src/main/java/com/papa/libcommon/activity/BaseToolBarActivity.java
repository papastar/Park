package com.papa.libcommon.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;

public abstract class BaseToolBarActivity extends BaseAppCompatActivity {


    protected void setToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            if (getSupportActionBar() != null)
                getSupportActionBar().setHomeButtonEnabled(true);//决定左上角的图标是否可以点击
        }
    }

}
