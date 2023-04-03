import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 *  @invar	Each class or subclass must have a properly spelled name.
 * 			| isValidName(getName())
 *
 *
 * @author bramo
 *
 */
public abstract class Thing {

    /**
     * A parameter describing the name of an object
     */
    private String name;

    /**
     * A parameter stating the directory of an object. The standard directory will always be "dir"
     */
    private Directory directory = null;

    /**
     * CONSTRUCTORS
     *
     * Initialise a new thing with given Directory
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
    public Thing(Directory mydirectory) throws DirAlreadyContainsThingException{
        if(mydirectory.getContent().contains(this))
            throw new DirAlreadyContainsThingException();
        setDirectory(mydirectory);
        mydirectory.add(this);
    }

    /**
     * Initialise a new root Directory
     *
     * @effect the directory will be set to 'null'
     *         |Thing(null)
     */

    public Thing(){
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
    public boolean isValidName(String name) {
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
    @Model
    private static String getDefaultName() {
        return "new_file";
    }

    /**
     * Change the name of this file to the given name.
     *
     * @param	name
     * 			The new name for this file.
     * @effect  The name of this file is set to the given name,
     * 			if this is a valid name otherwise there is no change.
     * 			| if (isValidName(name)
     *          | then setName(name)
     *
     */
    public void changeName(String name) {

        if (isValidName(name)){
            setName(name);
        }
    }


    /**
     *  Sets the directory of the thing to the giving directory
     *
     * @param mydirectory
     *        a parameter stating the directory in which we want to place the thing
     *
     */
    public void setDirectory(Directory mydirectory) {
        this.directory = mydirectory;
    }

    /**
     *
     * @return returns the current directory
     */
    public Directory getDirectory(){
        return directory;
    }

    /**
     *
     */
    protected void remove(){
        Directory currentdir = getDirectory();
        if(currentdir != null){
            currentdir.getContent().remove(this);
        }
    }
    /**
     * Checks if the name of a map already contains a File, Link or Map with the same name
     * @param location
     *        the map that is investigated
     * @return returns true if the name is still available
     */
    @Model
    protected boolean nameNotInMap(Directory location){
        for(int i =0; i<location.getContent().size(); i++){
            Thing item = location.getContent().get(i);
            if(item.getName() == getName())
                return false;
        }
        return true;
    }
}