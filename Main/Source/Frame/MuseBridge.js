import LibMuse from "react-native-libmuse";

@Bind class MuseBridge {
	static initialized = false;
	static Init() {
		LibMuse.AddListener_OnChangeMuseList(MuseBridge.OnChangeMuseList);
		LibMuse.AddListener_OnChangeMuseConnectStatus(MuseBridge.OnChangeMuseConnectStatus);
		LibMuse.AddListener_OnReceiveMuseDataPacket(MuseBridge.OnReceiveMuseDataPacket);
		LibMuse.Init();
		MuseBridge.initialized = true;
		Log("muse link", `LibMuse initialized.`);
	}
	async IfInEmulator_ReplaceLibMuseFuncsWithStubs() {
		var inEmulator = await JavaBridge.Main.IsInEmulator();
		if (inEmulator) {
			for (var key in LibMuse) {
				var value = LibMuse[key];
				if (value instanceof Function)
					libMuse[key] = ()=>{};
			}
		}
	}

	static museList = [];
	static StopSearch() {
		LibMuse.StopSearch();
	}
	static StartSearch(stopThenStart = true) {
		if (stopThenStart)
			LibMuse.RestartSearch();
		else
			LibMuse.StartSearch();
	}
	static OnChangeMuseList(museList) {
		MuseBridge.museList = museList;
		Log("muse link", `Muse list changed: ${ToJSON(museList)}`);

		if (museList.length && MuseBridge.currentMuse == null && LL.monitor.connect)
			MuseBridge.Connect();

		if (LL.monitor.ui) LL.monitor.ui.forceUpdate();
	}

	static currentMuse = null;
	static Connect() {
		LibMuse.Connect(0); // always try to connect to first muse
	}
	static Disconnect() {
		LibMuse.Disconnect();
	}
	static status = "disconnected";
	static OnChangeMuseConnectStatus(status) {
		Log("muse link", `Muse connection status changed: ${status}`);
		if (status == "connected") {
			Log("muse link", "LibMuse connected.");
			MuseBridge.currentMuse = MuseBridge.museList[0];
		}
		else if (status == "disconnected") {
			Log("muse link", "LibMuse disconnected.");
			MuseBridge.currentMuse = null;
			// since we're disconnected now, restart listening (assuming "connect" is enabled)
			if (LL.monitor.connect)
				MuseBridge.StartSearch();
		}
		MuseBridge.status = status;
		if (LL.monitor.ui) LL.monitor.ui.forceUpdate();
	}
	
	// where eg. [0][1] (channel, x-pos/index) is [1,9] (x-pos, y-pos)
	static channelPoints = Array(4).fill([]); // break point; investigate cyclicnessk
	static lastX = -1;
	static maxX = 100;

	static OnReceiveMuseDataPacket(type, column) {
		if (type != "eeg") return;

		var currentX = MuseBridge.lastX + 1;
		if (currentX > MuseBridge.maxX)
			currentX = 0;

		//for (var channel = 0; channel < 6; channel++)
		for (var channel = 0; channel < 4; channel++)
			MuseBridge.channelPoints[channel][currentX] = [currentX, column[channel]];

		MuseBridge.lastX = currentX;
		//Log("muse link", `Type: ${type} Data: ${ToJSON(data)}`);
		
		// for the moment, assume 60 packets per second (get the actual number at some point)
		const packetsPerSecond = 60;
		var patternMatchInterval_inPackets = LL.settings.patternMatchInterval * packetsPerSecond;
		var scanOffset_inPackets = LL.settings.patternMatchOffset * packetsPerSecond;
		if (currentX % patternMatchInterval_inPackets == 0 && LL.monitor.patternMatch) {
			MuseBridge.patternMatchProbabilities = {};

			for (let pattern of LL.settings.patterns) {
				if (!pattern.channel1 && !pattern.channel2 && !pattern.channel3 && !pattern.channel4) continue;
				Assert(pattern.points.length >= 2, `Pattern point count too low. Should be 2+, not ${pattern.points.length}.`);

				let patternDuration = pattern.Duration;
				let highestMatchProb = Number.MIN_SAFE_INTEGER;
				for (let scanRight = currentX; scanRight >= currentX - patternMatchInterval_inPackets; scanRight -= scanOffset_inPackets) {
					var scanLeft = scanRight - patternDuration;
					var patternPoints_final = MuseBridge.ConvertPoints(pattern.points);
					patternPoints_final = MuseBridge.CenterOnAverageY(patternPoints_final);

					for (let channel = 0; channel < 4; channel++) {
						if (!pattern["channel" + (channel + 1)]) continue;

						let points = MuseBridge.GetPointsForScanRange(channel, scanLeft, scanRight);
						Assert(points && points.length >= 2, `Scanned point count too low. Should be 2+, not ${points ? points.length : "n/a"}.`);
						let channelPoints_final = MuseBridge.ConvertPoints(points);
						channelPoints_final = MuseBridge.CenterOnAverageY(channelPoints_final);
						
						/*let minPos = new Vector2i(V.maxInt, V.maxInt);
						let maxPos = new Vector2i(V.minInt, V.minInt);
						for (let point of points.concat(channelPoints_final)) {
							minPos = minPos.NewX(x=>Math.min(x, point.x)).NewY(y=>Math.min(y, point.y));
							maxPos = maxPos.NewX(x=>Math.max(x, point.x)).NewY(y=>Math.max(y, point.y));
						}
						let maxDistancePossible = V.Distance(minPos, maxPos);*/
						const yValRange = 100; // temp
						let maxDistancePossible = V.Distance({x: scanLeft, y: 0}, {x: scanRight, y: yValRange});

						//let matchProb = Sketchy.shapeContextMatch(channelPoints_final, patternPoints_final);
						//let distance = Sketchy.hausdorff(channelPoints_final, patternPoints_final, {x: 0, y: 0});
						let distance = MuseBridge.GetHausdorffDistance(channelPoints_final, patternPoints_final);
						let matchProb = 1 - (distance / maxDistancePossible);
						//Log(`${distance} | ${maxDistancePossible} | ${matchProb}`);
						highestMatchProb = Math.max(highestMatchProb, matchProb);
					}
				}
				MuseBridge.patternMatchProbabilities[pattern.name] = highestMatchProb;
				//Log(`Setting pattern match prob. Match: ${pattern.name} Prob: ${highestMatchProb}`);
				//JavaLog(`Setting pattern match prob. Match: ${pattern.name} Prob: ${highestMatchProb}`);
			}

			for (let listener of LL.scripts.scriptRunner.listeners_onUpdatePatternMatchProbabilities)
				listener(MuseBridge.patternMatchProbabilities);
		}
	}
	static GetPointsForScanRange(channel, scanLeft, scanRight) {
		var channelPoints = MuseBridge.channelPoints[channel];
		
		var result = [];
		for (let i = scanLeft; i < scanRight; i++) {
			let iFinal = i >= 0 ? i : ((MuseBridge.maxX + 1) + i); // if in range, get it; else, loop aroundand grab from end
			result.push(channelPoints[iFinal] || [i, 0]);
		}
		return result;
	}

	static CenterOnAverageY(points) {
		var averageY = points.Select(a=>a.y).Average();
		var result = points.Select(point=> {
			return {x: point.x, y: point.y - averageY};
		});
		/*Log(`Centering... ${averageY}
=== ${ToJSON(points)}
=== ${ToJSON(result)}`)*/
		return result;
	}
	static ConvertPoints(points) {
		var result = [];
		for (let point of points)
			result.push({x: point[0], y: point[1]});
		return result;
	}

	// Compute the directed hausdorff distance of pixels1 and pixels2.
	// Calculate the lowest upper bound over all points in shape1 of the distances to shape2.
	static GetHausdorffDistance(points1, points2) {
		var h_max = Number.MIN_VALUE;
		for (var i = 0; i < points1.length; i++) {
			let closestOtherPointDist = Number.MAX_VALUE;
			for (var i2 = 0; i2 < points2.length; i2++) {
				let dist = MuseBridge.EuclideanDistance(points1[i].x, points1[i].y, points2[i2].x, points2[i2].y);
				if (dist < closestOtherPointDist) {
					closestOtherPointDist = dist;
				} else if (dist == 0)
					break;
			}
			if (closestOtherPointDist > h_max)
				h_max = closestOtherPointDist;
		}
		return h_max;
	}
	// Compute the Euclidean distance (as a crow flies) between two points.
	// Shortest distance between two pixels
	static EuclideanDistance(x1, y1, x2, y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	};

	static patternMatchProbabilities = {};
}
g.MuseBridge = MuseBridge;
export default MuseBridge;