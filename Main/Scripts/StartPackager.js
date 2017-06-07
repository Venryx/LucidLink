var path = require("path");
var child_process = require('child_process');
const packagerDir = path.resolve("node_modules/react-native/packager");
const launchPackagerScript = path.resolve(packagerDir, "launchPackager.bat");

const procConfig = {cwd: packagerDir, detached: true, stdio: 'ignore'};
child_process.spawn('cmd.exe', ['/C', 'start', launchPackagerScript], procConfig);