package com.example.appka1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appka1.R
import com.example.appka1.models.User
import com.example.appka1.network.WroCinemaApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface LoginResultsUiState {
    data class Success(val apiLoginResult: User) : LoginResultsUiState
    object Error : LoginResultsUiState
    object Loading : LoginResultsUiState
}

class LoginActivity : AppCompatActivity() {
    private var loginState: LoginResultsUiState = LoginResultsUiState.Loading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val errorTextView = findViewById<TextView>(R.id.errorTextView)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    val user = withContext(Dispatchers.IO) {
                        WroCinemaApi.retrofitService.userLogin(email = username, password = password)
                    }

                    if (user != null) {
                        loginState = LoginResultsUiState.Success(user)
                        val userJson = Gson().toJson(user)
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java).apply {
                            putExtra("USER", userJson)
                        }
                        Log.d("Login", "Zalogowano użytkownika: $user")
                        startActivity(intent)
                        finish()
                    } else {
                        loginState = LoginResultsUiState.Error
                        Log.e("Login", "Błąd logowania: użytkownik nie istnieje")
                    }

                } catch (e: Exception) {
                    loginState = LoginResultsUiState.Error
                    Log.e("Login", "Błąd logowania: ${e.message}")
                }
            }
        }
    }
}