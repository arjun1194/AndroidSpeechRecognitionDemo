package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Word

class WordListAdapter: RecyclerView.Adapter<WordListAdapter.WordViewHolder>(){

    private var items : List<Word> = ArrayList();

    fun setItems(list: List<Word>){
        items = list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_list_item,parent,false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item,position)
    }

    inner class WordViewHolder(view: View): RecyclerView.ViewHolder(view){
         val frame = view.findViewById<FrameLayout>(R.id.frame)
         val textView = view.findViewById<TextView>(R.id.word)
        fun bind(item: Word,position: Int){
            textView.text = item.value
        }
    }

}