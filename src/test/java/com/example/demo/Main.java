package com.example.demo;

import java.util.Date;

public class Main {
	
	public static void main(String[] args) {
		System.out.printf("%tT\t Thread=%s\t Msg=%s\r"
				,new Date()
				, Thread.currentThread().getName()
				, "aaaaaa");
	}

}
