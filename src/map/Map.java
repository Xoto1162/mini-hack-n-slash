package map;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entities.Entity;
import entities.HellDoor;
import entities.Item;
import entities.Player;
import entities.Monster;
import entities.monsters.Demon;
import entities.monsters.Necromancer;
import entities.monsters.Zombie;
import entities.monsters.traps.BearTrap;
import entities.monsters.traps.Spike;
import gameworld.GameWorld;
import map.cells.FloorCell;
import map.cells.HellDoorCell;
import map.cells.TeleportationCell;
import map.cells.VoidCell;
import map.cells.WallCell;
import utils.Position;

public class Map {

	/**
	 * Identifiant de la map
	 */
	private int id;

	/**
	 * Taille de la map
	 */
	private int width;
	private int height;

	/**
	 * Liste des monstres présents sur la map
	 */
	private ArrayList<Monster> monsters;

	/**
	 * Liste des cellules de la map
	 */
	private Cell[][] cells;

	/**
	 * Constructeur de la map
	 */
	public Map() {
		this.monsters = new ArrayList<Monster>();
	}

	/**
	 * Ajoute une entité sur la map
	 * @param e
	 * 				Entité a ajouter sur la map
	 */
	public void addEntity(Entity e){
		if(e.getClass().toString().contains("class entities.monsters.")) {
			((FloorCell)cells[e.getX()][e.getY()]).addMonster((Monster) e);
			this.monsters.add((Monster) e);
		} else {
			((FloorCell)cells[e.getX()][e.getY()]).addItem((Item) e);
		}
	}

	/**
	 * Supprime une entité de la map
	 * @param e
	 * 				Entité a supprimé de la map
	 */
	public void removeEntity(Entity e){
		if(e.getClass().toString().contains("class entities.monsters.")) {
			((FloorCell)cells[e.getX()][e.getY()]).removeMonster();
			this.monsters.remove((Monster) e);
		}
	}

	/**
	 * Verifie si c'est bien une celule a la position demandé
	 * @param pos
	 * 				Position à verifier
	 * @return true si c'est bien une cellule a la position sinon false
	 */
	public boolean isCellAtPos(Position pos) {
		if(pos.getX() >= 0 && pos.getX() < cells.length) {
			if(pos.getY() >= 0 && pos.getY() < cells[pos.getX()].length) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Indique si l'on peut se deplacer a une postion de la map
	 * @param pos
	 * 				Position ou l'on souhaite se deplacer
	 * @return true si l'on peut se deplacer false sinon
	 */
	public boolean canMoveAt(Position pos) {
		if (isCellAtPos(pos)) {
			Cell cell = cells[pos.getX()][pos.getY()];
			Player player = GameWorld.getPlayer();
			if (cell.getCellType().equals("FloorCell") && (player.getX() != pos.getX() || player.getY() != pos.getY()) && ((FloorCell) cell).getMonster() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Récupère l'identifiant de la map a partir du nom du fichier
	 * @param name
	 * 				Chemin du fichier
	 * @return id de la map
	 */
	public static int readIdFromFile(String name) {
		String id = "";
		Pattern pattern = Pattern.compile("map\\[([0-9]+)].txt$");
		Matcher matcher = pattern.matcher(name);
		while (matcher.find()) {
			id += matcher.group(1);
		}
		return Integer.parseInt(id);
	}

	/**
	 * Renvoi une map chargé à partir d'un fichier
	 * @param path
	 * 				Chemin de fichier de la map
	 * @return un objet qui représente la map
	 */
	public static Map readMapFromFile(String path) {
		Map map = new Map();
		map.id = Map.readIdFromFile(path);
		DataInputStream dis;
		try {
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(path))));
			String read = "";

			// Lecture de la taille de la map
			char c = (char) dis.read();

			while (!(c > 57 || c < 48)) {
				read += c;
				c = (char) dis.read();
			}
			map.width = Integer.parseInt(read);
			dis.read();
			c = (char) dis.read();
			read = "";
			while (!(c > 57 || c < 48)) {
				read += c;
				c = (char) dis.read();
			}
			map.height = Integer.parseInt(read);
			map.cells = new Cell[map.width][map.height];

			// Saut du retour a la ligne
			dis.read();
			
			for(int i = 0; i < map.height; i++) {
				for(int j = 0; j < map.width; j++) {
					c = (char) dis.read();
					read = "";
					while (c != ' ' && c != '\n') {
						read += c;
						c = (char) dis.read();
					}
					read = read.trim();
					int cellType = Integer.parseInt(read);
					int x = j;
					int y = map.height - 1 - i;
					switch(cellType) {
						case Cell.VOID:
							map.cells[x][y] = new VoidCell(new Position(x, y));
							break;
						case Cell.FLOOR:
							map.cells[x][y] = new FloorCell(new Position(x, y), "res/textures/floor.png", new ArrayList<Item>());
							break;
						case Cell.WALL:
							map.cells[x][y] = new WallCell(new Position(x, y), "res/textures/brick.png");
							break;
						case Cell.TELEPORTATION:
							map.cells[x][y] = new TeleportationCell(new Position(x, y), "res/textures/door.png", 0, new Position(0, 0));
							break;
						case Cell.DEMON:
							map.cells[x][y] = new FloorCell(new Position(x, y), "res/textures/floor.png", new ArrayList<Item>());
							map.addEntity(new Demon(x, y));
							break;
						case Cell.NECROMANCER:
							map.cells[x][y] = new FloorCell(new Position(x, y), "res/textures/floor.png", new ArrayList<Item>());
							map.addEntity(new Necromancer(x, y));
							break;
						case Cell.ZOMBIE:
							map.cells[x][y] = new FloorCell(new Position(x, y), "res/textures/floor.png", new ArrayList<Item>());
							map.addEntity(new Zombie(x, y));
							break;
						case Cell.SPIKE:
							map.cells[x][y] = new FloorCell(new Position(x, y), "res/textures/floor.png", new ArrayList<Item>());
							map.addEntity(new Spike(x, y));
							break;
						case Cell.BEARTRAP:
							map.cells[x][y] = new FloorCell(new Position(x, y), "res/textures/floor.png", new ArrayList<Item>());
							map.addEntity(new BearTrap(x, y));
							break;
						case Cell.HELLDOOR:
							map.cells[x][y] = new HellDoorCell(new Position(x, y), "res/textures/floor.png", new HellDoor(x, y));
							break;
					}
				}
			}

			read = "";

			// Lectures des infos supplémentaires
			while (dis.available() > 0) {
				c = (char) dis.read();
				read += c;
			}

			Position cell;

			// Traitement des infos supplémentaires
			while (read.length() > 0) {
				if (read.startsWith("#teleportation_cells:")) {
					read = read.substring("#teleportation_cells:".length());
					String buffer = "";
					while (read.charAt(0) < 57 && read.charAt(0) > 48) {
						buffer += read.charAt(0);
						read = read.substring(1);
					}
					int amount = Integer.parseInt(buffer);
					int mapID;
					Position pos;
					buffer = "";
					while (read.length() > 0 && read.charAt(0) != '#') {
						buffer += read.charAt(0);
						read = read.substring(1);
					}
					String[] bordersInfo = buffer.split("\n");
					for (int i = 1; i < amount + 1; i++) {
						cell = new Position(Integer.parseInt(bordersInfo[i].split("-")[0]),
								Integer.parseInt(bordersInfo[i].split("-")[1]));
						mapID = Integer.parseInt(bordersInfo[i].split("-")[2]);
						pos = new Position(Integer.parseInt(bordersInfo[i].split("-")[3]),
								Integer.parseInt(bordersInfo[i].split("-")[4].trim()));
						map.cells[cell.getX()][cell.getY()] = new TeleportationCell(cell, "res/textures/door.png" , mapID, pos);
					}
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * Affiche la réprésentation d'une map sous forme de chaine de caractère
	 * @return une map sous forme de string
	 */
	public String toString() {
		String s = "";
		s += "[MAP " + this.id + "]\nEntities :\n";
		for(int i = 0; i < this.width; i++) {
			for(int j = 0; j < this.height; j++) {
				if(cells[i][j].getCellType() == "FloorCell" && ((FloorCell) cells[i][j]).getMonster() != null) {
					s += ((FloorCell) cells[i][j]).getMonster().getNom() + "\n";
				}
			}
		}
		return s;
	}

	/**
	 * Retourne l'id de la map
	 * @return id de la map
	 */
	public int getMapId() { return this.id; }

	/**
	 * Retourne la taille de la map
	 * @return
	 */
	public int getMapSize() { return 0; }

	/**
	 * Retourn les monstres de la map
	 * @return monstres de la maps
	 */
	public ArrayList<Monster> getMonsters() { return monsters; }

	/**
	 * Retourne les cellules de la map
	 * @return cellules de la map
	 */
	public Cell[][] getCells() { return cells; }

}
