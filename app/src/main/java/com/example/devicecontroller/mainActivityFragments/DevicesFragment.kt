package com.example.devicecontroller.mainActivityFragments

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.FloatRange
import com.example.devicecontroller.R
import com.example.devicecontroller.constants.DeviceNumber
import com.example.devicecontroller.databinding.FragmentDevicesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DevicesFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentDevicesBinding
    private lateinit var database: DatabaseReference



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDevicesBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        OnDataBaseChange()
        binding.switch1.setOnClickListener(this)
        binding.switch2.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(p0: View?) {
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val deviceID = sharedPreferences?.getString(DeviceNumber, "NO ID")
        when(p0!!.id){
            (R.id.switch1)->{
                if(binding.switch1.isChecked){
                    binding.bulb1.playAnimation()
                    database.child("Device").child(deviceID.toString()).child("Bulb1").setValue("ON")
                } else{
                    binding.bulb1.cancelAnimation()
                    binding.bulb1.frame = 10
                    database.child("Device").child(deviceID.toString()).child("Bulb1").setValue("OFF")
                }
            }
            (R.id.switch2)->{
                if(binding.switch2.isChecked){
                    binding.bulb2.playAnimation()
                    database.child("Device").child(deviceID.toString()).child("Bulb2").setValue("ON")
                } else{
                    binding.bulb2.cancelAnimation()
                    binding.bulb2.frame = 10
                    database.child("Device").child(deviceID.toString()).child("Bulb2").setValue("OFF")
                }
            }
        }
    }

    fun OnDataBaseChange() {
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val deviceID = sharedPreferences?.getString(DeviceNumber, "NO ID")
        database.child("Device").child(deviceID.toString()).child("Bulb1").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val isOn = snapshot.value
                if(isOn == "ON"){
                    binding.bulb1.playAnimation()
                    binding.switch1.isChecked = true
                } else{
                    binding.bulb2.cancelAnimation()
                    binding.bulb1.frame = 10
                    binding.switch1.isChecked = false
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })

        database.child("Device").child(deviceID.toString()).child("Bulb2").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val isOn = snapshot.value
                if(isOn == "ON"){
                    binding.bulb2.playAnimation()
                    binding.switch2.isChecked = true
                } else{
                    binding.bulb2.cancelAnimation()
                    binding.bulb2.frame = 10
                    binding.switch2.isChecked = false
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}