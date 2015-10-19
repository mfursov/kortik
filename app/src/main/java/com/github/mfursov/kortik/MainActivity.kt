package com.github.mfursov.kortik

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class MainActivity : AppCompatActivity(), AnkoLogger {
    private var navBarController: NavBarController = NavBarController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        debug { "onCreate $savedInstanceState" }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navBarController.onCreate();
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        debug { "onCreateOptionsMenu $menu" }
        menuInflater.inflate(R.menu.menu_main, menu);
        return true
    }

    override fun onDestroy() = navBarController.onDestroy();
    override fun onOptionsItemSelected(item: MenuItem): Boolean = navBarController.onOptionsItemSelected(item);
}

