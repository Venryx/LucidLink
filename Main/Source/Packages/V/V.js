g.V = class V {
	// example:
	// var multilineText = V.Multiline(function() {/*
	//		Text that...
	//		spans multiple...
	//		lines.
	// */});
	static Multiline(functionWithInCommentMultiline, useExtraPreprocessing) {
		useExtraPreprocessing = useExtraPreprocessing != null ? useExtraPreprocessing : true;

		var text = functionWithInCommentMultiline.toString().replace(/\r/g, "");

		// some extra preprocessing
		if (useExtraPreprocessing) {
			text = text.replace(/@@.*/g, ""); // remove single-line comments
			//text = text.replace(/@\**?\*@/g, ""); // remove multi-line comments
			text = text.replace(/@\*/g, "/*").replace(/\*@/g, "*/"); // fix multi-line comments
		}

		var firstCharPos = text.indexOf("\n", text.indexOf("/*")) + 1;
		return text.substring(firstCharPos, text.lastIndexOf("\n"));
	};
	static Multiline_NotCommented(functionWithCode) {
		var text = functionWithCode.toString().replace(/\r/g, "");
		var firstCharOfSecondLinePos = text.indexOf("\n") + 1;
		var enderOfSecondLastLine = text.lastIndexOf("\n");
		var result = text.substring(firstCharOfSecondLinePos, enderOfSecondLastLine);
		// replace the start and end tokens of special string-containers (used for keeping comments in-tact)
		result = result.replace(/'@((?:.|\n)+)@';\n/g, (match, sub1)=>sub1.replace(/\\n/, "\n"));
		return result;
	}
};