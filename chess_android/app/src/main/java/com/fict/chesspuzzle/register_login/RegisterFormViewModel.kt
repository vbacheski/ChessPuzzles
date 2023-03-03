package com.fict.chesspuzzle.register_login

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.fict.chesspuzzle.App
import com.fict.chesspuzzle.models.RegisterFormModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterFormViewModel : ViewModel() {
    private var _myRegisterModel = MutableStateFlow(RegisterFormModel())
    val myRegisterModel = _myRegisterModel.asStateFlow()

    lateinit var auth: FirebaseAuth

    fun updateNickname(newNickname: String) {
        _myRegisterModel.update { it.copy(nickname = newNickname) }
    }

    fun updateEmail(newEmail: String) {
        _myRegisterModel.update { it.copy(email = newEmail) }
    }

    fun updatePassword(newPassword: String) {
        _myRegisterModel.update { it.copy(password = newPassword) }
        println("HERE $newPassword")
    }

    fun onRegisterClick(email: String, password: String) {
        auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(App.getAppContext(), "Authentication Successful", Toast.LENGTH_SHORT)
                    .show()

                val intent = Intent(App.getAppContext(), LoginFormActivity::class.java)
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