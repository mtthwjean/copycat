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
package net.kuujo.copycat.io.serializer;

import net.kuujo.copycat.io.Buffer;
import net.kuujo.copycat.io.util.ReferenceCounted;

/**
 * Provides pooled object serialization.
 * <p>
 * The {@code PooledSerializer} is provided as a base class for {@link net.kuujo.copycat.io.util.ReferenceCounted} object serializers. When objects
 * are deserialized by pooled serializers, available objects will be acquired via {@link PooledSerializer#acquire(Class)}
 * rather than being constructed new.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public abstract class PooledSerializer<T extends ReferenceCounted<T>> implements Serializer<T> {

  @Override
  public T read(Class<T> type, Buffer buffer) {
    T object = acquire(type);
    read(object, buffer);
    return object;
  }

  /**
   * Acquires a reference.
   *
   * @param type The reference type.
   * @return The acquired reference.
   */
  protected abstract T acquire(Class<T> type);

  /**
   * Reads the object from the given buffer.
   *
   * @param object The object to read.
   * @param buffer The buffer from which to read the object.
   */
  protected abstract void read(T object, Buffer buffer);

}
