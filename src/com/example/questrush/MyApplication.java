package com.example.questrush;

import android.app.Application;

import com.parse.Parse;

public class MyApplication extends Application {
	public void onCreate() {
		Parse.initialize(this, "HUjBUjTRRxyiY87IokQct469qO1ZAxySNihD8Jn5", "Mg5rTCoAb5skwe1TxyhjaxOhIQmroHCIJotc82Sj");
	}
}
