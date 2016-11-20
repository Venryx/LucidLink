var g = global;
//var VDFTokenType;
(function (VDFTokenType) {
    //WiderMetadataEndMarker,
    //MetadataBaseValue,
    //LiteralStartMarker, // this is taken care of within the TokenParser class, so we don't need a passable-to-the-outside enum-value for it
    //LiteralEndMarker
    //DataPropName,
    //DataStartMarker,
    //PoppedOutDataStartMarker,
    //PoppedOutDataEndMarker,
    //ItemSeparator,
    //DataBaseValue,
    //DataEndMarker,
    //Indent,
    // helper tokens for token-parser (or reader)
    VDFTokenType[VDFTokenType["LiteralStartMarker"] = 0] = "LiteralStartMarker";
    VDFTokenType[VDFTokenType["LiteralEndMarker"] = 1] = "LiteralEndMarker";
    VDFTokenType[VDFTokenType["StringStartMarker"] = 2] = "StringStartMarker";
    VDFTokenType[VDFTokenType["StringEndMarker"] = 3] = "StringEndMarker";
    VDFTokenType[VDFTokenType["InLineComment"] = 4] = "InLineComment";
    VDFTokenType[VDFTokenType["SpaceOrCommaSpan"] = 5] = "SpaceOrCommaSpan";
    VDFTokenType[VDFTokenType["None"] = 6] = "None";
    VDFTokenType[VDFTokenType["Tab"] = 7] = "Tab";
    VDFTokenType[VDFTokenType["LineBreak"] = 8] = "LineBreak";
    VDFTokenType[VDFTokenType["Metadata"] = 9] = "Metadata";
    VDFTokenType[VDFTokenType["MetadataEndMarker"] = 10] = "MetadataEndMarker";
    VDFTokenType[VDFTokenType["Key"] = 11] = "Key";
    VDFTokenType[VDFTokenType["KeyValueSeparator"] = 12] = "KeyValueSeparator";
    VDFTokenType[VDFTokenType["PoppedOutChildGroupMarker"] = 13] = "PoppedOutChildGroupMarker";
    VDFTokenType[VDFTokenType["Null"] = 14] = "Null";
    VDFTokenType[VDFTokenType["Boolean"] = 15] = "Boolean";
    VDFTokenType[VDFTokenType["Number"] = 16] = "Number";
    VDFTokenType[VDFTokenType["String"] = 17] = "String";
    VDFTokenType[VDFTokenType["ListStartMarker"] = 18] = "ListStartMarker";
    VDFTokenType[VDFTokenType["ListEndMarker"] = 19] = "ListEndMarker";
    VDFTokenType[VDFTokenType["MapStartMarker"] = 20] = "MapStartMarker";
    VDFTokenType[VDFTokenType["MapEndMarker"] = 21] = "MapEndMarker";
})(g.VDFTokenType || (g.VDFTokenType = {}));
g.VDFToken = (function () {
    function VDFToken(type, position, index, text) {
        this.type = type;
        this.position = position;
        this.index = index;
        this.text = text;
    }
    return VDFToken;
}());
g.VDFTokenParser = (function () {
    function VDFTokenParser() {
    }
    VDFTokenParser.ParseTokens = function (text, options, parseAllTokens, postProcessTokens) {
        if (parseAllTokens === void 0) { parseAllTokens = false; }
        if (postProcessTokens === void 0) { postProcessTokens = true; }
        text = (text || "").replace(/\r\n/g, "\n"); // maybe temp
        options = options || new VDFLoadOptions();
        var result = new List("VDFToken");
        var currentTokenFirstCharPos = 0;
        var currentTokenTextBuilder = new StringBuilder();
        var currentTokenType = VDFTokenType.None;
        var activeLiteralStartChars = null;
        var activeStringStartChar = null;
        var lastScopeIncreaseChar = null;
        var addNextCharToTokenText = true;
        var specialEnderChar = '№';
        text += specialEnderChar; // add special ender-char, so don't need to use Nullable for nextChar var
        var ch;
        var nextChar = text[0];
        for (var i = 0; i < text.length - 1; i++) {
            ch = nextChar;
            nextChar = text[i + 1];
            if (addNextCharToTokenText)
                currentTokenTextBuilder.Append(ch);
            addNextCharToTokenText = true;
            if (activeLiteralStartChars != null) {
                // if first char of literal-end-marker
                if (ch == '>' && i + activeLiteralStartChars.length <= text.length && text.substr(i, activeLiteralStartChars.length) == activeLiteralStartChars.replace(/</g, ">")) {
                    if (parseAllTokens)
                        result.Add(new VDFToken(VDFTokenType.LiteralEndMarker, i, result.Count, activeLiteralStartChars.replace(/</g, ">"))); // (if this end-marker token is within a string, it'll come before the string token)
                    currentTokenTextBuilder.Remove(currentTokenTextBuilder.Length - 1, 1); // remove current char from the main-token text
                    currentTokenFirstCharPos += activeLiteralStartChars.length; // don't count this inserted token text as part of main-token text
                    i += activeLiteralStartChars.length - 2; // have next char processed be the last char of literal-end-marker
                    addNextCharToTokenText = false; // but don't have it be added to the main-token text
                    if (text[i + 1 - activeLiteralStartChars.length] == '#')
                        currentTokenTextBuilder.Remove(currentTokenTextBuilder.Length - 1, 1); // remove current char from the main-token text
                    activeLiteralStartChars = null;
                    nextChar = i < text.length - 1 ? text[i + 1] : specialEnderChar; // update after i-modification, since used for next loop's 'ch' value
                    continue;
                }
            }
            else {
                if (ch == '<' && nextChar == '<') {
                    activeLiteralStartChars = "";
                    while (i + activeLiteralStartChars.length < text.length && text[i + activeLiteralStartChars.length] == '<')
                        activeLiteralStartChars += "<";
                    if (parseAllTokens)
                        result.Add(new VDFToken(VDFTokenType.LiteralStartMarker, i, result.Count, activeLiteralStartChars));
                    currentTokenTextBuilder.Remove(currentTokenTextBuilder.Length - 1, 1); // remove current char from the main-token text
                    currentTokenFirstCharPos += activeLiteralStartChars.length; // don't count this inserted token text as part of main-token text
                    i += activeLiteralStartChars.length - 1; // have next char processed be the one right after literal-start-marker
                    if (text[i + 1] == '#') {
                        currentTokenFirstCharPos++;
                        i++;
                    }
                    nextChar = i < text.length - 1 ? text[i + 1] : specialEnderChar; // update after i-modification, since used for next loop's 'ch' value
                    continue;
                }
                // else
                {
                    if (activeStringStartChar == null) {
                        if (ch == '\'' || ch == '"') {
                            activeStringStartChar = ch;
                            if (parseAllTokens)
                                result.Add(new VDFToken(VDFTokenType.StringStartMarker, i, result.Count, activeStringStartChar));
                            currentTokenTextBuilder.Remove(currentTokenTextBuilder.Length - 1, 1); // remove current char from the main-token text
                            currentTokenFirstCharPos++; // don't count this inserted token text as part of main-token text
                            // special case; if string-start-marker for an empty string
                            if (ch == nextChar)
                                result.Add(new VDFToken(VDFTokenType.String, currentTokenFirstCharPos, result.Count, ""));
                            continue;
                        }
                    }
                    else if (activeStringStartChar == ch) {
                        if (parseAllTokens)
                            result.Add(new VDFToken(VDFTokenType.StringEndMarker, i, result.Count, ch));
                        currentTokenTextBuilder.Remove(currentTokenTextBuilder.Length - 1, 1); // remove current char from the main-token text
                        currentTokenFirstCharPos++; // don't count this inserted token text as part of main-token text
                        activeStringStartChar = null;
                        continue;
                    }
                }
            }
            // if not in literal
            if (activeLiteralStartChars == null)
                // if in a string
                if (activeStringStartChar != null) {
                    // if last-char of string
                    if (activeStringStartChar == nextChar)
                        currentTokenType = VDFTokenType.String;
                }
                else {
                    var firstTokenChar = currentTokenTextBuilder.Length == 1;
                    if (nextChar == '>')
                        currentTokenType = VDFTokenType.Metadata;
                    else if (nextChar == ':')
                        currentTokenType = VDFTokenType.Key;
                    else {
                        // at this point, all the options are mutually-exclusive (concerning the 'ch' value), so use a switch statement)
                        switch (ch) {
                            case '#':
                                if (nextChar == '#' && firstTokenChar) {
                                    currentTokenTextBuilder = new StringBuilder(text.substr(i, (text.indexOf("\n", i + 1) != -1 ? text.indexOf("\n", i + 1) : text.length) - i));
                                    currentTokenType = VDFTokenType.InLineComment;
                                    i += currentTokenTextBuilder.Length - 1; // have next char processed by the one right after comment (i.e. the line-break char)
                                    nextChar = i < text.length - 1 ? text[i + 1] : specialEnderChar; // update after i-modification, since used for next loop's 'ch' value
                                }
                                break;
                            case ' ':
                            case ',':
                                if ((ch == ' ' || (options.allowCommaSeparators && ch == ','))
                                    && (nextChar != ' ' && (!options.allowCommaSeparators || nextChar != ','))
                                    && currentTokenTextBuilder.ToString().TrimStart(options.allowCommaSeparators ? [' ', ','] : [' ']).length == 0)
                                    currentTokenType = VDFTokenType.SpaceOrCommaSpan;
                                break;
                            case '\t':
                                if (firstTokenChar)
                                    currentTokenType = VDFTokenType.Tab;
                                break;
                            case '\n':
                                if (firstTokenChar)
                                    currentTokenType = VDFTokenType.LineBreak;
                                break;
                            case '>':
                                if (firstTokenChar)
                                    currentTokenType = VDFTokenType.MetadataEndMarker;
                                break;
                            case ':':
                                //else if (nextNonSpaceChar == ':' && ch != ' ')
                                if (firstTokenChar)
                                    currentTokenType = VDFTokenType.KeyValueSeparator;
                                break;
                            case '^':
                                if (firstTokenChar)
                                    currentTokenType = VDFTokenType.PoppedOutChildGroupMarker;
                                break;
                            case 'l':
                                if (currentTokenTextBuilder.Length == 4 && currentTokenTextBuilder.ToString() == "null" && (nextChar == null || !VDFTokenParser.charsAToZ.Contains(nextChar)))
                                    currentTokenType = VDFTokenType.Null;
                                break;
                            case 'e':
                                if ((currentTokenTextBuilder.Length == 5 && currentTokenTextBuilder.ToString() == "false")
                                    || (currentTokenTextBuilder.Length == 4 && currentTokenTextBuilder.ToString() == "true")) {
                                    currentTokenType = VDFTokenType.Boolean;
                                    break;
                                }
                            /*else
                                goto case '0';
                            break;*/
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                            case '.':
                            case '-':
                            case '+': /*case 'e':*/
                            case 'E':
                            case 'y':
                                if ((VDFTokenParser.chars0To9DotAndNegative.Contains(currentTokenTextBuilder[0]) && currentTokenTextBuilder[0].toLowerCase() != "e" // if first-char is valid as start of number
                                    && !VDFTokenParser.chars0To9DotAndNegative.Contains(nextChar) && nextChar != 'I' // and next-char is not valid as part of number
                                    && (lastScopeIncreaseChar == "[" || result.Count == 0 || result.Last().type == VDFTokenType.Metadata || result.Last().type == VDFTokenType.KeyValueSeparator))
                                    || ((currentTokenTextBuilder.Length == 8 && currentTokenTextBuilder.ToString() == "Infinity") || (currentTokenTextBuilder.Length == 9 && currentTokenTextBuilder.ToString() == "-Infinity")))
                                    currentTokenType = VDFTokenType.Number;
                                break;
                            case '[':
                                if (firstTokenChar) {
                                    currentTokenType = VDFTokenType.ListStartMarker;
                                    lastScopeIncreaseChar = ch;
                                }
                                break;
                            case ']':
                                if (firstTokenChar)
                                    currentTokenType = VDFTokenType.ListEndMarker;
                                break;
                            case '{':
                                if (firstTokenChar) {
                                    currentTokenType = VDFTokenType.MapStartMarker;
                                    lastScopeIncreaseChar = ch;
                                }
                                break;
                            case '}':
                                if (firstTokenChar)
                                    currentTokenType = VDFTokenType.MapEndMarker;
                                break;
                        }
                    }
                }
            if (currentTokenType != VDFTokenType.None) {
                if (parseAllTokens || (currentTokenType != VDFTokenType.InLineComment && currentTokenType != VDFTokenType.SpaceOrCommaSpan && currentTokenType != VDFTokenType.MetadataEndMarker))
                    result.Add(new VDFToken(currentTokenType, currentTokenFirstCharPos, result.Count, currentTokenTextBuilder.ToString()));
                currentTokenFirstCharPos = i + 1;
                currentTokenTextBuilder.Clear();
                currentTokenType = VDFTokenType.None;
            }
        }
        if (postProcessTokens)
            result = VDFTokenParser.PostProcessTokens(result, options);
        return result;
    };
    /*static FindNextNonXCharPosition(text: string, startPos: number, x: string) {
        for (var i = startPos; i < text.length; i++)
            if (text[i] != x)
                return i;
        return -1;
    }*/
    /*static UnpadString(paddedString: string) {
        var result = paddedString;
        if (result.StartsWith("#"))
            result = result.substr(1); // chop off first char, as it was just added by the serializer for separation
        if (result.EndsWith("#"))
            result = result.substr(0, result.length - 1);
        return result;
    }*/
    /*static PostProcessTokens(tokens: List<VDFToken>, options: VDFLoadOptions): List<VDFToken> {
        // pass 1: update strings-before-key-value-separator-tokens to be considered keys, if that's enabled (one reason being, for JSON compatibility)
        // ----------
        
        if (options.allowStringKeys)
            for (var i = <any>0; i < tokens.Count; i++)
                if (tokens[i].type == VDFTokenType.String && i + 1 < tokens.Count && tokens[i + 1].type == VDFTokenType.KeyValueSeparator)
                    tokens[i].type = VDFTokenType.Key;

        // pass 2: re-wrap popped-out-children with parent brackets/braces
        // ----------

        tokens.Add(new VDFToken(VDFTokenType.None, -1, -1, "")); // maybe temp: add depth-0-ender helper token

        var line_tabsReached = 0;
        var tabDepth_popOutBlockEndWrapTokens = new Dictionary<number, List<VDFToken>>("int", "List(VDFToken)");
        for (var i = <any>0; i < tokens.Count; i++) {
            var lastToken = i - 1 >= 0 ? tokens[i - 1] : null;
            var token = tokens[i];
            if (token.type == VDFTokenType.Tab)
                line_tabsReached++;
            else if (token.type == VDFTokenType.LineBreak)
                line_tabsReached = 0;
            else if (token.type == VDFTokenType.PoppedOutChildGroupMarker) {
                if (lastToken.type == VDFTokenType.ListStartMarker || lastToken.type == VDFTokenType.MapStartMarker) { //lastToken.type != VDFTokenType.Tab)
                    var enderTokenIndex = i + 1;
                    while (enderTokenIndex < tokens.Count - 1 && tokens[enderTokenIndex].type != VDFTokenType.LineBreak && (tokens[enderTokenIndex].type != VDFTokenType.PoppedOutChildGroupMarker || tokens[enderTokenIndex - 1].type == VDFTokenType.ListStartMarker || tokens[enderTokenIndex - 1].type == VDFTokenType.MapStartMarker))
                        enderTokenIndex++;
                    // the wrap-group consists of the on-same-line text after the popped-out-child-marker (eg the "]}" in "{children:[^]}")
                    var wrapGroupTabDepth = tokens[enderTokenIndex].type == VDFTokenType.PoppedOutChildGroupMarker ? line_tabsReached - 1 : line_tabsReached;
                    tabDepth_popOutBlockEndWrapTokens.Set(wrapGroupTabDepth, tokens.GetRange(i + 1, enderTokenIndex - (i + 1)));
                    tabDepth_popOutBlockEndWrapTokens.Get(wrapGroupTabDepth)[0].index = i + 1; // update index
                    i = enderTokenIndex - 1; // have next token processed be the ender-token (^^[...] or line-break)
                }
                else if (lastToken.type == VDFTokenType.Tab) {
                    var wrapGroupTabDepth = lastToken.type == VDFTokenType.Tab ? line_tabsReached - 1 : line_tabsReached;
                    tokens.InsertRange(i, tabDepth_popOutBlockEndWrapTokens.Get(wrapGroupTabDepth));
                    tokens.RemoveRange(tabDepth_popOutBlockEndWrapTokens.Get(wrapGroupTabDepth)[0].index, tabDepth_popOutBlockEndWrapTokens.Get(wrapGroupTabDepth).Count); // index was updated when set put together

                    i -= tabDepth_popOutBlockEndWrapTokens.Get(wrapGroupTabDepth).Count + 1; // have next token processed be the first pop-out-block-end-wrap-token
                    tabDepth_popOutBlockEndWrapTokens.Remove(wrapGroupTabDepth);
                }
            }
            else if (lastToken != null && (lastToken.type == VDFTokenType.LineBreak || lastToken.type == VDFTokenType.Tab || token.type == VDFTokenType.None)) {
                if (token.type == VDFTokenType.None) // if depth-0-ender helper token
                    line_tabsReached = 0;

                // if we have no popped-out content that we now need to wrap
                if (tabDepth_popOutBlockEndWrapTokens.Count == 0) continue;

                var maxTabDepth = -1;
                for (var key in tabDepth_popOutBlockEndWrapTokens.Keys)
                    if (parseInt(key) > maxTabDepth)
                        maxTabDepth = parseInt(key);
                for (var tabDepth = maxTabDepth; tabDepth >= line_tabsReached; tabDepth--) {
                    if (!tabDepth_popOutBlockEndWrapTokens.ContainsKey(tabDepth)) continue;
                    tokens.InsertRange(i, tabDepth_popOutBlockEndWrapTokens.Get(tabDepth));
                    tokens.RemoveRange(tabDepth_popOutBlockEndWrapTokens.Get(tabDepth)[0].index, tabDepth_popOutBlockEndWrapTokens.Get(tabDepth).Count); // index was updated when set put together

                    // old; maybe temp; fix for that tokens were not post-processed correctly for multiply-nested popped-out maps/lists
                    //VDFTokenParser.RefreshTokenPositionAndIndexProperties(tokens);

                    tabDepth_popOutBlockEndWrapTokens.Remove(tabDepth);
                }
            }
        }

        tokens.RemoveAt(tokens.Count - 1); // maybe temp: remove depth-0-ender helper token

        // pass 3: remove all now-useless tokens
        // ----------
        
        var result = new List<VDFToken>(); //"VDFToken");
        for (let i in tokens.Indexes()) {
            let token = tokens[i];
            if (!(token.type == VDFTokenType.Tab || token.type == VDFTokenType.LineBreak || token.type == VDFTokenType.MetadataEndMarker || token.type == VDFTokenType.KeyValueSeparator || token.type == VDFTokenType.PoppedOutChildGroupMarker))
                result.Add(token);
        }

        // pass 4: fix token position-and-index properties
        // ----------

        VDFTokenParser.RefreshTokenPositionAndIndexProperties(result); //tokens);

        //Console.Write(String.Join(" ", tokens.Select(a=>a.text).ToArray())); // temp; for testing

        return result;
    }*/
    VDFTokenParser.PostProcessTokens = function (origTokens, options) {
        var result = new List(); //"VDFToken");
        // 1: update strings-before-key-value-separator-tokens to be considered keys, if that's enabled (one reason being, for JSON compatibility)
        // 2: re-wrap popped-out-children with parent brackets/braces
        var groupDepth_tokenSetsToProcessAfterGroupEnds = new Dictionary("int", "List(VDFToken)");
        var tokenSetsToProcess = [];
        // maybe temp: add depth-0-ender helper token
        tokenSetsToProcess.push(new TokenSet(new List("VDFToken", new VDFToken(VDFTokenType.None, -1, -1, ""))));
        tokenSetsToProcess.push(new TokenSet(origTokens));
        while (tokenSetsToProcess.length > 0) {
            var tokenSet = tokenSetsToProcess[tokenSetsToProcess.length - 1];
            var tokens = tokenSet.tokens;
            var i = tokenSet.currentTokenIndex;
            var lastToken = i - 1 >= 0 ? tokens[i - 1] : null;
            var token = tokens[i];
            //var addThisToken = true;
            var addThisToken = !(token.type == VDFTokenType.Tab || token.type == VDFTokenType.LineBreak
                || token.type == VDFTokenType.MetadataEndMarker || token.type == VDFTokenType.KeyValueSeparator
                || token.type == VDFTokenType.PoppedOutChildGroupMarker
                || token.type == VDFTokenType.None);
            if (token.type == VDFTokenType.String && i + 1 < tokens.Count && tokens[i + 1].type == VDFTokenType.KeyValueSeparator && options.allowStringKeys) {
                token.type = VDFTokenType.Key;
            }
            //var line_tabsReached_old = line_tabsReached;
            if (token.type == VDFTokenType.Tab) {
                tokenSet.line_tabsReached++;
            }
            else if (token.type == VDFTokenType.LineBreak) {
                tokenSet.line_tabsReached = 0;
            }
            if (token.type == VDFTokenType.None ||
                ((lastToken && (lastToken.type == VDFTokenType.LineBreak || lastToken.type == VDFTokenType.Tab))
                    && token.type != VDFTokenType.LineBreak && token.type != VDFTokenType.Tab)) {
                // if there's popped-out content, check for any end-stuff that we need to now add (because the popped-out block ended)
                if (groupDepth_tokenSetsToProcessAfterGroupEnds.Count > 0) {
                    var tabDepthEnded = token.type == VDFTokenType.PoppedOutChildGroupMarker ? tokenSet.line_tabsReached : tokenSet.line_tabsReached + 1;
                    var deepestTokenSetToProcessAfterGroupEnd_depth = groupDepth_tokenSetsToProcessAfterGroupEnds
                        .keys[groupDepth_tokenSetsToProcessAfterGroupEnds.keys.length - 1];
                    if (deepestTokenSetToProcessAfterGroupEnd_depth >= tabDepthEnded) {
                        var deepestTokenSetToProcessAfterGroupEnd = groupDepth_tokenSetsToProcessAfterGroupEnds.Get(deepestTokenSetToProcessAfterGroupEnd_depth);
                        tokenSetsToProcess.push(deepestTokenSetToProcessAfterGroupEnd);
                        groupDepth_tokenSetsToProcessAfterGroupEnds.Remove(deepestTokenSetToProcessAfterGroupEnd_depth);
                        if (token.type == VDFTokenType.PoppedOutChildGroupMarker)
                            tokenSet.currentTokenIndex++;
                        // we just added a collection-to-process, so go process that immediately (we'll get back to our collection later)
                        continue;
                    }
                }
            }
            if (token.type == VDFTokenType.PoppedOutChildGroupMarker) {
                addThisToken = false;
                if (lastToken.type == VDFTokenType.ListStartMarker || lastToken.type == VDFTokenType.MapStartMarker) {
                    var enderTokenIndex = i + 1;
                    while (enderTokenIndex < tokens.Count && tokens[enderTokenIndex].type != VDFTokenType.LineBreak && (tokens[enderTokenIndex].type != VDFTokenType.PoppedOutChildGroupMarker || tokens[enderTokenIndex - 1].type == VDFTokenType.ListStartMarker || tokens[enderTokenIndex - 1].type == VDFTokenType.MapStartMarker))
                        enderTokenIndex++;
                    // the wrap-group consists of the on-same-line text after the popped-out-child-marker (eg the "]}" in "{children:[^]}")
                    var wrapGroupTabDepth = tokenSet.line_tabsReached + 1;
                    var wrapGroupTokens = tokens.GetRange(i + 1, enderTokenIndex - (i + 1));
                    groupDepth_tokenSetsToProcessAfterGroupEnds.Add(wrapGroupTabDepth, new TokenSet(wrapGroupTokens, tokenSet.line_tabsReached));
                    groupDepth_tokenSetsToProcessAfterGroupEnds.Get(wrapGroupTabDepth).tokens[0].index = i + 1; // update index
                    // skip processing the wrap-group-tokens (at this point)
                    i += wrapGroupTokens.Count;
                }
            }
            if (addThisToken)
                result.Add(token);
            // if last token in this collection, remove collection (end its processing)
            if (i == tokens.Count - 1)
                tokenSetsToProcess.pop();
            tokenSet.currentTokenIndex = i + 1;
        }
        // fix token position-and-index properties
        VDFTokenParser.RefreshTokenPositionAndIndexProperties(result); //tokens);
        // for testing
        // ==========
        /*Log(origTokens.Select(a=>a.text).join(" "));
        Log("==========");
        Log(result.Select(a=>a.text).join(" "));*/
        /*Assert(groupDepth_tokenSetsToProcessAfterGroupEnds.Count == 0, "A token-set-to-process-after-group-end was never processed!");
        Assert(result.Where(a=>a.type == VDFTokenType.MapStartMarker).length == result.Where(a=>a.type == VDFTokenType.MapEndMarker).length,
            `Map start-marker count ${result.Where(a=>a.type == VDFTokenType.MapStartMarker).length} and `
                + `end-marker count ${result.Where(a=>a.type == VDFTokenType.MapEndMarker).length} must be equal.`);
        Assert(result.Where(a=>a.type == VDFTokenType.ListStartMarker).length == result.Where(a=>a.type == VDFTokenType.ListEndMarker).length,
            `List start-marker count ${result.Where(a=>a.type == VDFTokenType.ListStartMarker).length} and `
                + `end- marker count ${result.Where(a=>a.type == VDFTokenType.ListEndMarker).length } must be equal.`);*/
        return result;
    };
    VDFTokenParser.RefreshTokenPositionAndIndexProperties = function (tokens) {
        var textProcessedLength = 0;
        for (var i = 0; i < tokens.Count; i++) {
            var token = tokens[i];
            token.position = textProcessedLength;
            token.index = i;
            textProcessedLength += token.text.length;
        }
    };
    VDFTokenParser.charsAToZ = List.apply(null, ["string"].concat("abcdefghijklmnopqrstuvwxyz".match(/./g)));
    VDFTokenParser.chars0To9DotAndNegative = List.apply(null, ["string"].concat("0123456789\.\-\+eE".match(/./g)));
    return VDFTokenParser;
}());
g.TokenSet = (function () {
    function TokenSet(tokens, line_tabsReached) {
        if (line_tabsReached === void 0) { line_tabsReached = 0; }
        this.currentTokenIndex = 0;
        this.line_tabsReached = 0;
        this.tokens = tokens;
        this.line_tabsReached = line_tabsReached;
    }
    return TokenSet;
}());
//# sourceMappingURL=VDFTokenParser.js.map