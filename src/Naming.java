import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 *  @invar	Each class or subclass must have a properly spelled name.
 * 			| isValidName(getName())
 *
 * @author bramo
 *
 */
public abstract class Naming {

    /**
     * A parameter describing the name of an object
     */
    private String name;

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


}
