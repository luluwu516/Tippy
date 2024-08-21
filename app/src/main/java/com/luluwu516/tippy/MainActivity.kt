// Tutorial: https://www.youtube.com/watch?v=FjrKMcnKahY
package com.luluwu516.tippy

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar

// const
private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {
    private lateinit var editTextBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var textViewTipPercentLabel: android.widget.TextView
    private lateinit var textViewTipAmount: android.widget.TextView
    private lateinit var textViewTotalAMount: android.widget.TextView
    private lateinit var textViewTipDescription: android.widget.TextView
    private lateinit var buttonPoor: Button
    private lateinit var buttonGood: Button
    private lateinit var buttonAwesome: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // find view by id
        editTextBaseAmount = findViewById(R.id.editTextBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        textViewTipPercentLabel = findViewById(R.id.textViewTipPercentLabel)
        textViewTipAmount = findViewById(R.id.textViewTipAmount)
        textViewTotalAMount = findViewById(R.id.textViewTotalAmount)
        textViewTipDescription = findViewById(R.id.textViewTipDescription)
        buttonPoor = findViewById(R.id.buttonPoor)
        buttonGood = findViewById(R.id.buttonGood)
        buttonAwesome = findViewById(R.id.buttonAwesome)

        // initial setting
        seekBarTip.progress = INITIAL_TIP_PERCENT
        textViewTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)

        // Listener
        seekBarTip.setOnSeekBarChangeListener(object: android.widget.SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                // android.util.Log.i(TAG, "onProgressChanged $p1")
                textViewTipPercentLabel.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        editTextBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                // android.util.Log.i(TAG, "afterTextChanged$p0")
                computeTipAndTotal()
            }
        })

        buttonPoor.setOnClickListener {
            seekBarTip.progress = 5
            computeTipAndTotal()
            updateTipDescription(5)
        }

        buttonGood.setOnClickListener {
            seekBarTip.progress = 15
            computeTipAndTotal()
            updateTipDescription(15)
        }

        buttonAwesome.setOnClickListener {
            seekBarTip.progress = 25
            computeTipAndTotal()
            updateTipDescription(25)
        }

    }

    private fun updateTipDescription(tipPercent: Int) {
        // 1. Determine the description
        val tipDescription = when(tipPercent) {
            in 0..9 -> "Poor"
            in 10..20 -> "Good"
            else -> "Amazing"
        }
        textViewTipDescription.text = tipDescription

        // 2. Update the color based on the tipPercent
        val color =ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max.toFloat(),
            androidx.core.content.ContextCompat.getColor(this, R.color.color_worst_tip),
            androidx.core.content.ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        textViewTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (editTextBaseAmount.text.isEmpty()) {
            textViewTipAmount.text = ""
            textViewTotalAMount.text = ""
            return
        }
        // 1. Get the value of the base and tip percent
        val baseAmount = editTextBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        // 2. Compute the tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        // 3. Update the UI
        textViewTipAmount.text = "$ %.2f".format(tipAmount)       // toString() can't format the floating point
        textViewTotalAMount.text = "$ %.2f".format(totalAmount)   // toString() can't format the floating point
    }
}