import {A, FromJSON, Log, ToJSON} from "../../Frame/Globals";
import DialogAndroid from "react-native-dialogs";
import {LL} from "../../LucidLink";

export class Script {
	static async Load(file) {
		let scriptText = await file.ReadAllText();
		var result = new Script(file, scriptText);
		await result.LoadMeta();
		return result;
	}
	async LoadMeta() {
		if (!await this.MetaFile.Exists()) return;
		let scriptMetaJSON = await this.MetaFile.ReadAllText();
		try {
			var scriptMeta = FromJSON(scriptMetaJSON);
			this.index = scriptMeta.index;
			this.editable = scriptMeta.editable;
			this.enabled = scriptMeta.enabled;
		} catch (ex) {
			Log(`Meta file for script "${this.Name}" invalid.
Meta file JSON: ${scriptMetaJSON}
Error: ${ex.stack}`);
		}
	}

	async Save() {
		await this.file.WriteAllText(this.text);
		await this.SaveMeta();
		this.fileOutdated = false;
	}
	async SaveMeta() {
		var scriptMeta = {index: this.index, editable: this.editable, enabled: this.enabled};
		var scriptMetaJSON = ToJSON(scriptMeta);
		await this.MetaFile.WriteAllText(scriptMetaJSON);
	}

	Delete(onDelete = null) {
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Delete script "${this.file.NameWithoutExtension}"`,
			"content": `Permanently delete script?`,
			"positiveText": "OK", "negativeText": "Cancel",
			"onPositive": ()=> {
				if (this.DisplayerScript) {
					A.NonNull = LL.tracker.displayerScripts.Remove(this);
					//LL.tracker.displayerScripts.remove(this);
					if (LL.tracker.selectedDisplayerScript == this)
						LL.tracker.selectedDisplayerScript = null;
				} else {
					A.NonNull = LL.scripts.scripts.Remove(this);
					if (LL.scripts.selectedScript == this)
						LL.scripts.selectedScript = null;
				}
				this.file.Delete();
				this.MetaFile.Delete();
				onDelete && onDelete();
			},
		});
		dialog.show();
	}
	Rename(onRename = null) {
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Rename script "${this.file.NameWithoutExtension}"`,
			"input": {
				prefill: this.file.NameWithoutExtension,
				callback: newName=> {
					this.file.Delete();
					this.file = this.file.Folder.GetFile(newName + ".js");
					this.Save();
					onRename && onRename();
				}
			},
			"positiveText": "OK",
			"negativeText": "Cancel"
		});
		dialog.show();
	}

	constructor(file, text) {
		this.file = file;
		this.text = text;
	}

	file = null;
	get DisplayerScript() { return this.file.Folder.Name == "Displayer scripts"; }
	fileOutdated = false;
	get Name() { return this.file.NameWithoutExtension; }
	get MetaFile() { return this.file.Folder.GetFile(this.file.NameWithoutExtension + ".meta"); }

	@O text = null;

	// stored in meta file
	index = -1000;
	editable = true;
	enabled = true;
}