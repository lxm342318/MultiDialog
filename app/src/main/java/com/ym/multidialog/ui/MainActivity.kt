package com.ym.multidialog.ui

import android.graphics.Color
import android.os.*
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.ym.multidialog.R
import com.ym.multidialog.view.LineWaveVoiceView

class MainActivity : AppCompatActivity() {

    private lateinit var voice: LineWaveVoiceView
    private var status = VoiceStatus.STOP

    companion object {
        const val VOICE_VIEW = 0x00
    }

    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                VOICE_VIEW -> {

                }
            }
        }
    }

    enum class VoiceStatus(val status: Int, val tag: String) {
        START(0, "开始"),
        STOP(1, "停止")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        //Activity全屏显示，且状态栏被隐藏覆盖掉
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or 0x00002000
        //设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //SDK版本21（5.0）之后 防止系统状态栏看不见和背景冲突
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_main)
        voice = findViewById(R.id.voice_view)
        findViewById<AppCompatTextView>(R.id.tv_start).setOnClickListener {
            when (status) {
                VoiceStatus.START -> {
                    status = VoiceStatus.STOP
                    voice.stopRecord()

                }
                VoiceStatus.STOP -> {
                    status = VoiceStatus.START
                    voice.startRecord()
                }
            }
        }
    }
}
