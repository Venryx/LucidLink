# LucidLink
Features:
* Dream journal
* Random-voice-prompt engine
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

### Compile and run

1) Start up the virtual Android device. (or connect Android device through usb/wifi)  
2) Compile the TS (TypeScript) files to JS, either by:  
2.A) Run ```npm run dev``` in main folder. (recommended)  
2.B) Open the project in Visual Studio Code and run the build task. (ctrl+shift+b)  
3) Build the Android project, and start the JS packager, either by:
3.A) Run "react-native start" in main folder, then press "Launch" in Android Studio. (recommended)  
3.B) Run "react-native run-android" in main folder.  

### Making changes

* For JS (ui) changes, edit the files in "Main/Source". (I use Visual Studio Code)  
* For Java changes, edit the files in "Main/android/app/src". (I use Android Studio)  

### Troubleshooting

For common troubleshooting solutions (for developers), see Troubleshooting.md.