/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.config.server;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author Alberto C. Ríos
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConfigServerApplication.class,
		properties = {
				"spring.profiles.active:composite",
				"spring.cloud.config.server.composite[0].type:credhub",
				"spring.cloud.config.server.composite[0].url:https://credhub:8844"
		},
		webEnvironment = RANDOM_PORT)
public class CredhubCompositeConfigServerIntegrationTests extends CredhubIntegrationTest {

	@LocalServerPort
	private int port;

	@Test
	public void shouldRetrieveValuesFromCredhub() {
		Environment environment = new TestRestTemplate().getForObject(
				"http://localhost:" + this.port + "/myapp/master/default", Environment.class);

		assertFalse(environment.getPropertySources().isEmpty());
		assertEquals("credhub-myapp", environment.getPropertySources().get(0).getName());
		assertEquals("{key=value}",
				environment.getPropertySources().get(0).getSource().toString());
	}

}
