package com.lucidlink.Frame;

import com.annimon.stream.Stream;
import com.facebook.react.bridge.ReadableMap;
import com.lucidlink.V;

import java.util.ArrayList;
import java.util.List;

public class Pattern {
	public static Pattern FromMap(ReadableMap map) {
		Pattern result = new Pattern();
		result.name = map.getString("name");
		result.channel1 = map.getBoolean("channel1");
		result.channel2 = map.getBoolean("channel2");
		result.channel3 = map.getBoolean("channel3");
		result.channel4 = map.getBoolean("channel4");
		for (ReadableMap pointMap : V.List_ReadableMaps(map.getArray("points"))) {
			result.points.add(Vector2i.FromMap(pointMap));
		}
		result.textEditor = map.getBoolean("textEditor");
		return result;
	}

	public String name;
	public boolean channel1;
	public boolean channel2;
	public boolean channel3;
	public boolean channel4;
	public List<Vector2i> points = new ArrayList<Vector2i>();

	public boolean textEditor;

	// in second-distances
	public int Duration() {
		int min = Stream.of(this.points).map(a->a.x).min((a, b)->a.compareTo(b)).get();
		int max = Stream.of(this.points).map(a->a.x).max((a, b)->a.compareTo(b)).get();
		return max - min;
	}

	public boolean IsChannelEnabled(int channel) {
		if (channel == 0) return channel1;
		if (channel == 1) return channel2;
		if (channel == 2) return channel3;
		V.Assert(channel == 3);
		return channel4;
	}
}