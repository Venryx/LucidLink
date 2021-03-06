import {Text, TextInput} from "react-native";
import {BaseComponent as Component, Panel, VButton} from "../../Frame/ReactGlobals";
import {colors, styles} from '../../Frame/Styles';
import {E} from '../../Frame/Globals';
import {Observer, observer} from "mobx-react/native";
import {transaction, autorun} from "mobx";
import Drawer from "react-native-drawer";

import DisplayerScriptsPanel from "./DisplayersUI/DisplayerScriptsPanel";
import {LL} from "../../LucidLink";
import {VTextInput} from "../../Packages/ReactNativeComponents/VTextInput";

@observer
export default class DisplayersUI extends Component<{}, {scriptLastRunsOutdated}> {
	_drawer = null;
	ToggleSidePanelOpen() {
		if (this._drawer._open)
			this._drawer.close();
		else
			this._drawer.open();
	}

	ComponentDidMount() {
		autorun(()=> {
			LL.tracker.selectedDisplayerScript; // listen for changes
			this._drawer.close();
		});
	}

	render() {
		var node = LL.tracker;
		var {selectedDisplayerScript: selectedScript} = node;
		var {scriptLastRunsOutdated} = this.state;
		
		const drawerStyles = {
			drawer: {shadowColor: "#000000", shadowOpacity: .8, shadowRadius: 3},
			main: {paddingLeft: 3},
		};
		
		return (
			<Drawer ref={comp=>this._drawer = comp}
					content={
						<DisplayerScriptsPanel scripts={node.displayerScripts} selectedScript={node.selectedDisplayerScript}/>
					}
					type="overlay" openDrawerOffset={0.5} panCloseMask={0.5} tapToClose={true}
					closedDrawerOffset={-3} styles={drawerStyles}>
				<Panel style={{flex: 1, flexDirection: "column", backgroundColor: colors.background}}>
					<Panel style={E(styles.header, {flexDirection: "row", flexWrap: "wrap", padding: 3, paddingBottom: -5})}>
						<VButton text="Scripts" style={{width: 100}} onPress={this.ToggleSidePanelOpen}/>
						<Text style={{marginLeft: 10, marginTop: 8, fontSize: 18}}>
						Script: {selectedScript ? selectedScript.file.NameWithoutExtension : "n/a"}
						{selectedScript && !selectedScript.editable ? " (read only)" : ""}
						</Text>
						{selectedScript && selectedScript.editable &&
							<VButton text="Rename" style={{marginLeft: 10, width: 100}}
								onPress={()=>selectedScript.Rename()}/>}
						<Panel style={{flex: 1}}/>
						<Panel style={{flexDirection: "row", alignItems: "flex-end"}}>
							<VButton color="#777" text="Save and apply all" enabled={LL.tracker.DisplayerScriptFilesOutdated}
								style={{width: 200, marginLeft: 5}}
								onPress={()=> {
									transaction(()=> {
										//selectedScript.Save();
										for (let script of LL.tracker.displayerScripts) {
											if (script.fileOutdated)
												script.Save();
										}
									});
									LL.tracker.ApplyDisplayerScripts();
								}}/>
						</Panel>
					</Panel>
					<Panel style={{marginTop: -7, flex: 1}}>
						<VTextInput value={selectedScript ? selectedScript.text : ""}
							accessible={true} accessibilityLabel="@ConvertStartSpacesToTabs"
							editable={selectedScript != null}
							onChangeText={text=> {
								if (!selectedScript.editable) return;
								transaction(()=> {
									selectedScript.text = text;
									selectedScript.fileOutdated = true;
								});
							}}/>
					</Panel>
				</Panel>
			</Drawer>
		);
	}

	/*componentWillUnmount() {
		this.SaveScripts();
	}*/
}