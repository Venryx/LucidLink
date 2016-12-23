import {BaseComponent, Column, Panel, Row, VButton, VText} from "../../Frame/ReactGlobals";
import {colors, styles} from "../../Frame/Styles";
import {now} from "moment";
import {VRect} from "../../Frame/Graphics/VectorStructs";
import Drawer from "react-native-drawer";
import Chart from "react-native-chart";
import {MKRangeSlider} from 'react-native-material-kit';
import NumberPickerDialog from "react-native-numberpicker-dialog";

import LeftPanel from "./GraphUI/LeftPanel";
import {observer, Observer} from "mobx-react/native";
import { View } from 'react-native';
import Bind from "autobind-decorator";
import {LL} from "../../LucidLink";
var Moment = require('moment');

@observer
@Bind
export default class GraphUI extends BaseComponent<any, any> {
	leftPanel = null;
	@Bind ToggleLeftPanelOpen() {
		if (this.leftPanel._open)
			this.leftPanel.close();
		else
			this.leftPanel.open();
	}

	render() {
		var {session} = this.props;
		var node = LL.tracker;
		
		const drawerStyles = {
			drawer: {shadowColor: "#000000", shadowOpacity: .8, shadowRadius: 3},
			main: {paddingLeft: 3},
		};
		
		return (
			<Drawer ref={comp=>this.leftPanel = comp}
					content={<LeftPanel parent={this}/>}
					type="overlay" openDrawerOffset={0.7} panCloseMask={0.7} tapToClose={true}
					disabled={true} // temp
					closedDrawerOffset={-3} styles={drawerStyles}>
				<Column style={{flex: 1, backgroundColor: colors.background}}>
					<Row style={{padding: 3, height: 56, backgroundColor: "#303030"}}>
						{/*<VButton text="Options" style={{width: 100}} onPress={this.ToggleLeftPanelOpen}/>*/}
						<VText ml10 mt10>Rows/days: </VText>
						<VButton text={node.rowCount.toString()} ml3 mt5 style={{width: 100, height: 32}}
							onPress={()=> {
								var values = [];
								for (let val = 1; val <= 10; val++)
									values.push(val);
								NumberPickerDialog.show({
									selectedValueIndex: values.indexOf(node.rowCount),
									values: values.Select(a=>a.toString()),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
									title: "Rows/days",
									message: "Select number of rows/days to show on-screen at once.",
								}).then(id=> {
									if (id == -1) return;
									let val = values[id];
									node.rowCount = val;
								});
							}}/>
						<Panel style={{flex: 1}}/>
					</Row>
					<Row style={{marginTop: -7, flex: 1}}>
						<ChartsUI session={session}/>
					</Row>
				</Column>
			</Drawer>
		);
	}
}

@observer
@Bind
class ChartsUI extends BaseComponent<any, any> {
	state = {width: -1, height: -1};

	ComponentDidMountOrUpdate() {
		var node = LL.tracker;
		var now = Moment();
		var rangeStart = now.clone().startOf("day").subtract(node.rowCount, "day");
		var rangeEnd = now.clone().endOf("day");
		LL.tracker.LoadSessionsForRange(rangeStart, rangeEnd);
	}
	
    render() {
		var {session} = this.props;
		var {width, height} = this.state;
		var node = LL.tracker;

		var now = Moment();
		var dayStart = now.clone().startOf("day");
		//var dayEnd = now.clone().endOf("day");
		var dayEnd = dayStart.clone().add(1, "day");

		var rows = [];
		if (width != -1) { // if width not obtained from OnLayout yet, don't render children
			for (let offset = -(node.rowCount - 1); offset <= 0; offset++) {
				let startTime = dayStart.clone().add(offset, "days");
				let endTime = dayEnd.clone().add(offset, "days");
				let rowHeight = height / node.rowCount;
				rows.push(
					<ChartUI key={startTime} {...{startTime, endTime, width, height: rowHeight}}/>
				);
			}
		}

        return (
            <View style={{flex: 1, backgroundColor: colors.background}} onLayout={this.OnLayout}>
				{rows}
            </View>
        );
    }

	//PostRender() {
	OnLayout(e) {
		var {x, y, width, height} = e.nativeEvent.layout;
		if (width != this.state.width || height != this.state.height)
			this.setState({width, height});
	}
}

@observer
@Bind
class ChartUI extends BaseComponent<any, any> {
	/*componentWillReact() {
        Log("Re-rendering ChartUI, because... " + new Error().stack);
    }*/
	
	eventBoxes = [];
	render() {
		var {startTime, endTime, width, height} = this.props;
		var node = LL.tracker;
		var events = node.GetEventsForRange(startTime, endTime);
		
		var mainLineColor = "#e1cd00";
		var mainLinePoints = [[0, 0]]; // placeholder

		this.eventBoxes = [];
		
        return (
            <View style={{width, height, backgroundColor: colors.background}}>
				<Chart style={{width, height, paddingRight: 10}}
					minX={0} maxX={24} legendStepsX={25}
					minY={0} maxY={1} legendStepsY={2} showYAxisLabels={false} yAxisWidth={0}
					axisColor="#AAA" axisLabelColor="#AAA" gridColor="#777"
					type="line" color={[mainLineColor]} data={[mainLinePoints]}/>
				{events.map((event, index)=> {
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
						event={event} x={x}/>;
				})}
            </View>
        );
	}
	PostRender() {
	//OnLayout() {
		//Log("Starting position-fix phase.");
		for (let eventBox of this.eventBoxes) {
			// if first-rects were already ready, cancel since event-boxes were already repositioned
			//if (eventBox.firstRectsReady) break;
			eventBox.firstRectsReady = true;
			eventBox.forceUpdate();
		}
	}
}

@Bind
class EventBox extends BaseComponent<any, any> {
	firstRectsReady = false;
	render() {
		var {parent, rowHeight, event, x} = this.props;

		var textHeight = 15;
		var rect = new VRect(x, (rowHeight - 25) - textHeight, 1, textHeight);
		if (this.firstRectsReady) {
			var otherBoxes = parent.eventBoxes.Except(this).Where(a=>a.firstRect);
			while (otherBoxes.Any(a=>a.firstRect.Intersects(rect)) && rect.y > 0)
				rect = rect.NewY(y=>y - textHeight);

			/*Log(`Rendering with first-rects ready.
Other-box first-rects: ${otherBoxes.Select(a=>a.firstRect)}
Our fixed rect: ${rect}`);*/

			this.firstRect = rect; // update rect immediately, so other ones know to avoid our new-rect too
		}

		var Text2 = Text as any;
		return (
			<Text2 style={{position: "absolute", left: rect.x, top: rect.y, height: rect.height}} onLayout={this.OnLayout}>
				{"|" + event.type}
			</Text2>
		);
	}

	firstRect = null;
	OnLayout(e) {
		if (this.firstRect) return;
		var {x, y, width, height} = e.nativeEvent.layout;
		this.firstRect = new VRect(x, y, width, height);
		//Log("Getting first-render rect:" + this.firstRect);
	}
}