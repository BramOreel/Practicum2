import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class expressing a link to a file or map
 *
 * @invar each link must have a valid name.
 *       |isValidName(name)
 * @invar a link must always reference a file or map. Once the link is made, it cannot be changed
 *       |setlink(directory || file)
 * @invar a link must always be contained within a directory
 *       |link.getdirectory != null
 *
 * @author WoutThiers
 * @version 1.0
 */
public class Link extends Thing{

    /**
     * a variabele representing the s
     */
    private boolean State = true;

    private Thing reference = null;


    public boolean getState() {
        if(getReference().isTerminated)
            setState(false);
        return this.State;
    }

    /**
     *
     * @return returns the object that the link references
     */
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
     *
     * @param dir
     * @param name
     * @param linkedItem
     */
    public Link(Directory dir, String name, Thing linkedItem){
        super(dir);
        setName(name);
        setReference(linkedItem);
    }

    /**
     * moves the link to the designated location if the location is effective and the location is different from the current location. Otherwise nothing will happen
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
     *         this is thrown when there already exists a File,Map or Link with the given name
     *         |(!nameNotInMap(location)
     */
    @Raw
    public void move(Directory location) throws IllegalArgumentException,NameNotAvailableException{
        if(!isValidLocation(location))
            throw new IllegalArgumentException();
        if(!nameNotInMap(location))
            throw new NameNotAvailableException();

        remove();
        setDirectory(location);
        location.add(this);
        location.sortMap();
        location.setModificationTime();
    }


}
