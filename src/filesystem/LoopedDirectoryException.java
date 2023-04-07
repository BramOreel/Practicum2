package filesystem;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * A class for signaling illegal attempts to change the directory of a directory
 * that would create a loop.
 *
 * @author 	Wout Thiers & Bram Oreel
 */
public class LoopedDirectoryException extends RuntimeException {

    /**
     * Required because this class inherits from Exception
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable referencing the file to which change was denied.
     */
    private final Directory map;

    /**
     * Initialize this new file not writable exception involving the
     * given file.
     *
     * @param	map
     * 			The map for the new file not writeable exception
     * @post	The file involved in the new file not writable exception
     * 			is set to the given file.
     * 			| new.getFile() == file
     */
    public LoopedDirectoryException(Directory map) {
        this.map = map;
    }

    /**
     * Return the file involved in this file not writable exception.
     */
    @Basic @Immutable
    public Directory getMap() {
        return map;
    }

}