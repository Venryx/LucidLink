import {BaseComponent, Row} from "../ReactGlobals";
import {View, TouchableOpacity} from "react-native";
import {E} from "../Globals";

export default class TreeView extends BaseComponent<
		{collapsible, defaultCollapsed,
			titleElement?: JSX.Element, nodeLabel?: string,
			selected?: boolean, style, titleStyle, onArrowPress?: Function, onPress?: Function},
		{collapsed}> {
	constructor(props) {
	    super(props);
		var {defaultCollapsed} = this.props;
		this.state = {collapsed: defaultCollapsed};
	}

	OnArrowPress(...args) {
		var {collapsible, onArrowPress} = this.props;
		var {collapsed} = this.state;
	    var newCollapsed = collapsed;
	    if (collapsible) {
	        newCollapsed = !collapsed;
	        this.setState({collapsed: newCollapsed});
	    }
	    if (onArrowPress) onArrowPress(newCollapsed);
	}

	OnPress(...args) {
		var {onPress} = this.props;
		if (onPress) onPress(...args);
	}

	render() {
		var {collapsible, titleElement, nodeLabel, children, selected, style, titleStyle} = this.props;
		var {collapsed} = this.state;

	    var iconSize = 8; // with padding: 12
		return (
			<Row style={style}>
				<TouchableOpacity onPress={this.OnArrowPress}
					style={E(
						{width: iconSize, height: iconSize},
						!collapsible && {opacity: 0},
					)}/>
				<TouchableOpacity onPress={this.OnPress}
						style={E(titleStyle, {backgroundColor: selected ? "rgba(44, 79, 122, .5)" : null} as any)}>
					{titleElement || nodeLabel}
				</TouchableOpacity>
				{!collapsed && <View>
					{/*collapsed ? null : children*/}
					{children}
				</View>}
			</Row>
		);
	}
}