package com.example.devicecontroller.mainActivityFragments

import android.content.Context
import android.graphics.Paint.Cap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.devicecontroller.R
import com.example.devicecontroller.constants.DeviceNumber
import com.example.devicecontroller.databinding.FragmentSettingsBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SettingsFragment : Fragment(), View.OnClickListener {


    private lateinit var binding: FragmentSettingsBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        auth = Firebase.auth
        PersonalData()
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val deviceID = sharedPreferences?.getString(DeviceNumber, "NO ID")
        binding.crntDevice.text = "CURRENT DEVICE ID: $deviceID"

        binding.PassChangeBtn.setOnClickListener(this)
        binding.ChangePassBtn.setOnClickListener(this)
        binding.CancelChangePass.setOnClickListener(this)
        binding.changeCurrentDeviceBtn.setOnClickListener(this)
        return binding.root
    }

    fun PersonalData(){
        database.child("users").child(auth.uid.toString()).child("dataName").get().addOnSuccessListener {
            binding.AccSetName.text = it.value.toString()
        }.addOnFailureListener{
            Log.e("Get Data: ", "Failed")
        }
        database.child("users").child(auth.uid.toString()).child("dataBirthday").get().addOnSuccessListener {
            binding.AccSetBday.text = it.value.toString()
        }.addOnFailureListener{
            Log.e("Get Data: ", "Failed")
        }
        database.child("users").child(auth.uid.toString()).child("dataContactNumber").get().addOnSuccessListener {
            binding.AccSetConNum.text = it.value.toString()
        }.addOnFailureListener{
            Log.e("Get Data: ", "Failed")
        }
        binding.AccSetEmail.text = auth.currentUser?.email.toString()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            (R.id.PassChangeBtn)->{
                binding.PassChangeBtn.visibility = View.GONE
                binding.changePasswordPanel.visibility =  View.VISIBLE
            }
            (R.id.ChangePassBtn)->{
                CheckingOldPassword()
            }
            (R.id.CancelChangePass)->{
                binding.PassChangeBtn.visibility = View.VISIBLE
                binding.changePasswordPanel.visibility =  View.GONE
            }
            (R.id.changeCurrentDeviceBtn)->{
                val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
                database.child("users").child(auth.uid.toString()).child("DeviceNumber").setValue(binding.changeCurrentDevicePlainText.text.toString())
                val editor = sharedPreferences?.edit()
                editor?.putString(DeviceNumber, binding.changeCurrentDevicePlainText.text.toString())
                editor?.apply()
                val deviceID = sharedPreferences?.getString(DeviceNumber, "NO ID")
                binding.crntDevice.text = "CURRENT DEVICE ID: $deviceID"
            }
        }
    }
    fun CheckingOldPassword() {
        val credential = EmailAuthProvider.getCredential(
            binding.AccSetEmail.text.toString(),
            binding.ChangeOldPassowordTxtBox.toString()
        )
        auth.currentUser?.reauthenticate(credential)
            ?.addOnCompleteListener {
            if (CheckingPasswordStr())
                auth.currentUser!!.updatePassword(binding.ChangeNewPasswordTxtBox.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "PASSWORD CHANGE SUCCESS",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.PassChangeBtn.visibility = View.VISIBLE
                            binding.changePasswordPanel.visibility = View.GONE
                        }
                    }
                else
                    Toast.makeText(context, "PASSWORD IS NOT STRONG ENOUGH", Toast.LENGTH_SHORT).show()
                    binding.ChangeNewPasswordTxtBox.error = "PASSWORD MUST BE 8 Character Long, Has Capital Letter, Has Small Letter Has Number."
            }
        }

    fun CheckingPasswordStr(isPassStrong:Boolean = false): Boolean {
        var SmallLetter = false
        var CapitalLetter = false
        var hasNumbers = false
        var isLongEnough =  false

        binding.ChangeNewPasswordTxtBox.doOnTextChanged { s, start, before, count ->
            val PasswordText = binding.ChangeNewPasswordTxtBox.text.toString().toCharArray()
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