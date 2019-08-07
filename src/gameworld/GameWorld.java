package gameworld;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Pattern;

import entities.Player;
import entities.Monster;
import main.StdDraw;
import map.Cell;
import map.Map;
import utils.Position;
import map.cells.*;
import camera.Camera;

public class GameWorld {

	/**
	 * Caméra
	 */
	private static Camera camera;

	/**
	 * Notre personnage
	 */
	private static Player personnage;

	/**
	 * Liste des maps du jeu
	 */
	private static TreeMap<Integer, Map> maps;

	/**
	 * Map sur laquelle on ce trouve
	 */
	private static Map actualMap;

	/**
	 * Indique si la partie est gagné
	 */
	private static boolean gameWon;

	/**
	 * Initialise le gameworld
	 */
	public static void init() {
		gameWon = false;
		personnage = new Player("Perso", 1, 0, 50, 5, 0);
		loadMaps();
		actualMap = maps.get(0);
		personnage.setPosition(new Position(2, 2));
		camera = new Camera(10);
		Random r = new Random();
		for(int i = 0; i < 4; i++) {
			int index = r.nextInt(actualMap.getMonsters().toArray().length - 1);
			((Monster) actualMap.getMonsters().toArray()[index]).addKey();
		}
	}

	/**
	 * Charge toutes les maps du jeu présente dans le dosier res/maps/
	 */
	public static void loadMaps() {
		maps = new TreeMap<Integer, Map>();
		File repository = new File("res/maps");
		File[] files = repository.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return Pattern.matches("^map\\[[0-9]+]\\.txt$", name);
			}
		});
		for (File f : files) {
			maps.put(Map.readIdFromFile(f.getPath()), Map.readMapFromFile(f.getPath()));
		}
	}

	/**
	 * En fonction d'une touche pressée par l'utilisateur, fait les mises à jour
	 * nécessaires dans le monde du jeu.
	 */
	public static void processUserInput() {
		Position cible;
		Cell targetCell;
		if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_Z)) {
			if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_Q))
				personnage.seDeplacer(1, -1);
			else if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_D))
				personnage.seDeplacer(1, 1);
			else
				personnage.seDeplacer(1, 0);
		} else if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_S)) {
			if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_Q))
				personnage.seDeplacer(-1, -1);
			else if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_D))
				personnage.seDeplacer(-1, 1);
			else
				personnage.seDeplacer(-1, 0);
		}
		else if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_Q))
			personnage.seDeplacer(0, -1);
		else if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_D))
			personnage.seDeplacer(0, 1);
		if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_SPACE)) {
			cible = personnage.getCible(1);
			targetCell = actualMap.getCells()[cible.getX()][cible.getY()];
			if(targetCell.getCellType() == "FloorCell" && (((FloorCell) targetCell).getMonster() != null))
				personnage.attaquer(((FloorCell) targetCell).getMonster());
		}
		if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_I))
			personnage.afficheInventaire();
		if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_E))
			personnage.afficheEquipement();
		if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_P)) {
			if(personnage.hasEquipedPotion()) {
				personnage.useEquipedPotion();
			}	
		}
		if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_R)) {
			cible = personnage.getCible(1);
			targetCell = actualMap.getCells()[cible.getX()][cible.getY()];
			if(targetCell.getCellType() == "FloorCell" && ((FloorCell) targetCell).hasItems())
				((FloorCell) targetCell).dropItems();
		}
		if(StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_M)) {
			cible = personnage.getCible(1);
			targetCell = actualMap.getCells()[cible.getX()][cible.getY()];
			if(targetCell.getCellType() == "HellDoorCell") {
				personnage.deposer(((HellDoorCell) targetCell).getHellDoor());
			}
		}
			
	}

	/**
	 * Gère l'orientation du personnage en fonction de la position de la souris
	 */
	public static void raycasting() {
		int mouseX = (int) (StdDraw.mouseX() * camera.getTailleGrille());
		int mouseY = (int) (StdDraw.mouseY() * camera.getTailleGrille());
		double distX = mouseX - camera.getCamSize();
		double distY = mouseY - camera.getCamSize();
		if (distX == 0 && distY == 0)
			return;
		double distance = Math.sqrt(distX*distX + distY*distY);
		double angle;
		if (distX < 0) {
			angle = 360 - Math.acos(distY / distance) * 180 / Math.PI;
		} else {
			angle = Math.acos(distY / distance) * 180 / Math.PI;
		}
		int orientation = (int) Math.round(angle / 45);
		orientation = (orientation > 7) ? 7 : orientation;
		personnage.setOrientation(orientation);
	}

	/**
	 * Déplace les monstre
	 */
	public static void step() {
		ArrayList<Monster> monsters = new ArrayList<Monster>();
		monsters.addAll(actualMap.getMonsters());
		
		for(Monster m : monsters) {
			if(m.isAlive()) {
				m.deplacer();
				m.attaquer(GameWorld.getPlayer());
			}
			else
				actualMap.removeEntity(m);
		}
	}
	
	/**
	 * Permet d'actualiser la variable gameWon une fois la porte des enfers ouverte
	 */
	public static void hellDoorHasBeenOpened() {
		gameWon = true;
	}

	/**
	 * Dessine le jeu
	 */
	public static void dessine() {
		camera.renderCamera();
	}

	// GETTERS AND SETTERS
	public static boolean gameWon() { return gameWon; }

	public static Camera getCamera() { return camera; }

	public static Player getPlayer() { return personnage; }

	public static Map getActualMap() { return actualMap; }

	public static void setActualMap(int idMap) { actualMap = maps.get(idMap); }
	public static void setActualMap(Map map) { actualMap = map; }

	public static TreeMap<Integer, Map> getMaps() { return maps; }
}
