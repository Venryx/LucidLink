export default class DisplayersUI extends BaseComponent {
	@Bind ToggleSidePanelOpen() {
		if (this._drawer._open)
			this._drawer.close();
		else
			this._drawer.open();
	}

	SelectScript(script) {
		LL.tracker.selectedDisplayerScript = script;
		this.forceUpdate();
		this._drawer.close();
	}

	render() {
		var node = LL.tracker;
		var {selectedDisplayerScript} = node;
		var {scriptLastRunsOutdated} = this.state;
		
		const drawerStyles = {
			drawer: {shadowColor: "#000000", shadowOpacity: .8, shadowRadius: 3},
			main: {paddingLeft: 3},
		};

		return (
			<Drawer ref={comp=>this._drawer = comp}
					content={<DisplayerScriptsPanel scripts={node.displayerScripts} selectedScript={node.selectedDisplayerScript}/>}
					type="overlay" openDrawerOffset={0.5} panCloseMask={0.5} tapToClose={true}
					closedDrawerOffset={-3} styles={drawerStyles}>
				<Panel style={{flex: 1, flexDirection: "column", backgroundColor: colors.background}}>
					<Panel style={E(styles.header, {flexDirection: "row", flexWrap: "wrap", padding: 3, paddingBottom: -5})}>
						<VButton text="Displayer scripts" style={{width: 100}} onPress={this.ToggleSidePanelOpen}/>
						<Text style={{marginLeft: 10, marginTop: 8, fontSize: 18}}>
						Script: {selectedScript ? selectedScript.file.NameWithoutExtension : "n/a"}
						{selectedScript && !selectedScript.editable ? " (read only)" : ""}
						</Text>
						{selectedScript && selectedScript.editable &&
							<VButton text="Rename" style={{marginLeft: 10, width: 100}}
								onPress={()=>selectedScript.Rename(()=>this.forceUpdate())}/>}
						<Panel style={{flex: 1}}/>
						<Panel style={{flexDirection: "row", alignItems: "flex-end"}}>
							<VButton color="#777" text="Save" enabled={selectedScript != null && selectedScript.fileOutdated}
								style={{width: 100, marginLeft: 5}}
								onPress={()=>selectedScript.Save().then(()=>this.forceUpdate())}/>
							<VButton color="#777" text="Apply all"
								//enabled={scriptLastRunsOutdated}
								enabled={true}
								style={{width: 100, marginLeft: 5}}
								onPress={()=>node.ApplyDisplayerScripts()}/>
						</Panel>
					</Panel>
					<Panel style={{marginTop: -7, flex: 1}}>
						<ScriptTextUI parent={this} text={selectedScript ? selectedScript.text : ""}
							//editable={selectedScript ? selectedScript.editable : false}
							editable={selectedScript != null}
							onChangeText={text=> {
								if (!selectedScript.editable) return;
								selectedScript.text = text;
								selectedScript.fileOutdated = true;
								this.forceUpdate();
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