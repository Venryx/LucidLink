import {BaseComponent, Column} from '../../../Frame/ReactGlobals';
import {colors} from '../../../Frame/Styles';
import Spinner from "rn-spinner";
import {LL} from "../../../LucidLink";

export default class LeftPanel extends BaseComponent<any, any> {
	render() {
		var {parent} = this.props;
		var node = LL.tools.monitor;
		return (
			<Column style={{flex: 1, backgroundColor: colors.background_light}}>
			</Column>
		)
	}
}