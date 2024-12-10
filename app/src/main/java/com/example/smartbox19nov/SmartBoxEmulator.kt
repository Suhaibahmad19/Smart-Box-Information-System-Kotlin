package com.example.smartbox19nov

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class SmartBoxEmulator : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val parcelId: String? = intent.getStringExtra("parcelId")
        val parcelDestination:String? = intent.getStringExtra("parcelDestination")
        val callFrom:String? = intent.getStringExtra("CALL_FROM")
        when(callFrom){
            "ParcelDeliveryByRider"->{
                setContentView(R.layout.activity_smart_box_emulator_type2)
                val vaultButton: Button = findViewById(R.id.vaultButton)
                val notifier = findViewById<TextView>(R.id.notifier)
                vaultButton.text = "Parcel $parcelId Delivered"
                notifier.text = "Thank You For Your Service. You may go now!"
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun startTheActivity(sbox:SmartBox){
        when (sbox.type) {
            1 -> setContentView(R.layout.activity_smart_box_emulator)
            2 -> setContentView(R.layout.activity_smart_box_emulator_type2)
            else -> setContentView(R.layout.activity_smart_box_emulator)
        }
    }
    private fun clientSmartBox(sbox:SmartBox,parcelPackage: ParcelPackage){
        startTheActivity(sbox)
        when(sbox.withdrawPackage(parcelPackage)){
            "Small"->{

            }
            ""->{

            }
        }
        val view:View = findViewById(R.id.vaultButton)
    }
    fun changeColor(view: View) {
        view.setBackgroundColor(Color.BLUE);
    }
    private fun makePackages():ArrayList<ParcelPackage>{
        val dummyPackages = ArrayList<ParcelPackage>()
        for (i in 1..4){
            i.toString()
            val p = ParcelPackage(i.toString(),"Medium")
            dummyPackages.add(p)
        }
        return dummyPackages
    }
}
