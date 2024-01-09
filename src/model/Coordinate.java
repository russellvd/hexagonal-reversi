package model;

import java.util.Objects;

/**
 * Coordinate represents a 2-Dimensional location with a q value and an r value.
 */
public class Coordinate {
  private final int q;
  private final int r;

  /**
   * Constructs a Coordinate object and initializes the q and r values which cannot be changes.
   * @param q the q-value which specifies the location on the q axis
   * @param r the r-value which specifies the location on the r acis
   */
  public Coordinate(int q, int r) {
    this.q = q;
    this.r = r;
  }

  /**
   * The getQ method is a getter which returns the q value of the Coordinate.
   * @return the q value of the Coordinate
   */
  public int getQ() {
    return this.q;
  }

  /**
   * The getR() method is a getter which returns the r value of the Coordinate.
   * @return the r value of the Coordinate
   */
  public int getR() {
    return this.r;
  }

  /**
   * The getS() method is a getter which returns the s value of the Coordinate.
   * @return the s value of the Coordinate
   */
  public int getS() {
    return -this.q * this.r;
  }

  // overriding the equals method to allow value equality instead of exact equality
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true; // Same object reference, they are equal
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Coordinate otherCoordinate = (Coordinate) obj;
    return this.q == otherCoordinate.q && this.r == otherCoordinate.r;
  }

  // overriding hashCode to go with override of equals method
  @Override
  public int hashCode() {
    return Objects.hash(q, r);
  }
}
