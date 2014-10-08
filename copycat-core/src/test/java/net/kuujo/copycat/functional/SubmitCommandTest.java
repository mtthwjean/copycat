/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.functional;

import java.util.Set;

import net.kuujo.copycat.AbstractCopycatTest;
import net.kuujo.copycat.CopycatContext;

import org.testng.annotations.Test;

/**
 * Submit command test.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
@Test
public class SubmitCommandTest extends AbstractCopycatTest {
  public void testCopyCat() throws Throwable {
    Set<CopycatContext> contexts = startCluster(3);
    final CopycatContext context = contexts.iterator().next();
    context.on().leaderElect().run((event) -> {
      context.submitCommand("set", "foo", "bar").thenRun(() -> {
        context.submitCommand("set", "bar", "baz").thenRun(() -> {
          context.submitCommand("get", "foo").whenComplete((result, error) -> {
            threadAssertNull(error);
            threadAssertEquals("bar", result);
            resume();
          });
        });
      });
    });

    await(100000);
  }
}
