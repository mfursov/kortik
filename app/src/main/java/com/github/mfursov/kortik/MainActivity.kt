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

    override fun onCreateOptionsMenu(menu: Menu): Boolean = navBarController.onCreateOptionsMenu(menu, menuInflater)

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean = navBarController.onPrepareOptionsMenu(menu)

    override fun onDestroy() = navBarController.onDestroy()

    override fun onOptionsItemSelected(item: MenuItem): Boolean = navBarController.onOptionsItemSelected(item)

}

