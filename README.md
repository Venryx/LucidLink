# LucidLink
Monitors Muse headband EEG data, and triggers events (eg audio) when REM sleep is detected.

### TODO
Task list (trello board): https://trello.com/b/2ZYLJ2l1/lucid-link

### How to compile (and run in Android emulator)
1) Ensure prerequisite programs are installed: Git (+TortoiseGit), NodeJS, Android SDK, VirtualBox, Genymotion  
2) Ensure prerequisite Android SDK packages are installed: [...]  
3) Ensure prerequisite npm modules are installed: react-native-cli  
4) Ensure a virtual Android device is set up in Genymotion (which uses VirtualBox), with Android 5.0 or later.  
5) Clone this repo to your computer using git. (I use the TortoiseGit ui)  
6) Open CMD in "Main" folder, and run "npm install".  
7) Start up the virtual Android device.  
8) Open CMD in "Main" folder, and run "react-native run-android".  

### How to modify (and see changes in Android emulator)
1) Open project in editor  
. . . A) For changes to the JS (ui), open files in "Main/Source". (I use the Atom text editor)  
. . . B) For changes to the Java, open files in "Main/android/app/src". (I use Android Studio)  
2) Make changes.  
3) Ensure the virtual Android device is running.  
4) Open CMD in "Main" folder, and run "react-native run-android". This will push the updated app to the Android emulator.  