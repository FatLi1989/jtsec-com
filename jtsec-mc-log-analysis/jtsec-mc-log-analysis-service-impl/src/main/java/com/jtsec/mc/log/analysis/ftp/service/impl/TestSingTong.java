package com.jtsec.mc.log.analysis.ftp.service.impl;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/10/1914:01
 */
public class TestSingTong {
	private static TestSingTong testSingTong = null;

	private TestSingTong(){
		System.out.println ("你好");
	}

	public static TestSingTong getInstance() {
		if (testSingTong == null) {
			testSingTong = new TestSingTong();
		}
		return testSingTong;
	}

	public static void main(String arg[]){

		TestSingTong.getInstance ();
	}
}
