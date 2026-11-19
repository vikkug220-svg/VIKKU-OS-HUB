package com.vikku.oshub

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.provider.Settings
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.*
import android.graphics.drawable.GradientDrawable

class MainActivity : Activity() {
    private val bg = Color.rgb(9,9,9)
    private val panel = Color.rgb(17,17,17)
    private val line = Color.rgb(55,55,55)
    private val white = Color.rgb(245,245,245)
    private val muted = Color.rgb(155,155,155)
    private val osNames = listOf("LineageOS","crDroid","Evolution X","PixelOS","DerpFest","GrapheneOS","Paranoid Android","Project Elixir")
    private val osDesc = listOf(
        "A community Android distribution focused on longevity, customization and broad device support. Exact device builds must be checked.",
        "A customization-focused Android ROM with device-specific builds and feature variations.",
        "A Pixel-inspired Android experience with extra customization. Availability depends on the exact device.",
        "A Pixel-style Android ROM. Confirm the exact device maintainer and build before considering installation.",
        "A feature-rich community ROM. Device support and build status vary.",
        "A security-focused Android OS officially designed for supported Google Pixel devices; it is not a Vivo Y02 ROM.",
        "An Android ROM project with device-specific availability. Verify official project status and exact device support.",
        "A community ROM with device-dependent builds. Confirm current support before any installation."
    )
    private lateinit var content: LinearLayout
    private val prefs by lazy { getSharedPreferences("vikku", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = 0
        buildUi()
    }

    private fun buildUi() {
        val scroll = ScrollView(this).apply { setBackgroundColor(bg) }
        content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(18), dp(14), dp(18), dp(28)); setBackgroundColor(bg) }
        scroll.addView(content)
        setContentView(scroll)

        val top = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val logo = TextView(this).apply { text = "V"; textSize = 24f; typeface = Typeface.DEFAULT_BOLD; gravity = Gravity.CENTER; setTextColor(white); background = shape(Color.TRANSPARENT, line, 1); layoutParams = LinearLayout.LayoutParams(dp(42), dp(42)) }
        top.addView(logo)
        val brand = TextView(this).apply { text = "  VIKKU  OS HUB"; textSize = 18f; typeface = Typeface.DEFAULT_BOLD; setTextColor(white); letterSpacing = .08f }
        top.addView(brand)
        content.addView(top)
        addGap(26)
        text("DEVICE TOOLS / CUSTOM OS", 10, muted, true)
        text("TAKE CONTROL.", 42, white, true)
        text("Explore Android systems, save one OS choice, and use native gaming tools.", 13, muted, false)
        addGap(22)
        sectionTitle("CUSTOM OS COLLECTION", "Choose one option at a time. This app does not flash or replace your phone's operating system.")
        val selected = prefs.getString("selected_os", null)
        osNames.forEachIndexed { i, name ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL; setPadding(dp(15), dp(14), dp(15), dp(14))
                background = shape(panel, if (selected == name) Color.LTGRAY else line, 1)
            }
            val row = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
            val title = TextView(this).apply { text = name.uppercase(); textSize = 17f; typeface = Typeface.DEFAULT_BOLD; setTextColor(white); layoutParams = LinearLayout.LayoutParams(0, -2, 1f) }
            row.addView(title)
            val status = TextView(this).apply { text = if (selected == name) "SELECTED" else "VIEW"; textSize = 9f; typeface = Typeface.DEFAULT_BOLD; setTextColor(if (selected == name) white else muted); setPadding(dp(9),dp(7),dp(9),dp(7)); background = shape(Color.TRANSPARENT,line,1) }
            row.addView(status)
            card.addView(row)
            val desc = TextView(this).apply { text = osDesc[i]; textSize = 11f; setTextColor(muted); setPadding(0,dp(8),0,dp(10)) }
            card.addView(desc)
            val actions = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
            val choose = button(if (selected == name) "REMOVE SELECTION" else "SELECT OS") {
                if (prefs.getString("selected_os", null) == name) prefs.edit().remove("selected_os").apply()
                else prefs.edit().putString("selected_os", name).apply()
                buildUi()
            }
            actions.addView(choose, LinearLayout.LayoutParams(0, dp(42), 1f))
            val details = button("DETAILS") { showDetails(name, osDesc[i]) }
            val lp = LinearLayout.LayoutParams(dp(105), dp(42)); lp.leftMargin = dp(8); actions.addView(details, lp)
            card.addView(actions)
            val cp = LinearLayout.LayoutParams(-1, -2); cp.bottomMargin = dp(10); content.addView(card, cp)
        }
        addGap(12)
        sectionTitle("GAMING MONITOR", "Android may restrict access to true game FPS and thermal sensors. Unsupported values are shown as unavailable, never invented.")
        val monitor = cardBox()
        monitor.addView(label("FPS     —  Not available to this app yet", muted))
        monitor.addView(label("CPU     —  Restricted / not exposed", muted))
        monitor.addView(label("TEMP  —  Sensor access not guaranteed", muted))
        content.addView(monitor)
        addGap(18)
        sectionTitle("AUTO CLICKER", "Native AccessibilityService tap automation. Enable the service below, then launch the floating controller.")
        val clickBox = cardBox()
        clickBox.addView(label("Up to 10 saved tap positions • Play / Stop control", white))
        clickBox.addView(label("Android may cancel injected taps when you touch or swipe at the same time. Simultaneous multi-touch cannot be guaranteed.", muted))
        val enable = button("OPEN ACCESSIBILITY SETTINGS") { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        clickBox.addView(enable, LinearLayout.LayoutParams(-1, dp(46)))
        val launch = button("START FLOATING CLICKER") {
            if (TapAccessibilityService.instance != null) TapAccessibilityService.instance?.showController()
            else Toast.makeText(this, "First enable VIKKU Auto Clicker in Accessibility settings.", Toast.LENGTH_LONG).show()
        }
        val lp = LinearLayout.LayoutParams(-1, dp(46)); lp.topMargin = dp(8); clickBox.addView(launch, lp)
        content.addView(clickBox)
        addGap(18)
        sectionTitle("SAFETY NOTICE", "Selecting an OS here only saves your choice. Installing a different ROM normally requires device-specific support, bootloader procedures and a reboot; this app will not bypass those requirements.")
        val foot = label("VIKKU OS HUB  •  VERSION 1.0", muted); foot.gravity = Gravity.CENTER; foot.setPadding(0,dp(24),0,0); content.addView(foot)
    }

    private fun showDetails(name: String, desc: String) {
        android.app.AlertDialog.Builder(this)
            .setTitle(name)
            .setMessage("$desc\n\nBefore installation, verify an official build for the exact model and variant. No compatible Vivo Y02 build is confirmed by this app.")
            .setPositiveButton("OK", null).show()
    }

    private fun sectionTitle(title: String, sub: String) {
        text(title, 24, white, true)
        text(sub, 11, muted, false)
        addGap(12)
    }
    private fun cardBox(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL; setPadding(dp(15),dp(14),dp(15),dp(14)); background = shape(panel,line,1)
    }
    private fun label(s: String, c: Int) = TextView(this).apply { text=s; textSize=12f; setTextColor(c); setPadding(0,dp(5),0,dp(5)) }
    private fun text(s: String, size: Int, color: Int, bold: Boolean) {
        val v = TextView(this).apply { text=s; textSize=size.toFloat(); setTextColor(color); if (bold) typeface=Typeface.create("sans-serif-condensed",Typeface.BOLD); setPadding(0,dp(3),0,dp(3)) }
        content.addView(v)
    }
    private fun button(s: String, action: () -> Unit) = TextView(this).apply {
        text=s; gravity=Gravity.CENTER; textSize=10f; typeface=Typeface.DEFAULT_BOLD; setTextColor(if (s=="SELECT OS" || s=="REMOVE SELECTION") Color.BLACK else white)
        background=shape(if (s=="SELECT OS" || s=="REMOVE SELECTION") Color.LTGRAY else Color.TRANSPARENT,line,1)
        setOnClickListener { action() }
    }
    private fun addGap(h: Int) { content.addView(View(this), LinearLayout.LayoutParams(1,dp(h))) }
    private fun shape(color: Int, stroke: Int, sw: Int): GradientDrawable = GradientDrawable().apply { setColor(color); setStroke(dp(sw),stroke); cornerRadius=dp(2).toFloat() }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
}
