package com.fb.qa.facebookkeyworddriven;

import org.testng.annotations.Test;

import com.fb.qa.engine.KeywordEngine;

public class LoginTest {

	public KeywordEngine keywordEngine;

	@Test
	public void loginTestWithKeywordDriven() {
		keywordEngine = new KeywordEngine();
		keywordEngine.startExecution("login");
	}
}
