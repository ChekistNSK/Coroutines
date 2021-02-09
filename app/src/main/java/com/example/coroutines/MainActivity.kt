package com.example.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"

    private lateinit var button: Button
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<Button>(R.id.button)
        textView = findViewById<TextView>(R.id.textView)

        button.setOnClickListener {
            // IO, Main, Default
            // IO - network & local DB  ::  Main - UI  ::  Default - heavy operations
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }
    }

    private fun setNewText(input: String) {
        val newText = textView.text.toString() + "\n$input"
        textView.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
//        CoroutineScope(Main).launch {
//            setNewText(input)
//        }
        withContext(Main) {
            setNewText(input)
        }
    }

    // MARK: Api Request Simulation
    private suspend fun fakeApiRequest() {
        val startTime = System.currentTimeMillis()
        CoroutineScope(IO).launch {
            val result1 = getResult1FromApi()
            println("debug: $result1")
            setTextOnMainThread(result1)

            val result2 = getResult2FromApi()
            println("debug: $result2")
            setTextOnMainThread(result2)
        }

        println("debug: done in ${System.currentTimeMillis() - startTime} ms")
    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1700)
        return RESULT_2
    }
    private fun logThread(methodName: String) {
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }

}