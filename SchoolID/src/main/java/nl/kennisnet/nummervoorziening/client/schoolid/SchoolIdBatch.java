package nl.kennisnet.nummervoorziening.client.schoolid;

import java.util.Map;

/**
 * Entity with information about generated in batch operation School IDs.
 */
public class SchoolIDBatch {

    private Map<Integer, String> success;

    private Map<Integer, String> failed;

    /**
     * Gets the map with indexes of passed hashed PGN as keys and School IDs as values.
     *
     * @return Map with indexes of passed hashed PGN as keys and School IDs as values.
     */
    public Map<Integer, String> getSuccess() {
        return success;
    }

    /**
     * Sets the map with indexes of passed hashed PGN as keys and School IDs as values.
     *
     * @param success Map with indexes of passed hashed PGN as keys and School IDs as values.
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
