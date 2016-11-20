import React, {Component} from "react";
import {NativeModules} from "react-native";
import RNFS from "react-native-fs";

var g = global;
g.g = g;

g.Log = function(...args) {
	console.log(...args);
};
g.Trace = function(...args) {
	console.trace(...args);
};

g.Assert = function(condition, message) {
	if (condition) return;
	throw new Error(message);
	console.error(message);
};
g.AssertWarn = function(condition, message) {
	if (condition) return;
	console.warn(message);
};

g.JavaBridge = class JavaBridge {
    static get Main() {
        return NativeModules.Main;
    }
}

// general
// ==========

import {AppRegistry, StyleSheet} from "react-native";

g.E = function(...objExtends) {
    var result = {};
    for (var extend of objExtends)
        result.Extend(extend);
	return result;
	//return StyleSheet.create(result);
}

// timer stuff
// ==========

function WaitXThenRun(waitTime, func) { setTimeout(func, waitTime); }
function Sleep(ms) {
	var startTime = new Date().getTime();
	while (new Date().getTime() - startTime < ms)
	{}
}
function WaitXThenRun_Multiple(waitTime, func, count = -1) {
	var countDone = 0;
	var timerID = setInterval(function() {
		func();
		countDone++;
		if (count != -1 && countDone >= count)
			clearInterval(timerID);
	}, waitTime);

	var controller = {};
	controller.Stop = function() { clearInterval(timerID); }
	return controller;
}

// interval is in seconds (can be decimal)
class Timer {
	constructor(interval, func, maxCallCount = -1) {
	    this.interval = interval;
	    this.func = func;
	    this.maxCallCount = maxCallCount;
	}

	timerID = -1;
	get IsRunning() { return this.timerID != -1; }

	callCount = 0;
	Start() {
		this.timerID = setInterval(()=> {
			this.func();
			this.callCount++;
			if (this.maxCallCount != -1 && this.callCount >= this.maxCallCount)
				this.Stop();
		}, this.interval * 1000);
	}
	Stop() {
		clearInterval(this.timerID);
		this.timerID = -1;
	}
}
class TimerMS extends Timer {
    constructor(interval_decimal, func, maxCallCount = -1) {
        super(interval_decimal / 1000, func, maxCallCount);
    }
}

var funcLastScheduledRunTimes = {};
g.BufferFuncToBeRun = function(key, minInterval, func) {
    var lastScheduledRunTime = funcLastScheduledRunTimes[key] || 0;
    var now = new Date().getTime();
    var timeSinceLast = now - lastScheduledRunTime;
    if (timeSinceLast >= minInterval) { // if we've waited enough since last run, run right now
        func();
        funcLastScheduledRunTimes[key] = now;
    } else if (timeSinceLast > 0) { // else, if we don't yet have a future-run scheduled, schedule one now
        var intervalEndTime = lastScheduledRunTime + minInterval;
        var timeTillIntervalEnd = intervalEndTime - now;
        WaitXThenRun(timeTillIntervalEnd, func);
		funcLastScheduledRunTimes[key] = intervalEndTime;
    }
}

// file stuff
// ==========

g.VFile = class VFile {
	static get MainBundlePath() { return RNFS.MainBundlePath; }
	static get CachesDirectoryPath() { return RNFS.CachesDirectoryPath; }
	static get DocumentDirectoryPath() { return RNFS.DocumentDirectoryPath; }
	static get ExternalDirectoryPath() { return RNFS.ExternalDirectoryPath; }
	static get ExternalStorageDirectoryPath() { return RNFS.ExternalStorageDirectoryPath; }
	static get TemporaryDirectoryPath() { return RNFS.TemporaryDirectoryPath; }
	static get LibraryDirectoryPath() { return RNFS.LibraryDirectoryPath; }
	static get PicturesDirectoryPath() { return RNFS.PicturesDirectoryPath; }

	static CreateFolderAsync(folderPath) {
		//return RNFS.mkdir(filePath);
		return new Promise((resolve, reject)=> {
			RNFS.mkdir(folderPath).then(()=> {
				resolve();
			})
			.catch(error=> {
				//console.log(error.message, error.code);
				//throw error;
				reject(error);
			});
		});
	}
	static WriteAllTextAsync(filePath, text, encoding = "utf8") {
		//return RNFS.writeFile(filePath, text);
		return new Promise((resolve, reject)=> {
			RNFS.writeFile(filePath, text, encoding).then(()=> {
				resolve();
			})
			.catch(error=> {
				reject(error);
			});
		});
	}

	static ReadAllTextAsync(filePath, defaultText = undefined, encoding = "utf8") {
		//return RNFS.readFile(filePath);

		return new Promise((resolve, reject)=> {
			RNFS.readFile(filePath, encoding).then(text=> {
				resolve(text);
			})
			.catch(error=> {
				if (error.toString().contains("ENOENT: no such file or directory, open ") && defaultText !== undefined)
					return resolve(defaultText);
				reject(error);
			});
		});
	}
	/*static ReadAllText(filePath) {
		return await VFile.ReadAllTextAsync(filePath);
	}*/
}