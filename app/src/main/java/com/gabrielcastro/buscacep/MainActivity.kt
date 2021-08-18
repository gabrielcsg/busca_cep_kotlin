package com.gabrielcastro.buscacep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.gabrielcastro.buscacep.repository.Repository
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var tvLoading: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        val btnSearch = findViewById<Button>(R.id.id_btn_search)
        tvLoading = findViewById(R.id.id_tv_loading)

        btnSearch.setOnClickListener {
            val inputCep = findViewById<TextInputEditText>(R.id.id_textinput_cep)
            when {
                inputCep.text.toString().isEmpty() -> Toast.makeText(
                    applicationContext,
                    getString(R.string.error_empty),
                    Toast.LENGTH_SHORT
                ).show()
                inputCep.text.toString().length != 8 -> Toast.makeText(
                    applicationContext,
                    getString(R.string.error_length),
                    Toast.LENGTH_SHORT
                ).show()
                else -> getCep(inputCep.text.toString())
            }
        }
    }

    private fun getCep(cep: String) {
        tvLoading.visibility = View.VISIBLE
        viewModel.getAddress(cep)
        viewModel.myResponse.observe(this, { response ->
            tvLoading.visibility = View.INVISIBLE
            if (response.isSuccessful) {
                if (response.body()?.error == true) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.error_invalid_cep),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        response.body()?.street,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
        })
    }
}