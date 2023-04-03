import be.kuleuven.cs.som.annotate.Model;

/**
 * A enumeration of possible file types
 *
 * @author bramo
 * @version 1.0
 */
public enum Type{
    JAVA(".java"),TXT(".txt"),PDF(".pdf");

    private final String extension;

    /**
     *
     * @param extension
     * the name of the extension type of the file
     *
     * @effect the extension type is set to the given name
     */
    @Model
    private Type(String extension){
        this.extension = extension;
    }

    /**
     *
     * @return returns the extension name listed in enum
     */
    public String getExtension(){
        return this.extension;
    }
}
