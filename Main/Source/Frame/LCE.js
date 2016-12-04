// Moment
// ==========

import Moment from "moment";
Moment.prototype.Clone = function() {
	return Moment(this);
}
Moment.prototype.AddingMonths = function(amount) {
	var clone = this.Clone();
	clone.add({month: amount});
	return clone;
}