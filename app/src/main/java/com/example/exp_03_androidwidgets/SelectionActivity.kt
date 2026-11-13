package com.example.exp_03_androidwidgets

import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.core.util.keyIterator

class SelectionActivity : LabActivity() {
    private lateinit var names: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        names = savedInstanceState?.getStringArrayList(ITEM_NAMES)
            ?: ArrayList(resources.getStringArray(R.array.selection_items).toList())
        val rows = names.mapTo(mutableListOf()) {
            mapOf("name" to it, "icon" to R.mipmap.ic_launcher)
        }
        val adapter = SimpleAdapter(
            this,
            rows,
            R.layout.item_selection,
            arrayOf("name", "icon"),
            intArrayOf(R.id.selection_name, R.id.selection_icon)
        )

        val list = findViewById<ListView>(R.id.selection_list)
        list.adapter = adapter
        list.emptyView = findViewById(R.id.selection_empty)
        list.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        list.setMultiChoiceModeListener(object : AbsListView.MultiChoiceModeListener {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                mode.menuInflater.inflate(R.menu.selection_actions, menu)
                mode.title = getString(R.string.selected_count, list.checkedItemCount)
                return true
            }

            override fun onItemCheckedStateChanged(
                mode: ActionMode,
                position: Int,
                id: Long,
                checked: Boolean
            ) {
                mode.title = getString(R.string.selected_count, list.checkedItemCount)
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                if (item.itemId != R.id.delete_selection) return false
                val checked = list.checkedItemPositions
                val positions = checked.keyIterator().asSequence()
                    .filter { checked[it] }
                    .sortedDescending()
                    .toList()
                mode.finish()
                positions.forEach { position ->
                    rows.removeAt(position)
                    names.removeAt(position)
                }
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false

            override fun onDestroyActionMode(mode: ActionMode) = Unit
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(ITEM_NAMES, names)
    }

    private companion object {
        const val ITEM_NAMES = "item_names"
    }
}
