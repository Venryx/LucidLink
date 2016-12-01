var ScrollableTabView = require("react-native-scrollable-tab-view");
var DialogAndroid = require("react-native-dialogs");

import GeneralUI from "./Settings/GeneralUI";
import PatternsUI from "./Settings/PatternsUI";
import AudiosUI from "./Settings/AudiosUI";

g.Settings = class Settings extends Node {
	@P() applyScriptsOnLaunch = false;
	@P() blockUnusedKeys = false;
	//@P() captureSpecialKeys = false;
	@P() patternMatchInterval = 1; // in seconds
	@P() patternMatchOffset = .2; // in second-distances

	@P() previewChartRangeX = 200;
	@P() previewChartRangeY = 100;
	@T("List(Pattern)") @P(true, true) patterns = [];

	@T("List(AudioFileEntry)") @P(true, true) audioFiles = [];
}
g.Pattern = class Pattern {
	constructor(name) {
		this.name = name;
	}

	@P() name = null;
	@P() enabled = true;
	@P() sensitivity = 50;
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