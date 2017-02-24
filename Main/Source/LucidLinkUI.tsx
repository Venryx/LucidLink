import {LL, Init} from "./LucidLink";
import {BaseComponent as Component, Panel, Column, VButton, Row, VText, SimpleShouldUpdate} from "./Frame/ReactGlobals";
import {JavaBridge, Log} from "./Frame/Globals";
import {TrackerUI} from "./LucidLink/Tracker";
import {JournalUI} from "./LucidLink/Journal";
import {ScriptsUI} from "./LucidLink/Scripts";
import {SettingsUI} from "./LucidLink/Settings";
import {MoreUI} from "./LucidLink/More";
import ScrollableTabView from "react-native-scrollable-tab-view";
import {View, Dimensions} from "react-native";
import {ToolsUI} from "./LucidLink/Tools";
import SleepSegmentsUI from "./LucidLink/Tracker/Graph/SleepSegmentsUI";
import {observer} from "mobx-react/native";
import {A} from "./Frame/General/Assert";

// note: we have to alias BaseComponent as Component, because otherwise react-native's hot-reloading breaks
// (see here for some info: https://github.com/gaearon/react-transform-hmr/issues/82)
@observer
export default class LucidLinkUI extends Component<{}, {}> {
    constructor(props) {
        super(props);
		if (LL == null)
			Init(this);
    }

	state = {activeTab: -1};

    render() {
		// if main-data not yet loaded, render blank ui
		if (LL == null || LL.mainDataLoaded == false) {
			var marker = null;
			return (
				<Column>
					<VText>Loading...</VText>
				</Column>
			);
		}

		var {activeTab} = this.state;
		let sleepSession = LL.tracker.openSleepSession; 
		return (
			<Panel style={{flex: 1}}>
				<LucidLinkTabUI activeTab={activeTab}/>
				{/*keyboardVisible &&
					<Panel style={{position: "absolute", bottom: 0, height: 30}}>
						<VButton style={{alignItems: "flex-end", width: 100, height: 30}}
							textStyle={{margin: 3}} color="#777" text="Copy" onPress={()=>alert("Test1")}/>
					</Panel>*/}

				{sleepSession &&
					<Column style={{top: 0, width: 500, height: 500}}>
						<Row style={{padding: 3, height: 56, backgroundColor: "#303030"}}>
							<VButton text="Back" style={{width: 100}} onPress={()=> {
								LL.tracker.openSleepSession = null;
							}}/>
							{/*<Panel style={{flex: 1}}/>
							<VButton text="Export" mr={10} style={{width: 100}} onPress={()=> {
							}}/>*/}
						</Row>
						<SleepSegmentsUI {...{startTime: sleepSession.startTime, endTime: sleepSession.endTime}}
							style={{top: 0, flex: 1}}
							sleepSessions={[sleepSession]}/>
					</Column>}
			</Panel>
		);
    }

	/*componentDidMount() {
		JavaBridge.Main.OnTabSelected(0);
	}*/

	/*componentWillUnmount() {
		SaveMainData();
	}*/
}

@SimpleShouldUpdate
class LucidLinkTabUI extends Component<{activeTab}, {}> {
	render() {
		var {activeTab} = this.props;
		return (
			<ScrollableTabView style_disabled={{flex: 1}}
				onChangeTab={data=> {
					this.setState({activeTab: data.i})
					JavaBridge.Main.OnTabSelected(data.i);
				}}>
				<ToolsUI tabLabel="Tools" active={activeTab == 0}/>
				<TrackerUI tabLabel="Tracker" active={activeTab == 1}/>
				<JournalUI tabLabel="Journal" active={activeTab == 2}/>
				<ScriptsUI tabLabel="Scripts" active={activeTab == 3}/>
				<SettingsUI tabLabel="Settings" active={activeTab == 4}/>
				<MoreUI tabLabel="More" active={activeTab == 5}/>
			</ScrollableTabView>
		);
	}
}