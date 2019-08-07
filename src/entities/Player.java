package entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import entities.items.Arme;
import entities.items.Armure;
import entities.items.consommables.Bombe;
import entities.items.consommables.Potion;
import gameworld.GameWorld;
import main.StdDraw;
import map.Cell;
import map.cells.*;
import utils.Pair;
import utils.Position;

public class Player extends LivingBeing {

	/**
	 * attente avant nouveau déplacement du joueur
	 */
	private int cooldownMovement = 0;
	
	/**
	 * attente avant nouvelle attaque du joueur
	 */
	private int cooldownAttack = 0;

	/**
	 * Inventaire du joueur
	 */
	private ArrayList<Pair<Integer, Item>> Inventory;

	/**
	 * Arme équipée du joueur
	 */
	private Arme weapon = null;
	
	/**
	 * Armure équipée du joueur
	 */
	private Armure armour = null;
	
	/**
	 * Potion équipée du joueur
	 */
	private Potion equipedPotion = null;
	
	/**
	 * Nombre de clés trouvées par le joueur
	 */
	private int keysFound = 0;

	/**
	 * Nombre de points d'expérience du joueur
	 */
	private int experience = 0;
	
	/**
	 * Niveau du joueur
	 */
	private int level = 1;

	public Player(String nom, int x, int y, int pDV, int pAtk, int pDef) {
		super(nom, "res/textures/player/player", x, y, pDV, pAtk, pDef);
		Inventory = new ArrayList<Pair<Integer, Item>>();
	}

	/**
	 * Permet d'afficher l'inventaire du joueur dans la console
	 */
	public void afficheInventaire() {
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.setFont(new Font("Courier New", Font.PLAIN, 72));
		StdDraw.text(0.5, 0.5, "Inventaire");
		StdDraw.show();
		StdDraw.setFont();

		System.out.println("--Inventaire--");
		if (Inventory.isEmpty())
			System.out.println("Vous avez malheureusement les fesses a l'air");
		int i = 0;
		for (Pair<Integer, Item> p : Inventory) {
			System.out.println(i + " - (" + p.getLeft() + ") " + p.getRight().nom);
			i++;
		}
		System.out.println("Pour quitter l'inventaire, appuyez sur la touche q puis entrer");
		boolean afficheInventaire = true;
		while (afficheInventaire) {
			Scanner sc = new Scanner(System.in);
			String str = sc.nextLine();
			if (str.equals("q")) {
				afficheInventaire = false;
			} else {
				try {
					int numeroItem = Integer.parseInt(str);
					if (numeroItem < Inventory.size() && numeroItem >= 0) {
						Item item = Inventory.get(numeroItem).getRight();
						if (item.getClass().getName().contains("Armour")) {
							ramasser(armour);
							armour = (Armure) item;
							removeItemFromInventory(item);
						} else if (item.getClass().getName().contains("Sword")) {
							ramasser(weapon);
							weapon = (Arme) item;
							removeItemFromInventory(item);
						} else {
							throw new Exception("l'item n'est pas equipable");
						}
					} else {
						throw new Exception("L'item n'existe pas");
					}
				} catch(Exception e) {
					System.out.println(e);
				}
			}
		}
	}

	/**
	 * Permet de gérer l'équipement du joueur
	 */
	public void afficheEquipement() {
		System.out.println("--Equipement--");
		if (armour == null && weapon == null) {
			System.out.println("Tout nu comme un vers");
		} else {
			if (armour != null) {
				System.out.println("Votre equipement : " + armour.getNom());
			}
			if (weapon != null) {
				System.out.println("Votre arme : " + weapon.getNom());
			}
		}
	}

	/**
	 * fais mourir le joueur
	 */
	@Override
	public void mourir() {
		System.out.println("VOUS ETES MORT !");
	}

	/**
	 * Permet de soigner le joueur d'un nombre de points de vie spécifiés
	 * @param healAmount
	 */
	public void heal(int healAmount) {
		this.pointsDeVie += healAmount;
	}

	/**
	 * Deplace le joueur
	 * 
	 * @param vertical
	 * @param horizontal
	 */
	public void seDeplacer(int vertical, int horizontal) {
		if (this.cooldownMovement == 0) {
			cooldownMovement = 5;
			Cell targetCell = GameWorld.getActualMap().getCells()[this.getX() + horizontal][this.getY() + vertical];
			if (targetCell.getCellType().equals("FloorCell")
					&& ((((FloorCell) targetCell).getMonster() == null) || ((FloorCell) targetCell).getMonster()
							.getClass().toString().contains("class entities.monsters.traps."))) {
				this.position.setX(this.getX() + horizontal);
				this.position.setY(this.getY() + vertical);
			} else if (targetCell.getCellType().equals("TeleportationCell")) {
				((TeleportationCell) targetCell).teleport();
			}
		} else {
			this.cooldownMovement--;
		}
	}

	/**
	 * Permet au joueur d'attaquer un être vivant spécifié
	 * @param e
	 */
	@Override
	public void attaquer(LivingBeing e) {
		if (e.getPos().distanceTo(this.getPos()) < 2 && this.cooldownAttack == 0) {
			this.cooldownAttack = 10;
			int finalAtk = this.pointsDAttaque + ((weapon != null) ? weapon.getBonus() : 0);
			e.prendreDegats(finalAtk);
		} else {
			this.cooldownAttack--;
		}
	}

	/**
	 * Fais prendre un nombre de dégats spécifié au joueur
	 * @param atk
	 */
	@Override
	public void prendreDegats(int atk) {
		if (pointsDeVie > 0) {
			Random r = new Random();
			int damage = (int) ((atk - (pointsDeDefense + ((armour != null) ? armour.getBonus() : 0)))
					* (1 + ((r.nextFloat() * 2 - 1) / 3)));
			this.pointsDeVie -= damage;
			if (this.pointsDeVie <= 0) {
				this.mourir();
			}
		}
	}

	/**
	 * Lance une bombe sur une cellule est ocasionne des dommages sur tout les
	 * monstres autour
	 * 
	 * @param pos
	 */
	public void lancerBombe(Position pos) {
		if (!haveInInventory("Bombe"))
			return;
		if (pos.distanceTo(GameWorld.getPlayer().getPosition()) < 4) {
			Cell[][] cells = GameWorld.getActualMap().getCells();
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (GameWorld.getActualMap().isCellAtPos(new Position(pos.getX() + i, pos.getY() + j))) {
						Cell targetCell = cells[pos.getX() + i][pos.getY() + j];
						if (targetCell.getCellType().equals("FloorCell")) {
							if (((FloorCell) targetCell).getMonster() != null) {
								float reductionDistance = 1 - Math.abs(i) * 25 / 100 - Math.abs(j) * 25 / 100;
								int degat = Math.round(15 * reductionDistance);
								((FloorCell) targetCell).getMonster().prendreDegats(degat);
								removeItemFromInventory(new Bombe());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Lance un missile sur le monstre le plus proche
	 */
	public void lancerMissile() {
		if (!haveInInventory("Missile")) {
			return;
		}
		Monster nearestMonster;
		double nearestDist;
		ArrayList<Monster> monsters = GameWorld.getActualMap().getMonsters();
		if (monsters.size() == 0)
			return;
		nearestMonster = monsters.get(0);
		nearestDist = monsters.get(0).getPos().distanceTo(GameWorld.getPlayer().getPosition());
		for (int i = 1; i < monsters.size(); i++) {
			Monster monster = monsters.get(i);
			double distance = monster.getPos().distanceTo(GameWorld.getPlayer().getPosition());
			if (distance < nearestDist) {
				nearestDist = distance;
				nearestMonster = monster;
			}
		}
		nearestMonster.prendreDegats(1000);
		removeItemFromInventory(getItemFromInventory("Missile"));
	}

	/**
	 * Ajoute l'item i a l'inventaire du joueur
	 * @param i
	 */
	public void ramasser(Item i) {
		if (i.nom.contains("Cle")) {
			this.keysFound++;
			System.out.println("VOUS VENEZ DE RAMASSER UNE CLE DEMONIAQUE, PLUS QUE " + (4 - this.keysFound));
		}
		for (Pair<Integer, Item> p : Inventory) {
			if (p.getRight().nom.equals(i.nom)) {
				Pair<Integer, Item> toAdd = new Pair<Integer, Item>(p.getLeft() + 1, p.getRight());
				Inventory.remove(p);
				Inventory.add(toAdd);
				return;
			}
		}
		Inventory.add(new Pair<Integer, Item>(1, i));
		if (i.nom.contains("Potion")) {
			System.out.println("Vous venez de ramasser " + i.nom + " et l'avez équipé");
			this.equipedPotion = (Potion) i;
		}
		if (i.nom.contains("Epee")) {
			System.out.println("Vous venez de ramasser " + i.nom + " et l'avez équipé");
			this.weapon = (Arme) i;
		}
		if (i.nom.contains("Armure")) {
			System.out.println("Vous venez de ramasser " + i.nom + " et l'avez équipé");
			this.armour = (Armure) i;
		}
	}

	/**
	 * Permet de vérifier si un item est dans l'inventaire du joueur a partir de son nom
	 * @param itemName
	 * @return true si l'objet est dans l'inventaire, false sinon
	 */
	public boolean haveInInventory(String itemName) {
		for (Pair<Integer, Item> p : Inventory) {
			if (p.getRight().nom.equals(itemName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Permet de récupérer un item dans l'inventaire grâce a son nom
	 * @param itemName
	 * @return l'item recherché, null si il n'est pas présent dans l'inventaire
	 */
	public Item getItemFromInventory(String itemName) {
		for (Pair<Integer, Item> p : Inventory) {
			if (p.getRight().nom.equals(itemName)) {
				return p.getRight();
			}
		}
		return null;
	}

	/**
	 * Permet de retirer un Item i de l'inventaire
	 * @param i
	 */
	public void removeItemFromInventory(Item i) {
		for (Pair<Integer, Item> p : Inventory) {
			if (p.getRight().nom.equals(i.nom)) {
				Pair<Integer, Item> toAdd = new Pair<Integer, Item>(p.getLeft() - 1, p.getRight());
				Inventory.remove(p);
				if (p.getLeft() > 0)
					Inventory.add(toAdd);
				return;
			}
		}
	}

	/**
	 * Permet d'utiliser la potion équipée
	 */
	public void useEquipedPotion() {
		if (haveInInventory(equipedPotion.nom) && this.pointsDeVie < this.pointsDeVieMax) {
			((Potion) this.getItemFromInventory(equipedPotion.nom)).use(this);
			this.removeItemFromInventory(this.getItemFromInventory(equipedPotion.nom));
		} else if (!haveInInventory(equipedPotion.nom))
			this.equipedPotion = null;
	}

	/**
	 * Permet de savoir si une potion est équipée
	 * @return true si une potion est équipée, false sinon
	 */
	public boolean hasEquipedPotion() {
		if (equipedPotion != null)
			return true;
		else
			return false;
	}

	/**
	 * Permet de déposer les clés présentes dans l'inventaire du joueur dans la porte de l'enfer spécifiée
	 * @param hd
	 */
	public void deposer(HellDoor hd) {
		while(haveInInventory("Cle Demoniaque")) {
			hd.placer();
			removeItemFromInventory(getItemFromInventory("Cle Demoniaque"));
		}
	}

	/**
	 * Permet au joueur de gagner le montant d'expérience spécifié
	 * @param exp
	 */
	public void gainExperience(int exp) {
		this.experience += exp;
		System.out.println("Expérience : " + this.experience + " / " + this.level * 100);
		if (this.experience >= this.level * 100) {
			this.level++;
			this.experience = this.experience - (this.level - 1) * 100;
			System.out.println("LEVEL UP : lvl " + this.level);
		}
	}

	// GETTERS AND SETTERS
	
	/**
	 * Permet de récupérer les points de vie du joueur
	 * @return pointsDeVie
	 */
	public int getPointsDeVie() {
		return this.pointsDeVie;
	}

	/**
	 * Permet de savoir si le joueur est en vie
	 * @return true si pointsDeVie > 0, false sinon
	 */
	public boolean isAlive() {
		return this.pointsDeVie > 0;
	}

	/**
	 * Permet de récupérer l'orientation du joueur
	 * @return orientation
	 */
	public int getOrientation() {
		return this.orientation;
	}

	/**
	 * Permet de modifier l'orientation du joueur
	 * @param orientation
	 */
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	/**
	 * Retourne la position du joueur
	 * @return position
	 */
	public Position getPosition() {
		return this.position;
	}
}
