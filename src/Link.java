public class Link extends Thing{

    private boolean State;

    private Thing reference;

    public boolean getState() {
        return getReference().state;
    }

    public Thing getReference() {
        return reference;
    }

    public void setState(boolean state) {
        State = state;
    }

    /**
     * Sets the item which the link references.
     * @param reference
     *        The item the link references, it cannot be another link.
     * @throws IllegalArgumentException
     *         if the argument is a link.
     */

    private void setReference(Thing reference) throws IllegalArgumentException {
        if(reference instanceof Link){
            throw new IllegalArgumentException();
        }
        else {
            this.reference = reference;
        }
    }

    public Link(Directory dir, String name, Thing linkedItem){
        super(dir);
        setName(name);
        setState(true);
        setReference(linkedItem);
    }

}
