package com.example.devicecontroller.mainActivityFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import com.airbnb.lottie.Lottie
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

        val ButtonArray = listOf(
            binding.BulbNumber1,
            binding.BulbNumber2,
            binding.BulbNumber3,
            binding.BulbNumber4,
            binding.BulbNumber5,
            binding.BulbNumber6,
            binding.BulbNumber7,
            binding.BulbNumber8
        )


        for(i in 0..7){
            ButtonArray[i].setOnClickListener(this)
        }
        return binding.root
    }

    override fun onClick(p0: View?) {
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val deviceID = sharedPreferences?.getString(DeviceNumber, "NO ID")
        when(p0!!.id){
            (R.id.BulbNumber1)->{
                if(binding.BulbNumber1.isChecked){
                    binding.bulb1.playAnimation()
                    database.child("Device").child(deviceID.toString()).child("Bulb1").setValue("ON")
                    Log.e("Error", "Button 1 is on")
                } else{
                    binding.bulb1.cancelAnimation()
                    binding.bulb1.frame = 10
                    database.child("Device").child(deviceID.toString()).child("Bulb1").setValue("OFF")
                    Log.e("Error", "Button 1 is off")
                }
            }
            (R.id.BulbNumber2)->{
                if(binding.BulbNumber2.isChecked){
                    binding.bulb2.playAnimation()
                    database.child("Device").child(deviceID.toString()).child("Bulb2").setValue("ON")
                    Log.e("Error", "Button 2 is on")
                } else{
                    binding.bulb2.cancelAnimation()
                    binding.bulb2.frame = 10
                    database.child("Device").child(deviceID.toString()).child("Bulb2").setValue("OFF")
                    Log.e("Error", "Button 2 is off")
                }
            }
            (R.id.BulbNumber3)->{
                if(binding.BulbNumber3.isChecked){
                    binding.bulb3.playAnimation()
                    database.child("Device").child(deviceID.toString()).child("Bulb3").setValue("ON")
                    Log.e("Error", "Button 3 is on")
                } else{
                    binding.bulb3.cancelAnimation()
                    binding.bulb3.frame = 10
                    database.child("Device").child(deviceID.toString()).child("Bulb3").setValue("OFF")
                    Log.e("Error", "Button 3 is off")
                }
            }
            (R.id.BulbNumber4)->{
                if(binding.BulbNumber4.isChecked){
                    binding.bulb4.playAnimation()
                    database.child("Device").child(deviceID.toString()).child("Bulb4").setValue("ON")
                    Log.e("Error", "Button 4 is on")
                } else{
                    binding.bulb4.cancelAnimation()
                    binding.bulb4.frame = 10
                    database.child("Device").child(deviceID.toString()).child("Bulb4").setValue("OFF")
                    Log.e("Error", "Button 4 is off")
                }
            }
            (R.id.BulbNumber5)->{
                if(binding.BulbNumber5.isChecked){
                    binding.bulb5.playAnimation()
                    database.child("Device").child(deviceID.toString()).child("Bulb5").setValue("ON")
                    Log.e("Error", "Button 5 is on")
                } else{
                    binding.bulb5.cancelAnimation()
                    binding.bulb5.frame = 10
                    database.child("Device").child(deviceID.toString()).child("Bulb5").setValue("OFF")
                    Log.e("Error", "Button 5 is off")
                }
            }
            (R.id.BulbNumber6)->{
                if(binding.BulbNumber6.isChecked){
                    binding.bulb6.playAnimation()
                    database.child("Device").child(deviceID.toString()).child("Bulb6").setValue("ON")
                    Log.e("Error", "Button 6 is on")
                } else{
                    binding.bulb6.cancelAnimation()
                    binding.bulb6.frame = 10
                    database.child("Device").child(deviceID.toString()).child("Bulb6").setValue("OFF")
                    Log.e("Error", "Button 6 is off")
                }
            }
            (R.id.BulbNumber7)->{
                if(binding.BulbNumber7.isChecked){
                    binding.bulb7.playAnimation()
                    database.child("Device").child(deviceID.toString()).child("Bulb7").setValue("ON")
                    Log.e("Error", "Button 7 is on")
                } else{
                    binding.bulb7.cancelAnimation()
                    binding.bulb7.frame = 10
                    database.child("Device").child(deviceID.toString()).child("Bulb7").setValue("OFF")
                    Log.e("Error", "Button 7 is off")
                }
            }
            (R.id.BulbNumber8)->{
                if(binding.BulbNumber8.isChecked){
                    binding.bulb8.playAnimation()
                    database.child("Device").child(deviceID.toString()).child("Bulb8").setValue("ON")
                    Log.e("Error", "Button 8 is on")
                } else{
                    binding.bulb8.cancelAnimation()
                    binding.bulb8.frame = 10
                    database.child("Device").child(deviceID.toString()).child("Bulb8").setValue("OFF")
                    Log.e("Error", "Button 8 is off")
                }
            }
        }
    }

    fun OnDataBaseChange() {
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE)
        val deviceID = sharedPreferences?.getString(DeviceNumber, "NO ID")

        val ButtonArray = listOf(
            binding.BulbNumber1,
            binding.BulbNumber2,
            binding.BulbNumber3,
            binding.BulbNumber4,
            binding.BulbNumber5,
            binding.BulbNumber6,
            binding.BulbNumber7,
            binding.BulbNumber8
        )

        val WidgetArray = listOf(
            binding.bulb1,
            binding.bulb2,
            binding.bulb3,
            binding.bulb4,
            binding.bulb5,
            binding.bulb6,
            binding.bulb7,
            binding.bulb8
        )

        for(i in 0..7){
            database.child("Device").child(deviceID.toString()).child("Bulb"+(i+1)).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isOn = snapshot.value
                    if(isOn == "ON"){
                        WidgetArray[i].playAnimation()
                        ButtonArray[i].isChecked = true

                    } else{
                        WidgetArray[i].cancelAnimation()
                        WidgetArray[i].frame = 10
                        ButtonArray[i].isChecked = false
                        Log.e("Error", "Button $i is off")
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }


    }

}