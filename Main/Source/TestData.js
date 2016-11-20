export default class TestData {
    static LoadInto(ld) {
		var vdf = `
LucidLink>{^}
	scripts:Scripts>{^}
	settings:Settings>{^}
		audioFiles:[^]
			{name:"Audio1" path:"FakePath"}
			{name:"Audio2" path:"FakePath"}
	about:About>{^}
		jsCode:""
`.trim();
		var ll = FromVDF(vdf);
		for (var propName in ll) {
			g.LL[propName] = ll[propName];
		}
	}
}