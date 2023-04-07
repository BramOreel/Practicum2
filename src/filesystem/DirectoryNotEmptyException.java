package filesystem;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * Exception that is trown when the directory is not empty when it has to be empty
 * @author Wout Thiers & Bram Oreel
 */
public class DirectoryNotEmptyException extends RuntimeException{

    /**
     * Required because this class inherits from Exception
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable referencing directory that was not empty.
     */
    private final Directory directory;

    /**
     * Initialize this new directory not empty exception involving the
     * given directory.
     *
     * @param	directory
     * 			The directory for the new file not writable exception.
     * @post	The directory involved in the exception
     * 			is set to the given directory.
     * 			| new.getDirectory() == directory
     */
    public DirectoryNotEmptyException(Directory directory) {
        this.directory =  directory;
    }

    /**
     * Return the directory involved that is not empty.
     */
    @Basic
    @Immutable
    public Thing getDirectory() {
        return directory;
    }

}
