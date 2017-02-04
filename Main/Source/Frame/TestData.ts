import {FromVDF, FromVDFToNode, FromVDFNode} from "./Globals";
import {LL} from "../LucidLink";
import {VDFNode} from "../Packages/VDF/VDFNode";
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
		var node: VDFNode = FromVDFToNode(vdf);
		var ll = FromVDFNode(node);
		for (var propName of node.mapChildren.keys as any[]) {
			LL[propName] = ll[propName];
		}
	}
}