export default class TestData {
    static LoadInto(ld) {
		ld.settings = new Settings();
		ld.settings.audioFiles = [
			{name: "Audio1", path: "FakePath"},
			{name: "Audio2", path: "FakePath"},
		];
	}
}