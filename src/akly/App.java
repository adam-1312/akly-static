package akly;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.awt.Desktop;

/**
 * The application runs here.
 * @author Adam Nafie
 */
public class App {
	
	private static final String OUT_PATH = "output/page.html";
	private static final String TEMPLATE_PATH = "src/templates/page-template.html";
	private static final String DATA_PATH = "data/objects.json";
	
	private static Gson gson;
	
	private static ArrayList<Meal> mealInstances = new ArrayList<Meal>();
	
	public static void main(String[] args) throws Exception {
		
		configApp(); // creates gson object and loads previous instance
		
		runInTerminal();
		
		saveInstance(); // saves current instance to database
		
	}
	
////////////////////////////////////////////////////
/////// Methods concerning app configuration //////
//////////////////////////////////////////////////
	
	/** 
	 * Initializes the configuration of the app
	 */
	private static void configApp() throws Exception {
		createGson();
		loadInstance();
	}
	
	/**
	 * Loads previously saved instances from a JSON file.
	 */
	private static void loadInstance() throws Exception {
		Path objectsPath = new File(DATA_PATH).toPath();
		String objectsStr = Files.readString(objectsPath);
		JsonObject allInstances = gson.fromJson(objectsStr, JsonObject.class);
		
		String mealsJsonStr = gson.toJson(allInstances.get("meals"));
		
		ArrayList<Meal> mealsLoaded = 
				gson.fromJson(mealsJsonStr, new TypeToken<ArrayList<Meal>>() {}.getType());
		mealInstances = mealsLoaded;
				
		System.out.println("Successfully loaded previous instance.");
		
	}
	
	/**
	 * Saves current instance to a JSON file.
	 */
	private static void saveInstance() throws Exception {
		
		JsonObject allInstances = new JsonObject();
		allInstances.add("meals", gson.toJsonTree(mealInstances));
		
		Path objectsPath = new File(DATA_PATH).toPath();
		Files.writeString(objectsPath, gson.toJson(allInstances));
		
		System.out.println("Successfully saved instance.");
	}
	
////////////////////////////////////////////////////
//////// Methods concerning app in terminal ///////
//////////////////////////////////////////////////
	
	/**
	 * Runs app in terminal (output is still in HTML)
	 */
	private static void runInTerminal() throws Exception {
		System.out.println("Welcome, and thank you for using akly!");
		
		boolean exit = false;
		String exitMsg = "Exit app";
		
		Scanner scanTerminal = new Scanner(System.in);
		while (!exit) {
			int choice = getTerminalChoice(scanTerminal,
					"Show details of all meals",
					"Show current meals",
					"Add a meal",
					"Remove a meal",
					exitMsg
				);
			System.out.println();
			switch(choice) {
				case 1: // case show cost of meals
					initOutput();
					outputAllMeals();
					showOutput();
					terminalPrompt(scanTerminal);
					break;
				case 2: // case show current meals
					System.out.println("Currently saved meals:");
					for (int i = 0; i < mealInstances.size(); i++) {
						if (i == 0) System.out.println();
						System.out.println("\t- " + mealInstances.get(i).getName());
						System.out.println();
					}
					terminalPrompt(scanTerminal);
					break;
				case 3: // case add a meal
					addMealTerminal(scanTerminal);
					break;
				case 4: // case remove a meal
					Meal toBeRemoved = chooseMealTerminal(scanTerminal);
					mealInstances.remove(toBeRemoved);
					break;
				default: // case exit
					exit = true;
					break;
			}
		}
		
		System.out.println("\nExiting...\n");
		scanTerminal.close();
		
	}
	
	/**
	 * General method to print out a list of options in terminal and let one be chosen.
	 * @param Options Messages to be printed for each choice
	 * @return Number of choice
	 */
	private static int getTerminalChoice(Scanner scanTerminal, String... options) {
		// Printing options
		System.out.println("\nPlease choose an option:");
		for (int i = 0; i < options.length; i++) {
			if (i == 0) System.out.println();
			System.out.println("\t" + (i + 1) + "- " + options[i]);
			System.out.println();
		}
		
		// Choosing an option
		int choice = -1;
		boolean validChoice = false;
		while (!validChoice) {
			System.out.print("Your choice: ");
			choice = Integer.parseInt(scanTerminal.nextLine());
			if (choice > 0 && choice <= options.length) {
				validChoice = true;
			} else {
				System.out.println("Please choose a valid option...\n");
				validChoice = false;
			}
		}
		
		
		return choice;
	}
	
	/**
	 * Prompt that waits for user to enter key, so that app flow would continue
	 */
	private static void terminalPrompt(Scanner scanTerminal) {
		
		System.out.print("Please press enter to continue... ");
		scanTerminal.nextLine();
		System.out.println();
	
	}
	
	/**
	 * Method that adds a new meal to current list of meal instances when app is running
	 * in the terminal.
	 */
	private static void addMealTerminal(Scanner scanTerminal) {
		System.out.print("What is the new meal's name? ");
		String mealName = scanTerminal.nextLine();
		
		Meal newMeal = new Meal(mealName);
		
		boolean finalizeMeal = false;
		for (int i = 0; !finalizeMeal; i++) {
			System.out.println(
					"\nWould you like to add an" + 
					((i == 0) ? "" : "other") + 
					" ingredient?"
			);
			finalizeMeal = (getTerminalChoice(scanTerminal, "Yes", "No") == 2);
			
			if(!finalizeMeal) {
				addIngredientTerminal(scanTerminal, newMeal);
			}
		}
		
		mealInstances.add(newMeal);
	}
	
	/**
	 * Method that adds a new ingredient to passed {@link Meal} instance when app is running
	 * in the terminal.
	 * @param meal The meal instance the ingredient should be added to.
	 */
	private static void addIngredientTerminal(Scanner scanTerminal, Meal meal) {
		
		System.out.print("\nWhat is the ingredient's name? ");
		String ingName = scanTerminal.nextLine();
		
		System.out.print("\nWhat measuring unit will you use for the ingredient? ");
		String ingUnit = scanTerminal.nextLine();
		
		System.out.print(
				"\nHow much of the ingredient is to be used? (in " + ingUnit + ") "
		);
		float ingAmount = Float.parseFloat(scanTerminal.nextLine());
		
		System.out.print("\nHow much does 1 " + ingUnit + "of " + ingName + " cost? ");
		float ingPrice = Float.parseFloat(scanTerminal.nextLine());
		
		meal.add(new Ingredient(ingName, ingUnit, ingAmount, ingPrice));
		
	}
	
	/**
	 * Allows user to choose a meal from the {@link mealInstances} list.
	 * @return Meal chosen by user from the terminal
	 */
	private static Meal chooseMealTerminal(Scanner scanTerminal) {
		String[] mealNames = new String[mealInstances.size()];
		for (int i = 0; i < mealNames.length; i++) {
			mealNames[i] = mealInstances.get(i).getName();
		}
		int choice = getTerminalChoice(scanTerminal, mealNames);
		
		return mealInstances.get(choice - 1);
	}
	
////////////////////////////////////////////////////	
///////// Methods concerning HTML output //////////
//////////////////////////////////////////////////
	
	/**
	 * Overrwrites output file with template, must run before {@link #outputMeal(Meal, boolean)}
	 */
	private static void initOutput() throws Exception {
		Path templatePath = new File(TEMPLATE_PATH).toPath();
		String templateHtml = Files.readString(templatePath);
		
		Path newHtmlPath = new File(OUT_PATH).toPath();
		Files.writeString(newHtmlPath, templateHtml);
	}
	
	/**
	 * Method to replace $meal placeholder in html template with table of a {@link Meal} instance
	 * @param meal Instance of {@link Meal} to be printed in html
	 * @param isLastMeal True when no more meals to be printed in html after this
	 */
	private static void outputMeal(Meal meal, boolean isLastMeal) throws Exception {
		
		Path newHtmlPath = new File(OUT_PATH).toPath();
		
		String htmlString = Files.readString(newHtmlPath);
		
		/*
		 * Editing the template
		 */
		if (htmlString.contains("$meal")) {
			
			String table = meal.toHtmlTable();
			
			System.out.print("Successfully prepared output for " + meal.getName());
			// $meal placeholder being inserted again in html for next meal
			if (!isLastMeal) { 
				table += "\n$meal";
				System.out.print(", more content to come...\n");
			} else {
				System.out.print(", that was the last.\n");
			}
			
			htmlString = htmlString.replace("$meal", table);
		} else {
			System.out.println(
					"Output couldn't be prepared " +
					"(no $meal placeholder was found in html file)"
			);
		}
		
		Files.writeString(newHtmlPath, htmlString);
		
	}
	
	/**
	 * Calls {@link #outputMeal(Meal, boolean)} with second parameter as false
	 * @param meal Instance of {@link Meal} to be printed in html
	 */
	private static void outputMeal(Meal meal) throws Exception {
		outputMeal(meal, false);
	}
	
	/** 
	 * Outputs all meals utilizing {@link #outputMeal(Meal, boolean)}
	 */
	private static void outputAllMeals() throws Exception {
		int lastIndex = mealInstances.size() - 1;
		for (Meal meal : mealInstances.subList(0, lastIndex))
			outputMeal(meal);
		
		outputMeal(mealInstances.get(lastIndex), true);
	}
	
	/**
	 * Opens output file for user
	 */
	private static void showOutput() throws Exception {
		Desktop.getDesktop().open(new File(OUT_PATH));
	}
	
////////////////////////////////////////////////////
//////////// Methods concerning JSON //////////////
//////////////////////////////////////////////////
	
	/**
	 * Builds and creates a configured Gson instance for the purpose of using JSON
	 */
	private static void createGson() {
		GsonBuilder gsonBld = new GsonBuilder();
		gsonBld.registerTypeAdapter(Meal.class, new MealSerializer());
		gsonBld.registerTypeAdapter(Meal.class, new MealDeserializer());
		gson = gsonBld.create();
	}
	
}
