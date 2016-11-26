export default class AudiosUI extends BaseComponent { 
	render() {
		var node = LL.settings;
		return (
			<View style={{flex: 1}}>
				<Row style={{flex: 1, flexDirection: "column"}}>
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
							<VButton onPress={()=>this.CreateAudioFile()} text="Create" style={{width: 100, height: 40}}/>
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

	CreateAudioFile() {
		LL.settings.audioFiles.push({name: "none", path: "none"});
		this.forceUpdate();
	}
}