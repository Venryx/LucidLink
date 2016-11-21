export default class ConsoleUI extends BaseComponent {
	render() {
		return (
			<View style={{flex: 1, flexDirection: "column"}}>
				<Text>{`
"Monitors Muse headband EEG data, and triggers events (eg audio) when REM sleep is detected."

Author: Stephen Wicklund (Venryx)
License: MIT (open source)
GitHub repository: https://github.com/Venryx/LucidLink
				`.trim()}</Text>
			</View>
		);
	}
}