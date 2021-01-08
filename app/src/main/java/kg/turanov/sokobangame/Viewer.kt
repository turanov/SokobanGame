package kg.turanov.sokobangame

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kg.turanov.sokobangame.controllers.Controller
import kg.turanov.sokobangame.data.Direction
import kg.turanov.sokobangame.views.Canvas

public class Viewer : AppCompatActivity {
    private val controller: Controller
    private lateinit var canvas: Canvas
    private lateinit var alertDialog: AlertDialog
    constructor(){
        this.controller =
            Controller(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkRequestPermission()
        canvas = Canvas(
            this,
            controller.getModel()
        )
        canvas.setOnTouchListener(controller)
        setContentView(canvas)

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.level_menu, menu)
        controller.initMenu(menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        controller.onOptionsItemSelectedHandler(item)
        return true
    }
    private fun checkRequestPermission() {
        val permissionWrite = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
        //For Android Version R or higher, check manage external storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri: Uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                this.startActivity(intent)
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty()) {
                if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "a permit is required", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
    fun dialogNextLevel(){
        val builder = AlertDialog.Builder(this)
        val view = View.inflate(this,
            R.layout.you_win_dialog, null)
        builder.setView(view)
        val next = view.findViewById<Button>(R.id.button_next)
        val replay = view.findViewById<Button>(R.id.button_replay)
        val previous = view.findViewById<Button>(R.id.button_previous)
        next.setOnClickListener(controller)
        replay.setOnClickListener(controller)
        previous.setOnClickListener(controller)
        alertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }
    fun dialogLastLevel() {
        val builder = AlertDialog.Builder(this)
        val view = View.inflate(this,
            R.layout.last_level_dialog, null)
        builder.setView(view)
        val restart = view.findViewById<Button>(R.id.button_restart)
        restart.setOnClickListener(controller)
        alertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }
    fun update() {
        canvas.update();
    }
    fun updateMan(direction: Direction) {
        canvas.updateMan(direction);
    }
    fun dismissDialog() {
        alertDialog.dismiss()
    }

}