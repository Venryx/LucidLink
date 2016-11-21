import RNFS from "react-native-fs";

// file stuff
// ==========

g.VFile = class VFile {
	static get MainBundlePath() { return RNFS.MainBundlePath; }
	static get CachesDirectoryPath() { return RNFS.CachesDirectoryPath; }
	static get DocumentDirectoryPath() { return RNFS.DocumentDirectoryPath; }
	static get ExternalDirectoryPath() { return RNFS.ExternalDirectoryPath; }
	static get ExternalStorageDirectoryPath() { return RNFS.ExternalStorageDirectoryPath; }
	static get TemporaryDirectoryPath() { return RNFS.TemporaryDirectoryPath; }
	static get LibraryDirectoryPath() { return RNFS.LibraryDirectoryPath; }
	static get PicturesDirectoryPath() { return RNFS.PicturesDirectoryPath; }
}

g.Folder = class Folder {
	constructor(path) {
		this.path = path;
	}

	path = null;
	get Path() { return this.path.replace(/\\/g, "/"); }	

	async Exists() {
		return await RNFS.exists(this.path);
	}
	async Create() {
		return RNFS.mkdir(this.path);
	}

	async GetFiles() {
		var subs = await RNFS.readDir(this.path);
		var result = [];
		for (let sub of subs) {
			let subInfo = await RNFS.stat(sub.path);
			if (subInfo.isFile()) {
				let file = new File(sub.path);
				result.push(file);
			}
		}
		return result;
	}

	GetFolder(subpath) {
		return new Folder(this.path + "/" + subpath);
	}
	GetFile(subpath) {
		return new File(this.path + "/" + subpath);
	}
}
g.File = class File {
	constructor(path) {
		this.path = path;
	}

	path = null;
	get Path() { return this.path.replace(/\\/g, "/"); }
	get Folder() {
		return new Folder(this.path.substr(0, this.Path.lastIndexOf("/") + 1));
	}
	get Name() {
		return this.path.substr(this.Path.lastIndexOf("/") + 1);
	}
	get NameWithoutExtension() {
		return this.Name.substr(0, this.Name.lastIndexOf("."));
	}
	get Extension() {
		return this.Name.substr(this.Name.lastIndexOf(".") + 1);
	}

	async Exists() {
		return await RNFS.exists(this.path);
	}

	async Delete() {
		return RNFS.unlink(this.path);
	}
	async WriteAllText(text, encoding = "utf8") {
		return RNFS.writeFile(this.path, text, encoding);
	}
	async AppendText(text, encoding = "utf8") {
		return RNFS.appendFile(this.path, text, encoding);
	}
	async ReadAllText(encoding = "utf8") {
		//if (error.toString().contains("ENOENT: no such file or directory, open "))
		return RNFS.readFile(this.path, encoding);
	}
}