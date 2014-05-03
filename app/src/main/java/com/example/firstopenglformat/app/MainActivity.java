package com.example.firstopenglformat.app;

import android.support.v7.app.ActionBarActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

@EActivity(resName = "activity_main")
@OptionsMenu(resName = "main")
public class MainActivity extends ActionBarActivity {
    @OptionsItem
    final void actionSettings() {
    }
}
