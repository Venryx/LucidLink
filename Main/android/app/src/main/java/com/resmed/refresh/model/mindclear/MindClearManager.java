package com.resmed.refresh.model.mindclear;

import com.google.gson.GsonBuilder;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.mindclear.MindClearBase;
import com.resmed.refresh.model.mindclear.MindClearItems;
import com.resmed.refresh.model.mindclear.MindClearText;
import com.resmed.refresh.model.mindclear.MindClearVoice;
import com.resmed.refresh.utils.Log;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MindClearManager {
	class MindClearManager$1 implements Comparator<MindClearBase> {
		@Override
		public int compare(final MindClearBase mindClearBase, final MindClearBase mindClearBase2) {
			return new Date(mindClearBase2.getTimestamp()).compareTo(new Date(mindClearBase.getTimestamp()));
		}
	}

	private static MindClearManager mindClearMapper;
	private static RefreshModelController refreshModelController;
	private Comparator comparator = (Comparator<MindClearBase>)new MindClearManager.MindClearManager$1();
	private int currentIndex;
	private MindClearItems mindClearItems;
	private String tempText;

	private MindClearManager() {
		refreshModelController = RefreshModelController.getInstance();
		this.fetchDataFromFilePreference();
		this.storeDataIntoFilePreference();
	}

	private void createStructure(String param1) {
		// $FF: Couldn't be decompiled
	}

	private void fetchDataFromFilePreference() {
		String var1 = refreshModelController.getClearMindData();
		if(var1 != null) {
			this.createStructure(var1);
		} else {
			this.mindClearItems = new MindClearItems();
		}

		this.currentIndex = -1;
	}

	public static MindClearManager getInstance() {
		synchronized(MindClearManager.class){}

		MindClearManager var1;
		try {
			if(mindClearMapper == null) {
				mindClearMapper = new MindClearManager();
			}

			var1 = mindClearMapper;
		} finally {
			;
		}

		return var1;
	}

	public void addMindClearText() {
		this.tempText = this.tempText.trim();
		if(this.tempText.length() > 0) {
			this.tempText = this.tempText.substring(0, 1).toUpperCase() + this.tempText.substring(1);
		}

		Log.d("com.resmed.refresh.mindClear", "Adding text note with text : " + this.tempText);
		this.mindClearItems.addElement(new MindClearText(this.tempText, System.currentTimeMillis()));
		Collections.sort(this.mindClearItems.getMindClearNotes(), this.comparator);
	}

	public void addMindClearVoice(String var1, Integer var2) {
		this.mindClearItems.addElement(new MindClearVoice(var1, System.currentTimeMillis(), var2));
		Collections.sort(this.mindClearItems.getMindClearNotes(), this.comparator);
	}

	public void deleteElement(int var1) {
		this.mindClearItems.getMindClearNotes().remove(var1);
		Collections.sort(this.mindClearItems.getMindClearNotes(), this.comparator);
	}

	public int getCurrentPosition() {
		return this.currentIndex;
	}

	public String getFileName() {
		return this.mindClearItems.getFileName();
	}

	public List getMindClearNotes() {
		Collections.sort(this.mindClearItems.getMindClearNotes(), this.comparator);
		return this.mindClearItems.getMindClearNotes();
	}

	public int getNumberOfUnreadNotes() {
		List var1 = this.mindClearItems.getMindClearNotes();
		int var2 = 0;
		Iterator var3 = var1.iterator();

		while(var3.hasNext()) {
			if(!((MindClearBase)var3.next()).isRead()) {
				++var2;
			}
		}

		return var2;
	}

	public String getTempFileName() {
		return this.mindClearItems.getTempFileName();
	}

	public void logout() {
		mindClearMapper = null;
	}

	public void setCurrentPosition(int var1) {
		this.currentIndex = var1;
	}

	public void setTempText(String var1) {
		this.tempText = var1;
	}

	public void storeDataIntoFilePreference() {
		GsonBuilder var1 = new GsonBuilder();
		var1.excludeFieldsWithoutExposeAnnotation();
		String var3 = var1.create().toJson(this.mindClearItems);
		refreshModelController.storeClearMindData(var3);
	}
}