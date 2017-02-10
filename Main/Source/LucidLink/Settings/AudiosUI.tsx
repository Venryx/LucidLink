import {BaseComponent, Column, Panel, Row, VButton} from "../../Frame/ReactGlobals";
import {colors} from "../../Frame/Styles";
import {Log, DN} from "../../Frame/Globals";
import {NativeModules} from "react-native";
import {TextInput} from "react-native";
import {LL} from "../../LucidLink";
var FilePickerManager = NativeModules.FilePickerManager;

export default class AudiosUI extends BaseComponent<any, {}> {
	render() {
		var node = LL.settings;
		return (
			<Column style={{flex: 1, backgroundColor: colors.background, padding: 10}}>
				{node.audioFiles.map((audioFile, index)=> {
					return (
						<Row key={index} height={35}>
							<TextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
								editable={true} value={audioFile.name}
								onChangeText={text=>DN(audioFile.name = text, this.forceUpdate())}/>
							<TextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
								editable={true} value={audioFile.path}
								onChangeText={text=>DN(audioFile.path = text, this.forceUpdate())}/>
							<VButton text="Select" style={{alignItems: "flex-end", width: 100, height: 28}} textStyle={{marginBottom: 3}}
								onPress={()=>this.SelectAudioFileForEntry(audioFile)}/>
							<VButton text="X" style={{alignItems: "flex-end", marginLeft: 5, width: 28, height: 28}} textStyle={{marginBottom: 3}}
								onPress={()=>DN(node.audioFiles.Remove(audioFile), this.forceUpdate())}/>
						</Row>
					);
				})}
				<Row height={45}>
					<VButton onPress={()=>this.CreateAudioFile()} text="Create" style={{width: 100, height: 40}}/>
				</Row>
				<Panel style={{flex: 111222}}/>
			</Column>
		);
	}

	SelectAudioFileForEntry(entry) {
		const options = {
			title: "Select audio file",
			chooseFileButtonTitle: "Select"
		};

		FilePickerManager.showFilePicker(options, async response=> {
			if (response.didCancel) return;
			if (response.error) {
				Log("FilePicker error:", response.error);
				return;
			}

			var path = response.path;
			// if response contains only the uri, convert that to a path, then continue
			/*if (path == null)
				path = await JavaBridge.Main.ConvertURIToPath(response.uri);*/
			// if response contains only the uri, extract path from it (not sure if this works in every case...)
			if (path == null && response.uri.contains("%2Fstorage%2F")) {
				path = decodeURIComponent(response.uri.substr(response.uri.indexOf("%2Fstorage%2F")));
			}

			entry.path = path;
			//Log(`Response: ${ToJSON(response)} Path: ${path}`);
			this.forceUpdate();
		});
	}

	CreateAudioFile() {
		LL.settings.audioFiles.push({name: "none", path: "none"});
		this.forceUpdate();
	}
}