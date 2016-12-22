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
3) Complete installation of "react-native-libmuse" module: https://github.com/Venryx/react-native-libmuse#install  

### Compile and run

1) Start up the virtual Android device. (or connect Android device through usb/wifi)  
2) Compile the TS (TypeScript) files to JS, either by:
2.A) Open CMD in "Main" folder, and run ```node ".\node_modules\typescript\bin\tsc.js" -w -p .```  
2.B) Open the project in Visual Studio Code and run the build task. (ctrl+alt+b)  
3) Open CMD in "Main" folder, and run "react-native run-android".  

### Making changes

* For JS (ui) changes, edit the files in "Main/Source". (I use Visual Studio Code)  
* For Java changes, edit the files in "Main/android/app/src". (I use Android Studio)  