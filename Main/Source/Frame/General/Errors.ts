import {Assert} from "./Assert";
//import StackTrace from "node_modules_CustomInternal/stacktrace-js";
//import {fetch} from "react-native";
var {fetch} = require("react-native");

/*g.bundleInfos = [];
export function GetBundleInfo(bundleName) {
    if (g.bundleInfos[bundleName] == null) {
        var info = {};
		info.moduleFileStartLines = g["ModuleFileStartLines_" + bundleName];
        info.moduleFileStartLines_props_sortedByStartLine = info.moduleFileStartLines.Props.OrderBy(a=>a.value);
        g.bundleInfos[bundleName] = info;
    }
    return g.bundleInfos[bundleName];
}*/

// loaded asynchonously
export function LoadBundleText() {
	GetBundleInfo("Main").LoadLateData();
	GetBundleInfo("Libraries").LoadLateData();
}

var bundleInfos = {} as {[s: string]: BundleInfo};
class BundleInfo {
	constructor(name) {
		this.name = name;
		this.LoadEarlyData();
		//this.LoadLateData();
	}

	name = null as string;
	moduleStartInfos: ModuleInfo[] = [];
	LoadEarlyData() {
		var bundle_modStartLines = window["ModuleFileStartLines_" + this.name];
		for (let {index, name: modulePath, value: startLine} of bundle_modStartLines.Props) {
			let nextPair = bundle_modStartLines.Props[index + 1];
			/*let stopLine = nextPair ? nextPair.value : this.lines.length;
			this.moduleStartInfos.push(new ModuleInfo(modulePath, startLine, stopLine));*/
			this.moduleStartInfos.push(new ModuleInfo(modulePath, startLine));
		}
	}
	FindModuleForBundleLine(bundleLine) {
		var minIndex = 0;
		var maxIndex = this.moduleStartInfos.length - 1;
		while (minIndex <= maxIndex) {
			let currentIndex = (minIndex + maxIndex) / 2 | 0;
			let currentEntry = this.moduleStartInfos[currentIndex];
			let nextEntry = this.moduleStartInfos[currentIndex + 1];
			
			// if target-line is earlier
			if (bundleLine < currentEntry.startLine) {
				maxIndex = currentIndex - 1;
			// if target-line is later
			} else if (nextEntry && bundleLine >= nextEntry.startLine) {
				minIndex = currentIndex + 1;
			// if target-line is within this module
			} else {
				return currentEntry;
			}
		}
	}

	// loaded async
	text = null as string;
	lines = null as string[];
	get LateDataLoaded() { return this.text != null; }
	async LoadLateData() {
		// "text" is needed, so jquery doesn't try to run it!
		//var text = await $.get({url: `http://192.168.0.100:8081/Build/Source/index.android.bundle`, dataType: "text"});
		let response = await fetch("https://mywebsite.com/mydata.json");
		let text = await response
		this.text = text;
		this.lines = text.split("\n");
		//this.CalculateData();
	}
}
class ModuleInfo {
	constructor(path, startLine) {
		this.Extend({path, startLine});
		this.fileName = path.substr(path.lastIndexOf("/") + 1);
		//this.name = this.fileName.substr(0, path.lastIndexOf("."));
	}
	path = null as string;
	fileName = null as string;
	//name = null as string;
	startLine = -1;
	//stopLine = -1;
}
function GetBundleInfo(bundleName) {
	if (bundleInfos[bundleName] === undefined) {
		var bundleInfo = new BundleInfo(bundleName);
		bundleInfos[bundleName] = bundleInfo;
	}
	return bundleInfos[bundleName];
}

export function GetSourceStackEntryInfo(bundleName, bundleLine): any {
	// fix for that sometimes instead of having Bundle file-path, it has blank, yielding blank bundleName
	if (bundleName == "")
		bundleName = "Main";

    var bundleInfo = GetBundleInfo(bundleName);
	if (bundleInfo == null)
		return {modulePath: `[Can't find bundle "${bundleName}"]`, moduleFileName: `[Can't find bundle "${bundleName}"]`};
	/*if (bundleInfo.text == null) {
		return {modulePath: `[Info loading for "${bundleName}"...]`, moduleFileName: `[Info loading for "${bundleName}"...]`};
	}*/

	var module = bundleInfo.FindModuleForBundleLine(bundleLine);
	if (module == null) {
		return {modulePath: `[Can't find module for bundle ${bundleName}, line: ${bundleLine}]`,
			moduleFileName: `[Can't find module for bundle ${bundleName}, line: ${bundleLine}]`};
	}

	var {path: modulePath, fileName: moduleFileName, startLine: moduleLine} = module;

	/*var moduleLine = bundleLine - moduleStartLine;
	moduleLine++; // off by one, for some reason
	//moduleLine += 8; // module-header offset (different per project)*/

    var moduleLine = -1;
	if (bundleInfo.LateDataLoaded) {
		//console.log("BundleText>length: " + (bundleText && bundleText.length));
	    for (var line = bundleLine; line < bundleLine + 100 && line < bundleInfo.lines.length; line++) {
	        var bundleLineText = bundleInfo.lines[line - 1]; // make line 0-based when getting from 0-based array
			if (bundleLineText[bundleLineText.length - 1] == ")" && bundleLineText.contains(" //@L(")) {
			    moduleLine = parseInt(bundleLineText.match(/ \/\/@L\((.+)\)/)[1]);
			    break;
			}
	    }
	}

	return {modulePath, moduleFileName, moduleLine};
}
// gets the source stack-trace of the error (i.e. the stack-trace as it would be without the js-files being bundled into one)
Object.defineProperty(Error.prototype, "Stack", {enumerable: false, get: function() {
	var rawStack = this.stack;
	var oldLines = rawStack.split("\n");
	var newLines = oldLines.map(oldLine=> {
		let lineParts = oldLine.match(/^(.+?)\(at (.*?):([0-9]+)(?::([0-9]+))?\)$/);
		if (lineParts == null) return oldLine;

		let [, beforeText, bundlePath, bundleLine, bundleColumn] = lineParts;
		let bundleFileName = bundlePath.substring(bundlePath.lastIndexOf("/") + 1);
		let bundleName = bundleFileName.substring(0, bundleFileName.lastIndexOf("."));

		let {modulePath, moduleFileName, moduleLine} = GetSourceStackEntryInfo(bundleName, bundleLine);

		/*let bundlePathOrName = inUnity ? bundleFileName : encodeURI(bundlePath);
		let modulePathOrName = inUnity ? moduleFileName : "file:///" + encodeURI(modulePath);*/
		return `${beforeText
			}(${bundleFileName}:${bundleLine}${bundleColumn ? ":" + bundleColumn : ""}) (${moduleFileName}:${moduleLine})`;
	});
	return newLines.join("\n");
}});

/*export async function LogSourceStackTraceFrom(error, asError = false) {
    var stackFrames = await StackTrace.fromError(error);
    (asError ? LogError : LogWarning)("SourceJSStack) " + stackFrames.join("\n"));
};
g.Extend({LogSourceStackTraceFrom});*/

// helper for when testing error source-maps and such
function ThrowError(message) { throw new Error(message || "Fake error for testing."); };
window.Extend({ThrowError});