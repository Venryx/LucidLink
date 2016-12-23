import {LL, Init} from "./LucidLink";
import {BaseComponent as Component, Panel} from "./Frame/ReactGlobals";
import {JavaBridge} from "./Frame/Globals";
import {MonitorUI} from "./LucidLink/Monitor";
import {TrackerUI} from "./LucidLink/Tracker";
import {JournalUI} from "./LucidLink/Journal";
import {ScriptsUI} from "./LucidLink/Scripts";
import {SettingsUI} from "./LucidLink/Settings";
import {MoreUI} from "./LucidLink/More";
import ScrollableTabView from "react-native-scrollable-tab-view";
import {View} from "react-native";

// note: we have to alias BaseComponent as Component, because otherwise react-native's hot-reloading breaks
// (see here for some info: https://github.com/gaearon/react-transform-hmr/issues/82)
export default class LucidLinkUI extends Component<{}, {}> {
    constructor(props) {
        super(props);
		if (LL == null)
			Init(this);
    }

	state = {activeTab: -1};

    render() {
		// if main-data not yet loaded, render blank ui
		if (LL.mainDataLoaded == false) {
			var marker = null;
			return (
				<ScrollableTabView style_disabled={{flex: 1}}
						onChangeTab={data=>this.setState({activeTab: data.i})}>
					<Panel tabLabel="Monitor">{marker}</Panel>
					<Panel tabLabel="Tracker">{marker}</Panel>
					<Panel tabLabel="Journal">{marker}</Panel>
					<Panel tabLabel="Scripts">{marker}</Panel>
					<Panel tabLabel="Settings">{marker}</Panel>
					<Panel tabLabel="More">{marker}</Panel>
				</ScrollableTabView>
			)
		}

     	var {activeTab} = this.state;
        return (
			//<Panel style={{flex: 1}}>
			<ScrollableTabView style_disabled={{flex: 1}}
					onChangeTab={data=> {
						this.setState({activeTab: data.i})
						JavaBridge.Main.OnTabSelected(data.i);
					}}>
				<MonitorUI tabLabel="Monitor" active={activeTab == 0}/>
				<TrackerUI tabLabel="Tracker" active={activeTab == 1}/>
				<JournalUI tabLabel="Journal" active={activeTab == 2}/>
				<ScriptsUI tabLabel="Scripts" active={activeTab == 3}/>
				<SettingsUI tabLabel="Settings" active={activeTab == 4}/>
				<MoreUI tabLabel="More" active={activeTab == 5}/>
			</ScrollableTabView>
				/*{keyboardVisible &&
					<Panel style={{position: "absolute", bottom: 0, height: 30}}>
						<VButton style={{alignItems: "flex-end", width: 100, height: 30}}
							textStyle={{margin: 3}} color="#777" text="Copy" onPress={()=>alert("Test1")}/>
					</Panel>}
			</Panel>*/
        );
    }

	/*componentDidMount() {
		JavaBridge.Main.OnTabSelected(0);
	}*/

	/*componentWillUnmount() {
		SaveMainData();
	}*/
}