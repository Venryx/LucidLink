import {Column, Panel, Row, VButton, BaseComponent as Component} from "../../../Frame/ReactGlobals";
import {VSwitch_Auto} from "../../../Packages/ReactNativeComponents/VSwitch";
import {colors} from "../../../Frame/Styles";
import {NumberPicker_Auto} from "../../../Packages/ReactNativeComponents/NumberPicker";
import {FBA} from "../FBA";
import {RVP} from "../RVP";
import {VTextInput} from "../../../Packages/ReactNativeComponents/VTextInput";
import {observer} from "mobx-react/native";
import VText from "../../../Frame/Components/VText";

@observer
export default class BackgroundMusicConfigUI extends Component<{node: FBA | RVP}, {}> {
	render() {
		var {node} = this.props;
		return (
			<Row style={{flex: 1, flexDirection: "column"}}>
				<Row mt={30} height={30}>
					<VText mt={1}>Background music: </VText>
					<VSwitch_Auto path={()=>node.p.backgroundMusic_enabled}/>
				</Row>
				{node.backgroundMusic_enabled &&
					<Row style={{backgroundColor: colors.background_dark, flexDirection: "column", padding: 5}}>
						<Row>
							<VText mt={5} mr={10}>Volume: </VText>
							<NumberPicker_Auto path={()=>node.p.backgroundMusic_volume}
								max={1} step={.01} format={val=>(val * 100).toFixed(0) + "%"}
								dialogTitle="Volume (background music)"/>
						</Row>
						<Row>
							<Column style={{flex: 1}}>
								<VText mt={5} mr={10}>Tracks:</VText>
								{node.backgroundMusic_tracks.map((name, index)=> {
									return (
										<Row key={index} height={35}>
											<VTextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
												editable={true} value={name} onChangeText={text=>node.backgroundMusic_tracks[index] = text}/>
											<VButton text="X" style={{alignItems: "flex-end", marginLeft: 5, width: 28, height: 28}}
												textStyle={{marginBottom: 3}} onPress={()=>node.backgroundMusic_tracks.RemoveAt(index)}/>
										</Row>
									);
								})}
								<Row height={45}>
									<VButton text="Add" style={{width: 100, height: 40}}
										onPress={()=>node.backgroundMusic_tracks.push("")}/>
								</Row>
								<Panel style={{flex: 111222}}/>
							</Column>
						</Row>
					</Row>}
			</Row>
		);
	}
}