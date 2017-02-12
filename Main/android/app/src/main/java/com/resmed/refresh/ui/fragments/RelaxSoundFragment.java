package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.resmed.refresh.ui.activity.RelaxActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;
import com.resmed.refresh.ui.utils.RelaxDataManager;
import com.resmed.refresh.utils.audio.PlaySoundManager;
import com.resmed.refresh.utils.audio.SoundResources;
import com.resmed.refresh.utils.audio.SoundResources.RelaxSound;

import java.util.ArrayList;
import java.util.List;

public class RelaxSoundFragment
		extends BaseFragment {
	private RelaxAdapter adapter;
	private boolean isPlaying;
	private ListView lvRelaxSelectSound;
	private ActiveFragmentInterface mcActiveFragmentInterface;
	private RelaxDataManager relaxDataManager;
	private int selectedIndex;
	private PlaySoundManager soundManager;

	private void selectSound(int paramInt) {
		if ((paramInt == this.selectedIndex) && (this.isPlaying)) {
		}
		for (boolean bool = false; ; bool = true) {
			this.isPlaying = bool;
			this.selectedIndex = paramInt;
			SoundResources.RelaxSound localRelaxSound = SoundResources.RelaxSound.getRelaxForPosition(paramInt);
			this.relaxDataManager.setRelaxSound(localRelaxSound.getId());
			this.adapter.updateList(paramInt);
			this.soundManager.stopAudio();
			if (this.isPlaying) {
				this.soundManager.playAudio(localRelaxSound.getAssetFileDescriptor());
			}
			return;
		}
	}

	public void onAttach(Activity paramActivity) {
		super.onAttach(paramActivity);
		try {
			this.mcActiveFragmentInterface = ((ActiveFragmentInterface) paramActivity);
			return;
		} catch (ClassCastException localClassCastException) {
			throw new ClassCastException(paramActivity.toString() + " ...you must implement RelaxBtn from your Activity ;-) !");
		}
	}

	public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
		paramLayoutInflater = paramLayoutInflater.inflate(2130903158, paramViewGroup, false);
		this.lvRelaxSelectSound = ((ListView) paramLayoutInflater.findViewById(2131100342));
		paramViewGroup = new ArrayList();
		for (int i = 0; ; i++) {
			if (i >= SoundResources.RelaxSound.values().length) {
				this.adapter = new RelaxAdapter(paramViewGroup);
				this.lvRelaxSelectSound.setAdapter(this.adapter);
				this.soundManager = PlaySoundManager.getInstance();
				this.lvRelaxSelectSound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
						RelaxSoundFragment.this.selectSound(paramAnonymousInt);
					}
				});
				this.soundManager = PlaySoundManager.getInstance();
				return paramLayoutInflater;
			}
			paramViewGroup.add(Boolean.valueOf(false));
		}
	}

	public void onPause() {
		super.onPause();
		if (this.soundManager != null) {
			this.soundManager.stopAudio();
		}
		this.isPlaying = false;
	}

	public void onResume() {
		super.onResume();
		this.relaxDataManager = RelaxDataManager.getInstance();
		int j = this.relaxDataManager.getRelaxSound().getId();
		int i = j;
		if (j == -1) {
			i = 0;
		}
		this.selectedIndex = 0;
		for (j = 0; ; j++) {
			if (j >= SoundResources.RelaxSound.values().length) {
			}
			for (; ; ) {
				this.adapter.updateList(this.selectedIndex);
				ActiveFragmentInterface localActiveFragmentInterface = this.mcActiveFragmentInterface;
				((RelaxActivity) getActivity()).getClass();
				localActiveFragmentInterface.notifyCurrentFragment(1);
				return;
				if (i != SoundResources.RelaxSound.getRelaxForPosition(j).getId()) {
					break;
				}
				this.selectedIndex = j;
			}
		}
	}

	private class RelaxAdapter
			extends BaseAdapter {
		List<Boolean> list;

		public RelaxAdapter() {
			this.list = new ArrayList<>();
		}

		public int getCount() {
			return this.list.size();
		}

		public Object getItem(int paramInt) {
			return this.list.get(paramInt);
		}

		public long getItemId(int paramInt) {
			return paramInt;
		}

		public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
			RelaxSoundFragment.ViewHolder paramViewGroup = paramView;
			if (paramViewGroup == null) {
				paramView = RelaxSoundFragment.this.getActivity().getLayoutInflater().inflate(2130903191, null);
				paramViewGroup = new RelaxSoundFragment.ViewHolder();
				paramViewGroup.radio_group_icon = ((ImageView) paramView.findViewById(2131099883));
				paramViewGroup.radio_group_label = ((TextView) paramView.findViewById(2131099884));
				paramViewGroup.radio_group_button = ((ImageView) paramView.findViewById(2131099885));
				paramView.setTag(paramViewGroup);
			}
			paramViewGroup = (RelaxSoundFragment.ViewHolder) paramView.getTag();
			paramViewGroup.radio_group_label.setText(SoundResources.RelaxSound.getRelaxForPosition(paramInt).getName());
			if (((Boolean) this.list.get(paramInt)).booleanValue()) {
				paramViewGroup.radio_group_button.setImageResource(2130837867);
				if (RelaxSoundFragment.this.isPlaying) {
					paramViewGroup.radio_group_icon.setImageResource(2130837886);
				}
			}
			for (; ; ) {
				paramView.invalidate();
				return paramView;
				paramViewGroup.radio_group_icon.setImageResource(2130837883);
				continue;
				paramViewGroup.radio_group_button.setImageResource(2130837863);
				paramViewGroup.radio_group_icon.setImageResource(2130837883);
			}
		}

		public void updateList(int paramInt) {
			int i = 0;
			if (i >= this.list.size()) {
				notifyDataSetChanged();
				return;
			}
			List localList = this.list;
			if (paramInt == i) {
			}
			for (boolean bool = true; ; bool = false) {
				localList.set(i, Boolean.valueOf(bool));
				i++;
				break;
			}
		}
	}

	static class ViewHolder {
		public ImageView radio_group_button;
		public ImageView radio_group_icon;
		public TextView radio_group_label;
	}
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\RelaxSoundFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */