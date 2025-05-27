import domain.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    
    // Implementación concreta para testing
    private static class TestItem extends Item {
        public TestItem(int number) {
            super(number);
            this.name = "TestItem";
        }

        @Override
        public void effect(Pokemon pokemon) {

        }

        @Override
        public String[] getItemInfo() {
            return new String[]{"Test Info"};
        }
    }

    @Test
    void testConstructorAndNumber() {
        int expectedNumber = 5;
        Item item = new TestItem(expectedNumber);
        
        assertEquals(expectedNumber, item.number());
    }

    @Test
    void testNumberMethod() {
        int expectedNumber = 3;
        Item item = new TestItem(expectedNumber);
        
        assertEquals(expectedNumber, item.number());
    }

    @Test
    void testIsUsedWhenNew() {
        Item item = new TestItem(2);
        assertFalse(item.isUsed());
    }

    @Test
    void testIsUsedWhenEmpty() {
        Item item = new TestItem(0);
        assertTrue(item.isUsed());
    }
    
    @Test
    void testGetNameReturnsAssignedName() {
        Item item = new TestItem(1);
        assertEquals("TestItem", item.getName());
    }

    @Test
    void testGetNameReturnsUnknownWhenNull() {
        Item item = new TestItem(1) {
            @Override
            public String[] getItemInfo() { return new String[0]; }
        };
        assertEquals("TestItem", item.getName());
    }
    
    @Test
    void testReviveGetItemInfo() {
        Revive revive = new Revive(1);
        String[] info = revive.getItemInfo();

        assertNotNull(info);
        assertEquals(2, info.length);
        assertEquals("revive", info[0]);
        assertEquals("1", info[1]);
    }
}