package com.karan.voiceassistant

import android.Manifest
import android.R.attr.text
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.http.UrlRequest.Status
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.karan.voiceassistant.databinding.ActivityMainBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    lateinit var binding: ActivityMainBinding
    private var permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false
    private lateinit var recorder: MediaRecorder
    private var audioFilePath: String? = null
    private var fileName: String? = null
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this));
        }
        //Python integration practise

//        val py = Python.getInstance()
//        val pyObj = py.getModule("hy")  // your script

        tts = TextToSpeech(this, this)
//        val python = Python.getInstance()
//        val py = python.getModule("hy")
//        val message = py.callAttr("get_greeting").toString()
//        speak(message)
        val py = Python.getInstance()
        val pyModule = py.getModule("hy")

        val result =
            pyModule.callAttr("handle_command", "search wikipedia Kotlin", "Alex").toString()
// You can then use TTS in Kotlin or return result in UI
        speak(result)
        Log.d("VOICE_ASSISTANT", "Speaking: $text")
        permissionGranted = ActivityCompat.checkSelfPermission(
            this,
            permissions[0]
        ) == PackageManager.PERMISSION_GRANTED
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            speak("Hello from TTS!")
        }
    }

    private fun speak(string: String) {
        tts.speak("hello my name is karan", TextToSpeech.QUEUE_FLUSH, null, null)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        startRecording()
    }

    private fun startRecording() {
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
            return
        }
        recorder = MediaRecorder()
        audioFilePath = "${externalCacheDir?.absolutePath}"
        var simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
        var date = simpleDateFormat.format(Date())
        fileName = "audio_record_$date"
        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile("$audioFilePath$fileName.mp3")

            try {
                prepare()
            } catch (e: IOException) {

            }
            start()
        }

    }


}
