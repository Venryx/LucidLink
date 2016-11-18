import React, {Component} from "react";
import {Dimensions, StyleSheet,
	Text, TextInput, View} from "react-native";
var ScrollableTabView = require("react-native-scrollable-tab-view");

var {JavaBridge, BaseComponent} = require("./Globals");

//let screenHeight = Dimensions.get("window").height;
var styles = StyleSheet.create({
	root: {},
	tab: {flex: 1},
	text: {
		//height: screenHeight,
		flex: 1, textAlignVertical: "top",
	},
});

class ScriptTextUI extends BaseComponent {
	static defaultProps = {editable: true};
	render() {
		var {editable, onChangeText} = this.props;
		return <TextInput {...{editable, onChangeText}} style={styles.text} multiline={true} editable={editable}/>;
	}
}

export default class ScriptsUI extends BaseComponent {
	componentWillMount() {
		this.LoadScriptTexts();
	}
	async LoadScriptTexts() {
		var scriptTexts = await JavaBridge.Main.LoadScriptTexts();
		this.setState({scriptText1: scriptTexts[0], scriptText2: scriptTexts[1],
			scriptText3: scriptTexts[2], scriptText4: scriptTexts[3], scriptText5: scriptTexts[4]});
	}
	render() {
		var {scriptText1, scriptText2, scriptText3, scriptText4, scriptText5} = this.state;
		return (
			<ScrollableTabView>
                <View style={styles.tab} tabLabel="1: Built-in functions">
					<ScriptTextUI text={scriptText1} editable={false}/>
                </View>
                <View style={styles.tab} tabLabel="2: Built-in helpers">
					<ScriptTextUI text={scriptText2} editable={false}/>
                </View>
                <View style={styles.tab} tabLabel="3: Built-in script">
					<ScriptTextUI text={scriptText3} onChangeText={text=>this.setState({script3Text: text})}/>
                </View>
                <View style={styles.tab} tabLabel="4: Custom helpers">
					<ScriptTextUI text={scriptText4} onChangeText={text=>this.setState({script4Text: text})}/>
                </View>
                <View style={styles.tab} tabLabel="5: Custom script">
					<ScriptTextUI text={scriptText5} onChangeText={text=>this.setState({script5Text: text})}/>
                </View>
            </ScrollableTabView>
		);
	}
	componentWillUnmount() {
		var {scriptText1, scriptText2, scriptText3, scriptText4, scriptText5} = this.state;
		JavaBridge.Main.SaveScriptTexts([scriptText1, scriptText2, scriptText3, scriptText4, scriptText5]);
	}
}