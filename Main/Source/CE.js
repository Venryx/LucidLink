var g = global;

// ClassExtensions.js
// ==========

// Object: base
// ==================

// the below lets you do stuff like this: Array.prototype._AddFunction(function AddX(value) { this.push(value); }); []._AddX("newItem");
Object.defineProperty(Object.prototype, "_AddItem", { // note; these functions should by default add non-enumerable properties/items
	//configurable: true,
	enumerable: false,
	value: function(name, value, forceAdd) {
		if (this[name])
			delete this[name];
		if (!this[name] || forceAdd) // workaround for some properties not being deleted
			Object.defineProperty(this, name, {
				enumerable: false,
				value: value
			});
	}
});
Object.prototype._AddItem("_AddFunction", function(name, func) {
	//this._AddItem(func.name || func.toString().match(/^function\s*([^\s(]+)/)[1], func);
	this._AddItem(name, func);
});

// the below lets you do stuff like this: Array.prototype._AddGetterSetter("AddX", null, function(value) { this.push(value); }); [].AddX = "newItem";
Object.prototype._AddFunction("_AddGetterSetter", function(name, getter, setter) {
	//var name = (getter || setter).name || (getter || setter).toString().match(/^function\s*([^\s(]+)/)[1];
	if (this[name])
		delete this[name];
	if (!this[name]) // workaround for some properties not being deleted
		if (getter && setter)
			Object.defineProperty(this, name, {enumerable: false, get: getter, set: setter});
		else if (getter)
			Object.defineProperty(this, name, {enumerable: false, get: getter});
		else
			Object.defineProperty(this, name, {enumerable: false, set: setter});
});

// the below lets you do stuff like this: Array.prototype._AddFunction_Inline = function AddX(value) { this.push(value); }; [].AddX = "newItem";
Object.prototype._AddGetterSetter("_AddFunction_Inline", null, function(func) { this._AddFunction(func.name, func); }); // maybe make-so: these use func.GetName()
Object.prototype._AddGetterSetter("_AddGetter_Inline", null, function(func) { this._AddGetterSetter(func.name, func, null); });
Object.prototype._AddGetterSetter("_AddSetter_Inline", null, function(func) { this._AddGetterSetter(func.name, null, func); });

// alias for _AddFunction_Inline, since now we need to add functions to the "window" object relatively often
Object.prototype._AddGetterSetter("AddFunc", null, function(func) { this._AddFunction(func.name, func); });

// Function (early)
// ==========

//Function.prototype._AddFunction_Inline = function GetName() { return this.name || this.name_fake || this.toString().match(/^function\s*([^\s(]+)/)[1]; };
Function.prototype._AddFunction_Inline = function GetName() { return this.name_fake || this.name || this.toString().match(/^function\s*([^\s(]+)/)[1]; };
Function.prototype._AddFunction_Inline = function SetName(name) { this.name_fake = name; return this; };
// probably make-so: SetName_Temp function exists
//Function.prototype._AddFunction_Inline = function Call_Silent(self) { this.apply(self, V.Slice(arguments, 1)); return this; }
//Function.prototype._AddFunction_Inline = function Call_Silent() { this.apply(this, arguments); return this; }

// Object: C# polyfills/emulators
// ==================

/*Object.prototype._AddGetterSetter("AddMethod", null, function(func) { // for steamlined prototype-method-adding, that doesn't overwrite the method if it already exists (maybe just for use in this project)
	if (this.prototype[func.GetName()] == null)
		this._AddFunction(func.GetName(), func);
});*/
Object.prototype._AddSetter_Inline = function AddMethod(func) { // for steamlined prototype-method-adding, that doesn't overwrite the method if it already exists (maybe just for use in this project)
	if (this[func.GetName()] == null)
		this._AddFunction(func.GetName(), func);
};
// maybe temp; shorthand version (i.e.: p.method = function MethodName() {};)
/*Object.prototype._AddSetter_Inline = function method(func) //Method, add, Add,
{
	if (this[func.GetName()] == null)
		this._AddFunction(func.GetName(), func);
};*/

Object.prototype._AddFunction_Inline = function SetBaseClass(baseClassFunc) {
	//this.prototype.__proto__ = baseClassFunc.prototype; // makes "(new ThisClass()) instanceof BaseClass" be true
	//Object.setPrototypeOf(this, baseClassFunc); // makes it easier to find base-classes from derived-class
	Object.setPrototypeOf(this.prototype, baseClassFunc.prototype); // makes "(new ThisClass()) instanceof BaseClass" be true

	//self.constructor = List; // makes "(new List()).constructor == List" be true

	var name = this.GetName();
	if (name != "")
		// this only runs on class constructor functions, so if function has name (i.e. name sucked in for self-knowledge purposes), create a variable by that name for global access
		window[name] = this;
};
Object.prototype._AddSetter_Inline = function SetAsBaseClassFor(derivedClassFunc) {
	derivedClassFunc.SetBaseClass(this);
	//window[derivedClassFunc.GetName()] = derivedClassFunc;
};
Object.prototype._AddFunction_Inline = function CallBaseConstructor(constructorArgs___) {
	//return this.prototype.__proto__.apply(this, V.AsArray(arguments));
	//this.__proto__.__proto__.constructor.apply(this, V.AsArray(arguments));
    var derivedClassFunc = arguments.callee.caller;
	derivedClassFunc.prototype.__proto__.constructor.apply(this, V.AsArray(arguments));
	return this;
};
Object.prototype._AddFunction_Inline = function CallBaseConstructor_Manual(derivedClassFunc, constructorArgs___) {
	derivedClassFunc.prototype.__proto__.constructor.apply(this, V.AsArray(arguments));
	return this;
};

// probably temp; helper so 'p' function is usable on objects that aren't Node's (e.g. to declare property types)
/*Object.prototype._AddFunction_Inline = function AddHelpers(obj) {
	this.p = Node_p;
	return this;
};*/

//Object.prototype._AddFunction_Inline = function GetVDFTypeInfo() { return VDFTypeInfo.Get(this.GetTypeName()); };
//Object.prototype._AddFunction_Inline = function GetVDFTypeInfo() { return VDFTypeInfo.Get(this.GetType()); };
Object.prototype._AddFunction_Inline = function GetVDFTypeInfo() { return VDFTypeInfo.Get(this.constructor); };

//Object.prototype._AddFunction_Inline = function GetType() { return this.constructor; };
Object.prototype._AddFunction_Inline = function GetTypeName(vdfType = true) { //, simplifyForVScriptSystem)
	/*var result = this.constructor.name;
	if (allowProcessing) 	{
		if (result == "String")
			result = "string";
		else if (result == "Boolean")
			result = "bool";
		else if (result == "Number")
			result = this.toString().contains(".") ? "double" : "int";
	}
	return result;*/


	/*var result = vdfTypeName ? VDF.GetTypeNameOfObject(this) : this.constructor.name;
	//if (simplifyForVScriptSystem)
	//	result = SimplifyTypeName(result);
	return result;*/
	if (vdfType) {
		/*if (this instanceof Multi)
			return "Multi(" + this.itemType + ")";*/
		return VDF.GetTypeNameOfObject(this);
	}
	return this.constructor.name;
};
Object.prototype._AddFunction_Inline = function GetType(vdfType = true, simplifyForVScriptSystem = false) {
    var result = Type(this.GetTypeName(vdfType));
    if (simplifyForVScriptSystem)
        result = SimplifyType(result);
    return result;
};
// probably temp
/*function SimplifyTypeName(typeName) {
	var result = typeName;
	if (result.startsWith("List("))
		result = "IList";
	return result;
}*/
g.SimplifyType = function(type) {
	var typeName = IsType(type) ? type.name : type;
    if (typeName.startsWith("List("))
        return Type("IList");
    if (typeName.startsWith("Dictionary("))
        return Type("IDictionary");
    return type;
};
g.UnsimplifyType = function(type) {
    var typeName = IsType(type) ? type.name : type;
	if (typeName == "IList")
        return Type("List");
    if (typeName == "IDictionary")
        return Type("Dictionary");
    return type;
};

// Object: normal
// ==================

//Object.prototype._AddSetter_Inline = function ExtendWith_Inline(value) { this.ExtendWith(value); };
//Object.prototype._AddFunction_Inline = function ExtendWith(value) { $.extend(this, value); };
/*Object.prototype._AddFunction_Inline = function GetItem_SetToXIfNull(itemName, /*;optional:*#/ defaultValue) {
	if (!this[itemName])
		this[itemName] = defaultValue;
	return this[itemName];
};*/
//Object.prototype._AddFunction_Inline = function CopyXChildrenAsOwn(x) { $.extend(this, x); };
//Object.prototype._AddFunction_Inline = function CopyXChildrenToClone(x) { return $.extend($.extend({}, this), x); };

Object.prototype._AddFunction_Inline = function Extend(x) {
	for (var name in x) {
		var value = x[name];
		//if (value !== undefined)
        this[name] = value;
    }
	return this;
};

// as replacement for C#'s 'new MyClass() {prop = true}'
Object.prototype._AddFunction_Inline = function Init(x) { return this.Extend(x); };
Object.prototype._AddFunction_Inline = function Init_VTree(x) { // by default, uses set_self method
    for (var name in x)
    	this.a(name).set_self = x[name];
	return this;
};

Object.prototype._AddFunction_Inline = function Set_Normal(x) { return this.Extend(x); };
Object.prototype._AddFunction_Inline = function Set_VTree(x) { return this.Init_VTree(x); };

Object.prototype._AddFunction_Inline = function Extended(x) {
	var result = {};
	for (var name in this)
		result[name] = this[name];
	if (x)
    	for (var name in x)
    		result[name] = x[name];
    return result;
};
//Object.prototype._AddFunction_Inline = function E(x) { return this.Extended(x); };

Object.prototype._AddFunction_Inline = function RemovingProps(...propNames) {
    var result = this.Extended();
    for (let propName of propNames)
        delete result[propName];
    return result;
}

/*Object.prototype._AddFunction_Inline = function Keys() {
	var result = [];
	for (var key in this)
		if (this.hasOwnProperty(key))
			result.push(key);
	return result;
};*/
//Object.prototype._AddFunction_Inline = function Keys() { return Object.keys(this); }; // 'Keys' is already used for Dictionary prop
//Object.prototype._AddGetter_Inline = function VKeys() { return Object.keys(this); }; // 'Keys' is already used for Dictionary prop
Object.prototype._AddFunction_Inline = function VKeys() { return Object.keys(this); }; // 'Keys' is already used for Dictionary prop
// like Pairs for Dictionary, except for Object
Object.prototype._AddGetter_Inline = function Props() {
	var result = [];
	var i = 0;
	for (var propName in this)
		result.push({index: i++, name: propName, value: this[propName]});
	return result;
};
/*Object.defineProperty(Object.prototype, "Keys", {
	enumerable: false,
	configurable: true,
	get: function() { return Object.keys(this); }
	//get: Object.keys
});*/
/*Object.prototype._AddFunction_Inline = function Items() {
	var result = [];
	for (var key in this)
		if (this.hasOwnProperty(key))
			result.push(this[key]);
	return result;
};*/
//Object.prototype._AddFunction_Inline = function ToJson() { return JSON.stringify(this); };

Object.prototype._AddFunction_Inline = function AddProp(name, value) {
	this[name] = value;
	return this;
};

/*Object.prototype._AddFunction_Inline = function GetVSData(context) {
	this[name] = value;
	return this;
};*/

Object.prototype._AddFunction_Inline = function VAct(action) {
	action.call(this);
	return this;
};

// Function
// ==========

Function.prototype._AddFunction_Inline = function AddTag(tag) {
	if (this.tags == null)
		this.tags = [];
	this.tags.push(tag);
	return this;
};
/*Function.prototype._AddFunction_Inline = function AddTags(/*o:*#/ tags___) { // (already implemented in VDF.js file)
	if (this.tags == null)
		this.tags = [];
	for (var i in arguments)
		this.tags.push(arguments[i]);
	return this;
};*/
/*function AddTags() {
	var tags = V.Slice(arguments, 0, arguments.length - 1);
	var func = V.Slice(arguments).Last();
	func.AddTags.apply(func, tags);
};*/
Function.prototype._AddFunction_Inline = function GetTags(/*o:*/ type) {
	return (this.tags || []).Where(a=>type == null || a instanceof type);
};

Function.prototype._AddFunction_Inline = function AsStr(args___) { return V.Multiline.apply(null, [this].concat(V.AsArray(arguments))); };

Function.prototype._AddFunction_Inline = function RunThenReturn(args___) { this.apply(null, arguments); return this; };

// Number
// ==========

Number.prototype._AddFunction_Inline = function IfN1Then(valIfSelfIsNeg1) {
	return this != -1 ? this : valIfSelfIsNeg1;
};

//Number.prototype._AddFunction_Inline = function RoundToMultipleOf(step) { return Math.round(new Number(this) / step) * step; }; //return this.lastIndexOf(str, 0) === 0; };
Number.prototype._AddFunction_Inline = function RoundToMultipleOf(step) {
	var integeredAndRounded = Math.round(new Number(this) / step);
	var result = (integeredAndRounded * step).toFixed(step.toString().TrimStart("0").length); // - 1);
	if (result.contains("."))
		result = result.TrimEnd("0").TrimEnd(".");
	return result;
};

Number.prototype._AddFunction_Inline = function KeepAtLeast(step) {
	return Math.max(min, this);
};
Number.prototype._AddFunction_Inline = function KeepAtMost(step) {
	return Math.min(max, this);
};
Number.prototype._AddFunction_Inline = function KeepBetween(min, max) {
	return Math.min(max, Math.max(min, this));
};

// String
// ==========

String.prototype._AddFunction_Inline = function TrimEnd(chars___) {
	var chars = V.Slice(arguments);

	var result = "";
	var doneTrimming = false;
	for (var i = this.length - 1; i >= 0; i--)
		if (!chars.Contains(this[i]) || doneTrimming) {
			result = this[i] + result;
			doneTrimming = true;
		}
	return result;
};

String.prototype._AddFunction_Inline = function startsWith(str) { return this.indexOf(str) == 0; }; //return this.lastIndexOf(str, 0) === 0; };
String.prototype._AddFunction_Inline = function endsWith(str) { var pos = this.length - str.length; return this.indexOf(str, pos) === pos; };
String.prototype._AddFunction_Inline = function contains(str, /*;optional:*/ startIndex) { return -1 !== String.prototype.indexOf.call(this, str, startIndex); };
String.prototype._AddFunction_Inline = function hashCode() {
	var hash = 0;
	for (var i = 0; i < this.length; i++) {
		var char = this.charCodeAt(i);
		hash = ((hash << 5) - hash) + char;
		hash |= 0; // convert to 32-bit integer
	}
	return hash;
};
String.prototype._AddFunction_Inline = function Matches(strOrRegex) {
	if (typeof strOrRegex == "string") {
		var str = strOrRegex;
		var result = [];
		var lastMatchIndex = -1;
		while (true) {
			var matchIndex = this.indexOf(str, lastMatchIndex + 1);
			if (matchIndex == -1) // if another match was not found
				break;
			result.push({index: matchIndex});
			lastMatchIndex = matchIndex;
		}
		return result;
	}

	var regex = strOrRegex;
	if (!regex.global)
		throw new Error("Regex must have the 'g' flag added. (otherwise an infinite loop occurs)");

	var result = [];
	var match;
	while (match = regex.exec(this))
		result.push(match);
	return result;
};
/*String.prototype._AddFunction_Inline = function matches_group(regex, /*o:*#/ groupIndex)
{
	if (!regex.global)
		throw new Error("Regex must have the 'g' flag added. (otherwise an infinite loop occurs)");

	groupIndex = groupIndex || 0; // default to the first capturing group
	var matches = [];
	var match;
	while (match = regex.exec(this))
		matches.push(match[groupIndex]);
	return matches;
};*/
/*String.prototype._AddFunction_Inline = function lastIndexOf(str)
{
    for (var i = this.length - 1; i >= 0; i--)
        if (this.substr(i).startsWith(str))
            return i;
    return -1;
};*/
String.prototype._AddFunction_Inline = function IndexOf_X(x, str) // (0-based)
{
	var currentPos = -1;
	for (var i = 0; i <= x; i++)
	{
		var subIndex = this.indexOf(str, currentPos + 1);
		if (subIndex == -1)
			return -1; // no such xth index
		currentPos = subIndex;
	}
	return currentPos;
};
String.prototype._AddFunction_Inline = function IndexOf_XFromLast(x, str) // (0-based)
{
	var currentPos = (this.length - str.length) + 1; // index just after the last-index-where-match-could-occur
	for (var i = 0; i <= x; i++)
	{
		var subIndex = this.lastIndexOf(str, currentPos - 1);
		if (subIndex == -1)
			return -1; // no such xth index
		currentPos = subIndex;
	}
	return currentPos;
};
String.prototype._AddFunction_Inline = function indexOfAny() {
    var args = arguments[0] instanceof Array ? args[0] : arguments;

	var lowestIndex = -1;
	for (var i = 0; i < args.length; i++) {
		var indexOfChar = this.indexOf(args[i]);
		if (indexOfChar != -1 && (indexOfChar < lowestIndex || lowestIndex == -1))
			lowestIndex = indexOfChar;
	}
	return lowestIndex;
};
String.prototype._AddFunction_Inline = function lastIndexOfAny() {
    var args = arguments[0] instanceof Array ? args[0] : arguments;

	var highestIndex = -1;
	for (var i = 0; i < args.length; i++) {
		var indexOfChar = this.lastIndexOf(args[i]);
		if (indexOfChar > highestIndex)
			highestIndex = indexOfChar;
	}
	return highestIndex;
};
String.prototype._AddFunction_Inline = function startsWithAny() { return this.indexOfAny.apply(this, arguments) == 0; };
String.prototype._AddFunction_Inline = function containsAny() {
	for (var i = 0; i < arguments.length; i++)
		if (this.contains(arguments[i]))
			return true;
	return false;
};
String.prototype._AddFunction_Inline = function splitByAny() {
    var args = arguments;
	if (args[0] instanceof Array)
		args = args[0];

	var splitStr = "/";
	for (var i = 0; i < args.length; i++)
		splitStr += (splitStr.length > 1 ? "|" : "") + args[i];
	splitStr += "/";

	return this.split(splitStr);
};
String.prototype._AddFunction_Inline = function splice(index, removeCount, insert) { return this.slice(0, index) + insert + this.slice(index + Math.abs(removeCount)); };
String.prototype._AddFunction_Inline = function Indent(indentCount) {
    var indentStr = "\t".repeat(indentCount);
    return this.replace(/^|(\n)/g, "$1" + indentStr);
};

String.prototype._AddFunction_Inline = function Func(func) {
	func.SetName(this);
    return func;
};

// special; creates a function with the given name, but also caches it per caller-line,
//   so every call from that line returns the same function instance
// REMOVED, because: we need to create new funcs to capture new closure values
/*var oneFuncCache = {};
String.prototype._AddFunction_Inline = function OneFunc(func) {
    var funcName = this;
    var callerLineStr = new Error().stack.split("\n")[3];
    var funcKey = `${funcName}@${callerLineStr}`;
	if (oneFuncCache[funcKey] == null) {
		func.SetName(this);
	    //func.cached = true;
	    oneFuncCache[funcKey] = func;
	}
    return oneFuncCache[funcKey];
};*/

String.prototype._AddFunction_Inline = function AsMultiline() {
    return this.substring(this.indexOf("\n") + 1, this.lastIndexOf("\n"));
};

String.prototype._AddFunction_Inline = function Substring(start, end) {
    if (end < 0)
        end = this.length + end;
    return this.substring(start, end);
};

// Array
// ==========

//Array.prototype._AddFunction_Inline = function Contains(items) { return this.indexOf(items) != -1; };
Array.prototype._AddFunction_Inline = function ContainsAny(...items) {
    for (let item of items)
        if (this.indexOf(item) != -1)
            return true;
    return false;
};
Array.prototype._AddFunction_Inline = function Indexes()
{
	var result = {};
	for (var i = 0; i < this.length; i++)
		result[i] = null; //this[i]; //null;
	return result;
};
Array.prototype._AddFunction_Inline = function Strings() // not recommended, because it doesn't allow for duplicates
{
	var result = {};
	for (var key in this)
		if (this.hasOwnProperty(key))
			result[this[key]] = null;
	return result;
};

// for some reason, this platform doesn't have entries() defined
Array.prototype._AddFunction_Inline = function entries() {
	var result = [];
	for (var i = 0; i < this.length; i++)
		result.push([i, this[i]]);
	return result;
};

Array.prototype._AddFunction_Inline = function Prepend(...newItems) { this.splice(0, 0, ...newItems); };
Array.prototype._AddFunction_Inline = function Add(item) { return this.push(item); };
Array.prototype._AddFunction_Inline = function CAdd(item) { this.push(item); return this; }; // CAdd = ChainAdd
Array.prototype._AddFunction_Inline = function TAdd(item) { this.push(item); return item; }; // TAdd = TransparentAdd
Array.prototype._AddFunction_Inline = function AddRange(array) {
	for (var i in array)
		this.push(array[i]);
};
Array.prototype._AddFunction_Inline = function Remove(item) {
	/*for (var i = 0; i < this.length; i++)
		if (this[i] === item)
			return this.splice(i, 1);*/
	var itemIndex = this.indexOf(item);
	this.splice(itemIndex, 1);
};
Array.prototype._AddFunction_Inline = function RemoveAll(items) {
    for (let item of items)
        this.Remove(item);
};
Array.prototype._AddFunction_Inline = function RemoveAt(index) { return this.splice(index, 1)[0]; };
Array.prototype._AddFunction_Inline = function Insert(index, obj) { this.splice(index, 0, obj); }

Object.prototype._AddFunction_Inline = function AsRef() { return new NodeReference_ByPath(this); }

// Linq replacements
// ----------

Array.prototype._AddFunction_Inline = function Any(matchFunc) {
    for (let [index, item] of this.entries())
        if (matchFunc == null || matchFunc.call(item, item, index))
            return true;
    return false;
};
Array.prototype._AddFunction_Inline = function All(matchFunc) {
    for (let [index, item] of this.entries())
        if (!matchFunc.call(item, item, index))
            return false;
    return true;
};
Array.prototype._AddFunction_Inline = function Where(matchFunc) {
	var result = this instanceof List ? new List(this.itemType) : [];
	for (let [index, item] of this.entries())
		if (matchFunc.call(item, item, index)) // call, having the item be "this", as well as the first argument
			result.Add(item);
	return result;
};
Array.prototype._AddFunction_Inline = function Select(selectFunc) {
	var result = this instanceof List ? new List(this.itemType) : [];
	for (let [index, item] of this.entries())
		result.Add(selectFunc.call(item, item, index));
	return result;
};
Array.prototype._AddFunction_Inline = function SelectMany(selectFunc) {
	var result = this instanceof List ? new List(this.itemType) : [];
	for (let [index, item] of this.entries())
		result.AddRange(selectFunc.call(item, item, index));
	return result;
};
//Array.prototype._AddFunction_Inline = function Count(matchFunc) { return this.Where(matchFunc).length; };
//Array.prototype._AddFunction_Inline = function Count(matchFunc) { return this.Where(matchFunc).length; }; // needed for items to be added properly to custom classes that extend Array
Array.prototype._AddGetter_Inline = function Count() { return this.length; }; // needed for items to be added properly to custom classes that extend Array
Array.prototype._AddFunction_Inline = function VCount(matchFunc) { return this.Where(matchFunc).length; };
Array.prototype._AddFunction_Inline = function Clear() {
	/*while (this.length > 0)
		this.pop();*/
    this.splice(0, this.length);
};
Array.prototype._AddFunction_Inline = function First(matchFunc = null) {
    if (matchFunc) {
        for (var item of this)
            if (matchFunc.call(item, item))
                return item;
        return null;
    }
    return this[0];
};
//Array.prototype._AddFunction_Inline = function FirstWithPropValue(propName, propValue) { return this.Where(function() { return this[propName] == propValue; })[0]; };
Array.prototype._AddFunction_Inline = function FirstWith(propName, propValue) { return this.Where(function() { return this[propName] == propValue; })[0]; };
Array.prototype._AddFunction_Inline = function Last(matchFunc = null) {
	if (matchFunc) {
        for (var i = this.length - 1; i >= 0; i--)
            if (matchFunc.call(this[i], this[i]))
                return this[i];
        return null;
    }
    return this[this.length - 1];
};
Array.prototype._AddFunction_Inline = function XFromLast(x) { return this[(this.length - 1) - x]; };

// since JS doesn't have basic 'foreach' system
Array.prototype._AddFunction_Inline = function ForEach(func) {
	for (var i in this)
		func.call(this[i], this[i], i); // call, having the item be "this", as well as the first argument
};

Array.prototype._AddFunction_Inline = function Move(item, newIndex) {
	var oldIndex = this.indexOf(item);
	this.RemoveAt(oldIndex);
	if (oldIndex < newIndex) // new-index is understood to be the position-in-list to move the item to, as seen before the item started being moved--so compensate for remove-from-old-position list modification
		newIndex--;
	this.Insert(newIndex, item);
};

Array.prototype._AddFunction_Inline = function ToList(itemType = null) {
	if (this instanceof List)
		return List.apply(null, [itemType || "object"].concat(this));
    return [].concat(this);
}
Array.prototype._AddFunction_Inline = function ToDictionary(keyFunc, valFunc) {
	var result = new Dictionary();
	for (var i in this)
		result.Add(keyFunc(this[i]), valFunc(this[i]));
	return result;
}
Array.prototype._AddFunction_Inline = function ToMap(keyFunc, valFunc) {
	var result = {};
	for (let item of this)
		result[keyFunc(item)] = valFunc(item);
	return result;
}
Array.prototype._AddFunction_Inline = function Skip(count) {
	var result = [];
	for (var i = count; i < this.length; i++)
		result.push(this[i]);
	return result;
};
Array.prototype._AddFunction_Inline = function Take(count) {
	var result = [];
	for (var i = 0; i < count && i < this.length; i++)
		result.push(this[i]);
	return result;
};
Array.prototype._AddFunction_Inline = function FindIndex(matchFunc) {
	for (var i in this)
		if (matchFunc.call(this[i], this[i])) // call, having the item be "this", as well as the first argument
			return i;
	return -1;
};
/*Array.prototype._AddFunction_Inline = function FindIndex(matchFunc) {
    for (let [index, item] of this.entries())
        if (matchFunc.call(item, item))
            return index;
    return -1;
};*/
Array.prototype._AddFunction_Inline = function OrderBy(valFunc) {
	/*var temp = this.ToList();
	temp.sort((a, b)=>V.Compare(valFunc(a), valFunc(b)));
	return temp;*/
    return V.StableSort(this, (a, b)=>V.Compare(valFunc(a), valFunc(b)));
};
Array.prototype._AddFunction_Inline = function Distinct() {
	var result = [];
	for (var i in this)
		if (!result.Contains(this[i]))
			result.push(this[i]);
	return result;
};
Array.prototype._AddFunction_Inline = function Except(otherArray) {
    return this.Where(a=>!otherArray.Contains(a));
};

//Array.prototype._AddFunction_Inline = function JoinUsing(separator) { return this.join(separator);};
Array.prototype._AddFunction_Inline = function Min(valFunc) {
    return this.OrderBy(valFunc).First();
};
Array.prototype._AddFunction_Inline = function Max(valFunc) {
    return this.OrderBy(valFunc).Last();
};

// like Pairs for Dictionary, except for Array (why is this on Object.prototype instead of Array.prototype?)
Object.prototype._AddGetter_Inline = function Entries() {
	var result = [];
	for (var i in this)
		if (IsNumberString(i))
			result.push({index: parseInt(i), value: this[i]});
	return result;
};

// ArrayIterator
// ==========

/*var ArrayIterator = [].entries().constructor;
ArrayIterator.prototype._AddFunction_Inline = function ToArray() {
    return Array.from(this);
};*/

// [offset construct] (e.g. {left: 10, top: 10})
// ==========

Object.prototype._AddFunction_Inline = function plus(offset) { return { left: this.left + offset.left, top: this.top + offset.top }; };