### Getting "cannot delete" and such errors while building

Problem: "cannot delete", "EPERM", etc. errors while building.

Solution: Install watchman, and make sure it's accessible globally. (ie. add it to the PATH)

### Packager finding missing paths

Running "react-native start --reset-cache" (or "npm start -- --reset-cache") can help, if you've changed the project structure. (or something causing duplicate and outdated paths)

### "Directory doesn't exist" for a file

Problem:
```
Loading dependency graph, done.
Bundling `Build\Source\index.android.js`
  Analysing...(node:22312) UnhandledPromiseRejectionWarning: Unhandled promise rejection (rejection id: 7): UnableToResolveError: Unable to resolve module `./EarlyRun` from `C:\Root\Apps\@V\LucidLink\Main\Build\Source\index.android.js`: Directory C:\Root\Apps\@V\LucidLink\Main\Build\Source\EarlyRun doesn't exist
```

Solution: Run "react-native start --reset-cache", then run "react-native run-android".