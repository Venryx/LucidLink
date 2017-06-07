package com.resmed.refresh.utils;

import java.util.*;

public class SortedList<T extends Comparable<T>> extends AbstractList<T> {
	private int capacity;
	private ArrayList<T> internalList;
	private T lastReplacedElement;

	public SortedList() {
		this.internalList = new ArrayList<T>();
	}

	public SortedList(final int capacity) {
		this.internalList = new ArrayList<T>(capacity);
		this.capacity = capacity;
	}

	private int belongsInList(final T t) {
		for (int i = 0; i < this.internalList.size(); ++i) {
			if (t.compareTo(this.internalList.get(i)) > 0) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public T get(final int n) {
		return this.internalList.get(n);
	}

	public T getLastReplacedElement() {
		return this.lastReplacedElement;
	}

	public boolean insert(final T t) {
		if (this.internalList.size() < this.capacity) {
			this.internalList.add(t);
			return true;
		}
		final int belongsInList = this.belongsInList(t);
		if (belongsInList >= 0) {
			this.lastReplacedElement = this.internalList.set(belongsInList, t);
			Collections.sort(this.internalList);
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return this.internalList.size();
	}
}
