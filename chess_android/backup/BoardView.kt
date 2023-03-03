package old

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.fict.chesspuzzle.R
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.File
import com.github.bhlangonijr.chesslib.Rank
import com.github.bhlangonijr.chesslib.Square
import kotlin.math.min

class BoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var originX = 40f
    private var originY = 250f
    private var cellSide = 170f
    private val scaleFactor = .9f
    private val paint = Paint ()
    private var fromCol: Int = -1
    private var fromRow: Int = -1
    private var movingFigureX = -1f
    private var movingFigureY = -1f
    private var movingFigureBitmap: Bitmap? = null
    private var movingFigure: ChessPiece? =null
    private final val imgResourceIDs = setOf(
        R.drawable.black_bishop,
        R.drawable.black_king,
        R.drawable.black_knight,
        R.drawable.black_pawn,
        R.drawable.black_queen,
        R.drawable.black_rook,
        R.drawable.white_bishop,
        R.drawable.white_king,
        R.drawable.white_knight,
        R.drawable.white_pawn,
        R.drawable.white_queen,
        R.drawable.white_rook,
    )

    private final val bitmaps = mutableMapOf<Int, Bitmap>()

    var chessDelegate: ChessDelegate? = null

    init {
        loadBitmaps()
    }

    override fun onDraw(canvas: Canvas?) {

        Log.d(TAG, "${canvas?.width}, ${canvas?.height}")
        canvas?.let {
            val chessBoardSide = min(it.width, it.height) * scaleFactor
            cellSide = chessBoardSide / 8f
            originX = (it.width - chessBoardSide) / 2f
            originY = (it.height - chessBoardSide) / 2f
        }

        drawChessBoard(canvas)
        drawPieces(canvas)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                fromCol = ((event.x - originX) / cellSide).toInt()
                fromRow = 7 - ((event.y - originY) / cellSide).toInt()

                chessDelegate?.pieceAt(Position(fromCol,fromRow))?.let {
                    movingFigure = it
                    movingFigureBitmap = bitmaps [movingFigure!!.resourceID]
                }
            }
            MotionEvent.ACTION_MOVE -> {
                movingFigureX = event.x
                movingFigureY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                Log.d(TAG, "from ($fromCol, $fromRow) to ($col, $row)")
                chessDelegate?.moveFigure(Position(fromCol,fromRow), Position(col, row))
                movingFigure = null
                movingFigureBitmap = null
            }
        }
        return true
    }

    private fun drawPieces(canvas: Canvas?){
        for (row in 0..7) {
            for (col in 0..7) {
                chessDelegate?.pieceAt(Position(col, row))?.let {
                    if (it != movingFigure) {
                        drawPieceAt(canvas, col, row, it.resourceID)
                    }
                }
            }
        }

        movingFigureBitmap?.let {
            canvas?.drawBitmap(it, null, RectF (movingFigureX - cellSide/2, movingFigureY - cellSide/2,
                movingFigureX + cellSide/2, movingFigureY + cellSide/2), paint)
        }

    }

    private fun drawPieceAt(canvas: Canvas?, col: Int, row: Int, resID: Int) {
        val bitmap = bitmaps[resID]!!
        canvas?.drawBitmap(bitmap, null, RectF (originX + col * cellSide, originY + (7-row) * cellSide,
            originX +(col+1) * cellSide, originY + ((7-row)+1) * cellSide), paint)
    }

    private fun loadBitmaps() {
        imgResourceIDs.forEach {
            bitmaps[it] = BitmapFactory.decodeResource(resources, it)
        }
    }
    private  fun drawChessBoard (canvas: Canvas?) {
        var board = Board();

        for (row in 0..7) {
            for (col in 0..7) {
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(resources.getColor(R.color.green2));
                canvas?.drawRect(
                    (originX - 0.35 * cellSide).toFloat(),
                    (originY - 0.35 * cellSide).toFloat(),
                    (originX + 8.35 * cellSide).toFloat(),
                    (originY + 8.35 * cellSide).toFloat(), paint);
            }
        }

//        rankIterator.get().forEach(i -> {
//            Rank r = Rank.allRanks[i];
//            fileIterator.get().forEach(n -> {
//            File f = File.allFiles[n];
//            if (!File.NONE.equals(f) && !Rank.NONE.equals(r)) {
//                Square sq = Square.encode(r, f);
//                Piece piece = board.getPiece(sq);
//                if(sq.isLightSquare()) {
//
//                }
////                    sb.append(piece.getFenSymbol());
//            }
//        });
//        });

        for (rank in 0..7) {
            var r = Rank.allRanks[rank];
            for(file in 0..7) {
                var f = File.allFiles[file]
                if(!File.NONE.equals(f) && !Rank.NONE.equals(r)) {
                    var sq = Square.encode(r,f);
                    var piece = board.getPiece(sq);

                    if(sq.isLightSquare) {
                        paint.color = resources.getColor(R.color.creamy)
                    } else {
                        paint.color = resources.getColor(R.color.green)
                    }

                    canvas?.drawRect(originX + rank * cellSide, originY + file * cellSide,
                        originX + (rank+1) * cellSide, originY + (file+1) * cellSide, paint)
                }
            }
        }

//        for (row in 0..7) {
//            for (col in 0..7) {
//                paint.color = if ((row + col) % 2 == 1) resources.getColor(R.color.green) else resources.getColor(R.color.creamy)
//                canvas?.drawRect(originX + row * cellSide, originY + col * cellSide,
//                    originX + (row+1) * cellSide, originY + (col+1) * cellSide, paint)
//            }
//        }

        for (row in 0..7) {
            for (col in 0..7) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.BLACK);
                canvas?.drawRect(
                    (originX - 0 * cellSide).toFloat(),
                    (originY - 0 * cellSide).toFloat(),
                    (originX + 8 * cellSide).toFloat(),
                    (originY + 8 * cellSide).toFloat(), paint);
            }
        }


        for (row in 0..7) {
            for (col in 0..7) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.BLACK);
                canvas?.drawRect(
                    (originX - 0.35 * cellSide).toFloat(),
                    (originY - 0.35 * cellSide).toFloat(),
                    (originX + 8.35 * cellSide).toFloat(),
                    (originY + 8.35 * cellSide).toFloat(), paint
                )
            }
        }
    }
}