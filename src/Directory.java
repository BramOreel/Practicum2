import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.*;
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
    private boolean isWriteable = true;

    /**
     * Variable which contains the content of the map
     */
    private ArrayList<Thing> content = new ArrayList<Thing>();
    /**
     * Variable stating the directory one level up of the Directory
     */
    private Directory root = null;



    public Directory(Directory dir, String name, boolean isWriteable){
        super(dir);
        setName(name);
        setWritable(isWriteable);
        getDirectory().sortMap();
    }

    public Directory(Directory dir, String name){
        this(dir,name,true);
    }

    public Directory(String name, boolean isWriteable){
        super();
        setName(name);
        setWritable(isWriteable);
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
     * Terminate the item.
     * @post  The item will be removed from its directory.
     *       | setDirectory(null)
     *       | remove()
     * @post  The terminated state will be set to true.
     *       | this.isTerminated = true
     * @throws FileNotWritableException
     *        if the directory is not writable, this exception will be thrown.
     * @throws DirectoryNotEmptyException
     *        If the directory is not empty, this exception will be thrown.
     */
    @Override
    public void terminate() throws FileNotWritableException, DirectoryNotEmptyException{
        if(! isWriteable){
            throw new FileNotWritableException();
        }
        if(getNbItems() != 0){
            throw new DirectoryNotEmptyException();
        }
        else{ this.isTerminated = true;
            remove();
            setDirectory(null);
    }}

    /**
     * changes the name of the Directory if writeable
     *
     * @param name The new name for this file.
     *
     * @throws FileNotWritableException is thrown when isWriteable = false
     *         |(!isWritable)
     */
    @Override
    public void changeName(String name) throws FileNotWritableException{
        if(!isWritable())
            throw new FileNotWritableException(this);
        super.changeName(name);
    }



    /**
     * adds a File, Map or Link to the directory's content if the thing isn't a null reference
     * @param thing
     *        the thing to be added
     * @throws IllegalArgumentException is thrown when the thing to be added is a null reference.
     *         |(thing == null)
     * @throws FileNotWritableException is thrown when the users tries to add a map, link or file to a directory that is not writable
     *         |(!isWritable)
     */
    @Model @Raw
    protected void add(Thing thing) throws IllegalArgumentException,FileNotWritableException{
        if(thing == null)
            throw new IllegalArgumentException();
        if(!isWritable())
            throw new FileNotWritableException(this);
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
     * @effect after the thing is added, the map is sorted by name
     *         |sortMap();
     * @throws LoopedDirectoryException is thrown when a map already exists within the destination, thus creating a loop
     *         is also thrown when the new directory is the current directory
     *         |(!noLoops(location)
     * @throws IllegalArgumentException is thrown when the location the map must go to is not effective
     *         |(location == null)
     * @throws NameNotAvailableException
     *         this is thrown when there already exists a File,Map or Link with the given name
     *         |(!nameNotInMap(location))
     */
    @Raw
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
        location.sortMap();

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
    /**
     * Sorts the arraylist alphabetically based on the name of the items.
     * @post  The arraylist is sorted alphabetically.
     */

    protected void sortMap(){
        ArrayList<Thing> items = getContent();
        if(items.size() > 0){
            for (int i = 0; i < items.size() - 1; i++) {
                for (int j = i + 1; j < items.size(); j++) {
                    if (items.get(i).getName().compareTo(items.get(j).getName()) > 0) {
                        Collections.swap(items, i, j);
    }}}}}

    /**
     * Returns the item or submap at a given index.
     * @param index
     *        the index of the item you want, starting from 1 with the first item.
     * @pre   The index must be in the range of the content of the directory.
     * @return The item at the index.
     */
    public Thing getItemAt(int index){
        return content.get(index-1);
    }

    /**
     * Returns an item in the directory that has the given name.
     * @param searchName
     *        The name of the item.
     * @return The item with the given name. If there is no such item, null is returned.
     *        | if "searchname" not in getContent(): return null
     *        | else return getContent().getItemAt(index)
     * @effect The getItemAt() function returns the item with the index.
     */

    public Thing getItem(String searchName){
        int index = Collections.binarySearch(getContent(), new Directory( searchName), new Comparator<Thing>() {
                    @Override
                    public int compare(Thing item1, Thing item2) {
                        return item1.getName().compareTo(item2.getName());
                    }
                });
        if (index < 0){
            return null;
        }
        return getItemAt(index - 1);
    }

    /**
     * Checks if the directory conatains an item with a given name, ignoring the difference between
     * lower and upper case letters.
     * @param searchName
     *        The given name.
     * @return true if contains the item, false if it does not.
     */

    public boolean containsDiskItemWithName(String searchName){
        int index = Collections.binarySearch(getContent(), new Directory( searchName), new Comparator<Thing>() {
            @Override
            public int compare(Thing item1, Thing item2) {
                return item1.getName().toLowerCase().compareTo(item2.getName().toLowerCase());
            }
        });
        if (index < 0){
            return false;
        }
        return true;
    }

    /**
     * Gives the index of the given item in the directory, the items are ordered starting
     * with index 1.
     * @pre   The item must be inside the directory.
     * @param item
     *        the given item.
     * @return the index of the given item.
     */

    public int getIndexOf(Thing item){
        return getContent().indexOf(item) + 1;
    }

    /**
     * Gives the number of items and maps in the map.
     */
    public int getNbItems(){
        return getContent().size();
    }

    /**
     * Gives the total number of items in the map and in the submaps in the map.
     * @return the total amount.
     */
    public int getTotalNbItems(){

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

    /**
     * WRITEABLE
     */


    /**
     * Check whether this map is writable.
     */
    @Raw @Basic
    public boolean isWritable() {
        return isWriteable;
    }

    /**
     * Set the writability of this map to the given writability.
     *
     * @param newWrite
     *        The new writability
     * @post  The given writability is registered as the new writability
     *        for this map.
     *        | new.isWritable() == isWritable
     */
    @Raw
    public void setWritable(boolean newWrite) {
        this.isWriteable = newWrite;
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

    /**
     *
     * @return returns the total amount of bytes which a map occupies
     */
    public int getTotalDiskUsage(){
        int j = 0;
        int nb = getNbItems();
        for(int i = 0; i < nb; i++){
            Thing currentItem = getItemAt(1+i);
            if(currentItem instanceof Directory) {
                j += ((Directory) currentItem).getTotalDiskUsage();
            } else if (currentItem instanceof File) {
                j+= ((File) currentItem).getSize();
            }
        }
        return j;
    }

}
