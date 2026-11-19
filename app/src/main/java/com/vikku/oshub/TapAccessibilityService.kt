package com.vikku.oshub

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.Color
import android.graphics.drawable.GradientDrawable

class TapAccessibilityService : AccessibilityService() {
    companion object { var instance: TapAccessibilityService? = null }
    private val handler = Handler(Looper.getMainLooper())
    private var window: View? = null
    private val points = mutableListOf<Pair<Float,Float>>()
    private var running = false
    private var index = 0
    private var intervalMs = 700L

    override fun onServiceConnected() { super.onServiceConnected(); instance = this }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() { stopTaps() }
    override fun onDestroy() { stopTaps(); removeController(); instance = null; super.onDestroy() }

    fun showController() {
        if (window != null) return
        val panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(10,8,10,8)
            background = GradientDrawable().apply { setColor(Color.rgb(18,18,18)); setStroke(2,Color.LTGRAY); cornerRadius=8f }
        }
        val title = TextView(this).apply { text="VIKKU CLICKER  •  0/10"; setTextColor(Color.WHITE); textSize=12f; setPadding(4,4,4,8) }
        panel.addView(title)
        fun btn(t: String, fn: () -> Unit) {
            val b = Button(this).apply { text=t; textSize=10f; setOnClickListener { fn(); title.text="VIKKU CLICKER  •  ${points.size}/10" } }
            panel.addView(b, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 42))
        }
        btn("＋ ADD TAP POINT") { addPlacementMarker(title) }
        btn("▶ PLAY") { startTaps() }
        btn("■ STOP") { stopTaps() }
        btn("CLEAR POINTS") { stopTaps(); points.clear(); title.text="VIKKU CLICKER  •  0/10" }
        btn("CLOSE") { stopTaps(); removeController() }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.END; x=8; y=90 }
        window = panel
        try { (getSystemService(WINDOW_SERVICE) as WindowManager).addView(panel, params) }
        catch (_: Exception) { window = null }
    }

    private fun addPlacementMarker(title: TextView) {
        if (points.size >= 10) return
        val marker = TextView(this).apply {
            text="DRAG TO TARGET\nTAP TO SAVE"; textSize=10f; setTextColor(Color.BLACK); gravity=Gravity.CENTER
            background=GradientDrawable().apply { setColor(Color.LTGRAY); setStroke(3,Color.WHITE); cornerRadius=60f }
        }
        val p = WindowManager.LayoutParams(150,90,WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,PixelFormat.TRANSLUCENT).apply {
            gravity=Gravity.TOP or Gravity.START; x=120; y=350
        }
        marker.setOnTouchListener(object: View.OnTouchListener {
            var sx=0f; var sy=0f; var ox=0; var oy=0
            override fun onTouch(v: View, e: MotionEvent): Boolean {
                when(e.action) {
                    MotionEvent.ACTION_DOWN -> { sx=e.rawX; sy=e.rawY; ox=p.x; oy=p.y; return true }
                    MotionEvent.ACTION_MOVE -> { p.x=ox+(e.rawX-sx).toInt(); p.y=oy+(e.rawY-sy).toInt(); (getSystemService(WINDOW_SERVICE) as WindowManager).updateViewLayout(marker,p); return true }
                    MotionEvent.ACTION_UP -> {
                        points.add(Pair(p.x+75f,p.y+45f))
                        (getSystemService(WINDOW_SERVICE) as WindowManager).removeView(marker)
                        title.text="VIKKU CLICKER  •  ${points.size}/10"
                        return true
                    }
                }
                return true
            }
        })
        try { (getSystemService(WINDOW_SERVICE) as WindowManager).addView(marker,p) } catch (_: Exception) {}
    }

    private fun startTaps() {
        if (running || points.isEmpty()) return
        running = true; index = 0
        tapNext()
    }
    private fun tapNext() {
        if (!running || points.isEmpty()) return
        val point = points[index % points.size]
        val path = Path().apply { moveTo(point.first, point.second) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path,0,65))
            .build()
        val accepted = dispatchGesture(gesture, object: GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                index = (index + 1) % points.size
                if (running) handler.postDelayed({ tapNext() }, intervalMs)
            }
            override fun onCancelled(gestureDescription: GestureDescription?) {
                if (running) handler.postDelayed({ tapNext() }, intervalMs)
            }
        }, null)
        if (!accepted && running) handler.postDelayed({ tapNext() }, intervalMs)
    }
    private fun stopTaps() { running=false; handler.removeCallbacksAndMessages(null) }
    private fun removeController() {
        window?.let { try { (getSystemService(WINDOW_SERVICE) as WindowManager).removeView(it) } catch (_: Exception) {} }
        window=null
    }
}
