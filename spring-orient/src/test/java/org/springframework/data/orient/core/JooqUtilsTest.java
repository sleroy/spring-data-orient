package org.springframework.data.orient.core;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JooqUtilsTest {

	@Test
	public void testContext() throws Exception {
		Assert.assertNotNull(JooqUtils.context());
	}

	@Test
	public void testFrom() throws Exception {
		Assert.assertNotNull(JooqUtils.from("SELECT * FROM Humans"));
	}

}
