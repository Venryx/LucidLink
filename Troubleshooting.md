### Getting "cannot delete" and such errors while building

Problem: "cannot delete", "EPERM", etc. errors while building.

Solution: Install watchman, and make sure it's accessible globally. (ie. add it to the PATH)

### Packager finding missing paths

Running "react-native start --resetCache" can help, if you've changed the project structure. (or something causing duplicate and outdated paths)