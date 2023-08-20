package com.example.devicecontroller.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.KeyListener
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.doOnTextChanged
import com.example.devicecontroller.MainActivity
import com.example.devicecontroller.R
import com.example.devicecontroller.dataClasses.Users
import com.example.devicecontroller.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Suppress("UNUSED_VALUE")
class RegisterActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var auth: FirebaseAuth

    lateinit var binding: ActivityRegisterBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        database = Firebase.database.reference

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        ChangeText()

        binding.RegisterRegbtn.setOnClickListener(this)
        binding.RegisterBckBtn.setOnClickListener(this)
        binding.RegisterBirthDay.setOnClickListener(this)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            view.onApplyWindowInsets(windowInsets)
        }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            (R.id.Register_Regbtn)->{
                CheckIfNull()
                if(CheckIfNull()){
                    if(CheckingPasswordStr()){
                        auth.createUserWithEmailAndPassword(binding.RegisterEmail.text.toString(), binding.RegisterPassword.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                var name = binding.RegisterFirstName.text.toString()+" "+binding.RegisterMidName.text.toString()+" "+binding.RegisterLastName.text.toString()
                                writeNewUser(name,binding.RegisterContactNumber.text.toString(),binding.RegisterBirthDay.text.toString())
                                val intent = Intent(applicationContext, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Log.e("Failed", "Register")
                                Toast.makeText(
                                    applicationContext,
                                    "Register Failed.",
                                    Toast.LENGTH_LONG,
                                ).show()
                            }
                        }
                    }
                    else{
                        Toast.makeText(applicationContext, "Password is not Strong Enough!",Toast.LENGTH_SHORT).show()
                        binding.RegisterPassword.error = "PASSWORD MUST BE 8 Character Long, Has Capital Letter, Has Small Letter Has Number."
                    }
                } else{
                    Toast.makeText(applicationContext, "Some Fields Are not fill up yet!",Toast.LENGTH_SHORT).show()
                }
            }
            (R.id.RegisterBirthDay)->{
                binding.RegisterBirthDay.setText("")
                binding.RegisterBirthDay.inputType = InputType.TYPE_CLASS_NUMBER
            }
            (R.id.Register_bckBtn)->{
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun CheckIfNull():Boolean{
        var NullTbCheck = 0
        if(binding.RegisterEmail.text.toString().isEmpty()){
            binding.RegisterEmail.error = "Email is needed"
            NullTbCheck = 1
        }
        else{
            NullTbCheck = 0
        }

        if(binding.RegisterPassword.text.toString().isEmpty()){
            binding.RegisterPassword.error = "Password is needed"
            NullTbCheck = 1
        }
        else{
            NullTbCheck = 0
        }

        if(binding.RegisterFirstName.text.toString().isEmpty()){
            binding.RegisterFirstName.error = "First Name is needed"
            NullTbCheck = 1
        }
        else{
            NullTbCheck = 0
        }

        if(binding.RegisterLastName.text.toString().isEmpty()){
            binding.RegisterLastName.error = "Last Name is needed"
            NullTbCheck = 1
        }
        else{
            NullTbCheck = 0
        }

        return NullTbCheck == 0
    }

    @SuppressLint("SetTextI18n")
    fun ChangeText(){
        binding.RegisterBirthDay.doOnTextChanged { text, start, count, after ->
            if(start == 1){
                binding.RegisterBirthDay.setText(binding.RegisterBirthDay.text.toString()+"/")
                binding.RegisterBirthDay.setSelection(start+after+after)
            } else if(start == 4){
                binding.RegisterBirthDay.setText(binding.RegisterBirthDay.text.toString()+"/")
                binding.RegisterBirthDay.setSelection(start+after+after)
            } else if(start >= 9){
                binding.RegisterBirthDay.keyListener = null
            }
        }
    }

    fun writeNewUser(name: String, contactNum: String,Birthday: String) {
        val currentUserUID = auth.currentUser?.uid
        val user = Users(name, contactNum,Birthday)
        if (currentUserUID != null) {
            database.child("users").child(currentUserUID).setValue(user)
        } else{
            Log.e("Unsuccessful: ","Data Wasn't Saved!")
        }
    }

    fun CheckingPasswordStr(isPassStrong:Boolean = false): Boolean {
        var SmallLetter = false
        var CapitalLetter = false
        var hasNumbers = false
        var isLongEnough =  false

        binding.RegisterPassword.doOnTextChanged { s, start, before, count ->
            val PasswordText = binding.RegisterPassword.text.toString().toCharArray()
            for(i in start..count){
                SmallLetter = PasswordText[i] in 'a'..'z'
                CapitalLetter = PasswordText[i] in 'A'..'Z'
                hasNumbers = PasswordText[i] in '0'..'9'
                isLongEnough = count >= 8

            }
        }
        return SmallLetter && CapitalLetter && hasNumbers && isLongEnough
    }
}