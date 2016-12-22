import {Text} from "react";
import {BaseComponent, Panel} from "../../Frame/ReactGlobals";
import {colors, styles} from "../../Frame/Styles";
export default class ConsoleUI extends BaseComponent {
	render() {
		return (
			<Panel style={{flex: 1, flexDirection: "column", backgroundColor: colors.background}}>
				<Text style={styles.text}>{`
"Monitors Muse headband EEG data, and triggers events (eg audio) when REM sleep is detected."

Author: Stephen Wicklund (Venryx)
License: MIT (open source)
GitHub repository: https://github.com/Venryx/LucidLink
				`.trim()}</Text>
			</Panel>
		);
	}
}