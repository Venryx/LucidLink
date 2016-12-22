var g: any = global;
g.g = g;

//import {LogEntry, More} from '../LucidLink/More';
import {LogEntry, More} from '../LucidLink/More';
import V from '../Packages/V/V';

import {AppRegistry, NativeModules, StyleSheet, DeviceEventEmitter} from "react-native";
//import {observable as O, autorun} from "mobx";
import {observable as O} from "mobx";
//import Moment from "moment";
var Moment = require("moment");

var globalComps = {O};
(globalComps as any).Moment = Moment;
//globalComps.Extend({Moment});
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
};

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
		More.AddLogEntry(new LogEntry(type, message, new Date()));
	} catch (ex) {}
	if (sendToJava) {
		//try {
			JavaLog(type, message);
		//} catch (ex) {}
	}
};
export function JavaLog(...args) {
	var type = "general", message;
	if (args.length == 1) [message] = args;
	else if (args.length == 2) [type, message] = args;
	JavaBridge.Main.PostJSLog(type, message);
}
export function Trace(...args) {
	console.trace.apply(this, args);
};

export function Toast(text, duration = 0) {
	if (!IsString(text))
		text = text != null ? text.toString() : "";
	JavaBridge.Main.ShowToast(text, duration);
}

export function Notify(text) {
	if (!IsString(text))
		text = text != null ? text.toString() : "";
	JavaBridge.Main.Notify(text);
}

export function Assert(condition, messageOrMessageFunc = "") {
	if (condition) return;

	var message = (messageOrMessageFunc as any) instanceof Function ? (messageOrMessageFunc as any)() : messageOrMessageFunc;

	Log("Assert failed) " + message);
	console.error(message);
	throw new Error(message);
};
export function AssertWarn(condition, messageOrMessageFunc) {
	if (condition) return;

	var message = messageOrMessageFunc instanceof Function ? messageOrMessageFunc() : messageOrMessageFunc;

	console.warn(message);
};

export class A {
    static set NonNull(value) {
		Assert(value != null, "Value cannot be null.");
	}
	static NotEqualTo(val1) {
	    return new A_NotEqualTo_Wrapper(val1);
	}
	static OfType(typeNameOrType) {
	    var type = Type(typeNameOrType);
	    return new A_OfType_Wrapper(type);
	}
} 
export class A_NotEqualTo_Wrapper {
	constructor(val1) { this.val1 = val1; }
	val1;
    set a(val2) { Assert(val2 != this.val1); }
}
export class A_OfType_Wrapper {
	constructor(type) { this.type = type; }
	type;
    set a(val) { Assert(val != null && val.GetType().IsDerivedFrom(this.type)); }
}

export class JavaBridge {
    static get Main() {
        return NativeModules.Main;
    }
}

// type system
// ==========

function IsType(obj: any) {
	return obj instanceof Function;
}
// probably temp
/*function SimplifyTypeName(typeName) {
	var result = typeName;
	if (result.startsWith("List("))
		result = "IList";
	return result;
}*/
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
}

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

export function E(...objExtends: any[]) {
    var result = {};
    for (var extend of objExtends)
        result.Extend(extend);
	return result;
	//return StyleSheet.create(result);
}
// for react-native-chart modifications...
g.E = E;

// methods: serialization
// ==========

// object-Json
export function FromJSON(json) { return JSON.parse(json); }
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

export function ToJSON_Try(...args) {
	try {
		return ToJSON.apply(this, args);
	} catch (ex) {}
	return "[converting to JSON failed]";
}

// object-VDF
// ----------

//Function.prototype._AddGetter_Inline = function VDFSerialize() { return function() { return VDF.CancelSerialize; }; };
(Function.prototype as any).Serialize = (function() { return VDF.CancelSerialize; } as any).AddTags(new VDFSerialize());

export function FinalizeFromVDFOptions(options = null) {
    options = options || new VDFLoadOptions();
	options.loadUnknownTypesAsBasicTypes = true;
	return options;
}
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
export function ToVDFNode(obj, /*o:*/ declaredTypeName_orOptions, options_orNothing) {
	try {
	    return VDFSaver.ToVDFNode(obj, declaredTypeName_orOptions, options_orNothing);
	}
	/*catch (error) { if (!InUnity()) throw error;
		LogError("Error) " + error + "Stack)" + error.Stack + "\nNewStack) " + new Error().Stack + "\nObj) " + obj);
	}/**/ finally {}
}

export function GetTypeName(obj) {
	if (obj === null || obj === undefined) return null;
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

// tags
// ==========

export function T(typeOrTypeName: any) {
    return (target, name)=> {
        //target.prototype[name].AddTags(new VDFPostDeserialize());
        //Prop(target, name, typeOrTypeName);
        //target.p(name, typeOrTypeName);
        var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
        propInfo.typeName = typeOrTypeName instanceof Function ? typeOrTypeName.name : typeOrTypeName;
    };
};
export function P(...args): PropertyDecorator {
    return function(target, name) {
        var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
        propInfo.AddTags(new VDFProp(...args));
    };
};
export function D(...args) {
    return (target, name)=> {
        var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
        propInfo.AddTags(new DefaultValue(...args));
    };
};

export function _VDFTypeInfo(...args) {
    return (target, name)=>target[name].AddTags(new VDFTypeInfo(...args));
};

export function _IgnoreStartData() {
    return (target, name)=>target[name].AddTags(new IgnoreStartData());
};

export function _NoAttach(...args) {
    return (target, name)=> {
        var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
        propInfo.AddTags(new NoAttach(...args));
    };
};
export function _ByPath(...args) {
    return (target, name)=> {
        var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
        propInfo.AddTags(new ByPath(...args));
    };
};
export function _ByPathStr(...args) {
	return (target, name)=> {
		var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
	    propInfo.AddTags(new ByPathStr(...args));
	};
};
export function _ByName(...args) {
    return (target, name)=> {
        var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
        propInfo.AddTags(new ByName(...args));
    };
};

export function _VDFDeserializeProp(...args) {
    return (target, name)=>target[name].AddTags(new VDFDeserializeProp(...args));
};
export function _VDFSerializeProp(...args) {
    return (target, name)=>target[name].AddTags(new VDFSerializeProp(...args));
};

export function _VDFPreDeserialize(...args) {
    return (target, name)=>target[name].AddTags(new VDFPreDeserialize(...args));
};
export function _VDFDeserialize(...args) {
    return (target, name)=>target[name].AddTags(new VDFDeserialize(...args));
};
export function _VDFPostDeserialize(...args) {
    return (target, name)=>target[name].AddTags(new VDFPostDeserialize(...args));
};

export function _VDFPreSerialize(...args) {
    return (target, name)=>target[name].AddTags(new VDFPreSerialize(...args));
};
export function _VDFSerialize(...args) {
    return (target, name)=>target[name].AddTags(new VDFSerialize(...args));
};
export function _VDFPostSerialize(...args) {
    return (target, name)=>target[name].AddTags(new VDFPostSerialize(...args));
};

// types stuff
// ==========

export function IsPrimitive(obj) { return IsBool(obj) || IsNumber(obj) || IsString(obj); }
export function IsBool(obj) { return typeof obj == "boolean"; } //|| obj instanceof Boolean
export function ToBool(boolStr) { return boolStr == "true" ? true : false; }
export function IsNumber(obj, allowNumberObj = false) { return typeof obj == "number" || (allowNumberObj && obj instanceof Number); }
export function IsNumberString(obj) { return IsString(obj) && parseInt(obj).toString() == obj; }
export function ToInt(stringOrFloatVal) { return parseInt(stringOrFloatVal); }
export function ToDouble(stringOrIntVal) { return parseFloat(stringOrIntVal); }
export function IsString(obj, allowStringObj = false) { return typeof obj == "string" || (allowStringObj && obj instanceof String); }
export function ToString(val) { return "" + val; }

export function IsNaN(obj) { return typeof obj == "number" && obj != obj; }

export function IsInt(obj) { return typeof obj == "number" && parseFloat(obj as any) == parseInt(obj as any); }
export function IsDouble(obj) { return typeof obj == "number" && parseFloat(obj as any) != parseInt(obj as any); }

// timer stuff
// ==========

export function WaitXThenRun(waitTime, func) { setTimeout(func, waitTime); }
export function Sleep(ms) {
	var startTime = new Date().getTime();
	while (new Date().getTime() - startTime < ms)
	{}
}
export function WaitXThenRun_Multiple(waitTime, func, count = -1) {
	var countDone = 0;
	var timerID = setInterval(function() {
		func();
		countDone++;
		if (count != -1 && countDone >= count)
			clearInterval(timerID);
	}, waitTime);

	var controller: any = {};
	controller.Stop = function() { clearInterval(timerID); }
	return controller;
}

// interval is in seconds (can be decimal)
export class Timer {
	constructor(interval, func, maxCallCount = -1) {
	    this.interval = interval;
	    this.func = func;
	    this.maxCallCount = maxCallCount;
	}

	interval;
	func;
	maxCallCount;
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
		return this;
	}
	Stop() {
		clearInterval(this.timerID);
		this.timerID = -1;
	}
}
export class TimerMS extends Timer {
    constructor(interval_decimal, func, maxCallCount = -1) {
        super(interval_decimal / 1000, func, maxCallCount);
    }
}

var funcLastScheduledRunTimes = {};
export function BufferAction(...args) {
	if (args.length == 2) var [minInterval, func] = args, key = null;
	else if (args.length == 3) var [key, minInterval, func] = args;

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

// Random
// ==========

export class Random {
	static canvasContext;
	static canvas;

	constructor(seed = new Date().getTime()) {
		if (seed == 0)
			throw new Error("Cannot use 0 as seed. (prng algorithm isn't very good, and doesn't work with seeds that are multiples of PI)");
		this.seed = seed;
	}
	seed = -1;
	NextInt() {
		var randomDouble = Math.sin(this.seed) * 10000;
		var randomDouble_onlyIntegerPart = Math.floor(this.seed);
		this.seed = randomDouble;
		return randomDouble_onlyIntegerPart;
	}
	NextDouble() {
		var randomDouble = Math.sin(this.seed) * 10000;
		var randomDouble_onlyFractionalPart = this.seed - Math.floor(this.seed);
		this.seed = randomDouble;
		return randomDouble_onlyFractionalPart;
	}
	/*s.NextColor = function() { return new VColor(s.NextDouble(), s.NextDouble(), s.NextDouble()); };
	NextColor_ImageStr() {
		var color = this.NextColor();
		Random.canvasContext.fillStyle = color.ToHexStr();
		Random.canvasContext.fillRect(0, 0, Random.canvas[0].width, Random.canvas[0].height);
		var imageStr = Random.canvas[0].toDataURL();
		return imageStr.substr(imageStr.indexOf(",") + 1);
	}*/
}