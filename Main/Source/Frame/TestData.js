// #mms: renamed StartData
export default class TestData {
    static LoadInto(ld) {
		var vdf = `
LucidLink>{^}
	scripts:Scripts>{^}
	settings:Settings>{^}
		audioFiles:[^]
			{name:"air raid siren" path:"FakePath"}
	more:More>{^}
		jsCode:""
`.trim();
		var ll = FromVDF(vdf);
		for (var propName in ll) {
			g.LL[propName] = ll[propName];
		}
	}
}