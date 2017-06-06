//const defaultConfig = require('react-native/local-cli/default.config.js');
var path = require("path");
let blacklist = require('react-native/packager/blacklist');
let config = {
	/*getProjectRoots() {
		/*const roots = defaultConfig.getProjectRoots();
		roots.unshift(path.resolve(__dirname, '../../assets'));
		roots.unshift(path.resolve(__dirname, '../../core'));
		return roots;*#/
		// change js-root from "<project folder>" to "<project folder>/Source"
		//return [defaultConfig.getProjectRoots()[0] + "/Source"];
		return [path.resolve(), path.resolve("Source")];
		//return [path.resolve().replace(/\\/g, "/"), path.resolve("Source").replace(/\\/g, "/")];
},*/
	getBlacklistRE() {
		console.log("Getting blacklist...");
		return blacklist([
			// Ignore IntelliJ directories
			// /.*\.idea\/.*/,
			// ignore git directories
			// /.*\.git\/.*/,
			// Ignore android directories
			// /.*\/app\/build\/.*/,
			/.*\/app\/build\/.*/,
			// /.*build.*/,

			/react-native-chart\/dist.*/,
		]);
	},
};
module.exports = config;