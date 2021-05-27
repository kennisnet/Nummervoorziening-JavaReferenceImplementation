/*
 * Copyright 2016, Stichting Kennisnet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.kennisnet.nummervoorziening.client.eckid.scrypter;

/**
 * Constants for hashing pgn values. Should not be changed if you want always
 * get the same values after hashing.
 *
 * The salt is configured in a property file which should remain the same as well, to always get the same value.
 * However the salt value does differ per environment.
 */
public interface Constants {

    int N = 131072;

    int r = 8;

    int p = 4;
}
