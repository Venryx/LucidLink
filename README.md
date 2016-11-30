# LucidLink
Monitors Muse headband EEG data, and triggers events (eg audio) when REM sleep is detected.

Task list can be found here: https://trello.com/b/2ZYLJ2l1/lucid-link

### Prepare

1) Ensure prerequisite programs are installed: Git (+TortoiseGit), NodeJS, Android SDK, VirtualBox, Genymotion  
2) Ensure prerequisite Android SDK packages are installed: [...]  
3) Ensure prerequisite npm modules are installed: react-native-cli  
4) Ensure a virtual Android 5+ device is set up in Genymotion (which uses VirtualBox). (or have an Android 5+ device)  

### Install

1) Clone this repo to your computer using git. (I use the TortoiseGit ui)  
2) Open CMD in "Main" folder, and run "npm install".  

### Compile and run

1) Start up the virtual Android device. (or connect Android device through usb/wifi)  
2) Open CMD in "Main" folder, and run "react-native run-android".  

### Making changes

* For JS (ui) changes, edit the files in "Main/Source". (I use Visual Studio Code)  
* For Java changes, edit the files in "Main/android/app/src". (I use Android Studio)  

### External folders

To have the react-native-libmuse and/or MPAndroidChart projects in external folders (to remove the need for copying/syncing after changes to them), you have to:  
1) Clone those repos into folders "../../react-native-libmuse" and "../../MPAndroidChart". (relative to this project's root folder)  
2) Create a UserConfig.json file in the Main folder, with this text:
```
{
	reactNativeLibMuse_external: true,
	mpChartLib_external: true,
}
```
3) If you want changes to those external folder's js files to reflect in your app, you have to either copy the contents each time into the node_modules folder, or do a directory-level "hard-link clone" from the external folder to that node_modules location. Whenever you add a new JS file that needs tracking, you also need to redo this directory-level "hard-link clone". (but at least not after every file change)

For knowing what I mean by that phrase (and for doing the deed itself pretty easily on Windows), see here: http://schinagl.priv.at/nt/hardlinkshellext/linkshellextension.html#hardlinkclones