package com.fict.chesspuzzle.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fict.chesspuzzle.R
import com.fict.chesspuzzle.models.PuzzleModel


class PuzzleListAdapter(private var clickListener: ClickListener) :
    RecyclerView.Adapter<PuzzleListAdapter.MyViewHolder>() {

    private var puzzlesList: List<PuzzleModel> = arrayListOf()
    private lateinit var context: Context

    fun setData(puzzleModel: List<PuzzleModel>) {
        this.puzzlesList = puzzleModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.puzzle_details_activity, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val puzzleListModel = puzzlesList[position]
        val description = puzzleListModel.description
        val fen = puzzleListModel.fen

        holder.description.text = description
        holder.fen.text = fen

        holder.itemView.setOnClickListener {
            clickListener.clickedItem(puzzleListModel)
        }
    }

    override fun getItemCount(): Int {
        return puzzlesList.size
    }

    interface ClickListener {
        fun clickedItem(puzzleModel: PuzzleModel)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        val fen: TextView = itemView.findViewById(R.id.fenTextView)
    }

}