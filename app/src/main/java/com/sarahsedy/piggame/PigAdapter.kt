package com.sarahsedy.piggame

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class PigAdapter(private var leaderBoardLogList: List<LeaderBoardItem>) :
    RecyclerView.Adapter<PigAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var item: TextView = view.findViewById(R.id.leaderBoardItemTxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.leaderboard_log_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val leaderBoardItem = leaderBoardLogList[position]

        holder.item.text = leaderBoardItem.toString()

        //change text color based on person or com
        if (leaderBoardItem.player == "You" || leaderBoardItem.player == "Usted") {
            holder.item.setTextColor(Color.BLUE)
        } else if (leaderBoardItem.player == "Computer" || leaderBoardItem.player == "Computadora") {
            holder.item.setTextColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return leaderBoardLogList.size
    }
}