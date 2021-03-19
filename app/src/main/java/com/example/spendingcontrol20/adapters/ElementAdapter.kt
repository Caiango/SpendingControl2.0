package com.example.spendingcontrol20.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spendingcontrol20.R

class ElementAdapter(
    var longClickListener: onLongClickListener,
    var clickListener: onClickListener
) : RecyclerView.Adapter<ElementAdapter.HolderData>() {

    var dataList: List<String> = ArrayList()

    class HolderData(v: View) : RecyclerView.ViewHolder(v) {
        val txElement = v.findViewById<TextView>(R.id.editTextElement)
        val txValor = v.findViewById<TextView>(R.id.editTextValor)


        fun initializeLong(item: String, action: onLongClickListener) {

            itemView.setOnLongClickListener {
                action.onLongItemClick(item, adapterPosition)
                true
            }

        }

        fun initializeClick(item: String, action: onClickListener) {

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderData {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_element, parent, false)
        return HolderData(v)
    }

    override fun onBindViewHolder(holder: HolderData, position: Int) {

        val data = dataList[position]

        holder.initializeLong(dataList[position], longClickListener)
        holder.initializeClick(dataList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface onLongClickListener {
        fun onLongItemClick(item: String, position: Int) {

        }
    }

    interface onClickListener {
        fun onItemClick(item: String, position: Int) {

        }
    }
}

