package com.udj009.aboutme

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udj009.aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mName = MyName("udj009")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mData = mName
    }

    fun onButtonClicked(view: View) {

        binding.apply {
            //mName.nickName = inputName.text.toString()
            invalidateAll()
            textResult.text = inputName.text
            textResult.visibility = View.VISIBLE
            view.visibility = View.GONE
            inputName.visibility = View.GONE
        }

        val inputService = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputService.hideSoftInputFromWindow(view.windowToken, 0)
    }


}