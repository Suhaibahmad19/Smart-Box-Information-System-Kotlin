package com.example.smartbox19nov

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class SmartBoxEmulator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sbox: SmartBox? = intent.getParcelableExtra("SMART_BOX")
        val user:User? = intent.getParcelableExtra("USER")
        val parcelPackage:ParcelPackage? = intent.getParcelableExtra("PARCEL_PACKAGE")
        print("sbox id is " + sbox?.type)
        when(user?.role){
            "Courier"->{
                print("nothing implemented here yet")
            }
            "Client"->{
                clientSmartBox(sbox,parcelPackage)
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
            true->{

            }
            false->{

            }
        }
        val view:View = findViewById(R.id.vaultButton)
    }
    fun changeColor(view: View) {
        view.setBackgroundColor(Color.BLUE);
    }
    private fun makePackages():ArrayList<PackageItem>{
        val dummyPackages = ArrayList<PackageItem>()
        for (i in 1..4){
            i.toString()
            val p = PackageItem(i.toString())
            dummyPackages.add(p)
        }
        return dummyPackages
    }
}
