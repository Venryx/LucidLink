import React, {Component} from "react";
import {Dimensions, StyleSheet,
	View, Button, Text, TextInput} from "react-native";
import RNFS from "react-native-fs";
var ScrollableTabView = require("react-native-scrollable-tab-view");
//var {JavaBridge, BaseComponent, VFile} = require("./Globals");

g.Settings = class Settings {
	audioFiles = [];
}

class Row extends BaseComponent {
	render() {
		var {style, height, children} = this.props;
		return (
			<View style={E({flexDirection: "row", padding: 3}, style,
					height != null ? {height} : {flex: 1})}>
				{children}
			</View>
		);
	}
}
class RowLR extends BaseComponent {
    render() {
		var {leftStyle, rightStyle, children} = this.props;
        Assert(children.length == 2, "Row child-count must be 2. (one for left-side, one for right-side)");
        return (
			<View style={{flex: 1, flexDirection: "row", padding: 3}}>
				<View style={E({alignItems: "flex-start", flex: 1}, leftStyle)}>
					{children[0]}
				</View>
				<View style={E({alignItems: "flex-end", flex: 1}, rightStyle)}>
					{children[1]}
				</View>
			</View>
        );
    }
}

export class SettingsUI extends BaseComponent {
	render() {
		var {scriptTexts} = this.state;
		var node = LL.settings;
		
		return (
			<View style={{flex: 1}}>
				<Row style={{flex: 1, flexDirection: "column"}}>
					<Row height={30}><Text>Audio files</Text></Row>
					<Row style={{flex: 1, flexDirection: "column"}}>
						{node.audioFiles.map((audioFile, index)=> {
							return (
								<Row key={index} height={30}>
									<Text style={{flex: 1}}>{audioFile.name}</Text>
									<Text style={{flex: 1}}>{audioFile.path}</Text>
									<VButton color="#777" title="X" style={{alignItems: "flex-end", marginTop: -3, paddingBottom: -5}}
										onPress={()=>node.audioFiles.Remove(audioFile) | this.forceUpdate()}/>
								</Row>
							);
						})}
						<Row height={50}>
							<VButton color="#777" onPress={()=>this.AddAudioFile()} title="Add"/>
						</Row>
					</Row>
					<View style={{flex: 1000}}>
					</View>
				</Row>
            </View>
		);
	}

	AddAudioFile() {

	}
}