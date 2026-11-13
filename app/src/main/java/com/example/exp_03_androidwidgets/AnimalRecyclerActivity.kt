package com.example.exp_03_androidwidgets

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AnimalRecyclerActivity : LabActivity() {
    private lateinit var records: MutableList<Animal>
    private lateinit var count: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_recycler)

        records = savedInstanceState?.getStringArrayList(RECORDS)
            ?.mapTo(mutableListOf(), Animal::valueOf)
            ?: Animal.entries.toMutableList()
        count = findViewById(R.id.record_count)

        val list = findViewById<RecyclerView>(R.id.animal_recycler)
        val adapter = AnimalAdapter(records) { animal ->
            Toast.makeText(this, animal.label, Toast.LENGTH_SHORT).show()
        }
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
        updateCount()

        findViewById<Button>(R.id.add_record).setOnClickListener {
            val animals = Animal.entries
            AlertDialog.Builder(this)
                .setTitle(R.string.add_record)
                .setItems(animals.map { it.label }.toTypedArray()) { _, index ->
                    val position = records.size
                    records.add(animals[index])
                    adapter.notifyItemInserted(position)
                    list.scrollToPosition(position)
                    updateCount()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }

    private fun updateCount() {
        count.text = getString(R.string.record_count, records.size)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(RECORDS, records.mapTo(ArrayList()) { it.name })
    }

    private companion object {
        const val RECORDS = "records"
    }
}

private class AnimalAdapter(
    private val records: List<Animal>,
    private val onAnimalClick: (Animal) -> Unit
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val row = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_animal, parent, false)
        return AnimalViewHolder(row)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = records[position]
        holder.name.text = animal.label
        holder.photo.setImageResource(animal.photo)
        holder.itemView.setOnClickListener { onAnimalClick(animal) }
    }

    override fun getItemCount(): Int = records.size

    class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.animal_name)
        val photo: ImageView = itemView.findViewById(R.id.animal_photo)
    }
}
