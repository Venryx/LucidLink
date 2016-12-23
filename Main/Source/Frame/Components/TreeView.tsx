import {BaseComponent as Component, Row, Column} from "../ReactGlobals";
import {View, TouchableOpacity} from "react-native";
import {E} from "../Globals";

export default class TreeView extends Component<
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

		var height = style && style.height ? style.height : 50;

	    var iconSize = 50;
		return (
			<Column style={style}>
				<Row>
					<TouchableOpacity onPress={this.OnArrowPress}
						style={E(
							{width: iconSize, height, backgroundColor: "gray"},
							!collapsible && {opacity: 0},
						)}/>
					<TouchableOpacity onPress={this.OnPress}
							style={E({flex: 1, height}, titleStyle, {backgroundColor: selected ? "rgba(44, 79, 122, .5)" : null} as any)}>
						{titleElement || nodeLabel}
					</TouchableOpacity>
				</Row>
				{!collapsed && children}
			</Column>
		);
	}
}