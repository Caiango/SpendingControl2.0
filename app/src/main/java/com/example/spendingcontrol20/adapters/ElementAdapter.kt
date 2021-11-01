package com.example.spendingcontrol20.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.spendingcontrol20.R
import com.example.spendingcontrol20.model.FireStoreUtils
import com.example.spendingcontrol20.utils.Constants
import com.example.spendingcontrol20.utils.db

class ElementAdapter(
    var longClickListener: onLongClickListener,
    var clickListener: onClickListener,
    var type: String,
    val context: Context
) : RecyclerView.Adapter<ElementAdapter.HolderData>() {

    var dataList: ArrayList<HashMap<String, String>> = ArrayList()

    class HolderData(v: View) : RecyclerView.ViewHolder(v) {
        val txElement = v.findViewById<TextView>(R.id.editTextElement)
        val txValor = v.findViewById<TextView>(R.id.editTextValor)
        val lay = v.findViewById<ConstraintLayout>(R.id.constraint)
        val txData = v.findViewById<TextView>(R.id.txtDataElement)
        val cb = v.findViewById<CheckBox>(R.id.cbPayed)

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
        holder.txElement.text = data[Constants.ITEM_NAME]
        holder.txValor.text = data[Constants.ITEM_VALUE]
        holder.txData.text = data[Constants.ITEM_DATA]
        if (type == "Desp") {
            holder.lay.setBackgroundColor(Color.parseColor("#D3F44336"))
        } else {
            holder.lay.setBackgroundColor(Color.parseColor("#0C8540"))
        }

        holder.initializeLong(dataList[position], longClickListener)
        holder.initializeClick(dataList[position], clickListener)
        holder.cb.isChecked = data[Constants.ITEM_PAYED] == "true"
        holder.cb.setOnCheckedChangeListener { cb, b ->
            FireStoreUtils.updateIsPayed(
                db,
                cb.isChecked.toString(), context,
                data[Constants.ITEM_UID]!!,
                type
            )
        }
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

