var ScrollableTabView = require("react-native-scrollable-tab-view");
var DialogAndroid = require("react-native-dialogs");

import GeneralUI from "./Settings/GeneralUI";
import PatternsUI from "./Settings/PatternsUI";
import AudiosUI from "./Settings/AudiosUI";

g.Settings = class Settings extends Node {
	@_VDFSerializeProp() SerializeProp(path, options) {
	    if (path.currentNode.prop.name == "selectedPattern" && this.selectedPattern)
	        return new VDFNode(this.selectedPattern.name);
	}
	@_VDFPostDeserialize() PostDeserialize() {
	    if (IsString(this.selectedPattern))
			this.selectedPattern = this.patterns.First(a=>a.name == this.selectedPattern);
	}
	
	@O @P() applyScriptsOnLaunch = false;
	@O @P() blockUnusedKeys = false;
	//@P() captureSpecialKeys = false;
	@O @P() patternMatchInterval = 1; // in seconds
	@O @P() patternMatchOffset = .2; // in second-distances
	@O @P() museEEGPacketBufferSize = 30;
	@O @P() eyeTracker_horizontalSensitivity = .33;
	@O @P() eyeTracker_verticalSensitivity = .33;
	@O @P() eyeTracker_ignoreXMovementUnder = .01;
	@O @P() eyeTracker_ignoreYMovementUnder = .01;
	@O @P() eyeTracker_relaxVSTenseIntensity = .85;
	@O @P() eyeTraceSegmentSize = .05;
	@O @P() eyeTraceSegmentCount = 100;

	@P() previewChartRangeX = 200;
	@P() previewChartRangeY = 100;
	@O @T("List(Pattern)") @P(true, true) patterns = [];
	@O @P() selectedPattern = null; // holds the actual pattern, but only the name is serialized

	@T("List(AudioFileEntry)") @P(true, true) audioFiles = [];

	ui = null;
}
g.Pattern = class Pattern {
	constructor(name) {
		this.name = name;
	}

	@O @P() name = null;
	@O @P() enabled = true;
	@O @P() sensitivity = 30;
	@O @P() channel1 = false;
	@O @P() channel2 = false;
	@O @P() channel3 = false;
	@O @P() channel4 = false;
	//@T("Vector2i") @P() offset = new Vector2i(0, 0);
	@O @T("List(Vector2i)") @P() points = [];

	// in second-distances
	get Duration() {
		var min = this.points.Select(a=>a.x).Min();
		var max = this.points.Select(a=>a.x).Max();
		return max - min;
	}

	@O @P() textEditor = false;

	Rename() {
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Rename pattern "${this.name}"`,
			"input": {
				prefill: this.name,
				callback: newName=> {
					this.name = newName;
					if (LL.settings.ui)
						LL.settings.ui.forceUpdate();
				}
			},
			"positiveText": "OK",
			"negativeText": "Cancel"
		});
		dialog.show();
	}
	Delete() {
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Delete pattern "${this.name}"`,
			"content": `Permanently delete pattern?`,
			"positiveText": "OK",
			"negativeText": "Cancel",
			"onPositive": ()=> {
				LL.settings.patterns.Remove(this);
				if (LL.settings.ui)
					LL.settings.ui.forceUpdate();
			},
		});
		dialog.show();
	}

	Clone() {
		var vdf = ToVDF(this);
		var result = FromVDF(vdf);
		return result;
	}

	ui = null;
}
g.AudioFileEntry = class AudioFileEntry {
	@P() name = null;
	@P() path = null;
}

export class SettingsUI extends BaseComponent {
	constructor(props) {
		super(props);
		LL.settings.ui = this;
	}	
	render() {
		return (
			<ScrollableTabView style={{flex: 1}}>
				<GeneralUI tabLabel="General"/>
				<PatternsUI tabLabel="Patterns"/>
				<AudiosUI tabLabel="Audios"/>
			</ScrollableTabView>
		);
	}
}