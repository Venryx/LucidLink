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
	
	@P() applyScriptsOnLaunch = false;
	@P() blockUnusedKeys = false;
	//@P() captureSpecialKeys = false;
	@P() patternMatchInterval = 1; // in seconds
	@P() patternMatchOffset = .2; // in second-distances

	@P() previewChartRangeX = 200;
	@P() previewChartRangeY = 100;
	@T("List(Pattern)") @P(true, true) patterns = [];
	@P() selectedPattern = null; // holds the actual pattern, but only the name is serialized

	@T("List(AudioFileEntry)") @P(true, true) audioFiles = [];

	ui = null;
}
g.Pattern = class Pattern {
	constructor(name) {
		this.name = name;
	}

	@P() name = null;
	@P() enabled = true;
	@P() sensitivity = 30;
	@P() channel1 = false;
	@P() channel2 = false;
	@P() channel3 = false;
	@P() channel4 = false;
	//@T("Vector2i") @P() offset = new Vector2i(0, 0);
	@T("List(Vector2i)") @P() points = [];

	// in second-distances
	get Duration() {
		var min = this.points.Select(a=>a.x).Min();
		var max = this.points.Select(a=>a.x).Max();
		return max - min;
	}

	@P() textEditor = false;

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