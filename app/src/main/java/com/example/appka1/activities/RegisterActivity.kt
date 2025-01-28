package com.example.appka1.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appka1.R
import com.example.appka1.RegisterUserDTO
import com.example.appka1.network.WroCinemaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface RegisterResultsUiState {
    object Success : RegisterResultsUiState
    object Error : RegisterResultsUiState
    object Loading : RegisterResultsUiState
}

class RegisterActivity : AppCompatActivity() {
    private var registerApiState: RegisterResultsUiState = RegisterResultsUiState.Loading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val codeButton = findViewById<Button>(R.id.codeButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val codeEditText = findViewById<EditText>(R.id.codeEditText)

        codeButton.setOnClickListener {
            if (codeEditText.visibility == View.GONE) {
                Log.d("RegisterActivity", "Code Button clicked - visible")
                codeEditText.visibility = View.VISIBLE
            } else {
                Log.d("RegisterActivity", "Code Butto n clicked - gone")
                codeEditText.visibility = View.GONE
            }
            Log.d("RegisterActivity", "Code Button clicked")
        }

        registerButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val email = findViewById<EditText>(R.id.emailEditText).text.toString()
                    val firstname = findViewById<EditText>(R.id.firstnameEditText).text.toString()
                    val lastname = findViewById<EditText>(R.id.lastnameEditText).text.toString()
                    val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
                    val confirmPassword = findViewById<EditText>(R.id.confirmPasswordEditText).text.toString()

                    if (password != confirmPassword) {
                        Log.e("RegisterActivity", "Error: Passwords do not match")
                        return@launch
                    }else{

                        if (codeEditText.visibility != View.VISIBLE) {
                            val registerUserDTO = RegisterUserDTO(email, firstname, lastname, password)
                            WroCinemaApi.retrofitService.registerUser(registerUserDTO)
                            registerApiState = RegisterResultsUiState.Success
                        }else {
                            val code = findViewById<EditText>(R.id.codeEditText).text.toString()
                            val registerUserDTO = RegisterUserDTO(email, firstname, lastname, password, code)
                            WroCinemaApi.retrofitService.registerUser(registerUserDTO)
                            registerApiState = RegisterResultsUiState.Success
                        }
                    }

                } catch (e: Exception) {
                    registerApiState = RegisterResultsUiState.Error
                    Log.e("RegisterActivity", "Error: ${e.message}")
                }
            }
        }
    }
}