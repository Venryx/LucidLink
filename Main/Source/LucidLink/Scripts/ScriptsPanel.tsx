import {colors} from "../../Frame/Styles";
import {BaseComponent, Panel, VButton} from "../../Frame/ReactGlobals";
import {Script} from "./Script";
var SortableListView = require("react-native-sortable-listview");
import {TouchableHighlight, Text, Switch} from "react-native";
import {DN} from "../../Frame/Globals";
import Bind from "autobind-decorator";
import {LL} from "../../LucidLink";

@Bind
class ScriptEntryUI extends BaseComponent {
	render() {
		var {script} = this.props;
		return (
			<TouchableHighlight {...this.props.sortHandlers} underlayColor="#EEE" delayLongPress={300}
					style={{backgroundColor: colors.background_lighter, borderBottomWidth: 1, borderColor: colors.background_light}}
					onPress={()=>LL.scripts.ui.SelectScript(script)}>
				<Panel style={{height: 40, paddingLeft: 10, paddingRight: 10, flexDirection: "row"}}>
					<Text style={{paddingTop: 10}}>{script.file.Name}</Text>
					<Panel style={{flex: 1}}/>
					<Switch value={script.enabled}
						onValueChange={value=>DN(script.enabled = value, this.forceUpdate())}/>
					{script.editable
						? <VButton text="X"
							style={{alignItems: "flex-end", marginLeft: 5, marginTop: 6, width: 28, height: 28}}
							textStyle={{marginBottom: 3}} onPress={()=>script.Delete(()=>this.forceUpdate())}/>
						: <Panel style={{marginLeft: 5, width: 28, height: 28}}/>}
				</Panel>
			</TouchableHighlight>
		);
	}
}

@Bind
export default class ScriptsPanel extends BaseComponent {
	render() {
		var {parent, scripts} = this.props;

		var scripts_map = scripts.ToMap(a=>a.file.Name, a=>a);
		var scriptNames_ordered = scripts.OrderBy(a=>a.index).Select(a=>a.file.Name);

		return (
			<Panel style={{flex: 1, flexDirection: "column", backgroundColor: colors.background_light}}>
				<Text style={{padding: 5, fontSize: 15}}>Scripts (drag to reorder; place dependencies first)</Text>
				<Panel style={{flex: 1}}>
					<SortableListView data={scripts_map} order={scriptNames_ordered}
						renderRow={script=><ScriptEntryUI parent={this} script={script}/>}
						onRowMoved={e=> {
							var movedEntryName = scriptNames_ordered.splice(e.from, 1)[0];
							scriptNames_ordered.Insert(e.to, movedEntryName);
							for (var i = 0; i < scripts.length; i++) {
								let scriptAtIndex_name = scriptNames_ordered[i];
								let script = scripts.First(a=>a.file.Name == scriptAtIndex_name);
								script.index = i;
							}
							this.forceUpdate();
						}}/>
					<VButton text="Add" style={{position: "absolute", top: (scripts.length * (40 + 1)) + 5, width: 100}}
						onPress={this.AddScript}/>
				</Panel>
			</Panel>
		)
	}
	async AddScript() {
		var fileName;
		for (var i = 2; i < 100; i++) {
			fileName = `Custom script ${i}.js`;
			if (!LL.scripts.scripts.Any(a=>a.file.Name == fileName))
				break;
		}

		var file = LL.RootFolder.GetFolder("Scripts").GetFile(fileName);
		var script = new Script(file, `Log("Hello world!");`);
		script.index = LL.scripts.scripts.length;
		LL.scripts.scripts.push(script);

		script.Save();

		this.forceUpdate();
	}
}