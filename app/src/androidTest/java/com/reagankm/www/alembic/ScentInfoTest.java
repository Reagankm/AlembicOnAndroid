package com.reagankm.www.alembic;

import com.reagankm.www.alembic.model.ScentInfo;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the methods of the ScentInfo model class.
 */
public class ScentInfoTest extends TestCase {

    ScentInfo caramel;
    ScentInfo ashes;
    ScentInfo oldBooks;
    List<String> booksIngreds;

    @Override
    public void setUp() {
        caramel = new ScentInfo("caramel.id", "Caramel");
        ashes = new ScentInfo("ashes.id", "Ashes", (float) 1.5);
        booksIngreds = new ArrayList<>();
        booksIngreds.add("Parchment");
        booksIngreds.add("Leather");
        booksIngreds.add("Glue");
        oldBooks = new ScentInfo("oldBooks.id", "Old Books", (float) 3.0, booksIngreds);
    }

    @Override
    public void tearDown() {
        caramel = null;
        ashes = null;
        oldBooks = null;

    }

    public void testConstructors() {
        assertNotNull(caramel);
        assertNotNull(ashes);
        assertNotNull(oldBooks);
    }

    public void testGetIngredientList() {
        List<String> ingreds = oldBooks.getIngredientList();
        assertEquals(ingreds, booksIngreds);
    }

    public void testEquals() {
        assertTrue(ashes.equals(ashes));
        assertTrue(!(ashes.equals(oldBooks)));
    }

    public void testHashCode() {
        assertTrue(ashes.hashCode() == ashes.hashCode());
        assertTrue(!(caramel.hashCode() == oldBooks.hashCode()));
    }

    public void testCompareTo() {
        assertTrue(ashes.compareTo(caramel) < 0);
        assertTrue(oldBooks.compareTo(ashes) > 0);
        assertTrue(ashes.compareTo(ashes) == 0);
    }

    public void testGetId() {
        assertTrue(ashes.getId().equals("ashes.id"));
        assertTrue(caramel.getId().equals("caramel.id"));
        assertTrue(oldBooks.getId().equals("oldBooks.id"));
    }

    public void testGetName() {
        assertTrue(ashes.getName().equals("Ashes"));
        assertTrue(caramel.getName().equals("Caramel"));
        assertTrue(oldBooks.getName().equals("Old Books"));
    }

    public void testGetRating() {
        assertTrue(ashes.getRating() == (float) 1.5);
        assertTrue(oldBooks.getRating() == (float) 3.0);
    }

}
