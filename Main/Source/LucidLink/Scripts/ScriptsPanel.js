export default class ScriptsPanel extends BaseComponent {
	render() {
		var {parent} = this.props;
		// ms: parent.setState({activeScript: ...}); is called
		return (
			<View style={{flex: 1, backgroundColor: "#777"}}>
			</View>
		)
	}
}