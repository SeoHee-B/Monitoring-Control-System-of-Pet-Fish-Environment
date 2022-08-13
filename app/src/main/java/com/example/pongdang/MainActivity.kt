package com.example.pongdang

import android.content.Intent
import android.graphics.Color
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        val temp28 : ImageView = findViewById(R.id.temp28)
        val temp27 : ImageView = findViewById(R.id.temp27)
        val temp26 : ImageView = findViewById(R.id.temp26)
        val temp25 : ImageView = findViewById(R.id.temp25)
        val bluebar28 : ImageView = findViewById(R.id.bluebar28)
        val graybar28 : ImageView = findViewById(R.id.graybar28)
        val bluebar27 : ImageView = findViewById(R.id.bluebar27)
        val graybar27 : ImageView = findViewById(R.id.graybar27)
        val bluebar26 : ImageView = findViewById(R.id.bluebar26)
        val graybar26 : ImageView = findViewById(R.id.graybar26)
        val bluebar25 : ImageView = findViewById(R.id.bluebar25)
        val graybar25 : ImageView = findViewById(R.id.graybar25)

        val num_25 : TextView = findViewById(R.id.num25)
        val num_26 : TextView = findViewById(R.id.num26)
        val num_27 : TextView = findViewById(R.id.num27)
        val num_28 : TextView = findViewById(R.id.num28)

        val flow_button: ImageButton = findViewById(R.id.btn_flows_visible)
        val flow_button_gone: ImageButton = findViewById(R.id.btn_flows_gone)
        val led_button : ImageButton = findViewById(R.id.btn_leds_visible)
        val led_button_gone : ImageButton = findViewById(R.id.btn_leds_gone)
        val temp_seekbar : SeekBar = findViewById(R.id.temp_seekbar)
        val phTxt: TextView = findViewById(R.id.phtxt)
        val tempTxt: TextView = findViewById(R.id.temptxt)
        val tdsTxt: TextView = findViewById(R.id.tdstxt)


        var dref = FirebaseDatabase.getInstance().reference

        dref.addValueEventListener( object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val ph =  p0.child("ph").getValue().toString()
                phTxt.text = ph
                val tds =  p0.child("tds").getValue().toString()
                tdsTxt.text = tds
                val temp =  p0.child("temp").getValue().toString()
                tempTxt.text = temp
                val dref_t =  p0.child("temp").getValue().toString()
                val temp_r = dref_t.toInt()
                val dref_s = p0.child("temp_set").getValue().toString()
                val tempset = dref_s.toInt()

                if(temp_r < tempset){
                    var database = FirebaseDatabase.getInstance().reference
                    var heat = database.ref.child("heater")
                    heat.setValue(1)
                }else{
                    var database = FirebaseDatabase.getInstance().reference
                    var heat = database.ref.child("heater")
                    heat.setValue(0)
                }


            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })



        flow_button.setOnClickListener {
            flow_button.setVisibility(View.INVISIBLE)
            flow_button_gone.setVisibility(View.VISIBLE)

            var database = FirebaseDatabase.getInstance().reference
            var flowsig = database.ref.child("flow")
            flowsig.setValue(0)


        }

        flow_button_gone.setOnClickListener {
            flow_button_gone.setVisibility(View.INVISIBLE)
            flow_button.setVisibility(View.VISIBLE)

            var database = FirebaseDatabase.getInstance().reference
            var flowsig = database.ref.child("flow")
            flowsig.setValue(1)
        }

        led_button.setOnClickListener {
            led_button.setVisibility(View.INVISIBLE)
            led_button_gone.setVisibility(View.VISIBLE)
            var database = FirebaseDatabase.getInstance().reference
            var ledsig = database.ref.child("light")
            ledsig.setValue(0)
        }

        led_button_gone.setOnClickListener {
            led_button_gone.setVisibility(View.INVISIBLE)
            led_button.setVisibility(View.VISIBLE)
            var database = FirebaseDatabase.getInstance().reference
            var ledsig = database.ref.child("light")
            ledsig.setValue(1)
        }




        temp_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                when (p1) {
                    0 -> {
                        var database = FirebaseDatabase.getInstance().reference
                        var tempSet = database.ref.child("temp_set")
                        tempSet.setValue(25)
                        temp28.setVisibility(View.INVISIBLE)
                        temp27.setVisibility(View.INVISIBLE)
                        temp26.setVisibility(View.INVISIBLE)
                        temp25.setVisibility(View.VISIBLE)

                        graybar25.setVisibility(View.INVISIBLE)
                        graybar26.setVisibility(View.VISIBLE)
                        graybar27.setVisibility(View.VISIBLE)
                        graybar28.setVisibility(View.VISIBLE)
                        bluebar25.setVisibility(View.VISIBLE)
                        bluebar26.setVisibility(View.INVISIBLE)
                        bluebar27.setVisibility(View.INVISIBLE)
                        bluebar28.setVisibility(View.INVISIBLE)

                        num_25.setTextColor(Color.parseColor("#73A1D0"))
                        num_26.setTextColor(Color.parseColor("#bababa"))
                        num_27.setTextColor(Color.parseColor("#bababa"))
                        num_28.setTextColor(Color.parseColor("#bababa"))
                    }
                    1 -> {
                        var database = FirebaseDatabase.getInstance().reference
                        var tempSet = database.ref.child("temp_set")
                        tempSet.setValue(26)
                        temp28.setVisibility(View.INVISIBLE)
                        temp27.setVisibility(View.INVISIBLE)
                        temp26.setVisibility(View.VISIBLE)
                        temp25.setVisibility(View.INVISIBLE)

                        graybar25.setVisibility(View.VISIBLE)
                        graybar26.setVisibility(View.INVISIBLE)
                        graybar27.setVisibility(View.VISIBLE)
                        graybar28.setVisibility(View.VISIBLE)
                        bluebar25.setVisibility(View.INVISIBLE)
                        bluebar26.setVisibility(View.VISIBLE)
                        bluebar27.setVisibility(View.INVISIBLE)
                        bluebar28.setVisibility(View.INVISIBLE)
                        num_25.setTextColor(Color.parseColor("#bababa"))
                        num_27.setTextColor(Color.parseColor("#bababa"))
                        num_28.setTextColor(Color.parseColor("#bababa"))
                        num_26.setTextColor(Color.parseColor("#73A1D0"))
                    }
                    2 -> {
                        var database = FirebaseDatabase.getInstance().reference
                        var tempSet = database.ref.child("temp_set")
                        tempSet.setValue(27)
                        temp28.setVisibility(View.INVISIBLE)
                        temp27.setVisibility(View.VISIBLE)
                        temp26.setVisibility(View.INVISIBLE)
                        temp25.setVisibility(View.INVISIBLE)


                        graybar25.setVisibility(View.VISIBLE)
                        graybar26.setVisibility(View.VISIBLE)
                        graybar27.setVisibility(View.INVISIBLE)
                        graybar28.setVisibility(View.VISIBLE)
                        bluebar25.setVisibility(View.INVISIBLE)
                        bluebar26.setVisibility(View.INVISIBLE)
                        bluebar27.setVisibility(View.VISIBLE)
                        bluebar28.setVisibility(View.INVISIBLE)

                        num_25.setTextColor(Color.parseColor("#bababa"))
                        num_26.setTextColor(Color.parseColor("#bababa"))
                        num_28.setTextColor(Color.parseColor("#bababa"))
                        num_27.setTextColor(Color.parseColor("#73A1D0"))
                    }
                    else -> {
                        var database = FirebaseDatabase.getInstance().reference
                        var tempSet = database.ref.child("temp_set")
                        tempSet.setValue(28)
                        temp28.setVisibility(View.VISIBLE)
                        temp27.setVisibility(View.INVISIBLE)
                        temp26.setVisibility(View.INVISIBLE)
                        temp25.setVisibility(View.INVISIBLE)


                        graybar25.setVisibility(View.VISIBLE)
                        graybar26.setVisibility(View.VISIBLE)
                        graybar27.setVisibility(View.VISIBLE)
                        graybar28.setVisibility(View.INVISIBLE)
                        bluebar25.setVisibility(View.INVISIBLE)
                        bluebar26.setVisibility(View.INVISIBLE)
                        bluebar27.setVisibility(View.INVISIBLE)
                        bluebar28.setVisibility(View.VISIBLE)

                        num_25.setTextColor(Color.parseColor("#bababa"))
                        num_26.setTextColor(Color.parseColor("#bababa"))
                        num_27.setTextColor(Color.parseColor("#bababa"))
                        num_28.setTextColor(Color.parseColor("#73A1D0"))
                    }
                }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }


        })

    }
}

class SplashScreenActivity : AppCompatActivity() {
    // After 3000 mileSeconds / 3 seconds your next activity will display.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        loadSplashScreen()
    }

    private fun loadSplashScreen(){
        Handler().postDelayed({
            // You can declare your desire activity here to open after finishing splash screen. Like MainActivity
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}



