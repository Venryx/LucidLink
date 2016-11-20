var g = global;
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
g.VDFType = (function () {
    function VDFType(propIncludeRegexL1, popOutL1) {
        this.propIncludeRegexL1 = propIncludeRegexL1;
        this.popOutL1 = popOutL1;
    }
    VDFType.prototype.AddDataOf = function (typeTag) {
        if (typeTag.propIncludeRegexL1 != null)
            this.propIncludeRegexL1 = typeTag.propIncludeRegexL1;
        if (typeTag.popOutL1 != null)
            this.popOutL1 = typeTag.popOutL1;
    };
    return VDFType;
}());
g.VDFTypeInfo = (function () {
    function VDFTypeInfo() {
        this.props = {};
    }
    VDFTypeInfo.Get = function (type_orTypeName) {
        //var type = type_orTypeName instanceof Function ? type_orTypeName : window[type_orTypeName];
        var typeName = type_orTypeName instanceof Function ? type_orTypeName.name : type_orTypeName;
        var typeNameBase = typeName.Contains("(") ? typeName.substr(0, typeName.indexOf("(")) : typeName;
        if (VDF.GetIsTypeAnonymous(typeNameBase)) {
            var result = new VDFTypeInfo();
            result.typeTag = new VDFType(VDF.PropRegex_Any);
            return result;
        }
        var typeBase = type_orTypeName instanceof Function ? type_orTypeName : window[typeNameBase];
        /*if (typeBase == null)
            throw new Error("Could not find constructor for type: " + typeNameBase);*/
        if (typeBase && !typeBase.hasOwnProperty("typeInfo")) {
            var result = new VDFTypeInfo();
            result.typeTag = new VDFType();
            var currentType = typeBase;
            while (currentType != null) {
                var currentTypeInfo = currentType.typeInfo;
                // load type-tag from base-types
                var typeTag2 = (currentTypeInfo || {}).typeTag;
                for (var key in typeTag2)
                    if (result.typeTag[key] == null)
                        result.typeTag[key] = typeTag2[key];
                // load prop-info from base-types
                if (currentTypeInfo)
                    for (var propName in currentTypeInfo.props)
                        result.props[propName] = currentTypeInfo.props[propName];
                currentType = currentType.prototype && currentType.prototype.__proto__ && currentType.prototype.__proto__.constructor;
            }
            typeBase.typeInfo = result;
        }
        return typeBase && typeBase.typeInfo;
    };
    VDFTypeInfo.prototype.GetProp = function (propName) {
        if (!(propName in this.props))
            this.props[propName] = new VDFPropInfo(propName, null, []);
        return this.props[propName];
    };
    return VDFTypeInfo;
}());
g.VDFProp = (function () {
    function VDFProp(includeL2, popOutL2) {
        if (includeL2 === void 0) { includeL2 = true; }
        this.includeL2 = includeL2;
        this.popOutL2 = popOutL2;
    }
    return VDFProp;
}());
/*g.P = (function (_super) {
    __extends(P, _super);
    function P(includeL2, popOutL2) {
        if (includeL2 === void 0) { includeL2 = true; }
        _super.call(this, includeL2, popOutL2);
    }
    return P;
}(VDFProp));*/
g.DefaultValue = (function () {
    function DefaultValue(defaultValue) {
        if (defaultValue === void 0) { defaultValue = D.DefaultDefault; }
        this.defaultValue = defaultValue;
    }
    return DefaultValue;
}());
g.D = (function (_super) {
    __extends(D, _super);
    function D(defaultValue) {
        if (defaultValue === void 0) { defaultValue = D.DefaultDefault; }
        _super.call(this, defaultValue);
    }
    //static NoDefault = new object(); // i.e. the prop has no default, so whatever value it has is always saved [commented out, since: if you want no default, just don't add the D tag]
    D.DefaultDefault = new object(); // i.e. the default value for the type (not the prop) ['false' for a bool, etc.]
    D.NullOrEmpty = new object(); // i.e. null, or an empty string or collection
    D.Empty = new object(); // i.e. an empty string or collection
    return D;
}(DefaultValue));
g.VDFPropInfo = (function () {
    function VDFPropInfo(propName, propTypeName, tags) {
        this.name = propName;
        this.typeName = propTypeName;
        this.tags = tags;
        this.propTag = tags.First(function (a) { return a instanceof VDFProp; });
        this.defaultValueTag = tags.First(function (a) { return a instanceof DefaultValue; });
    }
    VDFPropInfo.prototype.AddTags = function () {
        var tags = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            tags[_i - 0] = arguments[_i];
        }
        (_a = this.tags).push.apply(_a, tags);
        this.propTag = tags.First(function (a) { return a instanceof VDFProp; });
        this.defaultValueTag = tags.First(function (a) { return a instanceof DefaultValue; });
        var _a;
    };
    VDFPropInfo.prototype.ShouldValueBeSaved = function (val) {
        //if (this.defaultValueTag == null || this.defaultValueTag.defaultValue == D.NoDefault)
        if (this.defaultValueTag == null)
            return true;
        if (this.defaultValueTag.defaultValue == D.DefaultDefault) {
            if (val == null)
                return false;
            if (val === false || val === 0)
                return true;
        }
        if (this.defaultValueTag.defaultValue == D.NullOrEmpty && val === null)
            return false;
        if (this.defaultValueTag.defaultValue == D.NullOrEmpty || this.defaultValueTag.defaultValue == D.Empty) {
            var typeName = VDF.GetTypeNameOfObject(val);
            if (typeName && typeName.startsWith("List(") && val.length == 0)
                return false;
            if (typeName == "string" && !val.length)
                return false;
        }
        if (val === this.defaultValueTag.defaultValue)
            return false;
        return true;
    };
    return VDFPropInfo;
}());
g.VDFSerializeProp = (function () {
    function VDFSerializeProp() {
    }
    return VDFSerializeProp;
}());
g.VDFDeserializeProp = (function () {
    function VDFDeserializeProp() {
    }
    return VDFDeserializeProp;
}());
g.VDFPreSerialize = (function () {
    function VDFPreSerialize() {
    }
    return VDFPreSerialize;
}());
g.VDFSerialize = (function () {
    function VDFSerialize() {
    }
    return VDFSerialize;
}());
g.VDFPostSerialize = (function () {
    function VDFPostSerialize() {
    }
    return VDFPostSerialize;
}());
g.VDFPreDeserialize = (function () {
    function VDFPreDeserialize() {
    }
    return VDFPreDeserialize;
}());
g.VDFDeserialize = (function () {
    function VDFDeserialize(fromParent) {
        if (fromParent === void 0) { fromParent = false; }
        this.fromParent = fromParent;
    }
    return VDFDeserialize;
}());
g.VDFPostDeserialize = (function () {
    function VDFPostDeserialize() {
    }
    return VDFPostDeserialize;
}());
/*class VDFMethodInfo {
    tags: any[];
    constructor(tags: any[]) { this.tags = tags; }
}*/ 
//# sourceMappingURL=VDFTypeInfo.js.map