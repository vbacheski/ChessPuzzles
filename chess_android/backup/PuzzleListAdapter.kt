package old

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fict.chesspuzzle.R


class PuzzleListAdapter(clickListener: ClickListener) : RecyclerView.Adapter<PuzzleListAdapter.MyViewHolder>() {

    private var puzzlesList: List<PuzzleListModel> = arrayListOf()
    private lateinit var context: Context
    private var clickListener: ClickListener = clickListener

    public fun setData(puzzleListModel: List<PuzzleListModel>) {
        this.puzzlesList = puzzleListModel
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        return MyViewHolder (LayoutInflater.from(parent.context).inflate(R.layout.puzzle_details_activity, parent, false))

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var puzzleListModel = puzzlesList.get(position)
        var title = puzzleListModel.puzzleTitle
        var fen = puzzleListModel.puzzleFen

        holder.tvTitle.text = title
        holder.tvFen.text = fen

        holder.itemView.setOnClickListener{
            clickListener.clickedItem(puzzleListModel)
        }

    }

    override fun getItemCount(): Int {
        return puzzlesList.size
    }

    interface ClickListener{
        fun clickedItem(puzzleListModel: PuzzleListModel)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.descriptionTextView)
        val tvFen = itemView.findViewById<TextView>(R.id.fenTextView)
    }



}