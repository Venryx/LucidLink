import {Event} from "../Session";
import {GraphRow, Segment} from "./GraphRow";
import {BaseComponent as Component, Row, Panel} from "../../../Frame/ReactGlobals";
import moment from "moment";
import {View, Text} from "react-native";
import {VRect} from "../../../Frame/Graphics/VectorStructs";
import {GraphOverlay, EventRenderInfo} from "./GraphOverlay";
import {Assert, Notify, WaitXThenRun} from "../../../Frame/Globals";

export default class GraphOverlayUI extends Component<
		{startTime: moment.Moment, endTime: moment.Moment, width: number, height: number, events: Event[], overlay: GraphOverlay}, {}> {
	render() {
		var {startTime, endTime, width, height, events, overlay} = this.props;
		
		this.eventBoxes = [];
		return (
			<View {...{} as any} style={{position: "absolute", left: 0, top: 0, width, height}}>
				{events.map((event, index)=> {
					let renderInfo = overlay.renderEvent(event);
					if (renderInfo == null) return null;

					var percentFromLeftToRight = event.date.diff(startTime) / endTime.diff(startTime);
					var x = percentFromLeftToRight * width;

					var compCache = null;
					return <EventBox key={index}
						ref={comp=> {
							if (comp)
								this.eventBoxes.push(comp)
							else
								this.eventBoxes.Remove(compCache);
							compCache = comp;
						}}
						parent={this} rowHeight={height}
						{...{overlay, event, x, renderInfo}}/>;
				})}
			</View>
		);
	}

	eventBoxes: EventBox[] = [];
	PostRender() {
	//OnLayout() {
		// if some of the event-boxes aren't ready, wait 1 second then retry
		if (this.eventBoxes.length != this.props.events.length || this.eventBoxes.Any(a=>a.firstRect == null))
			return WaitXThenRun(1000, this.PostRender);
		
		//Log("Starting position-fix phase.");
		//for (let eventBox of this.eventBoxes.OrderBy(a=>a.props.x)) {
		for (let eventBox of this.eventBoxes) {
			// if first-rects were already ready, cancel since event-boxes were already repositioned
			//if (eventBox.firstRectsReady) break;
			eventBox.firstRectsReady = true;
			eventBox.forceUpdate();
		}
	}
}

class EventBox extends Component<{parent: GraphOverlayUI, rowHeight: number, event: Event, x: number, renderInfo: EventRenderInfo}, any> {
	firstRectsReady = false;
	root: View;
	render() {
		var {parent, rowHeight, event, x, renderInfo} = this.props;

		var textHeight = 15;
		var rect = new VRect(x, (rowHeight - 25) - textHeight, this.firstRect ? this.firstRect.width : 1, textHeight);
		if (this.firstRectsReady) {
			//var otherBoxes = parent.eventBoxes.Except(this); //.Where(a=>a.firstRect);
			var earlierBoxes = parent.eventBoxes.Where(a=>a.props.x < x);
			while (earlierBoxes.Any(a=>a.firstRect.Intersects(rect)) && rect.y > 0)
				rect = rect.NewY(y=>y - textHeight);

			/*Log(`Rendering with first-rects ready.
Other-box first-rects: ${otherBoxes.Select(a=>a.firstRect)}
Our fixed rect: ${rect}`);*/

			Assert(rect.width > 1);
			this.firstRect = rect; // update rect immediately, so other ones know to avoid our new-rect too
		}

		return (
			<Text ref={c=>this.root = c} //onLayout={this.OnLayout}
				style={{position: "absolute", left: rect.x, top: rect.y, height: rect.height, color: renderInfo.color || "#ffffff"}}>
				{renderInfo.text}
			</Text>
		);
	}

	firstRect: VRect = null;
	/*OnLayout(e) {
		if (this.firstRect) return;
		var {x, y, width, height} = e.nativeEvent.layout;
		this.firstRect = new VRect(x, y, width, height);
		//Log("Getting first-render rect:" + this.firstRect);
	}*/
	PostRender() {
		if (this.firstRect) return;
		this.root.measure((ox, oy, width, height, x, y) => {
			//Notify("Hi" + ox + ";" + oy + ";" + width + ";" + height + ";" + px + ";" + py);
			if (width > 1)
				this.firstRect = new VRect(x, y, width, height);
			//Log("Getting first-render rect:" + this.firstRect);
		});
	}
}