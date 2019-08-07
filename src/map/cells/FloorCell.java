package map.cells;

import java.util.ArrayList;

import entities.Item;
import entities.Monster;
import entities.items.Chest;
import map.Cell;
import utils.Position;

public class FloorCell extends Cell {
	
	protected ArrayList<Item> items;
	protected Monster m;
	

	public FloorCell(Position pos, String texture, ArrayList<Item> arrayList) {
		super(pos, "FloorCell", texture);
		this.items = new ArrayList<Item>();
	}
	
	public void addItem(Item i) {
		items.add(i);
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public boolean hasItems() {
		return !items.isEmpty();
	}
	
	public void dropItems() {
		Chest chest = new Chest(this.getCellPos().getX(), this.getCellPos().getY(), items);
		chest.loot();
		items.clear();
	}
	
	public void addMonster(Monster m) {
		this.m = m;
	}
	
	public Monster getMonster() {
		return this.m;
	}
	
	public void removeMonster() {
		this.m = null;
	}
	
	@Override
	public void drawAt(Position drawAt) {
		super.drawAt(drawAt);
		renderChestAt(drawAt);
		renderMonsterAt(drawAt);
	}
	
	public void renderMonsterAt(Position drawAt) {
		if(!(this.m == null))
			this.m.drawAt(drawAt);
	}
	
	public void renderChestAt(Position drawAt) {
		if(!(this.items.isEmpty())) {
			Chest chest = new Chest(this.getCellPos().getX(), this.getCellPos().getY(), items);
			chest.drawAt(drawAt);
		}
	}
}
