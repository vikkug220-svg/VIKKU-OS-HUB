VIKKU OS HUB — Native Android project (VSX-inspired monochrome design)

यह HTML वेबसाइट नहीं है। यह Android Studio/Gradle project है, जिससे APK build होगा।

अभी इस build में:
- Native Android UI, VSX reference से inspired: black background, white/grey text, condensed bold headings, sharp rectangular cards.
- 8 Custom OS options; एक समय में एक selection; दूसरी चुनने पर पुरानी replace; selection remove.
- OS details dialog और safety notice.
- Native AccessibilityService आधारित Auto Clicker: floating panel, drag करके tap point save, अधिकतम 10 points, Play/Stop/Clear.
- Gaming Monitor में metrics को unavailable बताया गया है; fake FPS/CPU/temperature नहीं दिखाए जाते.

महत्वपूर्ण सीमाएँ:
1. OS चुनना केवल app के अंदर selection है। इससे phone का ROM/OS install या switch नहीं होता।
2. Vivo Y02 पर compatible custom ROM build की पुष्टि इस project ने नहीं की है।
3. Auto Clicker Android Accessibility gesture injection से tap करता है। Android/game के अनुसार manual swipe के साथ simultaneous touch cancel/interfere हो सकता है; हर फोन पर guarantee नहीं।
4. True game FPS/CPU/temperature सामान्य third-party app को हर device पर नहीं मिलते। इस starter build में ये live sensor readings implement नहीं हैं.
5. APK इस ZIP में prebuilt नहीं है। GitHub Actions से debug APK बनाना है.

फोन से APK बनाने के steps:
A. GitHub account में sign in करो.
B. GitHub पर New repository बनाओ, नाम VIKKU-OS-HUB रखो, Public या Private चुनो.
C. इस ZIP को extract करो. अंदर की सारी files/folders (settings.gradle, build.gradle, app, .github आदि) repository के root में upload करो. ZIP को सिर्फ एक folder के अंदर nested मत रखना.
D. Commit changes करो. ऊपर Actions tab खोलो. "Build VIKKU OS HUB APK" workflow चुनकर Run workflow दबाओ. Push पर भी build शुरू होगा.
E. Build पूरा होने पर workflow run खोलो. नीचे Artifacts में VIKKU-OS-HUB-debug डाउनलोड करो. ZIP extract करके app-debug.apk install करो.
F. Android अगर रोकता है तो Settings में उस browser/file manager के लिए "Install unknown apps" अनुमति दो. APK install के बाद यह permission वापस बंद कर सकते हो.

Auto Clicker चालू करना:
1. App खोलो → OPEN ACCESSIBILITY SETTINGS.
2. Downloaded services / Installed services में VIKKU Auto Clicker खोलो और ON करो.
3. वापस app में आकर START FLOATING CLICKER दबाओ.
4. ＋ ADD TAP POINT दबाओ. स्क्रीन पर marker को game के button पर drag करो और marker पर tap करके point save करो.
5. अधिकतम 10 points जोड़ो; PLAY/STOP से control करो.
6. CLEAR POINTS सभी saved positions हटाता है.

यदि GitHub mobile UI में file upload कठिन हो, तो GitHub app/browser में repository खोलकर Add file → Upload files इस्तेमाल करो.
