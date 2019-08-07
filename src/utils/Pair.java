package utils;

public class Pair<L,R> {

  /**
   * cot� gauche de la pair
   */
  private final L left;

  /**
   * cot� droit de la pair
   */
  private final R right;

  /**
   * Constructeur
   * @param left
   *              cot� gauche de la pair
   * @param right
   *              cot� droit de la pair
   */
  public Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  /**
   * Retourne la parti gauche de la pair
   * @return parti gauche de la pair
   */
  public L getLeft() { return left; }

  /**
   * Retourne la parti droite de la pair
   * @return partie droite de la pair
   */
  public R getRight() { return right; }

  /**
   * Compare deux pair
   * @param o
   * @return true si m�me pair sinon false
   */
  public boolean equals(Object o) {
    if (!(o instanceof Pair)) return false;
    Pair pairo = (Pair) o;
    return this.left.equals(pairo.getLeft()) &&
           this.right.equals(pairo.getRight());
  }

}
