package com.lucidlink.Frame;

import com.facebook.react.bridge.ReadableMap;

public class Vector2i implements Comparable<Vector2i> {
	@Override
	public int compareTo(Vector2i other) {
		return ((Integer)y).compareTo(other.y);
	}

	public interface Func_Int {
		public int run(int a);
	}

	public static Vector2i FromMap(ReadableMap map) {
		return new Vector2i(map.getInt("x"), map.getInt("y"));
	}

	static Vector2i zero() { return new Vector2i(0, 0); }
	static Vector2i one() { return new Vector2i(1, 1); }

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int x;
	public int y;

	@Override
	public String toString() {
		return this.x + " " + this.y;
	}
	@Override public boolean equals(Object other) { return this.toString() == other.toString(); }

	public Vector2i NewX(int x) { return new Vector2i(x, this.y); }
	public Vector2i NewX(Func_Int xFunc) { return new Vector2i(xFunc.run(this.x), this.y); }
	public Vector2i NewY(int y) { return new Vector2i(this.x, y); }
	public Vector2i NewY(Func_Int yFunc) { return new Vector2i(this.x, yFunc.run(this.y)); }

	Vector2i Minus(Vector2i other) {
		return new Vector2i(this.x - other.x, this.y - other.y);
	}
	Vector2i Plus(Vector2i other) {
		return new Vector2i(this.x + other.x, this.y + other.y);
	}
	Vector2i Times(Vector2i other) {
		return new Vector2i(this.x * other.x, this.y * other.y);
	}

	public double Distance(Vector2i other) {
		int width = Math.abs(other.x - x);
		int height = Math.abs(other.y - y);
		return Math.sqrt((width * width) + (height * height));
	}
}