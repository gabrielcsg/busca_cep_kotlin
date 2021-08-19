package com.gabrielcastro.buscacep

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private lateinit var tvError: TextView
    private lateinit var btnOpenMap: Button

    private lateinit var street: String
    private lateinit var neighborhood: String
    private lateinit var city: String
    private lateinit var state: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        val btnSearch = findViewById<Button>(R.id.id_btn_search)
        tvLoading = findViewById(R.id.id_tv_loading)
        tvError = findViewById(R.id.id_tv_errorText)
        btnOpenMap = findViewById(R.id.id_btn_map)

        btnSearch.setOnClickListener {
            val inputCep = findViewById<TextInputEditText>(R.id.id_textinput_cep)
            when {
                inputCep.text.toString().isEmpty() -> {
                    tvError.text = getString(R.string.error_empty)
                    tvError.visibility = View.VISIBLE
                }
                inputCep.text.toString().length != 8 -> {
                    tvError.text = getString(R.string.error_length)
                    tvError.visibility = View.VISIBLE
                }
                else -> getCep(inputCep.text.toString())
            }
        }

        btnOpenMap.setOnClickListener {
            val geolocation = Uri.parse(
                "geo:0,0?q=${street.replace(" ", "+")}%2C+${
                    neighborhood.replace(" ", "+")
                }"
            )
            showMap(geolocation)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getCep(cep: String) {
        tvLoading.visibility = View.VISIBLE
        viewModel.getAddress(cep)
        viewModel.myResponse.observe(this, { response ->
            tvLoading.visibility = View.GONE
            if (response.isSuccessful) {
                if (response.body()?.error == true) {
                    tvError.text = getString(R.string.error_invalid_cep)
                    tvError.visibility = View.VISIBLE
                } else {
                    tvError.visibility = View.GONE
                    btnOpenMap.visibility = View.VISIBLE
                    street = response.body()?.street!!
                    neighborhood = response.body()?.neighborhood!!
                    city = response.body()?.city!!
                    state = response.body()?.state!!
                    findViewById<TextView>(R.id.id_tv_street).text =
                        getString(R.string.street) + street
                    findViewById<TextView>(R.id.id_tv_neighborhood).text =
                        getString(R.string.neighborhood) + neighborhood
                    findViewById<TextView>(R.id.id_tv_city).text = getString(R.string.city) + city
                    findViewById<TextView>(R.id.id_tv_state).text =
                        getString(R.string.state) + state
                    return@observe
                }
            } else Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()

            clearFields()
        })
    }

    private fun clearFields() {
        findViewById<TextView>(R.id.id_tv_street).text = ""
        findViewById<TextView>(R.id.id_tv_neighborhood).text = ""
        findViewById<TextView>(R.id.id_tv_city).text = ""
        findViewById<TextView>(R.id.id_tv_state).text = ""
        btnOpenMap.visibility = View.INVISIBLE
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun showMap(geoLocation: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = geoLocation
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}