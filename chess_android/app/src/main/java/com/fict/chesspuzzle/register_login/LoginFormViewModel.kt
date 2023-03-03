package com.fict.chesspuzzle.register_login

import android.widget.Toast
import com.fict.chesspuzzle.tournament.JoinTournamentActivity
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.fict.chesspuzzle.App
import com.fict.chesspuzzle.models.LoginFormModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class LoginFormViewModel : ViewModel() {
    private var _myLoginModel = MutableStateFlow(LoginFormModel())
    val myLoginModel = _myLoginModel.asStateFlow()

    lateinit var auth: FirebaseAuth


    fun updateEmail(newEmail: String) {
        _myLoginModel.update { it.copy(email = newEmail) }
    }

    fun updatePassword(newPassword: String) {
        _myLoginModel.update { it.copy(password = newPassword) }
    }

    fun onLoginClick(email: String, password: String) {
        auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        App.getAppContext(),
                        "Authentication Successful",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(App.getAppContext(), JoinTournamentActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    App.getAppContext().startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(App.getAppContext(), "Authentication Failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}