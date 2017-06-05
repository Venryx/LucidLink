import {BaseComponent, BasicStyles} from "../ReactGlobals";
import {TextProperties, Text} from "react-native";
import {E} from "../Globals";

export default class VText extends BaseComponent<{style?} & TextProperties, {}> {
	render() {
		var {style, children, ...rest} = this.props as any;
		return (
			<Text {...rest}
					style={E(
						{},
						BasicStyles(this.props),
						style
					)}>
				{children}
			</Text>
		);
	}
}