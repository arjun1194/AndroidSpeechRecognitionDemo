package com.example.myapplication

import android.Manifest
import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.WordListAdapter
import com.example.myapplication.models.Dummy
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {
    lateinit var speechRecognizer: SpeechRecognizer

    lateinit var textView: TextView
    lateinit var startButton: Button
    lateinit var stopButton: Button
    lateinit var recyclerView: RecyclerView
    lateinit var debugCurrentValue: TextView

    private val myAdapter: WordListAdapter = WordListAdapter()
    private var currentPosition:Int = 0

    private val itemsList: MutableList<Boolean> = Dummy.getBooleanList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setRecognizerListener()

        debugCurrentValue.text = currentPosition.toString()

    }

    private fun initViews() {
        startButton = findViewById<Button>(R.id.startButton)
        stopButton = findViewById<Button>(R.id.stopButton)

        //TextViews
        textView = findViewById<TextView>(R.id.textView)
        debugCurrentValue = findViewById(R.id.debug_current_position)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        myAdapter.setItems(Dummy.getWordlist())
        recyclerView.adapter = myAdapter


        startButton.setOnClickListener {
           markItem(true)
        }
        stopButton.setOnClickListener {
            markItem(false)
        }
    }

    private fun setRecognizerListener() {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                println("Ready for speech")
            }

            override fun onRmsChanged(rmsdB: Float) {
                //println("RMS value for Speech is $rmsdB")
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                println("Buffer recieved")

            }

            override fun onPartialResults(partialResults: Bundle?) {
                println("Partial results --> $partialResults")

            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                println("onEvent--> $eventType")

            }

            override fun onBeginningOfSpeech() {
                println("--Beginning of speech--")

            }

            override fun onEndOfSpeech() {
                println("--End of speech--")

            }

            override fun onError(error: Int) {
                if (error == SpeechRecognizer.ERROR_NO_MATCH) {
                    println("Voice not detected")
                } else {
                    println("Error has occurred $error")
                    // if there is an error, or no sound is made
                    // mark current item as false and continue to next item
                    markItem(false)

                }


            }

            override fun onResults(results: Bundle?) {
                val textResult = results?.get("results_recognition").toString();
                val spokenWord =  textResult.subSequence(1, textResult.length - 1).toString()
                if (checkCurrentWordCorrect(spokenWord)){
                    //mark current word as green
                    markItem(true)
                } else {
                    // mark current word as red
                    markItem(false)
                }

            }

        })
    }

    private fun markItem(value: Boolean){
       val currentChild = recyclerView.getChildViewHolder(recyclerView.layoutManager!!.getChildAt(0)!!)
        (currentChild as WordListAdapter.WordViewHolder).frame.background = if (value){
            itemsList[currentPosition++] = true
            ContextCompat.getDrawable(this,R.drawable.view_border_correct)
        } else {
            itemsList[currentPosition++] = true
            ContextCompat.getDrawable(this,R.drawable.view_border_incorrect)
        }
        recyclerView.smoothScrollToPosition(currentPosition)

        //debug stuff here
        printDebug("currentPosition is $currentPosition")
        printDebug("recyclerView child count is ${recyclerView.childCount}")
        printDebug("currentChild adapter position is  ${recyclerView.getChildAdapterPosition(recyclerView.layoutManager!!.getChildAt(0)!!)}")
    }

    private fun startSpeechRecognition() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
        try {
            speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(this))
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this,"Error While Locating a Recognizer",Toast.LENGTH_LONG).show()
        }
    }



    fun  checkCurrentWordCorrect(spokenWord: String): Boolean{
        val currentWord = Dummy.getWordlist()[currentPosition]
        return currentWord.value == spokenWord
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            startSpeechRecognition()
        }
    }

    private fun printDebug(message: String){
        debugCurrentValue.text = "${debugCurrentValue.text} \n $message"
    }

}