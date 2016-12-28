import {Event} from "../Session";
import {A} from "../../../Frame/Globals";

export class EventRenderInfo {
	color: string;
	text: string;
}

export class GraphOverlay {
	constructor(info) {
		this.events = A.NonNull = info.events as string[];
		this.renderEvent = A.NonNull = info.renderEvent;
	}

	events: string[];
	renderEvent: (event: Event)=>EventRenderInfo;
}