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

    var dataList: ArrayList<HashMap<String, String>> = ArrayList()

    class HolderData(v: View) : RecyclerView.ViewHolder(v) {
        val txElement = v.findViewById<TextView>(R.id.editTextElement)
        val txValor = v.findViewById<TextView>(R.id.editTextValor)

        fun initializeLong(item: java.util.HashMap<String, String>, action: onLongClickListener) {

            itemView.setOnLongClickListener {
                action.onLongItemClick(item, adapterPosition)
                true
            }

        }

        fun initializeClick(item: java.util.HashMap<String, String>, action: onClickListener) {

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
        holder.txElement.text = data["item_value"]
        holder.txValor.text = data["item_name"]

        holder.initializeLong(dataList[position], longClickListener)
        holder.initializeClick(dataList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface onLongClickListener {
        fun onLongItemClick(item: java.util.HashMap<String, String>, position: Int) {

        }
    }

    interface onClickListener {
        fun onItemClick(item: java.util.HashMap<String, String>, position: Int) {

        }
    }

    fun setAdapterList(list: ArrayList<HashMap<String, String>>) {
        dataList = list
        notifyDataSetChanged()
    }
}

