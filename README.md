# LucidLink
Monitors Muse headband EEG data, and triggers events (eg audio) when REM sleep is detected.

### TODO
Task list (trello board): https://trello.com/b/2ZYLJ2l1/lucid-link

### Prepare

1) Ensure prerequisite programs are installed: Git (+TortoiseGit), NodeJS, Android SDK, VirtualBox, Genymotion  
2) Ensure prerequisite Android SDK packages are installed: [...]  
3) Ensure prerequisite npm modules are installed: react-native-cli  
4) Ensure a virtual Android device is set up in Genymotion (which uses VirtualBox), with Android 5.0 or later.  

### Install and compile (and run in Android emulator)

1) Clone this repo to your computer using git. (I use the TortoiseGit ui)  
2) Open CMD in "Main" folder, and run "npm install".  
3) Start up the virtual Android device.  
4) Open CMD in "Main" folder, and run "react-native run-android".  

### Modify and compile (and see changes in Android emulator)
1) Open project in editor  
. . . A) For changes to the JS (ui), open files in "Main/Source". (I use the Atom text editor)  
. . . B) For changes to the Java, open files in "Main/android/app/src". (I use Android Studio)  
2) Make changes.  
3) Ensure the virtual Android device is running.  
4) Open CMD in "Main" folder, and run "react-native run-android". This will push the updated app to the Android emulator.  

### External folders

To have the react-native-libmuse and/or MPAndroidChart projects in external folders (to remove the need for copying/syncing), you have to:  
1) Create a UserConfig.json file in the Main folder, with this text:
```
{
	reactNativeLibMuse_external: true,
	mpChartLib_external: true,
}
```
2) If you want changes to those external folder's js files to reflect in your app, you have to either copy the contents each time into the node_modules folder, or do a directory-level "hard-link clone" from the external folder to that node_modules location. Whenever you add a new JS file that needs tracking, you also need to redo this directory-level "hard-link clone". (but at least not after every file change)

For knowing what I mean by that phrase (and for doing the deed itself pretty easily on Windows), see here: http://schinagl.priv.at/nt/hardlinkshellext/linkshellextension.html#hardlinkclones