package z6;

import processing.core.PApplet;
import processing.core.PImage;

public final class Renderer {
	private static PApplet p;

	public static int WIDTH = 500;
	public static int HEIGHT = 500;

	public static float frameRate(){
		return p.frameRate;
	}
	
	public static float noise(float x, float y){
		return p.noise(x, y);
	}
	public static void noiseSeed(long s){
		p.noiseSeed(s);
	}

	public static void rect(float x, float y, float w, float h) {
		p.rect(x, y, w, h);
	}
	
	public static void ellipse(float x, float y, float rx, float ry){
		p.ellipse(x, y, rx, ry);
	}

	public static PImage loadImage(String path) {
		return p.loadImage(path);
	}
	
	public static int millis(){
		return p.millis();
	}

	public static void setup(PApplet _p) {
		p = _p;
	}

	public static void translate(float x, float y) {
		p.translate(x, y);
	}

	public static void pushMatrix() {
		p.pushMatrix();
	}

	public static void popMatrix() {
		p.popMatrix();
	}

	public static void rotate(float r) {
		p.rotate(r);
	}

	public static void image(PImage img, int x, int y) {
		p.image(img, x, y);
	}

	public static void pushStyle() {
		p.pushStyle();
	}

	public static void popStyle() {
		p.popStyle();
	}

	public static void line(float x, float y, float x2, float y2) {
		p.line(x, y, x2, y2);
	}

	public static void stroke(int r, int g, int b) {
		p.stroke(r, g, b);
	}
	
	public static void noStroke(){
		p.noStroke();
	}
	
	public static void strokeWeight(int w) {
		p.strokeWeight(w);
	}
	
	public static void fill(int r, int g, int b){
		p.fill(r,g,b);
	}

	public static void fill(int b, int a) {
		p.fill(b, a);
	}

	public static void textSize(float s) {
		p.textSize(s);
	}

	public static void text(String str, float x, float y) {
		p.text(str, x, y);
	}
}
