import React, {Component} from "react";
import {Dimensions, StyleSheet,
	View, Button, Text, Switch, TextInput} from "react-native";
import RNFS from "react-native-fs";
var ScrollableTabView = require("react-native-scrollable-tab-view");
//var {JavaBridge, BaseComponent, VFile} = require("./Globals");

var FilePickerManager = NativeModules.FilePickerManager;

g.Settings = class Settings extends Node {
	@P() applyScriptsOnLaunch = false;
	@P() blockUnusedKeys = false;
	//@P() captureSpecialKeys = false;
	@T("List(AudioFileEntry)") @P(true, true) audioFiles = [];
}
g.AudioFileEntry = class AudioFileEntry {
	@P() name = null;
	@P() path = null;
}

export class SettingsUI extends BaseComponent {
	render() {
		var node = LL.settings;
		
		return (
			<View style={{flex: 1}}>
				<Row style={{flex: 1, flexDirection: "column"}}>
					<RowLR height={25}>
						<Text>Apply scripts on launch</Text>
						<Switch value={node.applyScriptsOnLaunch}
							onValueChange={value=>{
								node.applyScriptsOnLaunch = value;
								this.forceUpdate();
							}}/>
					</RowLR>
					<RowLR height={25}>
						<Text>Block unused keys</Text>
						<Switch value={node.blockUnusedKeys}
							onValueChange={value=>{
								node.blockUnusedKeys = value;
								LL.PushBasicDataToJava();
								this.forceUpdate();
							}}/>
					</RowLR>
					<Row height={30} style={{position: "absolute"}}><Text>Audio files</Text></Row>
					<Row style={{marginTop: 10, flex: 1, flexDirection: "column"}}>
						{node.audioFiles.map((audioFile, index)=> {
							return (
								<Row key={index} height={35}>
									<TextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
										editable={true} value={audioFile.name}
										onChangeText={text=>(audioFile.name = text) | this.forceUpdate()}/>
									<TextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
										editable={true} value={audioFile.path}
										onChangeText={text=>(audioFile.path = text) | this.forceUpdate()}/>
									<VButton text="Select" style={{alignItems: "flex-end", width: 100, height: 28}} textStyle={{marginBottom: 3}}
										onPress={()=>this.SelectAudioFileForEntry(audioFile)}/>
									<VButton text="X" style={{alignItems: "flex-end", marginLeft: 5, width: 28, height: 28}} textStyle={{marginBottom: 3}}
										onPress={()=>node.audioFiles.Remove(audioFile) | this.forceUpdate()}/>
								</Row>
							);
						})}
						<Row height={40}>
							<VButton onPress={()=>this.AddAudioFile()} text="Add" style={{width: 100, height: 40}}/>
						</Row>
					</Row>
					<View style={{flex: 111222}}>
					</View>
				</Row>
            </View>
		);
	}

	SelectAudioFileForEntry(entry) {
		const options = {
			title: "Select audio file",
			chooseFileButtonTitle: "Select"
		};

		FilePickerManager.showFilePicker(options, response=> {
			if (response.didCancel) return;
			if (response.error) {
				console.log("FilePicker error:", response.error);
				return;
			}
			entry.path = response.path;
			Log("New content0:" + ToVDF(response.path));
			Log("New content1:" + ToVDF(entry.path));
			Log("New content1:" + ToVDF(entry));
			Log("New contents:" + ToVDF(LL.settings.audioFiles));
			this.forceUpdate();
		});
	}

	AddAudioFile() {
		LL.settings.audioFiles.push({name: "none", path: "none"});
		this.forceUpdate();
	}
}