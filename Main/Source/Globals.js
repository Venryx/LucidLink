import React, {Component} from "react";

export class JavaBridge {
    static get Main() {
        return NativeModules.Main;
    }
}

export class BaseComponent extends Component {
	constructor(props) {
		super(props);
		this.state = {};
	}
}