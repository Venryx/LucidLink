// standard types
// ----------

/*export class bool extends Boolean {}
export class int extends Number {}
export class double extends Number {}
export var string = "string" as any as (new(..._)=>string);*/

export var bool = ()=>"bool" as any as (new(..._)=>boolean);
export var int = ()=>"int" as any as (new(..._)=>number);
export var double = ()=>"double" as any as (new(..._)=>number);
export var string = ()=>"string" as any as (new(..._)=>string);

export function IsNaN(obj) { return typeof obj == "number" && obj != obj; }
export function IsPrimitive(obj) { return IsBool(obj) || IsNumber(obj) || IsString(obj); }
export function IsBool(obj) : obj is boolean { return typeof obj == "boolean"; } //|| obj instanceof Boolean
export function ToBool(boolStr) { return boolStr == "true" ? true : false; }

export function IsNumber(obj, allowNumberObj = false) : obj is number {
	return typeof obj == "number" || (allowNumberObj && obj instanceof Number);
}
export function IsNumberString(obj) { return IsString(obj) && parseInt(obj).toString() == obj; }
export function IsInt(obj) : obj is number { return typeof obj == "number" && parseFloat(obj as any) == parseInt(obj as any); }
export function ToInt(stringOrFloatVal) { return parseInt(stringOrFloatVal); }
export function IsDouble(obj) : obj is number { return typeof obj == "number" && parseFloat(obj as any) != parseInt(obj as any); }
export function ToDouble(stringOrIntVal) { return parseFloat(stringOrIntVal); }

export function IsString(obj, allowStringObj = false) : obj is string {
	return typeof obj == "string" || (allowStringObj && obj instanceof String);
}
export function ToString(val) { return "" + val; }

export function IsConstructor(obj) : obj is new(..._)=>any {
	return obj instanceof Function && obj.name;
}