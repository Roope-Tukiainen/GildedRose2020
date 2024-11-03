package fi.oulu.tol.sqat.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.oulu.tol.sqat.GildedRose;
import fi.oulu.tol.sqat.Item;

public class GildedRoseTest {
	
	private List<Item> giveItems(int sellIn) {
        List<Item> items = new ArrayList<Item>();
        items.add(new Item("+5 Dexterity Vest", sellIn, 20));
        items.add(new Item("Aged Brie", sellIn, 0));
        items.add(new Item("Sulfuras, Hand of Ragnaros", sellIn, 80));
        items.add(new Item("Backstage passes to a TAFKAL80ETC concert", sellIn, 20));
        return items;
	}
	
	@Test
	public void testTheTruth() {
		assertTrue(true);
	}
	@Test
	public void exampleTest() {
		//create an inn, add an item, and simulate one day
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));
		inn.oneDay();
		
		//access a list of items, get the quality of the one set
		List<Item> items = inn.getItems();
		int quality = items.get(0).getQuality();
		
		//assert quality has decreased by one
		assertEquals("Failed quality for Dexterity Vest", 19, quality);
	}
	
	
	@Test
	public void sellInDecreases() {
		List<Item> items = giveItems(10);
		GildedRose inn = new GildedRose();
		
		for (Item item : items) {
			inn.setItem(item);
		}
		inn.oneDay();
		inn.oneDay();
		List<Item> innItems = inn.getItems();
		String errMsg = " , wrong SellIn value";
		for (int i = 0; i < innItems.size(); i++) {
			String name = innItems.get(i).getName();
			if (!name.equals("Sulfuras, Hand of Ragnaros")) {
				assertEquals(name + errMsg, 8, items.get(i).getSellIn());
			} else {
				assertEquals(name + errMsg, 10, items.get(i).getSellIn());
			}
			
		}
	}
	
	@Test
	public void degradeBeforeSellIn() {
		List<Item> items = giveItems(15);
		GildedRose inn = new GildedRose();
		
		for (Item item : items) {
			inn.setItem(item);
		}
		
		for (int i = 0; i < 15; i++) {
			inn.oneDay();
		}
		List<Integer> expected = new ArrayList<Integer>();
		expected.add(5);
		expected.add(15);
		expected.add(80);
		expected.add(20+5+10+15);
		
		List<Item> innItems = inn.getItems();
		String errMsg = " wrong degrade when SellIn >= 0";
		for (int i = 0; i < innItems.size(); i++) {
			assertEquals(innItems.get(i).getName() + errMsg, (int) expected.get(i), items.get(i).getQuality());
		}
	}
	
	@Test
	public void degradeAfterSellIn() {
		List<Item> items = giveItems(0);
		GildedRose inn = new GildedRose();
		
		for (Item item : items) {
			inn.setItem(item);
		}
		
		for (int i = 0; i < 5; i++) {
			inn.oneDay();
		}
		List<Integer> expected = new ArrayList<Integer>();
		expected.add(10);
		expected.add(10);
		expected.add(80);
		expected.add(0);
		
		List<Item> innItems = inn.getItems();
		String errMsg = " wrong degrade when Sellin < 0";
		for (int i = 0; i < innItems.size(); i++) {
			assertEquals(innItems.get(i).getName() + errMsg, (int) expected.get(i), items.get(i).getQuality());
		}
	}
	
	@Test
	public void qualityExtremes() {
		List<Item> items = giveItems(36);
		GildedRose inn = new GildedRose();
		
		for (Item item : items) {
			inn.setItem(item);
		}
		for (int i = 0; i < 35; i++ ) {
			inn.oneDay();
		}
		
		List<Item> innItems = inn.getItems();
		String errMsg = " had negative quality, it shouldn't";
		for (Item item : innItems) {
			String name = item.getName();
			if (name.equals("Aged Brie") || name.equals("Backstage passes to a TAFKAL80ETC concert")) {
				boolean less51 = item.getQuality() < 51;
				assertEquals(name + "had 50 < quality, which is not allowed", true, less51);
			}
			boolean negQual = item.getQuality() < 0;
			assertEquals(name + errMsg, false, negQual);
		}
	}
}
