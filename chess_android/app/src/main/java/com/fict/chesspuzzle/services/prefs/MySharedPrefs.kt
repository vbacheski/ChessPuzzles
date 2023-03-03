package com.fict.chesspuzzle.services.prefs

import android.content.Context

class MySharedPrefs(val context: Context) {

    fun getLastTournamentDuration(): Int {
        val duration = getI("last_tournament_duration")
        if (duration < 1) {
            return 7
        }
        return duration
    }

    fun setLastTournamentDuration(duration: Int) {
        saveI("last_tournament_duration", duration)
    }

    fun getLastFenIndex(): Int {
        return getI("last_fen_idex")
    }

    fun setLastFenIndex(lastFenIndex: Int) {
        saveI("last_fen_idex", lastFenIndex)
    }

    // TODO: the hardcoded false are for testing only, they should be removed

    //todo this seems wrong because of the default
    fun getIsFirstTimeLogged(): Boolean {
        return getB("first_time_logged")
    }

    fun setIsFirstTimeLogged(isLogged: Boolean) {
        saveB("first_time_logged", isLogged)
    }

    fun getTournamentCompleted(tournamentId: String): Boolean {
        return getB("tournament_completed" + tournamentId)
    }

    fun setTournamentCompleted(completed: Boolean, tournamentId: String) {
        saveB("tournament_completed" + tournamentId, completed)
    }

    fun getTournamentStartedTime(tournamentId: String): Long {
        return getL("tournament_started_time" + tournamentId)
    }

    fun setTournamentStartedTime(completed: Long, tournamentId: String) {
        saveL("tournament_started_time" + tournamentId, completed)
    }

    fun getEmail(): String {
        return getS("email")
    }

    fun saveEmail(email: String) {
        saveS("email", email)
    }

    fun getPassword(): String {
        return getS("password")
    }

    fun savePassword(password: String) {
        saveS("password", password)
    }

    fun getNickName(): String {
        return getS("nick")
    }

    fun saveNickName(nick: String) {
        saveS("nick", nick)
    }

    fun getTournamentName(): String {
        return getS("tournamentName")
    }

    fun saveTournamentName(tournamentName: String) {
        saveS("tournamentName", tournamentName)
    }

    fun getTournamentId(): String {
        return getS("tournamentId")
    }

    fun saveTournamentId(tournamentId: String) {
        saveS("tournamentId", tournamentId)
    }

    fun getS(key: String): String {
        val sharedPref = context.getSharedPreferences(
            "prefs", Context.MODE_PRIVATE
        )
        return sharedPref.getString(key, "") ?: ""
    }

    fun saveS(key: String, data: String) {
        val sharedPref = context.getSharedPreferences(
            "prefs", Context.MODE_PRIVATE
        )
        sharedPref.edit().putString(key, data).commit()
    }

    fun getB(key: String): Boolean {
        val sharedPref = context.getSharedPreferences(
            "prefs", Context.MODE_PRIVATE
        )
        return sharedPref.getBoolean(key, false)
    }

    fun saveB(key: String, data: Boolean) {
        val sharedPref = context.getSharedPreferences(
            "prefs", Context.MODE_PRIVATE
        )
        sharedPref.edit().putBoolean(key, data).commit()
    }

    fun getL(key: String): Long {
        val sharedPref = context.getSharedPreferences(
            "prefs", Context.MODE_PRIVATE
        )
        return sharedPref.getLong(key, 0L)
    }

    fun saveL(key: String, data: Long) {
        val sharedPref = context.getSharedPreferences(
            "prefs", Context.MODE_PRIVATE
        )
        sharedPref.edit().putLong(key, data).commit()
    }

    fun getI(key: String): Int {
        val sharedPref = context.getSharedPreferences(
            "prefs", Context.MODE_PRIVATE
        )
        return sharedPref.getInt(key, 0)
    }

    fun saveI(key: String, data: Int) {
        val sharedPref = context.getSharedPreferences(
            "prefs", Context.MODE_PRIVATE
        )
        sharedPref.edit().putInt(key, data).commit()
    }

    fun hasKey(key: String): Boolean {
        val sharedPref = context.getSharedPreferences(
            "prefs", Context.MODE_PRIVATE
        )
        return sharedPref.contains(key)
    }
}

