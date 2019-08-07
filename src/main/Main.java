package main;

import java.awt.Font;

import gameworld.GameWorld;

public class Main {

	public static void main(String[] args) {
		// initialisation du monde
		GameWorld.init();
		
		// permet le double buffering, pour permettre l'animation
		StdDraw.enableDoubleBuffering();

		// permet de redimenssioner la fenêtre
		StdDraw.setCanvasSize(
				(GameWorld.getCamera().getCamSize() * 2 + 1) * 40,
				(GameWorld.getCamera().getCamSize() * 2 + 1) * 40
		);

		// la boucle principale de notre jeu
		while (GameWorld.getPlayer().isAlive() && !GameWorld.gameWon()) {
			// Efface la fenêtre graphique
			StdDraw.clear(StdDraw.BLACK);

			GameWorld.raycasting();
			
			// Capture et traite les éventuelles interactions clavier du joueur
			GameWorld.processUserInput();
			
			GameWorld.step();
			
			// dessine la carte
			GameWorld.dessine();
			
			// Montre la fenêtre graphique mise à jour et attends 20 millisecondes
			StdDraw.show();
			StdDraw.pause(10);
		}
		
		StdDraw.filledRectangle(0.5, 0.5, 0.35, 0.1);
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.setFont(new Font("Courier New", Font.PLAIN, 72));
		if (GameWorld.gameWon())
			StdDraw.text(0.5, 0.5, "GAME WON !");
		else
			StdDraw.text(0.5, 0.5, "GAME OVER !");
		StdDraw.show();

	}

}
