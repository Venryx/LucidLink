import V from "../../Packages/V/V";
import {E, IsBool, IsNumber, IsString, Toast} from "../Globals";
import {BaseComponent as Component, Row, Column} from "../ReactGlobals";
//import TreeView from 'react-native-treeview'
import TreeView from "../../Frame/Components/TreeView";
import {View, Text} from "react-native";
import {Dictionary, List} from "../../Packages/VDF/VDFExtras";
import {D, P} from "../../Packages/VDF/VDFTypeInfo";

export default class ObjectInspectorUI extends Component<
		{object, objectKey?: string, style?, textElementStyle?, titleModifierFunc: Function, keyModifierForTreeStateFunc: Function},
		{loadChildren: boolean}> {
	static LiteralValueToDisplayText(val) {
	    if (val == null)
			return "nothing";
	    if (IsString(val))
	        return `"${val}"`;
	    return val.toString(); // toString() already works correctly for true, false, and numbers
	}
	
	constructor(props) {
	    super(props);
		this.state = {loadChildren: false};
	}

	treeView: TreeView = null;
	childUIs = [];

	render() {
		var {object, objectKey: objKey, style, textElementStyle, titleModifierFunc, keyModifierForTreeStateFunc} = this.props;
		var props_subInspectorPassthroughs = {textElementStyle, titleModifierFunc, keyModifierForTreeStateFunc};
		var {loadChildren} = this.state;
	    var obj = V.CloneObject(object);
		var title = objKey || (obj != null ? obj._title : null);
	    if (titleModifierFunc)
	        title = titleModifierFunc(title);

		var isPrimitive = obj == null || IsBool(obj) || IsNumber(obj) || IsString(obj); //typeof obj == "string" || typeof obj == "number" || obj == null; //obj instanceof Array || obj instanceof Object;
		var hasContent = obj && (obj instanceof Array || obj instanceof Dictionary || obj.Props.length);
	    var hasExpandForChildren = !isPrimitive && hasContent;

	    /*var selfUI = (
			<View style={{flex: 1, height: 100}}>
				{title &&
					<Text style={E({whiteSpace: "pre"}, textElementStyle)}>
						{title + (isPrimitive || obj._title ? ": " : "")}
					</Text>}
				{!hasExpandForChildren && (
					isPrimitive
						? <Text style={textElementStyle}>{ObjectInspectorUI.LiteralValueToDisplayText(obj)}</Text>
						: <Text style={textElementStyle}>{obj instanceof List ? "[]" : "{}"}</Text>
				)}
			</View>
		);*/
		var selfUIText = ">";
		if (title)
			selfUIText += title + (isPrimitive || obj._title ? ": " : "");
		if (!hasExpandForChildren)
			selfUIText += isPrimitive ? ObjectInspectorUI.LiteralValueToDisplayText(obj) : obj instanceof List ? "[]" : "{}";
		//Toast(selfUIText);
		var selfUI = <Text>{selfUIText}</Text>;

	    this.childUIs = [];
		return (
			<TreeView ref={c=>this.treeView = c} style={style}
					titleElement={selfUI} titleStyle={textElementStyle}
					defaultCollapsed={true} collapsible={hasExpandForChildren}
					onArrowPress={collapsed=>!collapsed && this.setState({loadChildren: true})}>
				{loadChildren &&
					<Column ml={10}>
						{obj instanceof Array ? obj.map((item, index)=> {
					        return <ObjectInspectorUI {...props_subInspectorPassthroughs} ref={c=>this.childUIs[index] = c}
								key={index} object={item} objectKey={index.toString()}/>;
					    }) :
						obj instanceof Dictionary ? obj.Pairs.Where(a=>a.key != "_title").map(pair=> {
						    return <ObjectInspectorUI {...props_subInspectorPassthroughs} ref={c=>this.childUIs[pair.index] = c}
								key={pair.key} object={pair.value} objectKey={pair.key}/>;
						}) :
						obj && obj.Props.Where(a=>a.name != "_title").map(prop=> {
						    return <ObjectInspectorUI {...props_subInspectorPassthroughs} ref={c=>this.childUIs[prop.index] = c}
								key={prop.name} object={prop.value} objectKey={prop.name}/>;
						})}
					</Column>}
			</TreeView>
		);
	}

	GetTreeState() {
		var {keyModifierForTreeStateFunc} = this.props;

	    var result = new TreeNodeState();
		result.expanded = this.treeView.state.collapsed;
		if (result.expanded) {
			for (let childUI of this.childUIs) {
				if (childUI == null) return;
		        let childUI_treeState = childUI.GetTreeState();
			    if (childUI_treeState.expanded) {
					var childKey = childUI.props.objectKey;
					if (keyModifierForTreeStateFunc)
						childKey = keyModifierForTreeStateFunc(childKey);
			        result.children.Add(childKey, childUI_treeState);
			    }
			}
		}
	    return result;
	}
	LoadTreeState(treeState) {
		var {loadChildren} = this.state;
		// if we're supposed to be expanded, but our children haven't even been loaded yet, load them, then re-call this function
		if (treeState.expanded && loadChildren == false) {
		    this.setState({loadChildren: true}, ()=> {
		        this.LoadTreeState(treeState);
		    });
		}

		var {keyModifierForTreeStateFunc} = this.props;
		this.treeView.setState({collapsed: !treeState.expanded});
	    for (let childUI of this.childUIs) {
	        if (childUI == null) return;
	        var childKey = childUI.props.objectKey;
	        if (keyModifierForTreeStateFunc)
	            childKey = keyModifierForTreeStateFunc(childKey);
	        var childState = treeState.children.Get(childKey);
	        //childState = childState || new TreeNodeState(); // old; if no state saved, load default state (collapsed)
			if (childState != null)
				childUI.LoadTreeState(childState);
	    }
	}
}

class TreeNodeState {
    @P() @D() expanded = false;
    @P() @D(D.NullOrEmpty) children = new Dictionary("string", "TreeNodeState");
};