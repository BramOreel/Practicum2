public class Main {
    /**
     *
     * @param args
     */

    public static void main(String[] args) {

        Map dir = new Map("dir");
        System.out.println(dir.getDirectory());

        Map map1 = new Map(dir,"maps");
        //System.out.println(map1.getDirectory());
    }

}