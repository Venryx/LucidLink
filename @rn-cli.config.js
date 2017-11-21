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

var path = require("path");
//let blacklist = require('react-native/packager/blacklist');
let config = {
	getProjectRoots() {
		// add "<project folder>/Build" as a root (so it can find "Build/index.android.js" consistently...)
		//return [path.resolve(), path.resolve("Build")];
		//return [path.resolve().replace(/\\/g, "/"), path.resolve("Build").replace(/\\/g, "/")];
		//return [path.resolve("C:/Root/Apps/@V/LucidLink/Main"), path.resolve("C:/Root/Apps/@V/LucidLink/Main/Build")]
		//return [path.resolve("node_modules"), path.resolve("Build")] // this was code when second-fix apparently worked
		//return [path.resolve("C:/Root/Apps/@V/LucidLink/Main/node_modules"), path.resolve("C:/Root/Apps/@V/LucidLink/Main/Build")]
		return [path.resolve("../../../node_modules"), path.resolve("../../../Build")]
	},
	/*getBlacklistRE() {
		console.log("Getting blacklist... @path:" + path.resolve());
		return blacklist([
			// Ignore IntelliJ directories
			// /.*\.idea\/.*#/,
			// ignore git directories
			// /.*\.git\/.*#/,
			// Ignore android directories
			// /.*\/app\/build\/.*#/,
			// /.*\/app\/build\/.*#/,
			/.*\/android\/.*#/,

			// /.*\/node_modules\/.*#/, // apparently, this is needed
			// /.*\/build\/.*#/,

			// /react-native-chart\/dist.*#/,
		]);
	},*/
};
module.exports = config;