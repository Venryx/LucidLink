package v.lucidlink.Frame;

import android.content.Context;
import android.media.AudioManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import v.lucidlink.MainActivity;

public class VolumeManager {
	public enum VolumeChannel {
		Normal,
		Bluetooth,
	}
	public static class VolumeChange {
		public VolumeChange(VolumeChannel channel, int amount, boolean increase) {
			this.channel = channel;
			this.amount = amount;
			this.increase = increase;
		}
		public VolumeChannel channel;
		public int amount;
		public boolean increase;
	}

	public static VolumeChannel GetCurrentChannel() {
		return MainActivity.main.bluetoothConnected ? VolumeChannel.Bluetooth : VolumeChannel.Normal;
	}

	static HashMap<VolumeChannel, List<VolumeChange>> volumeChangesToApply = new HashMap<>();
	public static void SetVolume(VolumeChannel channel, int volume) {
		if (!volumeChangesToApply.containsKey(channel)) {
			volumeChangesToApply.put(channel, new ArrayList<>());
		}
		volumeChangesToApply.get(channel).add(new VolumeChange(channel, volume, false));
		TryToApplyVolumeChanges(channel);
	}
	public static void IncreaseVolume(VolumeChannel channel, int volumeChange) {
		if (!volumeChangesToApply.containsKey(channel)) {
			volumeChangesToApply.put(channel, new ArrayList<>());
		}
		volumeChangesToApply.get(channel).add(new VolumeChange(channel, volumeChange, true));
		TryToApplyVolumeChanges(channel);
	}
	public static void TryToApplyVolumeChanges(VolumeChannel channel) {
		if (GetCurrentChannel() != channel) return;

		AudioManager audioManager = (AudioManager)MainActivity.main.getSystemService(Context.AUDIO_SERVICE);
		for (VolumeChange change: volumeChangesToApply.get(channel)) {
			int oldVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			int newVolume = change.increase ? oldVolume + change.amount : change.amount;
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
		}
		volumeChangesToApply.remove(channel);
	}
}