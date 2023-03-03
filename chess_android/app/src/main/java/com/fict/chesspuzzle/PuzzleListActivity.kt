package com.fict.chesspuzzle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fict.chesspuzzle.adapters.PuzzleListAdapter
import com.fict.chesspuzzle.models.PuzzleModel

import com.fict.chesspuzzle.tournament.JoinTournamentActivity

class PuzzleListActivity : AppCompatActivity(), PuzzleListAdapter.ClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var puzzleListAdapter: PuzzleListAdapter
    var puzzleList: List<PuzzleModel> = java.util.ArrayList<PuzzleModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.puzzles_activity)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        puzzleListAdapter = PuzzleListAdapter(this)
        recyclerView.adapter = puzzleListAdapter

        //by-pass to Join Tournament Screen
        val intent = Intent(this, JoinTournamentActivity::class.java)
        startActivity(intent)
        finish()


//        val intent2 = Intent(this, Setup2Activity::class.java)
//        startActivity(intent2)
//        finish()

//        val intent2 = Intent(this, SetupPositionActivity::class.java)
//        startActivity(intent2)
//        finish()

        //fetchPuzzles()
        //puzzleListAdapter.setData(getMockPuzzles())
    }

//    private fun fetchPuzzles(): List<PuzzleModel> {
//        val spinner = findViewById<ImageView>(R.id.iv_piece)
//        loadingSpinner(spinner)
//        puzzleList = java.util.ArrayList<PuzzleModel>()
//        val apiService: Api = RetrofitClient().getRetrofitClient()
//        val call: Call<List<PuzzleModel>> = apiService.getPuzzles()
//        call.enqueue(object : Callback<List<PuzzleModel>> {
//            override fun onResponse(
//                call: Call<List<PuzzleModel>>,
//                response: Response<List<PuzzleModel>>
//            ) {
//                puzzleList = response.body() as List<PuzzleModel>
//                Log.d("TAG", "Response = $puzzleList")
//                puzzleListAdapter.setData(puzzleList)
//                spinner.visibility = View.GONE
//            }
//
//            override fun onFailure(call: Call<List<PuzzleModel>>, t: Throwable) {
//                spinner.visibility = View.VISIBLE
//                Log.d("TAG", "Response = $t")
//            }
//        })
//        return puzzleList
//    }

    private fun loadingSpinner(loadingSpinner: ImageView){
        loadingSpinner.visibility = View.VISIBLE
        loadingSpinner.animate().apply {
            duration = 2000
            rotationYBy(720f)
        }.start()
    }

//    private fun getMockPuzzles(): List<PuzzleModel> {
//        val puzzlesList = ArrayList<PuzzleModel>()
//
//        val suffix = " b KQkq e3 0 1"
//        //b/w - side to move
//        //KQ - caste king and queen side
//        //K - king side
//        //Q - queen side
//        //with lower case for the black side, capital for the white side
//
//        puzzlesList.add(PuzzleModel("5bnr/ppp/8/8/8/8/8/6NR b KQkq - 1 2", "Chess Title 100"))
//        puzzlesList.add(PuzzleModel("8/1p1p1p/8/8/8/8/8/8 b KQkq - 1 2", "Chess Title 101"))
//        puzzlesList.add(PuzzleModel("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2", "Title"))
//        puzzlesList.add(PuzzleModel("1nb1kbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 1 2", "Chess Title 1"))
//        puzzlesList.add(PuzzleModel("2bqkbnr/1ppppppp/p7/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 1 2", "Chess Title 2"))
//        puzzlesList.add(PuzzleModel("1nbqkbnr/1pppp1pp/p7/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 1 2", "Chess Title 3"))
//        puzzlesList.add(PuzzleModel("rnbqkbnr/ppppp1pp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 1 2", "Chess Title 4"))
//
//        return puzzlesList
//    }

    override fun clickedItem(puzzleModel: PuzzleModel) {

//        val intent = Intent(this, BoardComposeActivity::class.java)
//        intent.putExtra("fen", puzzleModel.fen)
//        startActivity(intent)
    }
}

