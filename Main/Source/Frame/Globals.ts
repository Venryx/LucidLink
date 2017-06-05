import {VDF} from "../Packages/VDF/VDF";
import {VDFDeserialize, VDFSerialize} from "../Packages/VDF/VDFTypeInfo";
import {VDFLoader, VDFLoadOptions} from "../Packages/VDF/VDFLoader";
import {VDFNode} from "../Packages/VDF/VDFNode";

//import {LogEntry, More} from '../LucidLink/More';
import V from '../Packages/V/V';

import {AppRegistry, NativeModules, StyleSheet, DeviceEventEmitter} from "react-native";
//import {observable as O, autorun} from "mobx";
import {observable as O} from "mobx";
//import Moment from "moment";
import {VDFSaveOptions, VDFSaver, VDFTypeMarking} from "../Packages/VDF/VDFSaver";
import {IsString} from "./Types";
var Moment = require("moment");

var globalComps = {O};
//(globalComps as any).Moment = Moment;
globalComps.Extend({Moment});
//g.Extend(globalComps);
for (let key in globalComps)
	g[key] = globalComps[key];

/*g.onerror = function(message, filePath, line, column, error) {
	LogError(`JS) ${message} (at ${filePath}:${line}:${column})
Stack) ${error.stack}`);
};*/
var ErrorUtils = require("ErrorUtils");
ErrorUtils._globalHandler_orig = ErrorUtils.getGlobalHandler() || ErrorUtils._globalHandler;
ErrorUtils.setGlobalHandler((error, isFatal)=> {
	HandleError(error, isFatal);
	ErrorUtils._globalHandler_orig(error, isFatal);
});
export function HandleError(error, isFatal = false) {
	Log(`${error}
Stack) ${error.stack}
Fatal) ${isFatal}`);
}

export function DoNothing(...args) {}
export function DN(...args) {}

export function Log(...args) {
	var type = "general", message, sendToJava = true;
	if (args.length == 1) [message] = args;
	else if (args.length == 2) [type, message] = args;
	else if (args.length == 3) [type, message, sendToJava] = args;

	//console.log(...args);
	console.log.apply(this, args);

	try {
		var {LogEntry, More} = require("../LucidLink/More");
		More.AddLogEntry(new LogEntry(type, message, new Date()));
	} catch (ex) {}
	if (sendToJava) {
		//try {
			LogJava(type, message);
		//} catch (ex) {}
	}
}
export function LogJava(...args) {
	var type = "general", message;
	if (args.length == 1) [message] = args;
	else if (args.length == 2) [type, message] = args;
	JavaBridge.Main.PostJSLog(type, message);
}
export function Trace(...args) {
	console.trace.apply(this, args);
}

export function Toast(text, duration = 0) {
	if (!IsString(text))
		text = text != null ? text.toString() : "";
	JavaBridge.Main.ShowToast(text, duration);
}
g.Extend({Toast});

enum NotifyLength { Short, Long, Persistent }
export function Notify(text: string, length = NotifyLength.Persistent) {
	if (!IsString(text))
		text = text != null ? (text as any).toString() : "";
	let lengthStr = NotifyLength[length];
	JavaBridge.Main.Notify(text, lengthStr);
}

export class JavaBridge {
    static get Main() {
        return NativeModules.LucidLink;
    }
}

// type system
// ==========

/*function IsType(obj: any) {
	return obj instanceof Function;
}
// probably temp
/*function SimplifyTypeName(typeName) {
	var result = typeName;
	if (result.startsWith("List("))
		result = "IList";
	return result;
}*#/
export function SimplifyType(type) {
	var typeName = IsType(type) ? type.name : type;
    if (typeName.startsWith("List("))
        return Type("IList");
    if (typeName.startsWith("Dictionary("))
        return Type("IDictionary");
    return type;
}
export function UnsimplifyType(type) {
    var typeName = IsType(type) ? type.name : type;
	if (typeName == "IList")
        return Type("List");
    if (typeName == "IDictionary")
        return Type("Dictionary");
    return type;
}*/

// polyfills for constants
// ==========

if (Number.MIN_SAFE_INTEGER == null)
	(Number as any).MIN_SAFE_INTEGER = -9007199254740991;
if (Number.MAX_SAFE_INTEGER == null)
	(Number as any).MAX_SAFE_INTEGER = 9007199254740991;

//g.Break = function() { debugger; };
export function Debugger() { debugger; }
export function Debugger_True() { debugger; return true; }
export function Debugger_If(condition) {
    if (condition)
        debugger;
}

// general
// ==========

g.G = G; declare global { function G(...globalHolders); }
function G(...globalHolders) {
	for (let globalHolder of globalHolders) {
		g.Extend(globalHolder);
	}
}

G({E}); // for react-native-chart modifications...
export function E(...objExtends: any[]) {
    var result = {};
    for (var extend of objExtends)
        result.Extend(extend);
	return result;
	//return StyleSheet.create(result);
}

// methods: serialization
// ==========

// object-Json
export function FromJSON(json) { return JSON.parse(json); }
g.Extend({FromJSON});
export function ToJSON(obj, ...excludePropNames) {
	try {
		if (arguments.length > 1) {
			return JSON.stringify(obj, function(key, value) {
				if (excludePropNames.Contains(key))
					return;
				return value;
			});
		}
		return JSON.stringify(obj);
	}
	catch (ex) {
		if (ex.toString() == "TypeError: Converting circular structure to JSON")
			return ToJSON_Safe.apply(this, arguments);
		throw ex;
	}
}
g.Extend({ToJSON});
export function ToJSON_Safe(obj, excludePropNames___) {
	var excludePropNames = V.Slice(arguments, 1);

	var cache = [];
	var foundDuplicates = false;
	var result = JSON.stringify(obj, function(key, value) {
		if (excludePropNames.Contains(key))
			return;
		if (typeof value == 'object' && value !== null) {
			// if circular reference found, discard key
			if (cache.indexOf(value) !== -1) {
				foundDuplicates = true;
				return;
			}
			cache.push(value); // store value in our cache
		}
		return value;
	});
	//cache = null; // enable garbage collection
	if (foundDuplicates)
		result = "[was circular]" + result;
	return result;
}
g.Extend({ToJSON_Safe});

export function ToJSON_Try(...args) {
	try {
		return ToJSON.apply(this, args);
	} catch (ex) {}
	return "[converting to JSON failed]";
}
g.Extend({ToJSON_Try});

// object-VDF
// ----------

//Function.prototype._AddGetter_Inline = function VDFSerialize() { return function() { return VDF.CancelSerialize; }; };
(Function.prototype as any).Serialize = (function() { return VDF.CancelSerialize; } as any).AddTags(new VDFSerialize());

export function FinalizeFromVDFOptions(options = null) {
    options = options || new VDFLoadOptions();
	options.loadUnknownTypesAsBasicTypes = true;
	return options;
}
export function FromVDF(vdf: string, options?: VDFLoadOptions);
export function FromVDF(vdf: string, declaredTypeName: string, options?: VDFLoadOptions);
export function FromVDF(vdf, declaredTypeName_orOptions?, options?) {
	if (declaredTypeName_orOptions instanceof VDFLoadOptions)
		return FromVDF(vdf, null, declaredTypeName_orOptions);

	try { return VDF.Deserialize(vdf, declaredTypeName_orOptions, FinalizeFromVDFOptions(options)); }
	/*catch(error) { if (!InUnity()) throw error;
		LogError("Error) " + error + "Stack)" + error.Stack + "\nNewStack) " + new Error().Stack + "\nVDF) " + vdf);
	}/**/ finally {}
}
export function FromVDFInto(vdf, obj, options?) {
	try { return VDF.DeserializeInto(vdf, obj, FinalizeFromVDFOptions(options)); }
	/*catch(error) { if (!InUnity()) throw error;
		LogError("Error) " + error + "Stack)" + error.Stack + "\nNewStack) " + new Error().Stack + "\nVDF) " + vdf); }
	/**/ finally {}
}
export function FromVDFToNode(vdf: string, options?: VDFLoadOptions);
export function FromVDFToNode(vdf: string, declaredTypeName: string, options?: VDFLoadOptions);
export function FromVDFToNode(vdf, declaredTypeName_orOptions?, options?) {
	if (declaredTypeName_orOptions instanceof VDFLoadOptions)
		return FromVDF(vdf, null, declaredTypeName_orOptions);

	try { return VDFLoader.ToVDFNode(vdf, declaredTypeName_orOptions, FinalizeFromVDFOptions(options)); }
	/*catch(error) { if (!InUnity()) throw error;
		LogError("Error) " + error + "Stack)" + error.Stack + "\nNewStack) " + new Error().Stack + "\nVDF) " + vdf);
	}/**/ finally {}
}
export function FromVDFNode(node, declaredTypeName?) { // alternative to .ToObject(), which applies default (program) settings
	return node.ToObject(declaredTypeName, FinalizeFromVDFOptions());
}

export function FinalizeToVDFOptions(options?) {
    options = options || new VDFSaveOptions();
	return options;
}
/*function ToVDF(obj, /*o:*#/ declaredTypeName_orOptions, options_orNothing) {
	try { return VDF.Serialize(obj, declaredTypeName_orOptions, options_orNothing); }
	/*catch(error) { if (!InUnity()) throw error; else LogError("Error) " + error + "Stack)" + error.stack + "\nNewStack) " + new Error().stack + "\nObj) " + obj); }
	//catch(error) { if (!InUnity()) { debugger; throw error; } else LogError("Error) " + error + "Stack)" + error.stack + "\nNewStack) " + new Error().stack + "\nObj) " + obj); }
}*/
export function ToVDF(obj, markRootType = true, typeMarking = VDFTypeMarking.Internal, options = null) {
	try {
		options = FinalizeToVDFOptions(options);
		options.typeMarking = typeMarking;
		return VDF.Serialize(obj, !markRootType && obj != null ? obj.GetTypeName() : null, options);
	}
	/*catch(error) {
		if (!InUnity()) throw error;
		LogError("Error) " + error + "Stack)" + error.Stack + "\nNewStack) " + new Error().Stack + "\nObj) " + obj);
	}/**/ finally {}
}
export function ToVDFNode(vdf: string, options?: VDFSaveOptions);
export function ToVDFNode(vdf: string, declaredTypeName: string, options?: VDFSaveOptions);
export function ToVDFNode(obj, declaredTypeName_orOptions?, options_orNothing?) {
	try {
	    return VDFSaver.ToVDFNode(obj, declaredTypeName_orOptions, options_orNothing);
	}
	/*catch (error) { if (!InUnity()) throw error;
		LogError("Error) " + error + "Stack)" + error.Stack + "\nNewStack) " + new Error().Stack + "\nObj) " + obj);
	}/**/ finally {}
}

export function GetTypeNameOf(obj) {
	if (obj === null || obj === undefined || obj.constructor == null) return null;
	return obj.GetTypeName();
}

// vdf extensions
// ==========

Moment.prototype.Serialize = (function() {
	return new VDFNode(this.format("YYYY-MM-DD HH:mm:ss.SSS"));
} as any).AddTags(new VDFSerialize());
Moment.prototype.Deserialize = (function(node) {
	return Moment(node.primitiveValue);
} as any).AddTags(new VDFDeserialize(true));

// fix for that ObservableArray's would be serialized as objects
var observableHelper = O({test1: []});
var ObservableArray = observableHelper.test1.constructor;
(ObservableArray as any).name_fake = "List(object)";
//alert(ObservableArray + ";" + VDF.GetTypeNameOfObject(observableHelper.test1))

// others
// ==========

export function Range(min, max, includeMax = true) {
	var result: number[] = [];
	for (let i = min; includeMax ? i <= max : i < max; i++)
		result.push(i);
	return result;
}

export function Global(target: Function) {
	var name = (target as any).GetName();
	//console.log("Globalizing: " + name);
	g[name] = target;
}

export class IDProvider {
	lastID = -1;
	GetID() {
		return ++this.lastID;
	}
}