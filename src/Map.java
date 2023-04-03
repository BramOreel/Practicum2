import be.kuleuven.cs.som.annotate.Raw;

import java.util.ArrayList;

/**
 * A class of Maps
 *
 * @invar each map must have a valid name
 *         | isValidName(getName())
 *
 */
public class Map extends Naming{

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
    private ArrayList<Naming> content = new ArrayList<Naming>();

    /**
     * Variable stating the map one level higher than the current map. If the map is a root directory, the variable will state 'null'
     */
    private Map directory = null;

    /**
     *  CONSTRUCTOR
     *
     *
     *
     *
     * @param name
     *        the name of the map
     */
    @Raw
    public Map(String name){
        setName(name);




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



    public void AddContent(Map map) throws LoopedDirectoryException, IllegalArgumentException{

        if(!noLoops(map))
            throw new LoopedDirectoryException(map);
        if(map == this)
            throw new IllegalArgumentException();
        content.add(map);
        map.setDirectory(this);
    }

    private boolean noLoops(Map addMap){

        Map currentDirec = this.getDirectory();
        if(currentDirec == null)
            return(addMap.getDirectory() == null);

        else{
            if(currentDirec == addMap){
                return(false);
            }
            noLoops(currentDirec);
        }
        return(addMap.getDirectory() == null);
    }
    //je moet gwn checken of de wortelroots van 2 items wel of niet overeenkomen



    public int getSize() {
        return content.size();
    }

    public ArrayList<Naming> getContent() {
        return content;
    }

    public void setDirectory(Map directory) {
        this.directory = directory;
    }

    public Map getDirectory(){
        return this.directory;
    }


}
