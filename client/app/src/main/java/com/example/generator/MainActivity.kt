package com.example.generator

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


private var api: HttpApiService = HttpApiService.create("http://127.0.0.1")

class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                val fragment = HomeFragment.newInstance()
                loadFragment(fragment)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_generator -> {

                val fragment = GeneratorFragment.newInstance()
                loadFragment(fragment)

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val fragment = HomeFragment()
        loadFragment(fragment)
    }

    fun setIP(v: View){
        val ipFieldHome: EditText = findViewById(R.id.home_ip_field)
        val messageLableHome: TextView = findViewById(R.id.home_message_label)

        if(ipFieldHome.text.isNotEmpty()) {
            api = HttpApiService.create("http://${ipFieldHome.text}")
            messageLableHome.text = "IP successfuly set!"
            messageLableHome.setTextColor(Color.GREEN)
        } else {
            messageLableHome.text = "Please enter correct ip!"
            messageLableHome.setTextColor(Color.RED)
        }

    }

    fun sendRequest(v: View){

        val linkField: EditText = findViewById(R.id.generator_link_field)
        val passwdField: EditText = findViewById(R.id.generator_password_field)
        val messageLable: TextView = findViewById(R.id.generator_message_label)

        if(passwdField.text.isNotEmpty()) {

            messageLable.text = "Requesting..."
            messageLable.setTextColor(Color.DKGRAY)

            disposable = api.testGet(passwdField.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { res ->
                        run {
                            linkField.setText(res.url)
                            messageLable.text = "Successfully requrested"
                            messageLable.setTextColor(Color.GREEN)
                        }
                    },
                    { error ->
                        run {
                            messageLable.text = error.message
                            messageLable.setTextColor(Color.RED)
                            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
        } else {
            messageLable.text = "Please enter password!"
            messageLable.setTextColor(Color.RED)
        }

    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        // transaction.addToBackStack(null)
        transaction.commit()
    }

}
