package old

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fict.chesspuzzle.R

class PuzzleList : AppCompatActivity(), PuzzleListAdapter.ClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var puzzleListAdapter: PuzzleListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.puzzles_activity)
        initData();

    }
    private fun initData(){
        recyclerView = findViewById(R.id.recyclerView)
        initRecyclerView();
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        puzzleListAdapter = PuzzleListAdapter(this);
        recyclerView.adapter = puzzleListAdapter;
        showData();
    }

    private fun addPuzzles():List<PuzzleListModel> {
        var puzzlesList = ArrayList<PuzzleListModel>()
        puzzlesList.add(PuzzleListModel("Chess Title 1", "FEN 1"))
        puzzlesList.add(PuzzleListModel("Chess Title 2", "FEN 2"))
        puzzlesList.add(PuzzleListModel("Chess Title 3", "FEN 3"))
        puzzlesList.add(PuzzleListModel("Chess Title 4", "FEN 4"))
        puzzlesList.add(PuzzleListModel("Chess Title 5", "FEN 5"))
        puzzlesList.add(PuzzleListModel("Chess Title 6", "FEN 6"))
        puzzlesList.add(PuzzleListModel("Chess Title 7", "FEN 7"))
        puzzlesList.add(PuzzleListModel("Chess Title 8", "FEN 8"))

        return puzzlesList
    }

    private fun showData(){
        puzzleListAdapter.setData(addPuzzles())
    }

    override fun clickedItem(puzzleListModel: PuzzleListModel) {
        startActivity(Intent(this, MainActivity::class.java).putExtra("puzzleTitle",puzzleListModel.puzzleTitle))
    }


}

