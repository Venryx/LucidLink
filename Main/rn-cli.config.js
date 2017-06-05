let blacklist = require('react-native/packager/blacklist');
let config = {
    getBlacklistRE() {
		console.log("Getting blacklist...");
        return blacklist([
            // Ignore local `.sample.js` files.
            /.*\.sample\.js$/,

            // Ignore IntelliJ directories
            /.*\.idea\/.*/,
            // ignore git directories
            /.*\.git\/.*/,
            // Ignore android directories
            /.*\/app\/build\/.*/,

            // Add more regexes here for paths which should be blacklisted from the packager.
        ]);
    }
};
module.exports = config;