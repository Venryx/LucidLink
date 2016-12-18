package com.lucidlink;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.lucidlink.Frame.Pattern;
import com.lucidlink.Frame.Vector2i;
import com.v.LibMuse.MainModule;
import com.v.LibMuse.VMuseDataPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class EEGProcessor {
	public EEGProcessor() {
		chartManager = new ChartManager(this);

		// set up listeners
		// ==========

		MainModule.onInit = ()-> {
			MainModule.main.customHandler = new VMuseDataPacket.Listener() {
				@Override
				public boolean OnReceivePacket(final VMuseDataPacket packet) {
					try {
						/*MainModule.main.dataListenerEnabled = Main.main.monitor;
						MainModule.main.packetSetSize = Main.main.museEEGPacketBufferSize;*/
						if (Main.main.monitor) {
							EEGProcessor.this.OnReceiveMuseDataPacket(packet);
						}

						return true;
					} catch(Throwable ex) {
						V.Log("Error in EEGProcessor.OnReceivePacket) " + V.GetStackTrace(ex));
						return true;
					}
				}
			};
		};
	}

	ChartManager chartManager;

	// for the moment, assume X packets per second (get the actual number at some point)
	public final int packetsPerSecond = 20;

	// general
	// ==========

	// where eg. [0][1] (channel, x-pos/index) is {x:1,y:9} (x-pos, y-pos)
	List<Vector2i[]> channelPoints = new ArrayList<>();
	double[] channelBaselines = new double[4];

	HashMap<String, HashMap<Integer, Double>> patternMatchProbabilities = new HashMap<>();

	int currentIndex = -1;
	int currentX = -1;
	int maxX = 1000;

	public void OnReceiveMuseDataPacket(VMuseDataPacket packet) {
		if (packet.Type().equals("eeg")) {
			packet.LoadEEGValues();

			currentIndex++;
			currentX = currentX < maxX ? currentX + 1 : 0;

			//for (var channel = 0; channel < 6; channel++)
			for (int channel = 0; channel < 4; channel++) {
				if (channelPoints.size() <= channel) {
					channelPoints.add(new Vector2i[maxX + 1]);
					for (int x = 0; x <= maxX; x++)
						channelPoints.get(channel)[x] = new Vector2i(x, 0);
				}
				Vector2i point = new Vector2i(currentX, (int)packet.eegValues[channel]);
				channelPoints.get(channel)[currentX] = point;
			}

			// only update the baseline once every chart-width
			if (currentX == maxX) {
				for (int channel = 0; channel < 4; channel++) {
					double oldBaseline = channelBaselines[channel];
					double newBaseline = GetChannelBaseline(channel);
					if (oldBaseline == 0)
						oldBaseline = newBaseline;

					// have new-baseline only slightly affect long-term baseline
					channelBaselines[channel] = ((oldBaseline * 2) + newBaseline) / 3;
					//channelBaselines[channel] = (oldBaseline + newBaseline) / 2;
				}
			}

			/*for (int channel = 0; channel < 4; channel++) {
				double oldBaseline = channelBaselines[channel];
				double newBaseline = packet.eegValues[channel];
				if (Double.isNaN(newBaseline)) continue;
				if (oldBaseline == 0)
					oldBaseline = newBaseline;
				// have new-baseline only slightly affect long-term baseline
				channelBaselines[channel] = ((oldBaseline * 999) + newBaseline) / 1000;
				//channelBaselines[channel] = (oldBaseline + newBaseline) / 2;
			}*/

			UpdateEyeTracking(currentX, packet.eegValues);

			int patternMatchInterval_inPackets = (int) (Main.main.patternMatchInterval * packetsPerSecond);
			if (currentX % patternMatchInterval_inPackets == 0 && Main.main.patternMatch) {
				UpdateMatchProbabilities(currentX);
			} else {
				/*let lastPatternMatchProbability = null;
				for (let x = currentX; x >= 0 && lastPatternMatchProbability == null; x++)
					lastPatternMatchProbability = patternMatchProbabilities.Props[0].value[x];
				if (lastPatternMatchProbability != null)
					JavaBridge.Main.OnSetPatternMatchProbability(lastPatternMatchProbability);*/
			}
		} else if (packet.Type().equals("accel")) {
			packet.LoadAccelValues();
		}

		chartManager.OnReceiveMusePacket(packet);

		WritableMap packetMap = packet.ToMap();
		double viewDir = GetXPosForDisplay();
		packetMap.putDouble("viewDirection", Double.isNaN(viewDir) ? .5 : viewDir);
		packetMap.putDouble("viewDistance", Double.isNaN(viewDistanceY) ? 0 : viewDistanceY);
		packetBuffer.pushMap(packetMap);

		// send buffer to js, if ready
		if (packetBuffer.size() == packetSetSize) {
			Main.main.SendEvent("OnReceiveMuseDataPacketSet", packetBuffer);
			packetBuffer = Arguments.createArray(); // create new set
		}
	}
	public int packetSetSize = 10;
	WritableArray packetBuffer = Arguments.createArray();

	List<Vector2i> GetPointsForScanRange(int channel, int scanLeft, int scanRight) {
		Vector2i[] channelPoints = EEGProcessor.this.channelPoints.get(channel);

		List<Vector2i> result = new ArrayList<>();
		for (int i = scanLeft; i < scanRight; i++) {
			int iFinal = i >= 0 ? i : ((maxX + 1) + i); // if in range, get it; else, loop around and grab from end
			result.add(channelPoints[iFinal]);
		}
		return result;
	}

	public double eyePosX = .5;
	public double viewDistanceY = .5;

	public double channel1VSChannel2Strength_averageOfLastX = 1;
	//public double upVSDownAmount_averageOfLastX = 0;

	double[] lastChannelValues;
	void UpdateEyeTracking(int currentX, double[] channelValues) {
		try {
			if (channelBaselines[0] == 0) return; // if baselines aren't calculated yet, return now

			/*ArrayList<Double> channelValDeltas = new ArrayList<>();
			for (int i = 0; i < 4; i++)
				channelValDeltas.add(channelValues.get(i) - lastChannelValues.get(i));*/

			ArrayList<Double> channelValDifs = new ArrayList<>();
			//for (int i = 0; i < channelValues.size(); i++)
			for (int i = 0; i < 4; i++)
				channelValDifs.add(channelValues[i] - channelBaselines[i]);

			/*for (int i = 0; i < 4; i++) {
				// if we're e.g. above line, but delta says we're moving down, ignore delta (set it to 0)
				if (channelValDifs.get(i) > 0 != channelValDeltas.get(i) > 0 || channelValDifs.get(i) < 0 != channelValDeltas.get(i) < 0)
					channelValDeltas.set(i, 0d);
			}*/

			// if both channels have at least X distance
			if (Math.min(Math.abs(channelValDifs.get(1)), Math.abs(channelValDifs.get(2))) > 30) {
				double channel1VSChannel2Strength = Math.abs(channelValDifs.get(1)) / Math.abs(channelValDifs.get(2));
				channel1VSChannel2Strength_averageOfLastX = ((channel1VSChannel2Strength_averageOfLastX * 999) + channel1VSChannel2Strength) / 1000;
			}

			// offset strength of channel-1's val to compensate for strength difference
			/*channelValDifs.set(1, channelValDifs.get(1) / channel1VSChannel2Strength_averageOfLastX);

			//channelValDeltas.set(1, channelValDeltas.get(1) / channel1VSChannel2Strength_averageOfLastX);*/

			// approach 1
			//double leftness = channelValDifs.get(1) + -channelValDifs.get(2);
			double rightness = channelValDifs.get(2) + -channelValDifs.get(1);
			//double downness = -channelValDifs.get(1) + -channelValDifs.get(2);
			double upness =
				channelValDifs.get(1) < 0 ? (channelValDifs.get(1) / Main.main.eyeTracker_relaxVSTenseIntensity) + channelValDifs.get(2) :
				channelValDifs.get(2) < 0 ? channelValDifs.get(1) + (channelValDifs.get(2) / Main.main.eyeTracker_relaxVSTenseIntensity) :
				channelValDifs.get(1) + channelValDifs.get(2);
			double rangeStart = 30000;
			double rangeEnd = 1000;

			/*if (Math.abs(rightness) > Math.abs(upness))
				upness = 0;
			else if (Math.abs(upness) > Math.abs(rightness))
				rightness = 0;*/

			// approach 2
			/*double rightness = channelValDeltas.get(2) + -channelValDeltas.get(1);
			double upness = channelValDeltas.get(1) + channelValDeltas.get(2);
			double rangeStart = 1500;
			double rangeEnd = 50;*/

			// approach 3
			// extract left-right component from eeg data
			/*double rightVSLeftAbsValDif = Math.abs(channelValDifs.get(2)) - Math.abs(channelValDifs.get(1));
			double leftRightComponent_val_asForRightness = rightVSLeftAbsValDif / (1 - Main.main.eyeTracker_relaxVSTenseIntensity); // is negative if looking left
			// extract close-far component from eeg data
			/*double highPointY = Math.max(channelValDifs.get(1), channelValDifs.get(2));
			double closeFarComponent_val_asForFarness = highPointY - Math.abs(leftRightComponent_val_asForRightness);*#/
			double closeFarComponent_val_asForFarness = channelValDifs.get(2) - leftRightComponent_val_asForRightness;
			// rename
			double rightness = leftRightComponent_val_asForRightness;
			double upness = closeFarComponent_val_asForFarness;
			double rangeStart = 30000;
			double rangeEnd = 1000;*/

			// apply sensitivity
			double rightnessNeededToGoFromLeftToRight = Main.main.eyeTracker_horizontalSensitivity == 0 ? Double.POSITIVE_INFINITY
				: V.Lerp(rangeStart, rangeEnd, Main.main.eyeTracker_horizontalSensitivity);
			double upnessNeededToGoFromBottomToTop = Main.main.eyeTracker_verticalSensitivity == 0 ? Double.POSITIVE_INFINITY
				: V.Lerp(rangeStart, rangeEnd, Main.main.eyeTracker_verticalSensitivity);

			double rightMovement = (rightness / rightnessNeededToGoFromLeftToRight);
			double upMovement = (upness / upnessNeededToGoFromBottomToTop);

			/*if (Math.abs(rightMovement) < Main.main.eyeTracker_ignoreXMovementUnder)
				rightMovement = 0;
			if (Math.abs(upMovement) < Main.main.eyeTracker_ignoreYMovementUnder)
				upMovement = 0;*/

			if (Double.isNaN(rightMovement) || Double.isNaN(upMovement)) return;
			if (Double.isInfinite(rightMovement) || Double.isInfinite(upMovement)) return;

			double distanceFromBaseline = V.Average(Math.abs(channelValDifs.get(1)), Math.abs(channelValDifs.get(2)));
			if (distanceFromBaseline < Main.main.eyeTracker_ignoreXMovementUnder * 1000) return;

			/*V.JavaLog(currentX + ";" + channelValues.get(1) + ";" + channelValues.get(2) + "\n"
				+ channelValDifs.get(1) + ";" + channelValDifs.get(2) + "\n"
				+ channelValDeltas.get(1) + ";" + channelValDeltas.get(2) + "\n"
				+ rightMovement + ";" + upMovement);*/

			eyePosX = eyePosX + rightMovement;
			//eyePosX = V.KeepXBetween(eyePosX + rightMovement, 0, 1);
			//eyePosY = eyePosY + upMovement;
			viewDistanceY = V.KeepXBetween(viewDistanceY + upMovement, 0, 1);

			//if (Double.isNaN(eyePosX_atStartOfCurrentSegment)) eyePosX_atStartOfCurrentSegment = eyePosX;
			//if (Double.isNaN(xTravelAverageOfLastNSegments)) xTravelAverageOfLastNSegments = eyePosX;
			if (lastNPositions.length != Main.main.eyeTraceSegmentCount)
				ResetLastNPositions();

			while (V.Distance(eyePosX, lastProcessedEyePosX) > Main.main.eyeTraceSegmentSize) {
				int currentIndex = lastNPositions_lastSetIndex < lastNPositions.length - 1 ? lastNPositions_lastSetIndex + 1 : 0;
				double currentOffset = eyePosX > lastProcessedEyePosX ? Main.main.eyeTraceSegmentSize : -Main.main.eyeTraceSegmentSize;
				lastNPositions[currentIndex] = lastProcessedEyePosX + currentOffset;
				lastNPositions_lastSetIndex = currentIndex;
				lastProcessedEyePosX += currentOffset; // var could also be named "xTravelForAllSegments"
			}

			// do some slight resetting to center each frame
			/*double amount = .01;
			if (eyePosX < .5)
				eyePosX += amount;
			else if (eyePosX > .5)
				eyePosX -= amount;
			if (eyePosY < .5)
				eyePosY += amount;
			else if (eyePosY > .5)
				eyePosY -= amount;*/

			//Main.main.SendEvent("UpdateEyeTracking", eyePosX, eyePosY, rightness, upness);

		} finally {
			lastChannelValues = channelValues;
		}
	}
	public void ResetLastNPositions() {
		lastNPositions = new double[Main.main.eyeTraceSegmentCount];
		for (int i = 0; i < lastNPositions.length; i++)
			lastNPositions[i] = .5;
	}

	double[] lastNPositions = new double[0];
	int lastNPositions_lastSetIndex = -1;
	double lastProcessedEyePosX = .5;
	public double GetCenterPoint() {
		//double centerPoint = xTravelAverageOfLastNSegments;
		/*double[] lastNSegments_offsetsFromCurrent = new double[lastNSegmentValues.length];
		double lastOffset = 0;
		// backtrack through segments-vals, constructing last-n-segments trace-image
		for (int i = lastNSegments_offsetsFromCurrent.length - 1; i >= 0; i--) {
			double currentOffset = lastOffset - lastNSegmentValues[i];
			lastNSegments_offsetsFromCurrent[i] = currentOffset;
			lastOffset = currentOffset;
		}*/
		// then finding average of points in it
		double result = V.Average(lastNPositions);
		return result;
	}
	public double GetXPosForDisplay() {
		double result = .5 + (eyePosX - GetCenterPoint());
		result = V.KeepXBetween(result, 0, 1);
		return result;
	}
	double GetYPosForDisplay() {
		/*double result = V.KeepXBetween(eyePosY, 0, 1);
		return result;*/
		return viewDistanceY;
	}

	void UpdateMatchProbabilities(int currentX) {
		int patternMatchInterval_inPackets = (int)(Main.main.patternMatchInterval * packetsPerSecond);
		int scanOffset_inPackets = (int)(Main.main.patternMatchOffset * packetsPerSecond);

		for (Pattern pattern : Main.main.patterns) {
			if (!pattern.enabled) continue;
			if (!pattern.channel1 && !pattern.channel2 && !pattern.channel3 && !pattern.channel4)
				continue;
			V.Assert(pattern.points.size() >= 2, "Pattern point count too low. Should be 2+, not " + pattern.points.size() + ".");

			int patternDuration = pattern.Duration();
			double highestMatchProb = Integer.MIN_VALUE;
			for (int scanRight = currentX; scanRight >= currentX - patternMatchInterval_inPackets; scanRight -= scanOffset_inPackets) {
				int scanLeft = scanRight - patternDuration;

				for (int channel = 0; channel < 4; channel++) {
					if (!pattern.IsChannelEnabled(channel)) continue;

					List<Vector2i> points = GetPointsForScanRange(channel, scanLeft, scanRight);
					V.Assert(points != null && points.size() >= 2, "Scanned point count too low. Should be 2+, not " + (points != null ? points.size() : "n/a") + ".");

					double channelBaseline = channelBaselines[channel];

					new Vector2i(0, 0).NewX(a -> a);
					//List<Vector2i> channelPoints_final = V.List(Stream.of(points).map(a->a.NewX(x->x - scanLeft).NewY(y->y - channelBaseline)));
					List<Vector2i> channelPoints_final = new ArrayList<>();
					for (int i = 0, x = scanLeft; x < scanRight; i++, x++) {
						boolean takenFromEnd = x < 0;
						Vector2i point = points.get(i);
						Vector2i point_final = new Vector2i(point.x - scanLeft, point.y - (int)channelBaseline);
						if (takenFromEnd)
							point_final.x -= (maxX + 1);
						channelPoints_final.add(point_final);
					}

					double probability0Distance = pattern.sensitivity;

					//let matchProb = Sketchy.shapeContextMatch(channelPoints_final, patternPoints_final);
					//let distance = Sketchy.hausdorff(channelPoints_final, patternPoints_final, {x: 0, y: 0});
					double distance = GetAverageOfPointClosestDistances(pattern.points, channelPoints_final);

					double matchProb = 1 - (distance / probability0Distance);
					matchProb = Math.max(0, matchProb); // maybe temp

					//V.Log("pattern-matching", channelBaseline + " | " + distance + " | " + probability0Distance + " | " + matchProb, false);
/*PatternPoints: ${ToVDF(pattern.points, false)}
ChannelPoints: ${ToVDF(points, false)}
ChannelPoints_Final: ${ToVDF(channelPoints_final, false)}`);*/
					highestMatchProb = Math.max(highestMatchProb, matchProb);
				}
			}
			if (!patternMatchProbabilities.containsKey(pattern.name))
				patternMatchProbabilities.put(pattern.name, new HashMap<>());
			patternMatchProbabilities.get(pattern.name).put(currentX, highestMatchProb);
			//JavaLog(`Setting pattern match prob. Match: ${pattern.name} Prob: ${highestMatchProb}`);
		}

		HashMap<String, Double> patternMatchProbabilitiesForFrame = new HashMap<>();
		for (String patternName : patternMatchProbabilities.keySet())
			patternMatchProbabilitiesForFrame.put(patternName, patternMatchProbabilities.get(patternName).get(currentX));
		/*for (let listener of LL.scripts.scriptRunner.listeners_onUpdatePatternMatchProbabilities)
			listener(patternMatchProbabilitiesForFrame);*/

		chartManager.OnSetPatternMatchProbabilities(currentX, patternMatchProbabilitiesForFrame);

		Main.main.SendEvent("OnSetPatternMatchProbabilities", currentX, V.ToWritableMap(patternMatchProbabilitiesForFrame));
	}

	int GetChannelBaseline(int channel) {
		/*List<Vector2i> channelPoints = EEGProcessor.this.channelPoints.get(channel);
		List<Vector2i> channelPoints_ordered = V.List(Stream.of(channelPoints).sortBy(a->a.y));
		int result = channelPoints_ordered.get(channelPoints_ordered.size() / 2).y;
		//Log("Baseline:" + result + ";" + ToJSON(channelPoints_ordered));
		return result;*/

		/*Vector2i[] channelPoints = EEGProcessor.this.channelPoints.get(channel);
		int[] channelPoints_array = new int[channelPoints.length];
		for (int i = 0; i < channelPoints.length; i++)
			channelPoints_array[i] = channelPoints[i].y;
		Arrays.sort(channelPoints_array);*/

		Vector2i[] channelPoints = EEGProcessor.this.channelPoints.get(channel);
		Vector2i[] channelPoints_clone = channelPoints.clone();
		Arrays.sort(channelPoints_clone);

		int median;
		if (channelPoints_clone.length % 2 == 0)
			median = (channelPoints_clone[channelPoints_clone.length / 2].y + channelPoints_clone[channelPoints_clone.length / 2 - 1].y) / 2;
		else
			median = channelPoints_clone[channelPoints_clone.length / 2].y;
		return median;
	}

	/*static ConvertPoints(points) {
		var result = [];
		for (let point of points)
			result.push({x: point[0], y: point[1]});
		return result;
	}*/

	// essentially: points1.Select(a=>a.DistanceToClosestPointIn(points2)).Average();
	// NOTE: points2 must have each point's x-value be one greater than the one prior!
	static double GetAverageOfPointClosestDistances(List<Vector2i> points1, List<Vector2i> points2) {
		double pointClosestDistances_total = 0;

		int lastPoint_closestPoint2_index = -1;
		double lastPoint_closestPoint2_dist = Double.MAX_VALUE;
		for (int i = 0; i < points1.size(); i++) {
			Vector2i lastPoint1 = i > 0 ? points1.get(i - 1) : null;
			Vector2i point1 = points1.get(i);

			//double closestPoint2Dist = point.DistanceSquared(lastPoint_closestPoint2);
			int closestPoint2_index = lastPoint_closestPoint2_index;
			double closestPoint2_dist = lastPoint_closestPoint2_dist + (lastPoint1 != null ? lastPoint1.Distance(point1) : 0);
			int startX = lastPoint_closestPoint2_index;

			int offset = V.HasIndex(points2, startX + 1) ? 1 : -1;
			while (true) {
				int currentX = startX + offset;
				Vector2i point2 = points2.get(currentX);
				boolean closerPoint2IsPossible = V.Distance(currentX, point1.x) < closestPoint2_dist;
				if (!closerPoint2IsPossible) break;

				double dist = point1.Distance(point2);
				if (dist < closestPoint2_dist) {
					closestPoint2_index = currentX;
					closestPoint2_dist = dist;
				}

				offset = offset >= 0 ? -offset : -offset + 1;
				// if offset leads to invalid index, flip to the other side
				currentX = startX + offset;
				if (currentX < 0 || currentX >= points2.size())
					offset = -offset;
				// if still leads to invalid index, then break, as there's no more point2s
				if (currentX < 0 || currentX >= points2.size()) break;
			}

			lastPoint_closestPoint2_index = closestPoint2_index;
			lastPoint_closestPoint2_dist = closestPoint2_dist;

			pointClosestDistances_total += closestPoint2_dist;
		}

		double pointClosestDistances_average = pointClosestDistances_total / points1.size();
		return pointClosestDistances_average;
	}
	// Compute the Euclidean distance (as a crow flies) between two points.
	// Shortest distance between two pixels
	/*static double Distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}*/
}