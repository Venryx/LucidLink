// vdf globals
// ==========
var g = eval("global");
g.Log = g.Log || console.log;
g.Assert = g.Assert || (function (condition, message) {
    if (condition)
        return;
    //console.assert(false, message || "");
	alert("Assert failed) " + (message || ""));
    debugger;
});
// the below lets you easily add non-enumerable properties
Object.defineProperty(Object.prototype, "_AddProperty", {
    enumerable: false,
    value: function (name, value) {
        Object.defineProperty(this, name, {
            enumerable: false,
            value: value
        });
    }
});
String.prototype._AddProperty("Contains", function (str) { return this.indexOf(str) != -1; });
String.prototype._AddProperty("StartsWith", function (str) { return this.indexOf(str) == 0; });
String.prototype._AddProperty("EndsWith", function (str) {
    var expectedPos = this.length - str.length;
    return this.indexOf(str, expectedPos) == expectedPos;
});
String.prototype._AddProperty("TrimStart", function (chars) {
    var result = "";
    var doneTrimming = false;
    for (var i = 0; i < this.length; i++)
        if (!chars.Contains(this[i]) || doneTrimming) {
            result += this[i];
            doneTrimming = true;
        }
    return result;
});
if (!Array.prototype["Contains"])
    Array.prototype._AddProperty("Contains", function (item) { return this.indexOf(item) != -1; });
if (!Array.prototype["Where"])
    Array.prototype._AddProperty("Where", function (matchFunc) {
        if (matchFunc === void 0) { matchFunc = (function () { return true; }); }
        var result = this instanceof List ? new List(this.itemType) : [];
        for (var _i = 0, _a = this; _i < _a.length; _i++) {
            var item = _a[_i];
            if (matchFunc.call(item, item))
                result[this instanceof List ? "Add" : "push"](item);
        }
        return result;
    });
if (!Array.prototype["First"])
    Array.prototype._AddProperty("First", function (matchFunc) {
        if (matchFunc === void 0) { matchFunc = (function () { return true; }); }
        return this.Where(matchFunc)[0];
    });
Function.prototype._AddProperty("AddTags", function () {
    var tags = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        tags[_i - 0] = arguments[_i];
    }
    if (this.tags == null)
        this.tags = new List("object");
    for (var i = 0; i < tags.length; i++)
        this.tags.push(tags[i]);
    return this;
});
/*Function.prototype._AddProperty("IsDerivedFrom", function(baseType) {
    if (baseType == null)
        return false;
    var currentDerived = this.prototype;
    while (currentDerived.__proto__)	{
        if (currentDerived == baseType.prototype)
            return true;
        currentDerived = currentDerived.__proto__;
    }
    return false;
});*/
// classes
// ==========
g.VDFNodePathNode = (function () {
    function VDFNodePathNode(obj, prop, list_index, map_keyIndex, map_key) {
        if (obj === void 0) { obj = null; }
        if (prop === void 0) { prop = null; }
        if (list_index === void 0) { list_index = null; }
        if (map_keyIndex === void 0) { map_keyIndex = null; }
        if (map_key === void 0) { map_key = null; }
        this.list_index = null;
        this.map_keyIndex = null;
        this.obj = obj;
        this.prop = prop;
        this.list_index = list_index;
        this.map_keyIndex = map_keyIndex;
        this.map_key = map_key;
    }
    //Clone(): VDFNodePathNode { return new VDFNodePathNode(this.obj, this.prop, this.list_index, this.map_keyIndex, this.map_key); }
    // for debugging
    VDFNodePathNode.prototype.toString = function () {
        if (this.list_index != null)
            return "i:" + this.list_index;
        if (this.map_keyIndex != null)
            return "ki:" + this.map_keyIndex;
        if (this.map_key != null)
            return "k:" + this.map_key;
        if (this.prop != null)
            //return "p:" + this.prop.name;
            return this.prop.name;
        return "";
    };
    return VDFNodePathNode;
}());
g.VDFNodePath = (function () {
    function VDFNodePath(nodes_orRootNode) {
        this.nodes = nodes_orRootNode instanceof Array ? nodes_orRootNode : [nodes_orRootNode];
    }
    Object.defineProperty(VDFNodePath.prototype, "rootNode", {
        get: function () { return this.nodes[0]; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(VDFNodePath.prototype, "parentNode", {
        get: function () { return this.nodes.length >= 2 ? this.nodes[this.nodes.length - 2] : null; },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(VDFNodePath.prototype, "currentNode", {
        get: function () { return this.nodes[this.nodes.length - 1]; },
        enumerable: true,
        configurable: true
    });
    VDFNodePath.prototype.ExtendAsListItem = function (index, obj) {
        var newNodes = this.nodes.slice(0);
        newNodes.push(new VDFNodePathNode(obj, null, index));
        return new VDFNodePath(newNodes);
    };
    VDFNodePath.prototype.ExtendAsMapKey = function (keyIndex, obj) {
        var newNodes = this.nodes.slice(0);
        newNodes.push(new VDFNodePathNode(obj, null, null, keyIndex));
        return new VDFNodePath(newNodes);
    };
    VDFNodePath.prototype.ExtendAsMapItem = function (key, obj) {
        var newNodes = this.nodes.slice(0);
        newNodes.push(new VDFNodePathNode(obj, null, null, null, key));
        return new VDFNodePath(newNodes);
    };
    VDFNodePath.prototype.ExtendAsChild = function (prop, obj) {
        var newNodes = this.nodes.slice(0);
        newNodes.push(new VDFNodePathNode(obj, prop));
        return new VDFNodePath(newNodes);
    };
    // for debugging
    VDFNodePath.prototype.toString = function () {
        var result = "";
        var index = 0;
        for (var _i = 0, _a = this.nodes; _i < _a.length; _i++) {
            var node = _a[_i];
            result += (index++ == 0 ? "" : "/") + node;
        }
        return result;
    };
    return VDFNodePath;
}());
g.VDF = (function () {
    function VDF() {
    }
    // v-name examples: "List(string)", "System.Collections.Generic.List(string)", "Dictionary(string string)"
    VDF.GetGenericArgumentsOfType = function (typeName) {
        var genericArgumentTypes = new Array(); //<string[]>[];
        var depth = 0;
        var lastStartBracketPos = -1;
        if (typeName != null)
            for (var i = 0; i < typeName.length; i++) {
                var ch = typeName[i];
                if (ch == ')')
                    depth--;
                if ((depth == 0 && ch == ')') || (depth == 1 && ch == ' '))
                    genericArgumentTypes.push(typeName.substring(lastStartBracketPos + 1, i)); // get generic-parameter type-str
                if ((depth == 0 && ch == '(') || (depth == 1 && ch == ' '))
                    lastStartBracketPos = i;
                if (ch == '(')
                    depth++;
            }
        return genericArgumentTypes;
    };
    VDF.IsTypeXDerivedFromY = function (xTypeName, yTypeName) {
        if (xTypeName == null || yTypeName == null || window[xTypeName] == null || window[yTypeName] == null)
            return false;
        var currentDerived = window[xTypeName].prototype;
        while (currentDerived.__proto__) {
            if (currentDerived == window[yTypeName].prototype)
                return true;
            currentDerived = currentDerived.__proto__;
        }
        return false;
    };
    VDF.GetIsTypePrimitive = function (typeName) { return ["byte", "sbyte", "short", "ushort", "int", "uint", "long", "ulong", "float", "double", "decimal", "bool", "char", "string"].Contains(typeName); };
    VDF.GetIsTypeAnonymous = function (typeName) { return typeName != null && typeName == "object"; };
    VDF.GetTypeNameOfObject = function (obj) {
        var rawType = typeof obj;
        if (rawType == "object") {
            if (obj.realTypeName)
                return obj.realTypeName;
            if (obj.itemType)
                return "List(" + obj.itemType + ")";
            var nativeTypeName = obj.constructor.name_fake || obj.constructor.name || null;
            if (nativeTypeName == "Boolean")
                return "bool";
            if (nativeTypeName == "Number")
                return obj.toString().Contains(".") ? "double" : "int";
            if (nativeTypeName == "String")
                return "string";
            if (nativeTypeName == "Object")
                return "object";
            if (nativeTypeName == "Array")
                return "List(object)";
            return nativeTypeName;
        }
        if (rawType == "boolean")
            return "bool";
        if (rawType == "number")
            return obj.toString().Contains(".") ? "double" : "int";
        if (rawType == "string")
            return "string";
        //return rawType; // string
        //return null;
        //return "object"; // consider objects with raw-types of undefined, function, etc. to just be anonymous-objects
        return "object"; // consider everything else to be an anonymous-object
    };
    VDF.GetTypeNameRoot = function (typeName) { return typeName != null && typeName.Contains("(") ? typeName.substr(0, typeName.indexOf("(")) : typeName; };
    VDF.GetClassProps = function (type, allowGetFromCache) {
        if (allowGetFromCache === void 0) { allowGetFromCache = true; }
        if (type == null)
            return {};
        if (!type.hasOwnProperty("classPropsCache") || type.allowPropsCache === false || !allowGetFromCache) {
            var result = {};
            var currentType = type;
            while (currentType && currentType != Object) {
                // get static props on constructor itself
                for (var _i = 0, _a = Object.getOwnPropertyNames(currentType); _i < _a.length; _i++) {
                    var propName = _a[_i];
                    if (propName in result)
                        continue;
                    var propInfo = Object.getOwnPropertyDescriptor(currentType, propName);
                    // don't include if prop is a getter or setter func (causes problems when enumerating)
                    if (propInfo == null || (propInfo.get == null && propInfo.set == null))
                        result[propName] = currentType[propName];
                }
                // get "real" props on the prototype-object
                for (var _b = 0, _c = Object.getOwnPropertyNames(currentType.prototype); _b < _c.length; _b++) {
                    var propName = _c[_b];
                    if (propName in result)
                        continue;
                    var propInfo = Object.getOwnPropertyDescriptor(currentType.prototype, propName);
                    // don't include if prop is a getter or setter func (causes problems when enumerating)
                    if (propInfo == null || (propInfo.get == null && propInfo.set == null))
                        result[propName] = currentType.prototype[propName];
                }
                currentType = Object.getPrototypeOf(currentType.prototype).constructor;
            }
            type.classPropsCache = result;
        }
        return type.classPropsCache;
    };
    VDF.GetObjectProps = function (obj) {
        if (obj == null)
            return {};
        var result = {};
        for (var propName in VDF.GetClassProps(obj.constructor))
            result[propName] = null;
        for (var propName in obj)
            result[propName] = null;
        return result;
    };
    VDF.Serialize = function (obj, declaredTypeName_orOptions, options_orNothing) {
        if (declaredTypeName_orOptions instanceof VDFSaveOptions)
            return VDF.Serialize(obj, null, declaredTypeName_orOptions);
        var declaredTypeName = declaredTypeName_orOptions;
        var options = options_orNothing;
        return VDFSaver.ToVDFNode(obj, declaredTypeName, options).ToVDF(options);
    };
    VDF.Deserialize = function (vdf, declaredTypeName_orOptions, options_orNothing) {
        if (declaredTypeName_orOptions instanceof VDFLoadOptions)
            return VDF.Deserialize(vdf, null, declaredTypeName_orOptions);
        var declaredTypeName = declaredTypeName_orOptions;
        var options = options_orNothing;
        return VDFLoader.ToVDFNode(vdf, declaredTypeName, options).ToObject(declaredTypeName, options);
    };
    VDF.DeserializeInto = function (vdf, obj, options) { VDFLoader.ToVDFNode(vdf, VDF.GetTypeNameOfObject(obj), options).IntoObject(obj, options); };
    // for use with VDFSaveOptions
    VDF.AnyMember = "#AnyMember";
    VDF.AllMembers = ["#AnyMember"];
    // for use with VDFType
    VDF.PropRegex_Any = ""; //"^.+$";
    return VDF;
}());
// helper classes
// ==================
g.VDFUtils = (function () {
    function VDFUtils() {
    }
    /*static SetUpHiddenFields(obj, addSetters?: boolean, ...fieldNames) 	{
        if (addSetters && !obj._hiddenFieldStore)
            Object.defineProperty(obj, "_hiddenFieldStore", {enumerable: false, value: {}});
        for (var i in fieldNames)
            (()=>{
                var propName = fieldNames[i];
                var origValue = obj[propName];
                if (addSetters)
                    Object.defineProperty(obj, propName, 					{
                        enumerable: false,
                        get: ()=>obj["_hiddenFieldStore"][propName],
                        set: value=>obj["_hiddenFieldStore"][propName] = value
                    });
                else
                    Object.defineProperty(obj, propName, 					{
                        enumerable: false,
                        value: origValue //get: ()=>obj["_hiddenFieldStore"][propName]
                    });
                obj[propName] = origValue; // for 'hiding' a prop that was set beforehand
            })();
    }*/
    VDFUtils.MakePropertiesHidden = function (obj, alsoMakeFunctionsHidden, addSetters) {
        for (var propName in obj) {
            var propDescriptor = Object.getOwnPropertyDescriptor(obj, propName);
            if (propDescriptor) {
                propDescriptor.enumerable = false;
                Object.defineProperty(obj, propName, propDescriptor);
            }
        }
    };
    return VDFUtils;
}());
g.StringBuilder = (function () {
    function StringBuilder(startData) {
        this.Length = 0;
        if (startData)
            this.Append(startData);
    }
    StringBuilder.prototype.Append = function (str) { this.push(str); this.Length += str.length; return this; }; // adds string str to the StringBuilder
    StringBuilder.prototype.Insert = function (index, str) { this.splice(index, 0, str); this.Length += str.length; return this; }; // inserts string 'str' at 'index'
    StringBuilder.prototype.Remove = function (index, count) {
        var removedItems = this.splice(index, count != null ? count : 1);
        for (var i = 0; i < removedItems.length; i++)
            this.Length -= removedItems[i].length;
        return this;
    };
    StringBuilder.prototype.Clear = function () {
        //this.splice(0, this.length);
        this.length = 0;
        this.Length = 0;
    };
    StringBuilder.prototype.ToString = function (joinerString) { return this.join(joinerString || ""); }; // builds the string
    StringBuilder.prototype.toString = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.toLocaleString = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.push = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.pop = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.concat = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.join = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.reverse = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.shift = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.slice = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.sort = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.splice = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.unshift = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.indexOf = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.lastIndexOf = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.every = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.some = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.forEach = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.map = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.filter = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.reduce = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.reduceRight = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    // fakes for extended members
    StringBuilder.prototype.Contains = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.Where = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    StringBuilder.prototype.First = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i - 0] = arguments[_i];
        }
        return null;
    };
    return StringBuilder;
}());
(function () {
    StringBuilder.prototype["__proto__"] = Array.prototype; // makes "(new StringBuilder()) instanceof Array" be true
    var reachedFakes = false;
    for (var name in StringBuilder.prototype) {
        if (name == "toString")
            reachedFakes = true;
        if (reachedFakes)
            StringBuilder.prototype[name] = Array.prototype[name];
    }
})();
// tags
// ----------
g.PropDeclarationWrapper = (function () {
    function PropDeclarationWrapper() {
    }
    Object.defineProperty(PropDeclarationWrapper.prototype, "set", {
        set: function (value) { },
        enumerable: true,
        configurable: true
    });
    return PropDeclarationWrapper;
}());
// maybe make-so: this is renamed PropInfo
g.Prop = function(typeOrObj, propName, propType_orFirstTag) {
    var tags = [];
    for (var _i = 3; _i < arguments.length; _i++) {
        tags[_i - 3] = arguments[_i];
    }
    if (propType_orFirstTag != null && typeof propType_orFirstTag != "string")
        return Prop.apply(this, [typeOrObj, propName, null, propType_orFirstTag].concat(tags));
    var type = typeOrObj instanceof Function ? typeOrObj : typeOrObj.constructor;
    var propType = propType_orFirstTag;
    var typeInfo = VDFTypeInfo.Get(type);
    if (typeInfo.props[propName] == null)
        typeInfo.props[propName] = new VDFPropInfo(propName, propType, tags);
    return new PropDeclarationWrapper();
}
;
/*function MethodDeclarationWrapper(tags) { this.tags = tags; };
MethodDeclarationWrapper.prototype._AddSetter_Inline = function set(method) { method.methodInfo = new VDFMethodInfo(this.tags); };
function Method(...tags) { return new MethodDeclarationWrapper(tags); };*/
g.TypeDeclarationWrapper = function(tags) { this.tags = tags; }
;
TypeDeclarationWrapper.prototype._AddSetter_Inline = function set(type) {
    var s = this;
    type = type instanceof Function ? type : type.constructor;
    var typeInfo = VDFTypeInfo.Get(type.name_fake || type.name);
    var typeTag = {};
    for (var i in s.tags)
        if (s.tags[i] instanceof VDFType)
            typeTag = s.tags[i];
    typeInfo.tags = s.tags;
    typeInfo.typeTag.AddDataOf(typeTag);
};
g.TypeInfo = function() {
    var tags = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        tags[_i - 0] = arguments[_i];
    }
    return new TypeDeclarationWrapper(tags);
}
;
// VDF-usable data wrappers
// ==========
//class object {} // for use with VDF.Deserialize, to deserialize to an anonymous object
// for anonymous objects (JS anonymous-objects are all just instances of Object, so we don't lose anything by attaching type-info to the shared constructor)
//var object = Object;
//object["typeInfo"] = new VDFTypeInfo(null, true);
g.object = (function () {
    function object() {
    }
    return object;
}()); // just an alias for Object, to be consistent with C# version
g.EnumValue = (function () {
    function EnumValue(enumTypeName, intValue) {
        this.realTypeName = enumTypeName;
        //this.intValue = intValue;
        this.stringValue = EnumValue.GetEnumStringForIntValue(enumTypeName, intValue);
    }
    EnumValue.prototype.toString = function () { return this.stringValue; };
    EnumValue.IsEnum = function (typeName) { return window[typeName] && window[typeName]["_IsEnum"] === 0; };
    //static IsEnum(typeName: string): boolean { return window[typeName] && /}\)\((\w+) \|\| \(\w+ = {}\)\);/.test(window[typeName].toString()); }
    EnumValue.GetEnumIntForStringValue = function (enumTypeName, stringValue) { return eval(enumTypeName + "[\"" + stringValue + "\"]"); };
    EnumValue.GetEnumStringForIntValue = function (enumTypeName, intValue) { return eval(enumTypeName + "[" + intValue + "]"); };
    return EnumValue;
}());
g["List"] = function List(itemType) {
    var items = [];
    for (var _i = 1; _i < arguments.length; _i++) {
        items[_i - 1] = arguments[_i];
    }
    var s = Object.create(Array.prototype);
    s = (Array.apply(s, items) || s);
    s["__proto__"] = List.prototype; // makes "(new List()) instanceof List" be true
    //self.constructor = List; // makes "(new List()).constructor == List" be true
    //Object.defineProperty(self, "constructor", {enumerable: false, value: List});
    //self.realTypeName = "List(" + itemType + ")";
    //Object.defineProperty(self, "realTypeName", {enumerable: false, value: "List(" + itemType + ")"});
    //self.itemType = itemType;
    Object.defineProperty(s, "itemType", { enumerable: false, value: itemType });
    return s;
};
(function () {
    var s = List.prototype;
    s["__proto__"] = Array.prototype; // makes "(new List()) instanceof Array" be true
    // new properties
    Object.defineProperty(s, "Count", { enumerable: false, get: function () { return this.length; } });
    // new methods
    /*s.Indexes = function () {
        var result = {};
        for (var i = 0; i < this.length; i++)
            result[i] = this[i];
        return result;
    };*/
    s.Add = function () {
        var items = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            items[_i - 0] = arguments[_i];
        }
        return this.push.apply(this, items);
    };
    s.AddRange = function (items) {
        for (var i = 0; i < items.length; i++)
            this.push(items[i]);
    };
    s.Insert = function (index, item) { return this.splice(index, 0, item); };
    s.InsertRange = function (index, items) { return this.splice.apply(this, [index, 0].concat(items)); };
    s.Remove = function (item) { this.RemoveAt(this.indexOf(item)); };
    s.RemoveAt = function (index) { this.splice(index, 1); };
    s.RemoveRange = function (index, count) { return this.splice(index, count); };
    s.Any = function (matchFunc) {
        for (var _i = 0, _a = this; _i < _a.length; _i++) {
            var item = _a[_i];
            if (matchFunc.call(item, item))
                return true;
        }
        return false;
    };
    s.All = function (matchFunc) {
        for (var _i = 0, _a = this; _i < _a.length; _i++) {
            var item = _a[_i];
            if (!matchFunc.call(item, item))
                return false;
        }
        return true;
    };
    s.Select = function (selectFunc, itemType) {
        var result = new List(itemType || "object");
        for (var _i = 0, _a = this; _i < _a.length; _i++) {
            var item = _a[_i];
            result.Add(selectFunc.call(item, item));
        }
        return result;
    };
    s.First = function (matchFunc) {
        var result = this.FirstOrDefault(matchFunc);
        if (result == null)
            throw new Error("Matching item not found.");
        return result;
    };
    s.FirstOrDefault = function (matchFunc) {
        if (matchFunc) {
            for (var _i = 0, _a = this; _i < _a.length; _i++) {
                var item = _a[_i];
                if (matchFunc.call(item, item))
                    return item;
            }
            return null;
        }
        else
            return this[0];
    };
    s.Last = function (matchFunc) {
        var result = this.LastOrDefault(matchFunc);
        if (result == null)
            throw new Error("Matching item not found.");
        return result;
    };
    s.LastOrDefault = function (matchFunc) {
        if (matchFunc) {
            for (var i = this.length - 1; i >= 0; i--)
                if (matchFunc.call(this[i], this[i]))
                    return this[i];
            return null;
        }
        else
            return this[this.length - 1];
    };
    s.GetRange = function (index, count) {
        var result = new List(this.itemType);
        for (var i = index; i < index + count; i++)
            result.Add(this[i]);
        return result;
    };
    s.Contains = function (item) { return this.indexOf(item) != -1; };
    VDFUtils.MakePropertiesHidden(s, true);
})();
g.Dictionary = (function () {
    function Dictionary(keyType, valueType, keyValuePairsObj) {
        //VDFUtils.SetUpHiddenFields(this, true, "realTypeName", "keyType", "valueType", "keys", "values");
        this.realTypeName = "Dictionary(" + keyType + " " + valueType + ")";
        this.keyType = keyType;
        this.valueType = valueType;
        this.keys = [];
        this.values = [];
        if (keyValuePairsObj)
            for (var key in keyValuePairsObj)
                this.Set(key, keyValuePairsObj[key]);
    }
    Object.defineProperty(Dictionary.prototype, "Keys", {
        // properties
        get: function () {
            var result = {};
            for (var i = 0; i < this.keys.length; i++)
                result[this.keys[i]] = null;
            return result;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Dictionary.prototype, "Pairs", {
        get: function () {
            var result = [];
            for (var i = 0; i < this.keys.length; i++)
                result.push({ index: i, key: this.keys[i], value: this.values[i] });
            return result;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Dictionary.prototype, "Count", {
        get: function () { return this.keys.length; },
        enumerable: true,
        configurable: true
    });
    // methods
    Dictionary.prototype.ContainsKey = function (key) { return this.keys.indexOf(key) != -1; };
    Dictionary.prototype.Get = function (key) { return this.values[this.keys.indexOf(key)]; };
    Dictionary.prototype.Set = function (key, value) {
        if (this.keys.indexOf(key) == -1)
            this.keys.push(key);
        this.values[this.keys.indexOf(key)] = value;
        if (typeof key == "string")
            this[key] = value; // make value accessible directly on Dictionary object
    };
    Dictionary.prototype.Add = function (key, value) {
        if (this.keys.indexOf(key) != -1)
            throw new Error("Dictionary already contains key '" + key + "'.");
        this.Set(key, value);
    };
    Dictionary.prototype.Remove = function (key) {
        var itemIndex = this.keys.indexOf(key);
        if (itemIndex == -1)
            return;
        this.keys.splice(itemIndex, 1);
        this.values.splice(itemIndex, 1);
        delete this[key];
    };
    return Dictionary;
}());
//VDFUtils.MakePropertiesHidden(Dictionary.prototype, true); 
//# sourceMappingURL=VDF.js.map