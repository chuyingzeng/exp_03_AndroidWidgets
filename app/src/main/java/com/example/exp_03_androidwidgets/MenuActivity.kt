package com.example.exp_03_androidwidgets

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast

class MenuActivity : LabActivity() {
    private lateinit var sample: TextView
    private var sizeSp = 16
    private var textColor = Color.BLACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        sample = findViewById(R.id.menu_sample)
        sizeSp = savedInstanceState?.getInt(TEXT_SIZE, 16) ?: 16
        textColor = savedInstanceState?.getInt(TEXT_COLOR, Color.BLACK) ?: Color.BLACK
        applyTextStyle()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.text_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.text_small -> sizeSp = 10
            R.id.text_medium -> sizeSp = 16
            R.id.text_large -> sizeSp = 20
            R.id.text_red -> textColor = Color.RED
            R.id.text_black -> textColor = Color.BLACK
            R.id.menu_regular -> Toast.makeText(this, R.string.menu_regular, Toast.LENGTH_SHORT).show()
            else -> return super.onOptionsItemSelected(item)
        }
        applyTextStyle()
        return true
    }

    private fun applyTextStyle() {
        sample.textSize = sizeSp.toFloat()
        sample.setTextColor(textColor)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TEXT_SIZE, sizeSp)
        outState.putInt(TEXT_COLOR, textColor)
    }

    private companion object {
        const val TEXT_SIZE = "text_size"
        const val TEXT_COLOR = "text_color"
    }
}
