public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Directory dir = new Directory("dir");
        System.out.println(dir.getDirectory());

        Directory map1 = new Directory(dir,"map");
        System.out.println(map1.getDirectory());

        File biem = new File(dir,"miauw",Type.PDF);
        System.out.println(biem.getDirectory());
        System.out.println(dir.getContent());
        File bong = new File(dir,"mauw",Type.PDF);
        System.out.println(dir.getContent());
        map1.makeRoot();
        System.out.println(dir.getContent());

        Directory mapa = new Directory("a");
        Directory mapb = new Directory("b");
        Directory mapc = new Directory("c");
        Directory mapd = new Directory("d");
        Directory mape = new Directory("e");
        Directory mapf = new Directory("f");
        Directory mapg = new Directory("d");
        Directory maph= new Directory("d");
        Directory mapi = new Directory("d");
        Directory mapj = new Directory("d");

        mapb.move(mapa);
        mapc.move(mapa);
        mapd.move(mapc);
        mape.move(mapc);
        mapf.move(mapc);
        bong.move(mapd);
        biem.move(mapd);

        System.out.println(mapd.getContent());

        System.out.println(mapa.getNbItems());
        System.out.println(mapa.getRootName());
        System.out.println(mapa);
    }





}