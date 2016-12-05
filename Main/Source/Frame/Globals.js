import React, {Component} from "react";
import {AppRegistry, NativeModules, StyleSheet, DeviceEventEmitter} from "react-native";

var g = global;
g.g = g;

var globalComps = {NativeModules, DeviceEventEmitter};
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
g.HandleError = function(error, isFatal) {
	Log(`${error}
Stack) ${error.stack}
Fatal) ${isFatal}`);
};

g.Log = function(...args) {
	var type = "general", message, sendToJava = true;
	if (args.length == 1) [message] = args;
	else if (args.length == 2) [type, message] = args;
	else if (args.length == 3) [type, message, sendToJava] = args;

	console.log(...args);

	try {
		More.AddLogEntry(new LogEntry(type, message, new Date()));
	} catch (ex) {}
	if (sendToJava) {
		//try {
			JavaLog(type, message);
		//} catch (ex) {}
	}
};
g.JavaLog = function(...args) {
	var type = "general", message;
	if (args.length == 1) [message] = args;
	else if (args.length == 2) [type, message] = args;
	JavaBridge.Main.PostJSLog(type, message);
}
g.Trace = function(...args) {
	console.trace(...args);
};

g.Toast = function(...args) {
	if (args.length == 1)
		JavaBridge.Main.ShowToast(args[0], 1);
	else
		JavaBridge.Main.ShowToast(args[0], args[1]);
}

g.Assert = function(condition, messageOrMessageFunc) {
	if (condition) return;

	var message = messageOrMessageFunc instanceof Function ? messageOrMessageFunc() : messageOrMessageFunc;

	Log("Assert failed) " + message);
	console.error(message);
	throw new Error(message);
};
g.AssertWarn = function(condition, messageOrMessageFunc) {
	if (condition) return;

	var message = messageOrMessageFunc instanceof Function ? messageOrMessageFunc() : messageOrMessageFunc;

	console.warn(message);
};

g.A = class A {
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
g.A_NotEqualTo_Wrapper = class A_NotEqualTo_Wrapper {
	constructor(val1) { this.val1 = val1; }
    set a(val2) { Assert(val2 != this.val1); }
}
g.A_OfType_Wrapper = class A_OfType_Wrapper {
	constructor(type) { this.type = type; }
    set a(val) { Assert(val != null && val.GetType().IsDerivedFrom(this.type)); }
}

g.JavaBridge = class JavaBridge {
    static get Main() {
        return NativeModules.Main;
    }
}

// polyfills for constants
// ==========

if (Number.MIN_SAFE_INTEGER == null)
	Number.MIN_SAFE_INTEGER = -9007199254740991;
if (Number.MAX_SAFE_INTEGER == null)
	Number.MAX_SAFE_INTEGER = 9007199254740991;

//g.Break = function() { debugger; };
g.Debugger = function() { debugger; }
g.Debugger_True = function() { debugger; return true; }
g.Debugger_If = function(condition) {
    if (condition)
        debugger;
}

// general
// ==========

g.E = function(...objExtends) {
    var result = {};
    for (var extend of objExtends)
        result.Extend(extend);
	return result;
	//return StyleSheet.create(result);
}

// methods: serialization
// ==========

// object-Json
g.FromJSON = function(json) { return JSON.parse(json); }
g.ToJSON = function(obj, excludePropNames___) {
	try {
		if (arguments.length > 1) {
			var excludePropNames = V.Slice(arguments, 1);
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
g.ToJSON_Safe = function(obj, excludePropNames___) {
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

g.ToJSON_Try = function(...args) {
	try {
		return ToJSON(...args);
	} catch (ex) {}
	return "[converting to JSON failed]";
}

// object-VDF
// ----------

//Function.prototype._AddGetter_Inline = function VDFSerialize() { return function() { return VDF.CancelSerialize; }; };
Function.prototype.Serialize = function() { return VDF.CancelSerialize; }.AddTags(new VDFSerialize());

g.FinalizeFromVDFOptions = function(options = null) {
    options = options || new VDFLoadOptions();
	options.loadUnknownTypesAsBasicTypes = true;
	return options;
}
g.FromVDF = function(vdf, /*o:*/ declaredTypeName_orOptions, options) {
	if (declaredTypeName_orOptions instanceof VDFLoadOptions)
		return FromVDF(vdf, null, declaredTypeName_orOptions);

	try { return VDF.Deserialize(vdf, declaredTypeName_orOptions, FinalizeFromVDFOptions(options)); }
	/*catch(error) { if (!InUnity()) throw error;
		LogError("Error) " + error + "Stack)" + error.Stack + "\nNewStack) " + new Error().Stack + "\nVDF) " + vdf);
	}/**/ finally {}
}
g.FromVDFInto = function(vdf, obj, /*o:*/ options) {
	try { return VDF.DeserializeInto(vdf, obj, FinalizeFromVDFOptions(options)); }
	/*catch(error) { if (!InUnity()) throw error;
		LogError("Error) " + error + "Stack)" + error.Stack + "\nNewStack) " + new Error().Stack + "\nVDF) " + vdf); }
	/**/ finally {}
}
g.FromVDFToNode = function(vdf, /*o:*/ declaredTypeName_orOptions, options) {
	if (declaredTypeName_orOptions instanceof VDFLoadOptions)
		return FromVDF(vdf, null, declaredTypeName_orOptions);

	try { return VDFLoader.ToVDFNode(vdf, declaredTypeName_orOptions, FinalizeFromVDFOptions(options)); }
	/*catch(error) { if (!InUnity()) throw error;
		LogError("Error) " + error + "Stack)" + error.Stack + "\nNewStack) " + new Error().Stack + "\nVDF) " + vdf);
	}/**/ finally {}
}
g.FromVDFNode = function(node, declaredTypeName = null) { // alternative to .ToObject(), which applies default (program) settings
	return node.ToObject(declaredTypeName, FinalizeFromVDFOptions());
}

g.FinalizeToVDFOptions = function(options = null) {
    options = options || new VDFSaveOptions();
	return options;
}
/*function ToVDF(obj, /*o:*#/ declaredTypeName_orOptions, options_orNothing) {
	try { return VDF.Serialize(obj, declaredTypeName_orOptions, options_orNothing); }
	/*catch(error) { if (!InUnity()) throw error; else LogError("Error) " + error + "Stack)" + error.stack + "\nNewStack) " + new Error().stack + "\nObj) " + obj); }
	//catch(error) { if (!InUnity()) { debugger; throw error; } else LogError("Error) " + error + "Stack)" + error.stack + "\nNewStack) " + new Error().stack + "\nObj) " + obj); }
}*/
g.ToVDF = function(obj, markRootType = true, typeMarking = VDFTypeMarking.Internal, options = null) {
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
g.ToVDFNode = function(obj, /*o:*/ declaredTypeName_orOptions, options_orNothing) {
	try {
	    return VDFSaver.ToVDFNode(obj, declaredTypeName_orOptions, options_orNothing);
	}
	/*catch (error) { if (!InUnity()) throw error;
		LogError("Error) " + error + "Stack)" + error.Stack + "\nNewStack) " + new Error().Stack + "\nObj) " + obj);
	}/**/ finally {}
}

g.GetTypeName = function(obj) {
	if (obj === null || obj === undefined) return null;
	return obj.GetTypeName();
}

// tags
// ==========

g.T = function(typeOrTypeName) {
    return (target, name, descriptor)=> {
        //target.prototype[name].AddTags(new VDFPostDeserialize());
        //Prop(target, name, typeOrTypeName);
        //target.p(name, typeOrTypeName);
        var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
        propInfo.typeName = typeOrTypeName instanceof Function ? typeOrTypeName.name : typeOrTypeName;
    };
};
g.P = function(...args) {
    return function Temp1(target, name, descriptor) {
        var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
        propInfo.AddTags(new VDFProp(...args));
    };
};
g.D = function(...args) {
    return (target, name, descriptor)=> {
        var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
        propInfo.AddTags(new DefaultValue(...args));
    };
};

g._VDFTypeInfo = function(...args) {
    return (target, name, descriptor)=>target[name].AddTags(new VDFTypeInfo(...args));
};

g._IgnoreStartData = function() {
    return (target, name, descriptor)=>target[name].AddTags(new IgnoreStartData());
};

g._NoAttach = function(...args) {
    return (target, name, descriptor)=> {
        var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
        propInfo.AddTags(new NoAttach(...args));
    };
};
g._ByPath = function(...args) {
    return (target, name, descriptor)=> {
        var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
        propInfo.AddTags(new ByPath(...args));
    };
};
g._ByPathStr = function(...args) {
	return (target, name, descriptor)=> {
		var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
	    propInfo.AddTags(new ByPathStr(...args));
	};
};
g._ByName = function(...args) {
    return (target, name, descriptor)=> {
        var propInfo = VDFTypeInfo.Get(target.constructor).GetProp(name);
        propInfo.AddTags(new ByName(...args));
    };
};

g._VDFDeserializeProp = function(...args) {
    return (target, name, descriptor)=>target[name].AddTags(new VDFDeserializeProp(...args));
};
g._VDFSerializeProp = function(...args) {
    return (target, name, descriptor)=>target[name].AddTags(new VDFSerializeProp(...args));
};

g._VDFPreDeserialize = function(...args) {
    return (target, name, descriptor)=>target[name].AddTags(new VDFPreDeserialize(...args));
};
g._VDFDeserialize = function(...args) {
    return (target, name, descriptor)=>target[name].AddTags(new VDFDeserialize(...args));
};
g._VDFPostDeserialize = function(...args) {
    return (target, name, descriptor)=>target[name].AddTags(new VDFPostDeserialize(...args));
};

g._VDFPreSerialize = function(...args) {
    return (target, name, descriptor)=>target[name].AddTags(new VDFPreSerialize(...args));
};
g._VDFSerialize = function(...args) {
    return (target, name, descriptor)=>target[name].AddTags(new VDFSerialize(...args));
};
g._VDFPostSerialize = function(...args) {
    return (target, name, descriptor)=>target[name].AddTags(new VDFPostSerialize(...args));
};

// types stuff
// ==========

g.IsPrimitive = function(obj) { return IsBool(obj) || IsNumber(obj) || IsString(obj); }
g.IsBool = function(obj) { return typeof obj == "boolean"; } //|| obj instanceof Boolean
g.ToBool = function(boolStr) { return boolStr == "true" ? true : false; }
g.IsNumber = function(obj, allowNumberObj = false) { return typeof obj == "number" || (allowNumberObj && obj instanceof Number); }
g.IsNumberString = function(obj) { return IsString(obj) && parseInt(obj).toString() == obj; }
g.ToInt = function(stringOrFloatVal) { return parseInt(stringOrFloatVal); }
g.ToDouble = function(stringOrIntVal) { return parseFloat(stringOrIntVal); }
g.IsString = function(obj, allowStringObj = false) { return typeof obj == "string" || (allowStringObj && obj instanceof String); }
g.ToString = function(val) { return "" + val; }

g.IsNaN = function(obj) { return typeof obj == "number" && obj != obj; }

g.IsInt = function(obj) { return typeof obj == "number" && parseFloat(obj) == parseInt(obj); }
g.IsDouble = function(obj) { return typeof obj == "number" && parseFloat(obj) != parseInt(obj); }

// timer stuff
// ==========

g.WaitXThenRun = function(waitTime, func) { setTimeout(func, waitTime); }
g.Sleep = function(ms) {
	var startTime = new Date().getTime();
	while (new Date().getTime() - startTime < ms)
	{}
}
g.WaitXThenRun_Multiple = function(waitTime, func, count = -1) {
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
g.Timer = class Timer {
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
g.TimerMS = class TimerMS extends Timer {
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

// Random
// ==========

g.Random = function(seed) {
	seed = seed != null ? seed : new Date().getTime();

	if (seed == 0)
		throw new Error("Cannot use 0 as seed. (prng algorithm isn't very good, and doesn't work with seeds that are multiples of PI)");

	var s = this;
	s.seed = seed;
	s.NextInt = function() {
		var randomDouble = Math.sin(s.seed) * 10000;
		var randomDouble_onlyIntegerPart = Math.floor(s.seed);
		s.seed = randomDouble;
		return randomDouble_onlyIntegerPart;
	};
	s.NextDouble = function() {
		var randomDouble = Math.sin(s.seed) * 10000;
		var randomDouble_onlyFractionalPart = s.seed - Math.floor(s.seed);
		s.seed = randomDouble;
		return randomDouble_onlyFractionalPart;
	};
	s.NextColor = function() { return new VColor(s.NextDouble(), s.NextDouble(), s.NextDouble()); };
	s.NextColor_ImageStr = function() {
		var color = s.NextColor();
		Random.canvasContext.fillStyle = color.ToHexStr();
		Random.canvasContext.fillRect(0, 0, Random.canvas[0].width, Random.canvas[0].height);
		var imageStr = Random.canvas[0].toDataURL();
		return imageStr.substr(imageStr.indexOf(",") + 1);
	};
}