/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.io;

import net.kuujo.copycat.io.util.ReferencePool;

/**
 * Buffer reader pool.
 * <p>
 * The reader pool reduces garbage produced by frequent reads by tracking references to existing readers and recycling
 * readers once they're closed.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class BufferReaderPool extends ReferencePool<BufferReader> {
  private final Buffer buffer;

  public BufferReaderPool(Buffer buffer) {
    super(r -> new BufferReader(buffer.bytes(), 0, 0, r));
    this.buffer = buffer;
  }

  @Override
  public BufferReader acquire() {
    BufferReader reader = super.acquire();
    buffer.acquire();
    return reader;
  }

  @Override
  public void release(BufferReader reference) {
    buffer.release();
    super.release(reference);
  }

}
