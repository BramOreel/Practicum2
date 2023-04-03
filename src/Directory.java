import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.ArrayList;

/**
 * A class for making a directory
 *
 * @author bramo
 * @version 1.1
 *
 * @invar A directory must always have a valid name
 *        |isValidName(name)
 * @invar A directory cannot have itself as one of the items within content
 *        |noLoops(directory)
 */

public class Directory extends Thing {

    /**
     * Variable referencing the name of this map
     */
    private String name = null;

    /**
     * Variable stating if the users is allowed to change the name of the map or add or remove items from it
     */
    private boolean writeable = true;

    /**
     * Variable which contains the content of the map
     */
    private ArrayList<Thing> content = new ArrayList<Thing>();
    /**
     * Variable stating the directory one level up of the Directory
     */
    private Directory root = null;



    public Directory(Directory dir, String name, boolean writeable){
        super(dir);
        setName(name);
        //setWriteable(writeable);
    }

    public Directory(Directory dir, String name){
        this(dir,name,true);
    }

    public Directory(String name, boolean writeable){
        super();
        setName(name);
        //setWriteable(writeable);
    }

    public Directory(String name){
        this(name,true);
    }


    /**
     * Check whether the given name is a legal name for a file.
     *
     * @param  	name
     *			The name to be checked
     * @return	True if the given string is effective, not
     * 			empty and consisting only of letters, digits,
     * 			hyphens and underscores; false otherwise.
     * 			| result ==
     * 			|	(name != null) && name.matches("[a-zA-Z_0-9-]+")
     */
    @Override
    public boolean isValidName(String name){
        return( name != null && name.matches("[a-zA-Z_0-9-]+"));
    }

    /**
     * adds a File, Map or Link to the directory's content
     * @param thing
     *        the thing to be added
     */
    @Model
    protected void add(Thing thing){
        content.add(thing);
    }

    /**
     *
     * @return returns the content of the directory
     */
    public ArrayList<Thing> getContent() {
        return content;
    }

    /**
     * Makes the directory a rootDirectory
     *
     * @effect the directory is set to null
     *         |setDirectory(null)
     * @effect the map is removed from the contents of the maps directory
     *         |remove(directory).fromcontent
     */
    public void makeRoot(){
        remove();
        setDirectory(null);
    }


    /**
     * Checks if a Directory contains an item one level down
     *
     * @param thing
     *        the thing we are looking for
     *
     * @return true or false
     */
    public boolean hasAsItem(Thing thing){
        return(getContent().contains(thing));
    }

    /**
     * Moves a map into another directory if allowed
     *
     * @param location
     *        the new directory for the map
     *
     * @effect the directory of the map is changed to the new directory
     *         |setDirectory(location)
     * @effect the map is deleted from the content of the old map
     *         |oldDir.remove(this)
     * @effect the map is added to the content of the location
     *         |newDir.add(this)
     * @throws LoopedDirectoryException is thrown when a map already exists within the destination, thus creating a loop
     *         is also thrown when the new directory is the current directory
     *         |(!noLoops(location)
     * @throws IllegalArgumentException is thrown when the location the map must go to is not effective
     *         |(location == null)
     * @throws NameNotAvailableException
     *         this is thrown when there already exists a File,Map or Link with the given name
     *         |(!nameNotInMap(location))
     */

    public void move(Directory location) throws LoopedDirectoryException,IllegalArgumentException,NameNotAvailableException{
        if(!noLoops(location))
            throw new LoopedDirectoryException(location);
        if(location ==null)
            throw new IllegalArgumentException();
        if(!nameNotInMap(location))
            throw new NameNotAvailableException();
        remove();
        location.add(this);
        setDirectory(location);

    }

    /**
     * Checks if a map is allowed to be added to another map
     * @param location
     *        the map we want to add our map to
     * @pre the map to be added must be a root Directory
     *      |getDirectory(this) == null
     * @pre destination cannot already be a submap of the map we want to add
     *      |!(location.contains(this))
     *
     * @return returns true if the map may be added, false otherwise
     */
    @Raw
    @Model
    private boolean noLoops(Directory location){
        Directory currentDirec = this.getDirectory();
        //Hier gaat ni niet in recursie
        if(currentDirec != null)
            return false;
        else{
            if(this == location){
                return false;
            }
            Directory nextDir = location.getDirectory();
            if(nextDir == null){
                return true;}
             return noLoops(nextDir);
        }
    }

    private void sortMap(){
        int mapSize = getContent().size();
    }


    public int getNbItems(){

        int j = 0;
        for(int i=0; i<getContent().size();i++){
            if(getContent().get(i) instanceof Directory){
                 Directory newDir = (Directory) getContent().get(i);
                j+= newDir.getNbItems();
            }
            j++;
        }
        return j;
    }

}
