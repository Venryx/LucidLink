# LucidLink
Features:
* Dream journal
* Random-voice-prompt engine
* Muse eeg monitor
* User scripts

Download page: https://play.google.com/store/apps/details?id=v.lucidlink  
Task list: https://trello.com/b/2ZYLJ2l1/lucid-link

### Prepare

1) Ensure prerequisite programs are installed: Git (+TortoiseGit), NodeJS, Android SDK, VirtualBox, Genymotion  
2) Ensure prerequisite Android SDK packages are installed: [...]  
3) Ensure prerequisite npm modules are installed: react-native-cli  
4) Ensure a virtual Android 5+ device is set up in Genymotion (which uses VirtualBox). (or have an Android 5+ device)  

### Install

1) Clone this repo to your computer using git. (I use TortoiseGit)  
2) Open console in "Main" folder, and run "npm install".  
3) Fix metro-bundler file (temp; till I update react-native): https://stackoverflow.com/a/58798648/2441655
4) Make sym-link from "./node-modules/react-native-libmuse" to "Source/react-native-libmuse". (temp)
5) Replace "./node_modules/rn-spinner/index.js" with (temp): https://github.com/RNComponents/rn-spinner/blob/f6b817071b22ea491acfe144ea191470ac1e9172/index.js

### Compile and run

1) Start up the virtual Android device. (or connect Android device through usb/wifi)  
2) Compile the TS (TypeScript) files to JS, either by:  
2.A) Open the project in Visual Studio Code and run the build task. (ctrl+shift+b)  
2.B) Run ```npm run dev``` in main folder.  
3) Build the Android project, and start the JS packager, either by:
3.A) Run "react-native run-android" in main folder.  
3.B) Run "react-native start" in main folder, then press "Launch" in Android Studio. (preferred if Android Studio is open, to avoid file-usage clashes)  
4) On first launch, the js won't load, because the server address+port aren't specified. Set these through the react-native options panel, and reload.

### Making changes

* For JS (ui) changes, edit the files in "Main/Source". (I use Visual Studio Code)  
* For Java changes, edit the files in "Main/android/app/src". (I use Android Studio)  

### Troubleshooting

For common troubleshooting solutions (for developers), see Troubleshooting.md.