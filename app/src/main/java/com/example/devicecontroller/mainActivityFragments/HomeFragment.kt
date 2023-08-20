package com.example.devicecontroller.mainActivityFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.devicecontroller.R
import com.example.devicecontroller.constants.DeviceNumber
import com.example.devicecontroller.databinding.FragmentDevicesBinding
import com.example.devicecontroller.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        OnDataBaseChange()
        return binding.root
    }

    fun OnDataBaseChange() {
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val deviceID = sharedPreferences?.getString(DeviceNumber, "NO ID")
        database.child("Device").child(deviceID.toString()).child("Bulb1").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isOn = snapshot.value
                if(isOn == "ON"){
                    binding.lottieAnimationView.progress = 0.9F
                } else{
                    binding.lottieAnimationView.progress = 0.1F
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })

        database.child("Device").child(deviceID.toString()).child("Bulb2").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isOn = snapshot.value
                if(isOn == "ON"){
                    binding.lottieAnimationView2.progress = 0.9F
                } else{
                    binding.lottieAnimationView2.progress = 0.1F
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}