package filesystem;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.Date;

/**
 *  @invar	Each class or subclass must have a properly spelled name.
 * 			| isValidName(getName())
 *
 * @author Bram Oreel & Wout Thiers
 */
public abstract class Thing {

    /**
     * A parameter describing the name of an object
     */
    protected String name = getDefaultName();
    /**
     * A parameter describing if the item still exists.
     */
    protected boolean isTerminated = false;

    /**
     * A parameter stating the directory of an object. The standard directory will always be "dir"
     */
    protected Directory directory = null;

    /**
     * CONSTRUCTORS
     *
     * Initialise a new thing with given filesystem.Directory
     *
     * @param mydirectory
     *        a parameter describing the address of the directory one level higher then the thing
     *
     * @effect the directory of the thing is set to the given directory
     *          |setDirectory(mydirectory)
     * @effect the thing is added to the Arraylist of items that the directory contains
     *          |myDirectory.add(this)
     *
     * @throws DirAlreadyContainsThingException
     *         the program will throw an error if the
     *         item already exists within the given directory.
     *         |if mydirectory.contains.this throw error.
     */
    @Raw
    public Thing(Directory mydirectory) throws DirAlreadyContainsThingException{
        if(mydirectory == null)
            setDirectory(mydirectory);
        else {
            if(mydirectory.getContent().contains(this))
                throw new DirAlreadyContainsThingException();
            setDirectory(mydirectory);
            mydirectory.add(this);
        }

    }

    /**
     * Initialise a new root Thing.
     *
     * @effect the directory will be set to 'null'
     *         |this(null)
     */
    public Thing(){
        this(null);
    }



    /**
     * Terminate the item.
     * @effect  The item will be removed from its directory.
     *          | setDirectory(null)
     *          | remove()
     * @effect  The terminated state will be set to true.
     *          | this.isTerminated = true
     * @throws FileNotWritableException
     *          If the directory is not writable, this exception is thrown.
     *          | !getDirectory().isWriteable()
     */

    public void terminate() throws FileNotWritableException{
        if(!getDirectory().isWriteable())
            throw new FileNotWritableException(getDirectory());
        this.isTerminated = true;
        remove(getDirectory());
        setDirectory(null);
    }


    /**
     * Return the name of this file.
     * @note		See Coding Rule 19 for the Basic annotation.
     */
    @Raw
    @Basic
    public String getName() {
        return name;
    }

    /**
     * Check whether the given name is a legal name for a file.
     *
     * @param  	name
     *			The name to be checked
     * @return	True if the given string is effective, not
     * 			empty and consisting only of letters, digits, dots,
     * 			hyphens and underscores; false otherwise.
     * 			| result ==
     * 			|	(name != null) && name.matches("[a-zA-Z_0-9.-]+")
     */
    @Model
    protected boolean isValidName(String name) {
        return (name != null && name.matches("[a-zA-Z_0-9.-]+"));
    }

    /**
     * Set the name of this file to the given name.
     *
     * @param   name
     * 			The new name for this file.
     * @post    If the given name is valid, the name of
     *          this file is set to the given name,
     *          otherwise the name of the file is set to a valid name (the default).
     *          | if (isValidName(name))
     *          |      then new.getName().equals(name)
     *          |      else new.getName().equals(getDefaultName())
     */
    @Raw @Model
    protected void setName(String name) {
        if (isValidName(name)) {
            this.name = name;
        } else {
            this.name = getDefaultName();
        }
    }

    /**
     * Return the name for a new file which is to be used when the
     * given name is not valid.
     *
     * @return   A valid file name.
     *         | isValidName(result)
     */
    @Model @Immutable
    private static String getDefaultName() {
        return "new_file";
    }

    /**
     * Change the name of this file to the given name.
     *
     * @return
     * @param    name The new name for this file.
     * @effect The name of this file is set to the given name,
     * if this is a valid name otherwise there is no change.
     * | if (isValidName(name)
     * | then setName(name)
      @effect after the name is changed, the object map is sorted by name
     *        |getDirectory.sortMap();
     */
    public void changeName(String name) {

        if (isValidName(name)){
            setName(name);
            setModificationTime();
            if(getDirectory() != null){
                getDirectory().sortMap();
        }
    }}


    /**
     *  Sets the directory of the thing to the giving directory
     * @param mydirectory
     *        a parameter stating the directory in which we want to place the thing
     */
    @Raw
    @Model
    protected void setDirectory(Directory mydirectory) {
        this.directory = mydirectory;
    }


    /**
     *
     * @return returns the current directory
     */
    @Basic
    public Directory getDirectory(){
        return this.directory;
    }

    /**
     * removes an item from a directory and sets its own directory to null.
     * @post after the item is removed, the items directory is sorted again
     * @throws FileNotWritableException
     *        this is thrown if the current directory is not writable.
     *        |!location.isWriteable()
     */
    @Model
    protected void remove(Directory dir) throws FileNotWritableException{
        if(!dir.isWriteable())
            throw new FileNotWritableException(dir);
        if(dir != null){
            dir.getContent().remove(this);
            dir.sortMap();
        }
    }
    /**
     * Checks if the name of a map already contains a filesystem.File, filesystem.Link or Map with the same name
     * @param location
     *        the map that is investigated
     * @return returns true if the name is still available.
     *         returns false if there already is an item with that name
     *         , ignoring the difference between lower- and uppercase letters.
     */
    @Model
    protected boolean nameNotInMap(Directory location){
        for(int i =0; i<location.getContent().size(); i++){
            Thing item = location.getContent().get(i);
            if(item.getName().toLowerCase() == getName().toLowerCase())
                return false;
        }
        return true;
    }

    /**
     * Returns the root filesystem.Directory of a thing. Returns itself when the directory is a root filesystem.Directory
     * @return The root filesystem.Directory
     */
    @Basic
    public Directory getRoot(){
        Directory nextdir = getDirectory();
        if(nextdir == null)
            return (Directory) this;
        else
            return nextdir.getRoot();

    }

    /**
     * @return returns the name a files root directory
     *         |getRoot().getName();
     */
    @Basic
    public String getRootName(){
        return getRoot().getName();
    }

    /**
     * Method to get a string of it's path.
     * @return returns the directory path  of a filesystem.Link or Map, divided by forward slashes
     *         | String path == "/" + nextDir.getName() "/" + getName();
     */
    public String getAbsolutePath(){
        String path = "/" + getName();
        Directory nextDir = getDirectory();
        while (nextDir != null){
            path = "/" + nextDir.getName() + path;
            nextDir = nextDir.getDirectory();
        }
        return path;
    }

    /**
     * checks if the location is not the current location or a null reference
     *
     * @param location
     *        the location to send the file to
     * @return returns true if valid location
     */
    @Model
    protected boolean isValidLocation(Directory location){
        return((location != this.getDirectory()) && (location != null));
    }

    /**********************************************************
     * creationTime
     **********************************************************/

    /**
     * Variable referencing the time of creation.
     */
    private final Date creationTime = new Date();

    /**
     * Return the time at which this file was created.
     */
    @Basic @Immutable
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * Check whether the given date is a valid creation time.
     *
     * @param  	date
     *         	The date to check.
     * @return 	True if and only if the given date is effective and not
     * 			in the future.
     *         	| result ==
     *         	| 	(date != null) &&
     *         	| 	(date.getTime() <= System.currentTimeMillis())
     */
    @Model
    @Raw
    public static boolean isValidCreationTime(Date date) {
        return 	(date!=null) &&
                (date.getTime()<=System.currentTimeMillis());
    }

    /**********************************************************
     * modificationTime
     **********************************************************/

    /**
     * Variable referencing the time of the last modification,
     * possibly null.
     */
    private Date modificationTime = null;

    /**
     * Return the time at which this thing was last modified, that is
     * at which the name or size was last changed. If this thing has
     * not yet been modified after construction, null is returned.
     */
    @Raw @Basic
    public Date getModificationTime() {
        return modificationTime;
    }

    /**
     * Check whether this file can have the given date as modification time.
     *
     * @param	date
     * 			The date to check.
     * @return 	True if and only if the given date is either not effective
     * 			or if the given date lies between the creation time and the
     * 			current time.
     *         | result == (date == null) ||
     *         | ( (date.getTime() >= getCreationTime().getTime()) &&
     *         |   (date.getTime() <= System.currentTimeMillis())     )
     */
    @Raw
    @Model
    protected boolean canHaveAsModificationTime(Date date) {
        return (date == null) ||
                ( (date.getTime() >= getCreationTime().getTime()) &&
                        (date.getTime() <= System.currentTimeMillis()) );
    }

    /**
     * Set the modification time of this thing to the current time.
     *
     * @post   The new modification time is effective.
     *         | new.getModificationTime() != null
     * @post   The new modification time lies between the system
     *         time at the beginning of this method execution and
     *         the system time at the end of method execution.
     *         | (new.getModificationTime().getTime() >=
     *         |                    System.currentTimeMillis()) &&
     *         | (new.getModificationTime().getTime() <=
     *         |                    (new System).currentTimeMillis())
     */
    @Model
    protected void setModificationTime() {
        modificationTime = new Date();
    }

    /**
     * Return whether this file, directory or link and the given other file, directory or link have an
     * overlapping use period.
     *
     * @param 	other
     *        	The other file, directory or link to compare with.
     * @return 	False if the other thing is not effective
     * 			False if the prime object does not have a modification time
     * 			False if the other thing is effective, but does not have a modification time
     * 			otherwise, true if and only if the open time intervals of this thing and
     * 			the other thing overlap
     *        	| if (other == null) then result == false else
     *        	| if ((getModificationTime() == null)||
     *        	|       other.getModificationTime() == null)
     *        	|    then result == false
     *        	|    else
     *        	| result ==
     *        	| ! (getCreationTime().before(other.getCreationTime()) &&
     *        	|	 getModificationTime().before(other.getCreationTime()) ) &&
     *        	| ! (other.getCreationTime().before(getCreationTime()) &&
     *        	|	 other.getModificationTime().before(getCreationTime()) )
     */
    @Raw
    public boolean hasOverlappingUsePeriod(Thing other) {
        if (other == null) return false;
        if(getModificationTime() == null || other.getModificationTime() == null) return false;
        return !( ( ! getCreationTime().after(other.getCreationTime()) &&
                ! getModificationTime().after(other.getCreationTime()) ) ||
                 (!other.getCreationTime().after(getCreationTime()) &&
                        !other.getModificationTime().after(getCreationTime()) ));
    }

    /**
     * Checks if a map is a child of another map
     *
     * @param map
     *        the parent map we want to check it's children of
     *
     * @return returns true if the parent directory has this as one of it's children.
     */
    public boolean isDirectOrIndirectChildOf(Directory map){
        if(this.getDirectory() == map)
            return true;
        else if (this.getDirectory() == null)
            return false;

        Directory nextDir = this.getDirectory();
        return nextDir.isDirectOrIndirectChildOf(map);
    }
}