import {FromVDF, ToVDF} from "../Frame/Globals";
import {BaseComponent} from "../Frame/ReactGlobals";
import ScrollableTabView from "react-native-scrollable-tab-view";
import DialogAndroid from "react-native-dialogs";
import Node from "../Packages/VTree/Node";
import GeneralUI from "./Settings/GeneralUI";
import AudiosUI from "./Settings/AudiosUI";
import {LL} from "../LucidLink";
import {P, T} from "../Packages/VDF/VDFTypeInfo";
import MuseUI from "./Settings/Muse";
import {_Enum, Enum} from "../Frame/General/Enums";

/*export enum Gender {
	Male,
	Female
}*/
@_Enum export class Gender extends Enum { static V: Gender;
	Male = this
	Female = this
}

export class Settings extends Node {	
	// general
	@O @P() applyScriptsOnLaunch = false;
	@O @P() blockUnusedKeys = false;
	//@P() captureSpecialKeys = false;
	@O @P() keepDeviceAwake = true;
	@O @P() age = 20;
	@O @P() gender = Gender.V.Male;
	@O @P() spBioBufferSize = 10; // in seconds

	// muse
	@O @P() museEEGPacketBufferSize = 30;
	@O @P() eyeTracker_horizontalSensitivity = .5;
	@O @P() eyeTracker_verticalSensitivity = .3;
	@O @P() eyeTracker_offScreenGravity = .5;
	@O @P() eyeTracker_relaxVSTenseIntensity = .85;
	@O @P() eyeTraceSegmentSize = .05;
	@O @P() eyeTraceSegmentCount = 100;
	@O @P() logStatsEveryXMinutes = 5;
	@O @P() reconnectAttemptInterval = 10;
	@O @P() sessionSaveInterval = 5;

	// audio
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
				<MuseUI tabLabel="Muse"/>
				<AudiosUI tabLabel="Audios"/>
			</ScrollableTabView>
		);
	}
}