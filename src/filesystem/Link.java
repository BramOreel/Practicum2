package filesystem;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class expressing a link to a file or map
 *
 * @invar each link must have a valid name.
 *       |isValidName(name)
 * @invar a link must always reference a thing and it cannot reference another link. Once the link is made, it cannot be changed
 *       |setlink(filesystem.Thing),  (filesystem.Thing != filesystem.Link)
 * @invar a link must always be contained within a directory
 *       |link.getdirectory != null
 *
 * @author WoutThiers, Bram Oreel
 * @version 1.0
 */
public class Link extends Thing {

    /**
     * A variabele representing the state of the referenced item, if its terminated the state is false.
     */
    private boolean State = true;
    /**
     * A variable representing the item the link is referencing.
     */
    private Thing reference = null;

    /**
     * @return returns whether the object that it references is terminated.
     *         True means it is not terminated and false when it is.
     */
    @Basic
    public boolean getState() {
        if(getReference().isTerminated)
            setState(false);
        return this.State;
    }

    /**
     * @return returns the object that the link references
     */
    @Raw
    @Immutable
    public Thing getReference() {
        return this.reference;
    }

    /**
     * sets the state of the link. True means the referenced object is still effective. False means it has been terminated.
     * @param state
     *        the state of the link
     */
    protected void setState(boolean state) {
        this.State = state;
    }

    /**
     * Sets the item which the link references.
     * @param reference
     *        The item the link references, it cannot be another link.
     * @throws IllegalArgumentException
     *         if the argument is a link.
     * @throws ReferenceDeletedException
     *         if the reference has been terminated and thus not effective
     */

    private final void setReference(Thing reference) throws IllegalArgumentException, ReferenceDeletedException {
        if(reference instanceof Link){
            throw new IllegalArgumentException();
        }
        else {
            if(reference.isTerminated)
                throw new ReferenceDeletedException();
            this.reference = reference;
        }
    }

    /**
     * Creates a link.
     * @param dir
     *        The directory where the link is placed.
     * @param name
     *        The name of the link.
     * @param linkedItem
     *        The item the link points to.
     * @effect The link is initialized as a new filesystem.Thing with the given
     *         directory.
     *         | super(dir)
     * @effect The given name is set as the name of the link.
     *         | setName(name)
     * @effect The given reference is set as the reference of the link.
     *         | setReference(name)
     */
    @Raw
    public Link(Directory dir, String name, Thing linkedItem){
        super(dir);
        setName(name);
        setReference(linkedItem);
    }

    /**
     * Moves the link to the designated location if the location is effective and
     * the location is different from the current location. Otherwise nothing will happen
     *
     * @param location
     *         the location of the directory
     * @effect the directory is changed to the listed directory
     *         |setDirectory(this)
     * @effect the link is removed from the contents of the old directory
     *         |this.remove();
     * @effect the link is added to the list of content of the designated location
     *         |location.add(this)
     * @effect after the thing is added, the map is sorted by name
     *        |sortMap();
     * @effect the modification time of the location is changed to the current time
     *        |setModificationTime();
     *
     * @throws IllegalArgumentException
     *         this is thrown when the location is the current location or the location does not exist
     *         |!isValidLocation(location)
     * @throws NameNotAvailableException
     *         this is thrown when there already exists a filesystem.File,Map or filesystem.Link with the given name
     *         |(!nameNotInMap(location)
     * @throws FileNotWritableException
     *         this is throw if the location or current directory is not writable.
     *         | !getDirectory().isWriteable() | !location.isWriteable()
     */
    @Raw
    public void move(Directory location) throws FileNotWritableException,IllegalArgumentException, NameNotAvailableException {
        if(!getDirectory().isWriteable())
            throw new FileNotWritableException(getDirectory());
        if(!location.isWriteable())
            throw new FileNotWritableException(location);
        if(!isValidLocation(location))
            throw new IllegalArgumentException();
        if(!nameNotInMap(location))
            throw new NameNotAvailableException(getName());

        Directory olddir = getDirectory();
        setDirectory(location);
        location.add(this);
        location.sortMap();
        remove(olddir);
        location.setModificationTime();
    }


}
