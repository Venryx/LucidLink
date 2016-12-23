import {DN} from "../../Frame/Globals";
import {colors} from "../../Frame/Styles";
import {BaseComponent, Panel, VButton} from "../../Frame/ReactGlobals";
import {Text, TouchableHighlight, Switch} from "react-native";
import {Pattern} from "../Settings";
import Bind from "autobind-decorator";
import {LL} from "../../LucidLink";
var SortableListView = require("react-native-sortable-listview");

@Bind
class PatternEntryUI extends BaseComponent<any, any> {
	render() {
		var {parent, pattern} = this.props;
		var patternsUI = parent.props.parent;
		return (
			<TouchableHighlight {...this.props.sortHandlers} underlayColor="#EEE" delayLongPress={300}
					style={{backgroundColor: colors.background_lighter, borderBottomWidth: 1, borderColor: colors.background_light}}
					onPress={()=>patternsUI.SelectPattern(pattern)}>
				<Panel style={{height: 40, paddingLeft: 10, paddingRight: 10, flexDirection: "row"}}>
					<Text style={{paddingTop: 10}}>{pattern.name}</Text>
					<Panel style={{flex: 1}}/>
					<Switch value={pattern.enabled}
						onValueChange={value=>DN(pattern.enabled = value, this.forceUpdate())}/>
					<VButton text="X"
						style={{alignItems: "flex-end", marginLeft: 5, marginTop: 6, width: 28, height: 28}}
						textStyle={{marginBottom: 3}} onPress={()=>pattern.Delete()}/>
				</Panel>
			</TouchableHighlight>
		);
	}
}

@Bind
export default class PatternsPanel extends BaseComponent<any, any> {
	render() {
		var {parent, patterns} = this.props;
		
		return (
			<Panel style={{flex: 1, flexDirection: "column", backgroundColor: colors.background_light}}>
				<Text style={{padding: 5, fontSize: 15}}>Patterns (drag to reorder)</Text>
				<Panel style={{flex: 1}}>
					<SortableListView data={patterns.ToMap(a=>a.name, a=>a)} order={patterns.Select(a=>a.name)}
						renderRow={pattern=><PatternEntryUI parent={this} pattern={pattern}/>}
						onRowMoved={e=> {
							/*var movedEntryName = patternNames_ordered.splice(e.from, 1)[0];
							patternNames_ordered.Insert(e.to, movedEntryName);
							for (var i = 0; i < patterns.length; i++) {
								let patternAtIndex_name = patternNames_ordered[i];
								let pattern = patterns.First(a=>a.name == patternAtIndex_name);
								pattern.index = i;
							}*/

							var removedEntry = LL.settings.patterns.splice(e.from, 1)[0];
							LL.settings.patterns.Insert(e.to, removedEntry);

							this.forceUpdate();
						}}/>
					<VButton text="Add" style={{position: "absolute", top: (patterns.length * (40 + 1)) + 5, width: 100}}
						onPress={this.AddPattern}/>
				</Panel>
			</Panel>
		)
	}
	async AddPattern() {
		var name;
		for (var i = 1; i < 100; i++) {
			name = `New pattern ${i}`;
			if (!LL.settings.patterns.Any(a=>a.name == name))
				break;
		}

		LL.settings.patterns.push(new Pattern(name));
		
		this.forceUpdate();
	}
}