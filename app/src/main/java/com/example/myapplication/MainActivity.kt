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
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(){
    lateinit var speechRecognizer: SpeechRecognizer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startButton = findViewById<Button>(R.id.startButton)
        val stopButton = findViewById<Button>(R.id.stopButton)
        val textView = findViewById<TextView>(R.id.textView)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)



        speechRecognizer.setRecognitionListener(object: RecognitionListener{
            override fun onReadyForSpeech(params: Bundle?) {
                println("Ready for speech")
            }

            override fun onRmsChanged(rmsdB: Float) {
                println("RMS value for Speech is $rmsdB")
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
                println("Error has occurred $error")

            }

            override fun onResults(results: Bundle?) {
                results?.keySet()?.forEach {
                    Log.d("myApplication", "${it} is a key in the bundle")
                }

                println("Results are -->"+results?.get("results_recognition").toString())
                textView.text = results?.get("results_recognition").toString()

                println("Confidence Scores are -->"+results?.get("confidence_scores").toString())
            }

        })

        startButton.setOnClickListener {
            if(checkSelfPermission(permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED){
                //permission Granted, start recognition
                startSpeechRecognition()
            } else {
                requestPermissions(listOf(Manifest.permission.RECORD_AUDIO).toTypedArray(),101)
            }
        }

        stopButton.setOnClickListener {
            speechRecognizer.stopListening()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==101 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }){
            startSpeechRecognition()
        }
    }

    fun startSpeechRecognition(){
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
        speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(this))
    }

}