package filesystem;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * An exception signaling the name isn't available anymore.
 * @author Wout Thiers & Bram Oreel
 */
public class NameNotAvailableException extends RuntimeException {
    private String name;
    public NameNotAvailableException(String name){
        this.name = name;
    }

    /**
     * @return The name that isn't available.
     */
    @Immutable
    @Basic
    public String getName() {
        return name;
    }
}
