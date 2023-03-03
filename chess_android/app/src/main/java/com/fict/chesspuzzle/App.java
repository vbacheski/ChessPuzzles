package com.fict.chesspuzzle;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import com.fict.chesspuzzle.models.LeaderboardModel;
import com.fict.chesspuzzle.models.PuzzleModel;
import com.fict.chesspuzzle.services.LeaderboardService;
import com.fict.chesspuzzle.services.PuzzleService;
import com.fict.chesspuzzle.tournament.TournamentComposeActivity;
import java.util.ArrayList;

public class App extends Application {

    // TODOS
    //todo - add drawable xxxhdpi, xxhdpi, xhdpi, hdpi etc
    //todo - treba da zastitime kill na app i reset na points per puzzle

    private static final String TAG = App.class.getSimpleName();

    private static App sInstance;

    public static PuzzleService puzzleService = new PuzzleService();

    public static LeaderboardService leaderboardService = new LeaderboardService();

    public static Context getAppContext() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static void startTournamentScreen() {
        //LeaderboardComposeActivity TournamentComposeActivity
        Intent intent = new Intent(App.getAppContext(), TournamentComposeActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        App.getAppContext().startActivity(intent);
    }

    public static void setPuzzles(ArrayList<PuzzleModel> list) {
        puzzleService.setPuzzle(list);
    }

    public static void setLeaderboard(ArrayList<LeaderboardModel> list) {
        leaderboardService.setLeaderboardData(list);
    }
}