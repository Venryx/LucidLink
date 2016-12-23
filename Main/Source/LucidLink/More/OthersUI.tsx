import {BaseComponent as Component, Panel, Row, VButton, Column} from "../../Frame/ReactGlobals";
import {colors} from "../../Frame/Styles";
import {LL} from "../../LucidLink";
import {Profiler_AllFrames} from "../../Frame/VProfiler";
import BlockRunInfo from "../../Frame/VProfiler/BlockRunInfo";
import ObjectInspectorUI from "../../Frame/VProfiler/ObjectInspectorUI";
import {FromVDF, Toast, ToJSON} from "../../Frame/Globals";

export default class OthersUI extends Component<any, any> {
	render() {
		var {profiler_allFrames_data} = this.state;
		return (
			<Column style={{flex: 1, backgroundColor: colors.background}}>
				<Row>
					<VButton text='Reset "built-in script"' style={{width: 300}}
						onPress={()=>LL.scripts.ResetScript("Built-in script")}/>
					<VButton text='Reset "fake-data provider"' style={{marginLeft: 5, width: 300}}
						onPress={()=>LL.scripts.ResetScript("Fake-data provider")}/>
					<VButton text='Reset "custom script"' style={{marginLeft: 5, width: 300}}
						onPress={()=>LL.scripts.ResetScript("Custom script")}/>
				</Row>
				<Row>
					<VButton text="Refresh profiler data" style={{width: 300}} onPress={this.AllFrames_Refresh}/>
				</Row>
				<ObjectInspectorUI object={profiler_allFrames_data}
					titleModifierFunc={this.TitleModifierFunc}
					keyModifierForTreeStateFunc={this.KeyModifierForTreeStateFunc}/>
			</Column>
		);
	}

	GetFakeProfilerData() {
	    var result = FromVDF(`
{children:{^}}
	Test1:{name:"Test1" runCount:20 runTime:500 children:{^}}
		work toward tasks:{name:"work toward tasks" method:false runCount:10 runTime:300 children:{}}
		A:{name:"A" runCount:10 runTime:100.5 children:{}}
		other stuff:{name:"other stuff" method:false runCount:10 runTime:200 children:{}}
		B:{name:"B" runCount:10 runTime:100 children:{^}}
		C:{name:"C" runCount:10 runTime:100 children:{}}
	Test2:{name:"Test2" runCount:10 runTime:100 children:{}}
`.trim());
	    return result;
	}
	ProcessBlockRunInfo(blockRunInfo, rootBlockRunInfo = true, depth = 0) {
		if (depth > 10) return Toast(`Profiler data too deep! (${depth})`), {};

		var result = {};
		for (let {key, value: child} of blockRunInfo.children.Pairs) {
			var newKey = `${child.method ? "@" : ""}${key}   (x${child.runCount})   (${child.runTime}ms)`;
			result[newKey] = this.ProcessBlockRunInfo(child, false, depth + 1);
		}
		return result;
	}

	KeyModifierForTreeStateFunc(key) {
	    var keyBaseEnderPos = key.indexOf("   (x");
	    if (keyBaseEnderPos == -1) return key;
	    return key.substr(0, keyBaseEnderPos);
	}
	TitleModifierFunc(title) {
	    var result = title;
		if (title) {
		    var [, callCountStr, totalTimeStr] = title.match(/\(x(.+?)\)   \((.+?)ms\)/);
		    var msPerCall = parseFloat(totalTimeStr) / parseFloat(callCountStr);
		    result += `   (${msPerCall}ms per call)`;
		}
	    return result;
	}

	// all frames
	// ==========

	AllFrames_Refresh() {
		var profilerData = Profiler_AllFrames.rootBlockRunInfo;
		profilerData = this.GetFakeProfilerData(); // for testing
		var profilerData_final = this.ProcessBlockRunInfo(profilerData);
		this.setState({profiler_allFrames_data: profilerData_final});
	}
	AllFrames_Clear() {
	    Profiler_AllFrames.ResetRootBlockRunInfo();
	}

	/*AllFrames_SaveExpansions() {
	    var treeState = this.refs.allFrames_inspectorUI.GetTreeState();
	    BD.console.a("profiler_allFrames_rootTreeNodeState").set = treeState;
	}
	AllFrames_LoadExpansions() {
	    var treeState = BD.console.profiler_allFrames_rootTreeNodeState;
		this.refs.allFrames_inspectorUI.LoadTreeState(treeState);
	}*/
}