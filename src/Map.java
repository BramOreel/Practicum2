//import be.kuleuven.cs.som.annotate.Raw;
//
//import java.util.ArrayList;
///***
///**
// * A class of Maps
// *
// * @invar each map must have a valid name
// *         | isValidName(getName())
// *
// */
//public class Map extends Naming{
//
//
//
//
//
//
//
//    public void AddContent(Naming thing) throws LoopedDirectoryException, IllegalArgumentException{
//        if(thing instanceof Map){
//            if(thing.getName() == "dir")
//                content.add(thing);
//            if(!noLoops((Map) thing))
//                throw new LoopedDirectoryException((Map) thing);
//            if( thing == this)
//                throw new IllegalArgumentException();
//            content.add(thing);
//            ((Map)thing).setDirectory(this);
//        } else if (thing instanceof File) {
//
//
//        }
//
//    }
//
//    private boolean noLoops(Map addMap){
//
//        Map currentDirec = this.getDirectory();
//        if(currentDirec == null)
//            return(addMap.getDirectory() == null);
//
//        else{
//            if(currentDirec == addMap){
//                return(false);
//            }
//            noLoops(currentDirec);
//        }
//        return(addMap.getDirectory() == null);
//    }
//    //je moet gwn checken of de wortelroots van 2 items wel of niet overeenkomen
//
//    public void removeContent(Naming thing)throws ClassNotFoundException{
//        if(!content.contains(thing))
//            throw new ClassNotFoundException();
//        content.remove(thing);
//
//
//    }
//
//
//    public int getSize() {
//        return content.size();
//    }
//
//    public ArrayList<Naming> getContent() {
//        return content;
//    }
//
//
//
//
//}
//