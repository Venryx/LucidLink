//@_VDFTypeInfo(new VDFType("^(?!_)(?!s$)(?!root$)"))
export default class Node {
	// maybe temp
	// todo: make this use an actual class
	// todo: make this use getters and setters
	get p(): this {
		var result: any = {};
		for (var key in this) {
			result[key] = {node: this, key, value: this[key]};
		}
		return result;
	}
}
//Node.typeInfo = new VDFTypeInfo(new VDFType("^(?!_)(?!s$)(?!root$)"));