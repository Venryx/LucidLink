package v.lucidlink;

import com.choosemuse.libmuse.MuseDataPacketType;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import v.lucidlink.Frame.Vector2i;
import v.LibMuse.LibMuseModule;
import v.LibMuse.VMuseDataPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static v.lucidlink.LLHolder.LL;
import static v.lucidlink.V.ToWritableArray;

class EEGProcessor {
	public EEGProcessor() {
		chartManager = new ChartManager(this);

		// set up listeners
		// ==========

		LibMuseModule.onInit = ()-> {
			LibMuseModule.main.customHandler = new VMuseDataPacket.Listener() {
				@Override
				public boolean OnReceivePacket(final VMuseDataPacket packet) {
					try {
						/*SPlusModule.main.dataListenerEnabled = LL.mainModule.monitor;
						SPlusModule.main.packetSetSize = LL.mainModule.museEEGPacketBufferSize;*/
						if (LL.mainModule.monitor) {
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

	// general
	// ==========

	// where eg. [0][1] (channel, x-pos/index) is {x:1,y:9} (x-pos, y-pos)
	List<Vector2i[]> channelPoints = new ArrayList<>();
	double[] channelBaselines = new double[4];

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
		} else if (packet.Type().equals("accel")) {
			packet.LoadAccelValues();
		}

		chartManager.OnReceiveMusePacket(packet);

		if (packet.type == MuseDataPacketType.EEG) { // maybe temp; only send eeg packets
			WritableMap packetMap = packet.ToMap();
			double viewDir = GetXPosForDisplay();
			packetMap.putDouble("viewDirection", V.EnsureNormalDouble(viewDir, .5));
			packetMap.putDouble("viewDistance", V.EnsureNormalDouble(viewDistanceY, 0));

			// if we just updated the baselines, include those as well
			if (currentX == maxX) {
				WritableArray baselinesArray = ToWritableArray(channelBaselines);
				packetMap.putArray("channelBaselines", baselinesArray);
			}

			packetBuffer.pushMap(packetMap);
		}

		// send buffer to js, if ready
		if (packetBuffer.size() == packetSetSize) {
			JSBridge.SendEvent("OnReceiveMuseDataPacketSet", packetBuffer);
			packetBuffer = Arguments.createArray(); // create new set
		}
	}
	public int packetSetSize = 10;
	WritableArray packetBuffer = Arguments.createArray();

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
				channelValDifs.get(1) < 0 ? (channelValDifs.get(1) / LL.mainModule.eyeTracker_relaxVSTenseIntensity) + channelValDifs.get(2) :
				channelValDifs.get(2) < 0 ? channelValDifs.get(1) + (channelValDifs.get(2) / LL.mainModule.eyeTracker_relaxVSTenseIntensity) :
				channelValDifs.get(1) + channelValDifs.get(2);
			double rangeStart = 50000;
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
			double leftRightComponent_val_asForRightness = rightVSLeftAbsValDif / (1 - LL.mainModule.eyeTracker_relaxVSTenseIntensity); // is negative if looking left
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
			double rightnessNeededToGoFromLeftToRight = LL.mainModule.eyeTracker_horizontalSensitivity == 0 ? Double.POSITIVE_INFINITY
				: V.Lerp(rangeStart, rangeEnd, LL.mainModule.eyeTracker_horizontalSensitivity);
			double upnessNeededToGoFromBottomToTop = LL.mainModule.eyeTracker_verticalSensitivity == 0 ? Double.POSITIVE_INFINITY
				: V.Lerp(rangeStart, rangeEnd, LL.mainModule.eyeTracker_verticalSensitivity);

			double rightMovement = (rightness / rightnessNeededToGoFromLeftToRight);
			double upMovement = (upness / upnessNeededToGoFromBottomToTop);

			/*if (Math.abs(rightMovement) < LL.mainModule.eyeTracker_ignoreXMovementUnder)
				rightMovement = 0;
			if (Math.abs(upMovement) < LL.mainModule.eyeTracker_ignoreYMovementUnder)
				upMovement = 0;*/

			if (Double.isNaN(rightMovement) || Double.isNaN(upMovement)) return;
			if (Double.isInfinite(rightMovement) || Double.isInfinite(upMovement)) return;

			/*double distanceFromBaseline = V.Average(Math.abs(channelValDifs.get(1)), Math.abs(channelValDifs.get(2)));
			if (distanceFromBaseline < LL.mainModule.eyeTracker_ignoreXMovementUnder * 1000) return;*/

			/*V.LogJava(currentX + ";" + channelValues.get(1) + ";" + channelValues.get(2) + "\n"
				+ channelValDifs.get(1) + ";" + channelValDifs.get(2) + "\n"
				+ channelValDeltas.get(1) + ";" + channelValDeltas.get(2) + "\n"
				+ rightMovement + ";" + upMovement);*/

			double oldEyePosX_difFromCenter = eyePosX - GetCenterPoint();
			if ((oldEyePosX_difFromCenter < -.5 && rightMovement < 0) || (oldEyePosX_difFromCenter > .5 && rightMovement > 0))
				rightMovement = rightMovement * (1 - LL.mainModule.eyeTracker_offScreenGravity);

			eyePosX = eyePosX + rightMovement;
			//eyePosX = V.KeepXBetween(eyePosX + rightMovement, 0, 1);
			//eyePosY = eyePosY + upMovement;
			viewDistanceY = V.KeepXBetween(viewDistanceY + upMovement, 0, 1);

			//if (Double.isNaN(eyePosX_atStartOfCurrentSegment)) eyePosX_atStartOfCurrentSegment = eyePosX;
			//if (Double.isNaN(xTravelAverageOfLastNSegments)) xTravelAverageOfLastNSegments = eyePosX;
			if (lastNPositions.length != LL.mainModule.eyeTraceSegmentCount)
				ResetLastNPositions();

			while (V.Distance(eyePosX, lastProcessedEyePosX) > LL.mainModule.eyeTraceSegmentSize) {
				int currentIndex = lastNPositions_lastSetIndex < lastNPositions.length - 1 ? lastNPositions_lastSetIndex + 1 : 0;
				double currentOffset = eyePosX > lastProcessedEyePosX ? LL.mainModule.eyeTraceSegmentSize : -LL.mainModule.eyeTraceSegmentSize;
				lastNPositions[currentIndex] = lastProcessedEyePosX + currentOffset;
				lastNPositions_lastSetIndex = currentIndex;
				lastProcessedEyePosX += currentOffset; // var could also be named "xTravelForAllSegments"
			}

		} finally {
			lastChannelValues = channelValues;
		}
	}
	public void ResetLastNPositions() {
		lastNPositions = new double[LL.mainModule.eyeTraceSegmentCount];
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

	// use compromise between median-approach and average-approach (get "median" 40%, then average those)
	double GetChannelBaseline(int channel) {
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

		/*int median;
		if (channelPoints_clone.length % 2 == 0)
			median = (channelPoints_clone[channelPoints_clone.length / 2].y + channelPoints_clone[channelPoints_clone.length / 2 - 1].y) / 2;
		else
			median = channelPoints_clone[channelPoints_clone.length / 2].y;
		return median;*/

		double subsetTotal = 0;
		int cutOffCount = (int)(channelPoints_clone.length * .3);
		for (int i = cutOffCount; i < channelPoints_clone.length - cutOffCount; i++) {
			subsetTotal += channelPoints_clone[i].y;
		}
		int subsetCount = channelPoints_clone.length - (cutOffCount * 2);
		return subsetTotal / subsetCount;
	}
}