package akly;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * This class is used to create a {@link Meal} instance from JSON format, refer to
 * <a href="https://github.com/google/gson/blob/master/UserGuide.md#custom-serialization-and-deserialization">
 * GSON User Guide</a>
 */
public class MealDeserializer implements JsonDeserializer<Meal> {

	/**
	 * Manual implementation that create a {@link Meal} instance from 
	 * {@link com.google.gson.JsonElement}.
	 * @return {@link Meal} instance created from a {@link com.google.gson.JsonElement}
	 * @see {@link MealSerializer}
	 */
	@Override
	public Meal deserialize(JsonElement mealJsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
		
		JsonObject mealObjJson = mealJsonElement.getAsJsonObject();
		String name = mealObjJson.getAsJsonPrimitive("name").getAsString();
		Meal mealObj = new Meal(name);
		
		JsonArray ingredientsJson = mealObjJson.getAsJsonArray("ingredients");
		for (int i = 0; i < ingredientsJson.size(); i++) {
			mealObj.add(new Gson().fromJson(ingredientsJson.get(i), Ingredient.class));
		}
		
		return mealObj;
	}

}
