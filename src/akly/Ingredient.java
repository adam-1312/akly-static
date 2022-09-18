package akly;

/**
 * 
 * This class represent an ingredient that can be combined with other ingredients to make a meal.
 * 
 * @author Adam Nafie
 * 
 */

public class Ingredient {

	/** Name of ingredient */
	private String name; 
	
	/** Measuring unit used for ingredient amount*/
	private String unit;
	
	/** Amount of ingredient where the unit of measure here must match unit of measure in {@link pricePerUnit} 
	 */
	private float amount;
	
	/** Buying price of ingredient per measuring unit where the unit of measure here must match with unit of measure in {@link amount} 
	 */
	private float pricePerUnit;
	
	public Ingredient(String name, String unit, float amount, float pricePerUnit) {
		this.name = name;
		this.unit = unit;
		this.amount = amount;
		this.pricePerUnit = pricePerUnit;
	}
	
	/**
	 * @return Name of ingredient
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return Measuring unit of ingredient
	 */
	public String getUnit() {
		return unit;
	}
	
	/**
	 * @return Amount used of ingredient
	 */
	public float getAmount() {
		return amount;
	}
	
	/**
	 * Calculates buying price of ingredient and returns it
	 * @return Buying price of ingredient
	 */
	public float getPrice() {
		return pricePerUnit * amount;
	}
	
}
