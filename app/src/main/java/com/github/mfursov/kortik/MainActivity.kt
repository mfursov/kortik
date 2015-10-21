package com.github.mfursov.kortik

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.github.mfursov.kortik.util.KortikLogger
import org.jetbrains.anko.debug

class MainActivity : AppCompatActivity(), KortikLogger {
    private var navBarController: NavBarController = NavBarController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        debug { "MainActivity::onCreate $savedInstanceState" }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navBarController.onCreate();
    }

    override fun onDestroy() {
        debug { "MainActivity::onDestroy" }
        navBarController.onDestroy()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean = navBarController.onCreateOptionsMenu(menu, menuInflater)

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean = navBarController.onPrepareOptionsMenu(menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = navBarController.onOptionsItemSelected(item)
}

