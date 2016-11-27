var ScrollableTabView = require("react-native-scrollable-tab-view");

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
	@P() name = null;
	@P() channel1 = false;
	@P() channel2 = false;
	@P() channel3 = false;
	@P() channel4 = false;
	@P() points = [];

	// in second-distances
	get Duration() {
		var min = this.points.Select(a=>a[0]).Min();
		var max = this.points.Select(a=>a[0]).Max();
		return max - min;
	}

	@P() textEditor = false;
}
g.AudioFileEntry = class AudioFileEntry {
	@P() name = null;
	@P() path = null;
}

export class SettingsUI extends BaseComponent {
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