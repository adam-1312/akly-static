package akly;

import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;


/**
 * This class represents a combination of ingredients, in other words a meal.
 * 
 * @author Adam Nafie
 *
 */
@SuppressWarnings("serial")
public class Meal extends ArrayList<Ingredient> {
	
	/** 
	 * Name of meal (has a default value based on HashCode if not set beforehand)
	 */
	private String name = "Meal:" + this.hashCode();
	
	public Meal() {
		super();
	}
	
	public Meal(String name) {
		super();
		setName(name);
	}
	
	/**
	 * Constructor that takes a {@link java.util.Collection} of ingredients.
	 * @param ingredients Combination of ingredients
	 */
	public Meal(Collection<Ingredient> ingredients) {
		super.addAll(ingredients);
	}
	
	/**
	 * Constructor that takes each ingredient as a separate parameter.
	 * @param ingredients Combination of ingredients
	 */
	public Meal(Ingredient... ingredients) {
		for (Ingredient ing : ingredients) {
			super.add(ing);
		}
	}
	
	/**
	 * Gets {@link name}
	 * @return Name of meal
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets name of meal
	 * @param name New name of meal
	 * @return Returns the same {@link Meal} instance; to allow chaining
	 */
	public Meal setName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Calculates total price of meal per portion and returns it.
	 * @return Total price of meal per portion
	 */
	public float getTotalPrice() {
		float total = 0;
		for (Ingredient ing : this) {
			total += ing.getPrice();
		}
		return total;
	}
	
	/**
	 * Returns a string with the meal structured in an html table.
	 * @return String of html table element.
	 */
	public String toHtmlTable() {
		// Ingredient name row
		String table = "<table><h3>" + this.getName()
						+"</h3><tr><td>Ingredients:</td><td>";
		
		for (Ingredient ing : this) {
			table += ing.getName() + "</td><td>";
		}
		
		// Amount row
		table += "Total</td></tr><tr><td>Amount:</td><td>";
		
		for (Ingredient ing : this) {
			table += ing.getAmount() + " " + ing.getUnit() + "</td><td>";
		}
		
		// Price row
		table += "n.a</td></tr><tr><td>Price:</td><td>";
		
		for (Ingredient ing : this) {
			table += ing.getPrice() + "</td><td>";
		}
		
		table += getTotalPrice() + "</td></tr>";
		
		return table;
	}
	
	/**
	 * Manual implementation that returns the {@link Meal} instance in JSON format, since 			   {@link com.google.gson.Gson#toJson(Object)} treats {@link Meal} as a 							   {@link java.util.Collection}. 
	 * @return JSON Format of instance
	 */
	public String toJson() {
		Gson gson = new Gson();
		JsonObject thisJson = new JsonObject();
		
		thisJson.add("name", new JsonPrimitive(this.name));
		thisJson.add("ingredients", gson.toJsonTree(this));
		
		
		return gson.toJson(thisJson);
	}
}
