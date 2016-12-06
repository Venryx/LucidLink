var DialogAndroid = require("react-native-dialogs");
import Moment from "moment";

@Bind
class Session {
	static async Load(folder) {
		var mainFile = folder.GetFile("Main.vdf");
		var vdf = await mainFile.ReadAllText();

		var result = FromVDF(vdf, "Session");
		result.folder = folder;
		result.file = mainFile;
		result.date = Moment(folder.Name);

		result.logFile = folder.GetFile("Log.txt");

		return result;
	}

	async Save() {
		var vdf = ToVDF(this, false);
		await this.folder.Create();
		this.file.WriteAllText(vdf);
	}
	Delete(onDone = null) {
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Delete session for "${this.date.format("YYYY-MM-DD HH:mm:ss")}"`,
			"content": `Permanently delete session?`,
			"positiveText": "OK", "negativeText": "Cancel",
			"onPositive": ()=> {
				LL.tracker.loadedSessions.Remove(this);
				this.folder.Delete();
				onDone && onDone();
			},
		});
		dialog.show();
	}

	constructor(date) {
		// if called by VDF, do nothing
		if (date == null) return;

		Assert(date instanceof Moment, `Date must be an instance of the Moment class, not ${GetTypeName(date)}.`);
		this.date = date;
		var sessionsFolder = LL.RootFolder.GetFolder("Sessions");
		this.folder = sessionsFolder.GetFolder(this.date.format("YYYY-MM-DD HH:mm:ss"));
		this.file = this.folder.GetFile("Main.vdf");

		this.logFile = this.folder.GetFile("Log.txt");
	}

	folder = null;
	file = null;
	date = null;

	logFile = null;
	@P() events = [];
}
g.Session = Session;

class Event {
	type = null;
	args = [];
}
g.Event = Event;