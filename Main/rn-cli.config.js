let blacklist = require('react-native/packager/blacklist');
let config = {
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
		]);
	}
};
module.exports = config;