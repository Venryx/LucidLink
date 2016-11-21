export default class ConsoleUI extends BaseComponent {
	render() {
		var {onChangeText, text} = this.props;
		var buttonStyle = {flex: .2, marginLeft: 5, height: isLandscape ? 35 : 50};
		var textStyle = {
			//height: screenHeight,
			flex: 1, textAlignVertical: "top",
		};

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
	RunScript() {
		eval(this.props.text);
	}
}