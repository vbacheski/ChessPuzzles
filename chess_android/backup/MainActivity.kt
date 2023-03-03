package old

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fict.chesspuzzle.R
import com.github.bhlangonijr.chesslib.Board


const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), ChessDelegate {

    private var chessModel = ChessModel
    private lateinit var chessBoardTitle : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chess_board_activity)
        initData()


        findViewById<BoardView>(R.id.board_view).chessDelegate = this

    }

    override fun pieceAt(position: Position): ChessPiece? = ChessModel.pieceAt(position)

    override fun moveFigure(from: Position, to: Position) {
        ChessModel.moveFigure(from, to)
        findViewById<BoardView>(R.id.board_view).invalidate()
    }

    private fun initData(){
        chessBoardTitle = findViewById(R.id.chessBoardTitle)
        getData()
    }

    private fun getData(){
        var intent = intent.extras

        var puzzle = intent!!.getString("puzzleTitle")
        chessBoardTitle.text = puzzle

    }
    var board = Board()
}

