interface Number { Distance: (this: number, other: number)=>number; }
Number.prototype._AddFunction_Inline = function Distance(other) {
	return Math.abs(this - other);
};