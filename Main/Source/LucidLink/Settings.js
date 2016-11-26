var ScrollableTabView = require("react-native-scrollable-tab-view");

import GeneralUI from "./Settings/GeneralUI";
import PatternsUI from "./Settings/PatternsUI";
import AudiosUI from "./Settings/AudiosUI";

g.Settings = class Settings extends Node {
	@P() applyScriptsOnLaunch = false;
	@P() blockUnusedKeys = false;
	//@P() captureSpecialKeys = false;

	@T("List(Pattern)") @P(true, true) patterns = [];

	@T("List(AudioFileEntry)") @P(true, true) audioFiles = [];
}
g.Pattern = class Pattern {
	@P() name = null;
	@P() points = [];
	@P() textEditorEnabled = false;
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