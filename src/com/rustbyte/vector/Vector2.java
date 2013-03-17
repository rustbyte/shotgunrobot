package com.rustbyte.vector;

public class Vector2 {
	public double x = 0.0;
	public double y = 0.0;
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public Vector2() {
		this.x = 0.0;
		this.y = 0.0;
	}
	
	public Vector2 add(Vector2 vec) {
		return new Vector2(this.x + vec.x, this.y + vec.y);
	}
	public Vector2 sub(Vector2 vec) {
		return new Vector2(this.x - vec.x, this.y - vec.y);
	}
	public Vector2 mult(double scalar) {
		return new Vector2(this.x * scalar, this.y * scalar);
	}
	public double length() {
		return Math.sqrt(Math.abs(this.x) * 2.0 + Math.abs(this.y) * 2.0);		
	}
	public Vector2 normalize() {
		double len = length();
		this.x /= len;
		this.y /= len;
		return new Vector2(this.x, this.y);
	}
	public double dot(Vector2 vec) {
		return (this.x * vec.x + this.y * vec.y);
	}
}
