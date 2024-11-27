package com.example.smartbox19nov

import android.graphics.Color
import android.os.Bundle
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
        print("sbox id is " + sbox?.type)
        if(sbox?.type == 1)
            setContentView(R.layout.activity_smart_box_emulator)
        else if(sbox?.type == 2)
            setContentView(R.layout.activity_smart_box_emulator_type2)
        else
            setContentView(R.layout.activity_smart_box_emulator)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun changeColor(view: View) {
        view.setBackgroundColor(Color.BLUE);
    }
    private fun makePackages():ArrayList<PackageItem>{
        var dummyPackages = ArrayList<PackageItem>()
        for (i in 1..4){
            i.toString()
            var p = PackageItem(i.toString())
            dummyPackages.add(p)
        }
        return dummyPackages
    }
}
