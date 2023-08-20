package com.example.devicecontroller.account

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.devicecontroller.MainActivity
import com.example.devicecontroller.R
import com.example.devicecontroller.constants.DeviceNumber
import com.example.devicecontroller.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityLoginBinding
    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = Firebase.auth
        database = Firebase.database.reference
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val sharedPreferences = this.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
            database.child("users").child(auth.uid.toString()).child("DeviceNumber").get().addOnSuccessListener {
                val editor = sharedPreferences?.edit()
                editor?.putString(DeviceNumber, it.value.toString())
                editor?.apply()
            }.addOnFailureListener{
                Log.e("Get Data: ", "Failed")
            }
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        binding.LogInBtn.setOnClickListener(this)
        binding.RegisterBtn.setOnClickListener(this)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            view.onApplyWindowInsets(windowInsets)
        }
    }

    override fun onClick(p0: View?) {
        val sharedPreferences = this.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        when(p0!!.id){
            (R.id.LogInBtn)->{
                auth.signInWithEmailAndPassword(binding.LogInEmail.text.toString(), binding.LogInPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            database.child("users").child(auth.uid.toString()).child("DeviceNumber").get().addOnSuccessListener {
                                val editor = sharedPreferences?.edit()
                                editor?.putString(DeviceNumber, it.value.toString())
                                editor?.apply()
                            }.addOnFailureListener{
                                Log.e("Get Data: ", "Failed")
                            }

                            val intent = Intent(applicationContext,MainActivity::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(
                                baseContext,
                                "Wrong Credentials.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
            (R.id.RegisterBtn)->{
                val intent = Intent(applicationContext,RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}