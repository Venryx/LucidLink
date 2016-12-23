import {AppRegistry, View} from "react-native";

// for some reason, a native (C++) error occurs unless we import this ahead of time here
import LibMuse from "react-native-libmuse";

import LucidLinkUI from "./Build/Source/LucidLinkUI";

AppRegistry.registerComponent("LucidLink", ()=>LucidLinkUI);