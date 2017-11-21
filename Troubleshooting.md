### Getting "cannot delete" and such errors while building

Problem: "cannot delete", "EPERM", etc. errors while building.

Solution: Install watchman (the standalone executable from https://facebook.github.io/watchman/docs/install.html, not the npm module), and make sure it's accessible globally. (ie. add it to the PATH)

~~You might still get some various build issues; just keep rebuilding (with the watchman process absent/killed), and it should work it's way through them, and not come up again during incremental builds.~~

Also, try restarting vscode and/or Android Studio. Once, I had issues, but after restarting vscode I had none for 10+ builds. (it seems to have some odd mechanism/pattern)

If you still have issues with deletions failing (on Windows), you could also try:
1) Create a second user account.
2) Give it access to everything your main account can access, except deny the project's "android" folder -- and the "node_modules/.../android" folders, if you want more robustness.
3) Modify the package.json "start2" script with your correct alt-account username, and path to react-native.
4) Run "npm run start2" instead of "react-native start".

### Packager finding missing paths

Running "react-native start --resetCache" can help, if you've changed the project structure. (or something causing duplicate and outdated paths)