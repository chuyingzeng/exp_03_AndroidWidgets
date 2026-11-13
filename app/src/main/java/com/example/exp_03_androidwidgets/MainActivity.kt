package com.example.exp_03_androidwidgets

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : LabActivity(showUp = false) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        link(R.id.open_animal_list, AnimalListActivity::class.java)
        link(R.id.open_menu, MenuActivity::class.java)
        link(R.id.open_selection, SelectionActivity::class.java)
        link(R.id.open_recycler, AnimalRecyclerActivity::class.java)
        findViewById<Button>(R.id.open_dialog).setOnClickListener { showSignInDialog() }
    }

    private fun link(buttonId: Int, destination: Class<out Activity>) {
        findViewById<Button>(buttonId).setOnClickListener {
            startActivity(Intent(this, destination))
        }
    }

    private fun showSignInDialog() {
        val form = layoutInflater.inflate(R.layout.dialog_sign_in, null)
        val username = form.findViewById<EditText>(R.id.username)
        val password = form.findViewById<EditText>(R.id.password)
        val dialog = AlertDialog.Builder(this)
            .setView(form)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.sign_in, null)
            .create()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            when {
                username.text.isBlank() -> username.error = getString(R.string.username_required)
                password.text.isBlank() -> password.error = getString(R.string.password_required)
                else -> {
                    Toast.makeText(this, R.string.sign_in_complete, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }
    }
}
