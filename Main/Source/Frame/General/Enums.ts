import {VDFNode} from "../../Packages/VDF/VDFNode";
import {_VDFSerialize, VDFDeserialize} from "../../Packages/VDF/VDFTypeInfo";
import {ToJSON} from "../Globals";

// maybe temp (maybe instead find way to have TypeScript enums work well)
export class Enum {
	static Deserialize; // attached using @Enum decorator
	static _IsEnum = 0; // mimic odd enum marker/flag, used by TypeScript

	static entries: Enum[];
	static names: string[];
	static values: number[];
	static options: {name, value}[];

	constructor(name: string, value: number) {
		//s.realTypeName = enumTypeName; // old: maybe temp; makes-so VDF system recognizes enumValues as of this enumType
		this.name = name;
		this.value = value;
	}

	name: string;
	//get N() { return this.name; } // helper for use in indexer
	/** ie index */ value: number;

	@_VDFSerialize() Serialize() { return new VDFNode(this.name, this.constructor.GetName()); }
	toString() { return this.name; };
	//valueOf() { return s.value; }; // currently removed, since overrides toString for to-primitive use, thus disabling the "player.age == 'Rodent'" functionality
}
export function _Enum(target: any) {
	var typeName = target.GetName();
	// for now at least, auto-add enum as global, since enums are types and VDF system needs types to be global
	g[typeName] = target;

	// extends class itself
	target.Deserialize = function(node) { return target[node.primitiveValue]; }.AddTags(new VDFDeserialize(true));
	target.V = new target("enum root");
	//target.name = enumTypeName;
	//target.realTypeName = enumTypeName;
	//target._IsEnum = 0; // mimic odd enum marker/flag, used by TypeScript

	// add enum entries
	//var names = Object.getOwnPropertyNames(target.prototype);
	var names = (Object.getOwnPropertyNames(new target()) as any).Except("name", "value");
	var index = -1;
	for (let name of names) {
		let entry = new (target as any)(name, ++index);
		// make accessible by MyEnum.MyEntry
		target[name] = entry;
		// make accessible by MyEnum.V.MyEntry
		target.V[name] = entry;
	}

	target.names = names;
	target.entries = target.names.Select(name=>target[name]);
	target.values = target.entries.Select(a=>a.value);
	target.options = target.entries.Select(a=>({name: a.name, value: a}));
}
/*export function EV(): Enum {
	return {} as any; // real value gets set later
}
export function EV<T>() : T {
	return T;
}*/
/*export function EH(type, propName) {
	type[propName] = new type("enum root");
}*/