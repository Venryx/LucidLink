//var fs = require("fs");
/*let lstat_old = fs.lstatSync;
fs.lstat = function() {
	//if ([].slice(arguments).find(a=>a.includes("Main\\android\\"))) {
	if ([].slice(arguments).find(a=>a.includes("google-services"))) {
		console.log("Hey2!" + file);
	}
	return lstat_old.apply(this, arguments);
}
let lstatSync_old = fs.lstatSync;
fs.lstatSync = function() {
	//if ([].slice(arguments).find(a=>a.includes("Main\\android\\"))) {
	if ([].slice(arguments).find(a=>a.includes("google-services"))) {
		console.log("Hey3!" + file);
	}
	return lstatSync_old.apply(this, arguments);
}*/
/*let oldFSFuncs = {};
for (let key in fs) {
	let oldFunc = fs[key];
	if (typeof oldFunc == "function") {
		oldFSFuncs[key] = oldFunc;
		fs[key] = function() {
			//if ([].slice(arguments).find(a=>a.includes("Main\\android\\"))) {
			let badPath = [].slice(arguments).find(a=>a.includes("google-services"));
			if (badPath) {
				console.log("Hey4!" + badPath);
			}
			return oldFunc.apply(this, arguments);
		};
		fs[key].length = oldFunc.length;
	}
}*/

// So apparently the packager can cause two types of file-access errors to occur during gradle syncing and building:
// 1) "EPERM: operation not permitted, lstat"
// 2) "java.io.IOException: Could not delete path"
// The first is caused by the built-in watchman node-module. You can solve it by installing the watchman executable, as described in Troubleshooting.md.
// The second is caused by the packager module. It is solved by the code below, which blacklists the "android" folders from packager processing.

var path = require("path");
let blacklist = require("metro-bundler/src/blacklist");
let config = {
	getProjectRoots() {
		return [path.resolve()];
		// add "<project folder>/Build" as a root (so it can find "Build/index.android.js" consistently...)
		//return [path.resolve(), path.resolve("Build")];
		//return [path.resolve().replace(/\\/g, "/"), path.resolve("Build").replace(/\\/g, "/")];
		//return [path.resolve("C:/Root/Apps/@V/LucidLink/Main"), path.resolve("C:/Root/Apps/@V/LucidLink/Main/Build")]
		//return [path.resolve("node_modules"), path.resolve("Build")] // this was code when second-fix apparently worked
		//return [path.resolve("C:/Root/Apps/@V/LucidLink/Main/node_modules"), path.resolve("C:/Root/Apps/@V/LucidLink/Main/Build")]
		//return [path.resolve("../../../node_modules"), path.resolve("../../../Build")]
	},
	getBlacklistRE() {
		console.log("Getting blacklist... @path:" + path.resolve());
		return blacklist([
			// Ignore IntelliJ directories
			// /.*\.idea\/.*#/,
			// ignore git directories
			// /.*\.git\/.*#/,
			// Ignore android directories
			// /.*\/app\/build\/.*#/,
			// /.*\/app\/build\/.*#/,
			/.*\/android\/.*/,

			// /.*\/node_modules\/.*#/, // apparently, this is needed
			// /.*\/build\/.*#/,

			// /react-native-chart\/dist.*#/,
		]);
	},
};
module.exports = config;