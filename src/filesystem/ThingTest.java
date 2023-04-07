package filesystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ThingTest {
    private static Directory someDirectory, someDirectory1, someDirectory2;

    private static File someFile, someFile1, someFile2, someFile3, someFile4;

    private static Link someLink, someLink1;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @BeforeEach

    public void setupBeforeAll() {
        someDirectory = new Directory("**@@");
        someFile = new File(someDirectory, "someFile", Type.JAVA);
        someLink = new Link(someDirectory, "someLink", someFile);
        someDirectory1 = new Directory(someDirectory, "someDirectory1");
        someLink1 = new Link(someDirectory, "someLink1", someDirectory);
        someDirectory1 = new Directory(someDirectory, "someDirectory1");
        someDirectory2 = new Directory(someDirectory, "someDirectory2");
        someFile2 = new File(someDirectory, "a", Type.JAVA);
        someFile3 = new File(someDirectory, "b", Type.JAVA);
        someFile4 = new File(someDirectory, "c", Type.JAVA);
    }
    @Test
    public void ConstructorTests() {
        // check if wrong name => default name
        assertEquals(someDirectory.getName(), "new_file");
        someDirectory.setName("7**@");
        assertEquals(someDirectory.getName(), "new_file");
        // check if correct name gets set
        someDirectory.setName("someDirectory");
        assertEquals(someDirectory.getName(), "someDirectory");
        // check if root directory has no directory
        assertEquals(null, someDirectory.getDirectory());
        // check for correct file type
        assertEquals(".java", someFile.getType());
        // check for correct directory
        assertEquals(someDirectory, someFile.getDirectory());
        // check for invalid directory name if it has a period
        someFile.setName("file.file");
        assertEquals("file.file", someFile.getName());
        someDirectory.setName("directory.directory");
        assertEquals(someDirectory.getName(), "new_file");
        // check for size
        assertEquals(someFile.getSize(), 0);
        // check for modification time and creation time
        someFile1 = new File(someDirectory, "someFile1", Type.PDF);
        assertTrue(someFile1.getCreationTime().equals(new Date()));
        someFile1.changeName("someFile2");
        assertTrue(someFile1.getModificationTime().equals(new Date()));
    }

    @Test
    public void MapTest() {
        assertEquals(someFile.getDirectory(), someDirectory);
        //check if loops throw the exception.
        assertThrows(LoopedDirectoryException.class,
                () -> {
                    someDirectory.move(someDirectory2);
                });
        //check if moving file to the same directory throws exception
        assertThrows(IllegalArgumentException.class,
                () -> {
                    someFile.move(someDirectory);
                });
        // check getters
        assertEquals(someFile.getDirectory(), someDirectory);
        assertEquals(someFile.getName(),"someFile");
        assertEquals(someFile, someDirectory.getItem("someFile"));
        // check if moving items to a directory works and puts them in alphabetical order.
        someFile3.move(someDirectory2);
        assertEquals(someDirectory2.getNbItems(), 1);
        assertEquals(someDirectory2.getItemAt(1), someFile3);
        someFile2.move(someDirectory2);
        assertEquals(someDirectory2.getItemAt(2), someFile3);
        assertEquals(someDirectory2.getItemAt(1), someFile2);
        someFile4.move(someDirectory2);
        assertEquals(someDirectory2.getItemAt(3), someFile4);
        // check if capital letters are ignored by containsDiskItemWithName
        assertTrue(someDirectory.containsDiskItemWithName("SomEfIle"));
        assertEquals(someDirectory2.getIndexOf(someFile4), 3);
        // check if exception gets thrown if file isn't in the directory
        assertThrows(ArgumentNotFoundException.class,
                () -> {
                    assertEquals(someDirectory.getIndexOf(someFile4), 3);
                });
    }

    @Test
    public void WritabilityTest() {
        someFile.setWritable(false);
        assertThrows(FileNotWritableException.class,
                () -> {
                    someFile.changeName("newname");
                });
        assertEquals(someFile.getName(), "someFile");
        assertEquals(null, someFile.getModificationTime());
        someDirectory.setWritable(false);
        assertThrows(FileNotWritableException.class,
                () -> {
                    someDirectory.changeName("newname");
                });
        assertEquals(someDirectory.getName(), "new_file");
        assertEquals(null, someDirectory.getModificationTime());
        assertThrows(FileNotWritableException.class,
                () -> {
                    someFile.move(someDirectory1);
                });
        someFile.setWritable(true);
        someDirectory.setWritable(false);
        assertThrows(FileNotWritableException.class,
                () -> {
                    someFile.move(someDirectory2);
                });
        someDirectory.setWritable(true);
        someDirectory2.setWritable(false);
        assertThrows(FileNotWritableException.class,
                () -> {
                    someFile.move(someDirectory2);
                });
        assertThrows(FileNotWritableException.class,
                () -> {
                    someDirectory2.terminate();
                });
    }

    @Test
    public void LinkTest() {
        assertEquals(someLink.getReference(), someFile);
        assertThrows(IllegalArgumentException.class,
                () -> {
                    Link someLink3 = new Link(someDirectory, "link", someLink);
                });
        someFile.terminate();
        assertFalse(someLink.getState());
        someLink.move(someDirectory2);
        assertEquals(someLink.getDirectory(), someDirectory2);

    }

    @Test
    public void TerminateTest() {
        someFile.terminate();
        assertEquals(null, someDirectory.getItem("someFile"));
        assertEquals(someFile.getDirectory(), null);
        assertTrue(someFile.isTerminated);

        assertThrows(DirectoryNotEmptyException.class,
                () -> {
                    someDirectory.terminate();
                });
    }


    /**
     * Will pause the program for a given amount of milliseconds.
     *
     * @param ms amount of milliseconds it needs to pause.
     */
    private static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    @Test
    public void hasOverLappingUseTest() {
        File newFile1 = new File(someDirectory, "newFile", Type.JAVA);
        Directory newDirectory1 = new Directory(someDirectory, "newDirectory");
        assertFalse(newFile1.hasOverlappingUsePeriod(newDirectory1));
        pause(1000);
        newFile1.enlarge(5);
        newDirectory1.changeName("newname");
        assertTrue(newFile1.hasOverlappingUsePeriod(newDirectory1));
        pause(1000);
        File newFile2 = new File(someDirectory, "newFile", Type.JAVA);
        pause(1000);
        newFile2.enlarge(5);
        newFile1.enlarge(5);
        assertTrue(newFile2.hasOverlappingUsePeriod(newFile1));
        File newFile3 = new File(someDirectory, "newFile", Type.JAVA);
        pause(1000);
        newFile1.enlarge(5);
        pause(1000);
        newFile3.enlarge(5);
        assertTrue(newFile1.hasOverlappingUsePeriod(newFile3));
    }

    @Test
    public void MakeRootTest() {
        Directory someDirectory3 = new Directory(someDirectory, "someDirectory2");
        someDirectory3.makeRoot();
        assertEquals(null, someDirectory3.getDirectory());
    }

    @Test
    public void getRootTest() {
        Directory someDirectory3 = new Directory(someDirectory2, "someDirectory2");
        assertEquals(someDirectory2.getRoot(), someDirectory);
        assertEquals(someDirectory3.getRoot(), someDirectory);
        assertEquals(someDirectory.getRoot(), someDirectory);
    }

    @Test
    public void isDirectorIndirectChildOfTest(){
        Directory someDirectory3 = new Directory(someDirectory2, "someDirectory2");
        assertFalse(someDirectory.isDirectOrIndirectChildOf(someDirectory));
        assertTrue(someDirectory3.isDirectOrIndirectChildOf(someDirectory));
        assertTrue(someDirectory2.isDirectOrIndirectChildOf(someDirectory));
        assertFalse(someFile.isDirectOrIndirectChildOf(someDirectory3));
    }

    @Test
    public void getTotalDiskUsageTest(){
        someFile.enlarge(10);
        someFile2.enlarge(15);
        assertEquals(25, someDirectory.getTotalDiskUsage());
        someFile3.move(someDirectory2);
        someFile3.enlarge(25);
        assertEquals(50, someDirectory.getTotalDiskUsage());
    }

    @Test
    public void getAbsolutePathTest(){
        Directory someDirectory3 = new Directory(someDirectory2, "someDirectory3");
        someFile.move(someDirectory3);
        assertEquals(someFile.getAbsolutePath(), "/new_file/someDirectory2/someDirectory3/someFile.java");
    }





}
