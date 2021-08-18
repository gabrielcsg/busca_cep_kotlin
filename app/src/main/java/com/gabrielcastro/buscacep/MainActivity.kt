package com.gabrielcastro.buscacep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.gabrielcastro.buscacep.repository.Repository

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getAddress("55292430")
        viewModel.myResponse.observe(this, { response ->
            if (response.isSuccessful) {
                Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                findViewById<TextView>(R.id.tvHello).text = response.body()?.street!!
            }
            else Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
        })
    }
}