import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;






/**
 * A class of files.
 *
 * @invar	Each file must have a properly spelled name.
 * 			| isValidName(getName())
 * @invar	Each file must have a valid size.
 * 			| isValidSize(getSize())
 * @invar   Each file must have a valid creation time.
 *          | isValidCreationTime(getCreationTime())
 * @invar   Each file must have a valid modification time.
 *          | canHaveAsModificationTime(getModificationTime())
 * @invar   Each file must be contained within a directory. A file can't be a root directory
 *          | getdirectory() != null
 *
 * @author  Mark Dreesen
 * @author  Tommy Messelis
 * @author bramo
 * @version 4.0
 *
 * @note		See Coding Rule 48 for more info on the encapsulation of class invariants.
 */
public class File extends Thing{

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Initialize a new file with given name, size and writability.
     *
     * @param  	name
     *         	The name of the new file.
     * @param  	size
     *         	The size of the new file.
     * @param  	writable
     *         	The writability of the new file.
     * @param   type
     *          the type of the file
     * @effect  The name of the file is set to the given name.
     * 			If the given name is not valid, a default name is set.
     *          | setName(name)
     * @effect	The size is set to the given size (must be valid)
     * 			| setSize(size)
     * @effect	The writability is set to the given flag
     * 			| setWritable(writable)
     * @effect  The type is set to a given option
     *
     * @post    The new creation time of this file is initialized to some time during
     *          constructor execution.
     *          | (new.getCreationTime().getTime() >= System.currentTimeMillis()) &&
     *          | (new.getCreationTime().getTime() <= (new System).currentTimeMillis())
     * @post    The new file has no time of last modification.
     *          | new.getModificationTime() == null
     *
     * @note	The constructor is annotated raw because at the start of the execution, not all fields are
     * 			defaulted to a value that is accepted by the invariants.
     * 			E.g. the name is defaulted to null, which is not allowed,
     * 			thus the object is in a raw state upon entry of the constructor.
     */
    @Raw
    public File(Directory dir,String name, int size, boolean writable, Type type) {
        super(dir);
        setName(name);
        setSize(size);
        setWritable(writable);
        this.type = type;
        getDirectory().sortMap();
    }

    /**
     * Initialize a new file with given name.
     *
     * @param   name
     *          The name of the new file.
     * @param   type
     *          THe type of the new file
     * @effect  This new file is initialized with the given name, a zero size
     * 			and true writability
     *         | this(name,0,true,type)
     */
    @Raw
    public File(Directory dir,String name, Type type) {
        this(dir,name,0,true, type);
    }

    /**
     * Variable referencing the type of the file. The type must be included in the enum list of type.java
     */
    private final Type type;

    public String getType() {
        return type.getExtension();
    }

    /**
     * Variable referencing the directory of the file
     */
    private Directory directory = new Directory("dir");



    /**
     * Terminate the item.
     * @post  The item will be removed from its directory and the items directory will be set to null.
     *       | setDirectory(null)
     *       | remove()
     * @post  The terminated state will be set to true.
     *       | this.isTerminated = true
     * @throws FileNotWritableException
     *        if the file is not writable, this exception will be thrown.
     */
    @Override
    public void terminate() throws FileNotWritableException, DirectoryNotEmptyException{
        if(! isWritable){
            throw new FileNotWritableException(this);
        }
        else{  super.terminate();
        }}


    /**********************************************************
     * name - total programming
     **********************************************************/

    /**
     * Variable referencing the name of this file.
     * @note		See Coding Rule 32, for information on the initialization of fields.
     */
    private String name = null;

    /**
     * Change the name of this file to the given name.
     *
     * @return
     * @throws FileNotWritableException(this) This file is not writable
     *                                        | ! isWritable()
     * @param    name The new name for this file.
     * @effect The name of this file is set to the given name,
     * if this is a valid name and the file is writable,
     * otherwise there is no change.
     * | if (isValidName(name) && isWritable())
     * | then setName(name)
     * @effect If the name is valid and the file is writable, the modification time
     * of this file is updated.
     * | if (isValidName(name) && isWritable())
     * | then setModificationTime()
     * @effect after the filename is changed, the files directory is sorted by name
     *         |getDirectory.sortMap();
     */
    @Override
    public void changeName(String name) throws FileNotWritableException {
        if (isWritable()) {
            if (isValidName(name)){
                setName(name);
                setModificationTime();
                getDirectory().sortMap();
            }
        } else {
            throw new FileNotWritableException(this);
        }
    }



    /**********************************************************
     * size - nominal programming
     **********************************************************/

    /**
     * Variable registering the size of this file (in bytes).
     */
    private int size = 0;

    /**
     * Variable registering the maximum size of any file (in bytes).
     */
    private static final int maximumSize = Integer.MAX_VALUE;


    /**
     * Return the size of this file (in bytes).
     */
    @Raw @Basic
    public int getSize() {
        return size;
    }

    /**
     * Set the size of this file to the given size.
     *
     * @param  size
     *         The new size for this file.
     * @pre    The given size must be legal.
     *         | isValidSize(size)
     * @post   The given size is registered as the size of this file.
     *         | new.getSize() == size
     */
    @Raw @Model
    private void setSize(int size) {
        this.size = size;
    }

    /**
     * Return the maximum file size.
     */
    @Basic @Immutable
    public static int getMaximumSize() {
        return maximumSize;
    }

    /**
     * Check whether the given size is a valid size for a file.
     *
     * @param  size
     *         The size to check.
     * @return True if and only if the given size is positive and does not
     *         exceed the maximum size.
     *         | result == ((size >= 0) && (size <= getMaximumSize()))
     */
    public static boolean isValidSize(int size) {
        return ((size >= 0) && (size <= getMaximumSize()));
    }

    /**
     * Increases the size of this file with the given delta.
     *
     * @param   delta
     *          The amount of bytes by which the size of this file
     *          must be increased.
     * @pre     The given delta must be strictly positive.
     *          | delta > 0
     * @effect  The size of this file is increased with the given delta.
     *          | changeSize(delta)
     */
    public void enlarge(int delta) throws FileNotWritableException {
        changeSize(delta);
        setModificationTime();
    }

    /**
     * Decreases the size of this file with the given delta.
     *
     * @param   delta
     *          The amount of bytes by which the size of this file
     *          must be decreased.
     * @pre     The given delta must be strictly positive.
     *          | delta > 0
     * @effect  The size of this file is decreased with the given delta.
     *          | changeSize(-delta)
     */
    public void shorten(int delta) throws FileNotWritableException {
        changeSize(-delta);
        setModificationTime();
    }

    /**
     * Change the size of this file with the given delta.
     *
     * @param  delta
     *         The amount of bytes by which the size of this file
     *         must be increased or decreased.
     * @pre    The given delta must not be 0
     *         | delta != 0
     * @effect The size of this file is adapted with the given delta.
     *         | setSize(getSize()+delta)
     * @effect The modification time is updated.
     *         | setModificationTime()
     * @throws FileNotWritableException(this)
     *         This file is not writable.
     *         | ! isWritable()
     */
    @Model
    private void changeSize(int delta) throws FileNotWritableException{
        if (isWritable()) {
            setSize(getSize()+delta);
            setModificationTime();
        }else{
            throw new FileNotWritableException(this);
        }
    }






    /**********************************************************
     * writable
     **********************************************************/

    /**
     * Variable registering whether or not this file is writable.
     */
    private boolean isWritable = true;

    /**
     * Check whether this file is writable.
     */
    @Raw @Basic
    public boolean isWritable() {
        return isWritable;
    }

    /**
     * Set the writability of this file to the given writability.
     *
     * @param isWritable
     *        The new writability
     * @post  The given writability is registered as the new writability
     *        for this file.
     *        | new.isWritable() == isWritable
     */
    @Raw
    public void setWritable(boolean isWritable) {
        this.isWritable = isWritable;
    }

    /**
     * moves the file to the designated location if the location is effective and the location is different from the current location. Otherwise nothing will happen
     *
     * @param location
     *         the location of the directory
     * @effect the directory is changed to the listed directory
     *         |setDirectory(this)
     * @effect the File is removed from the contents of the old directory
     *         |this.remove();
     * @effect the File is added to the list of content of the designated location
     *         |location.add(this)
     * @effect after the thing is added, the map is sorted by name
     *        |sortMap();
     * @effect the modification time of the location is changed to the current time
     *        |setModificationTime();
     * @throws IllegalArgumentException
     *         this is thrown when the location is the current location or the location does not exist
     *         |!isValidLocation(location)
     * @throws NameNotAvailableException
     *         this is thrown when there already exists a File,Map or Link with the given name
     *         |(!nameNotInMap(location)
     * @throws FileNotWritableException
     *         this is throw if the location or current directory is not writable.
     *         | !getDirectory().isWriteable() | !location.isWriteable()
     */
    @Raw
    public void move(Directory location) throws FileNotWritableException,IllegalArgumentException,NameNotAvailableException{
        if(!location.isWriteable())
            throw new FileNotWritableException(location);
        if(!getDirectory().isWriteable())
            throw new FileNotWritableException(getDirectory());
        if(!isValidLocation(location))
            throw new IllegalArgumentException();
        if(!nameNotInMap(location))
            throw new NameNotAvailableException();
        Directory olddir = getDirectory();
        setDirectory(location);
        location.add(this);
        location.sortMap();
        remove(olddir);
        location.setModificationTime();
    }



    /**
     *
     * @return returns the directory path of a file divided by forward slashes. The file type is specified at the end of the path.
     */
    @Override
    public String getAbsolutePath(){
        return( super.getAbsolutePath() + type.getExtension());
    }



}

