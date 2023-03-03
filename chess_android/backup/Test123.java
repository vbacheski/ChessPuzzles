package old;

import android.os.Build;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.File;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Rank;
import com.github.bhlangonijr.chesslib.Square;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import androidx.annotation.RequiresApi;

public class Test123 {


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void bla123(){
        Board board = new Board();

//        for (row in 0..7) {
//            for (col in 0..7) {
//                paint.color = if ((row + col) % 2 == 1) resources.getColor(R.color.green); else resources.getColor(R.color.creamy)
//                canvas?.drawRect(originX + row * cellSide, originY + col * cellSide,
//                        originX + (row+1) * cellSide, originY + (col+1) * cellSide, paint)
//            }
//        }

        final Supplier<IntStream> rankIterator = Test123::sevenToZero;
        final Supplier<IntStream> fileIterator = Test123::zeroToSeven;

        rankIterator.get().forEach(i -> {
            Rank r = Rank.allRanks[i];
            fileIterator.get().forEach(n -> {
                File f = File.allFiles[n];
                if (!File.NONE.equals(f) && !Rank.NONE.equals(r)) {
                    Square sq = Square.encode(r, f);
                    Piece piece = board.getPiece(sq);
                    if(sq.isLightSquare()) {

                    }
//                    sb.append(piece.getFenSymbol());
                }
            });
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static IntStream zeroToSeven() {
        return IntStream.iterate(0, i -> i + 1).limit(8);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static IntStream sevenToZero() {
        return IntStream.iterate(7, i -> i - 1).limit(8);
    }
}
