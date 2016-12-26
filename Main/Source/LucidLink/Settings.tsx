import {_VDFPostDeserialize, _VDFSerializeProp, FromVDF, IsString, P, T, ToVDF} from "../Frame/Globals";
import {max, min} from "moment";
import {BaseComponent} from "../Frame/ReactGlobals";
var ScrollableTabView = require("react-native-scrollable-tab-view");
var DialogAndroid = require("react-native-dialogs");
import Node from "../Packages/VTree/Node";

import GeneralUI from "./Settings/GeneralUI";
import AudiosUI from "./Settings/AudiosUI";
import {LL} from "../LucidLink";

export class Settings extends Node {	
	@O @P() applyScriptsOnLaunch = false;
	@O @P() blockUnusedKeys = false;
	//@P() captureSpecialKeys = false;
	@O @P() museEEGPacketBufferSize = 30;
	@O @P() eyeTracker_horizontalSensitivity = .5;
	@O @P() eyeTracker_verticalSensitivity = .3;
	@O @P() eyeTracker_offScreenGravity = .5;
	@O @P() eyeTracker_relaxVSTenseIntensity = .85;
	@O @P() eyeTraceSegmentSize = .05;
	@O @P() eyeTraceSegmentCount = 100;
	@O @P() logStatsEveryXMinutes = 5;
	@O @P() reconnectAttemptInterval = 10;

	@T("List(AudioFileEntry)") @P(true, true) audioFiles = [];

	ui = null;
}
global.Extend({Settings});

export class AudioFileEntry {
	@P() name = null;
	@P() path = null;
}
global.Extend({AudioFileEntry});

export class SettingsUI extends BaseComponent<any, any> {
	constructor(props) {
		super(props);
		LL.settings.ui = this;
	}	
	render() {
		return (
			<ScrollableTabView style={{flex: 1}}>
				<GeneralUI tabLabel="General"/>
				<AudiosUI tabLabel="Audios"/>
			</ScrollableTabView>
		);
	}
}