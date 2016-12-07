// Moment
// ==========

import Moment from "moment";
Moment.prototype._AddGetter_Inline = function C() {
	return this.clone();
}