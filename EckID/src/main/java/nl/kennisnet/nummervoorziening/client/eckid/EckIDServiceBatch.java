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
package nl.kennisnet.nummervoorziening.client.eckid;

import java.util.Map;

/**
 * Entity with information about generated in batch operation EckIDs.
 */
public class EckIDServiceBatch {

    private Map<Integer, String> success;

    private Map<Integer, String> failed;

    /**
     * Gets the map with indexes of passed hashed PGN as keys and EckIDs as values.
     *
     * @return Map with indexes of passed hashed PGN as keys and EckIDs as values.
     */
    public Map<Integer, String> getSuccess() {
        return success;
    }

    /**
     * Sets the map with indexes of passed hashed PGN as keys and EckIDs as values.
     *
     * @param success Map with indexes of passed hashed PGN as keys and EckIDs as values.
     */
    public void setSuccess(Map<Integer, String> success) {
        this.success = success;
    }

    /**
     * Gets the map with indexes of passed hashed PGN as keys and error messages as values.
     *
     * @return Map with indexes of passed hashed PGN as keys and error messages as values.
     */
    public Map<Integer, String> getFailed() {
        return failed;
    }

    /**
     * Sets the map with indexes of passed hashed PGN as keys and error messages as values.
     *
     * @param failed Map with indexes of passed hashed PGN as keys and error messages as values.
     */
    public void setFailed(Map<Integer, String> failed) {
        this.failed = failed;
    }
}
