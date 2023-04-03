public class Main {
    /**
     *
     * @param args
     */

    public static void main(String[] args) {

         File yeet = new File("hello&",0,true, Type.JAVA);

         System.out.println(yeet.getType());
        yeet.changeName("hello");

         System.out.println(yeet.getName());
        System.out.println(yeet.getModificationTime());

        Map bing = new Map("Bram");
        System.out.println(yeet.isValidName("Bram."));
        bing.changeName("Bramk");
        System.out.println(bing.getName());


        Map map1 = new Map("eerstemap");
        Map map2 = new Map("tweedemap");
        Map map3 = new Map("derdemap");

        map2.AddContent(map1);
        map2.AddContent(map3);

        map3.AddContent(map1);
        map1.getContent();
        map3.getContent();
        System.out.println(map2.getContent());
        System.out.println(map3.getContent());
    }

}