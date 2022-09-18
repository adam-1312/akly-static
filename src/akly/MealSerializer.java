package akly;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * This class is used to convert {@link Meal} instance into JSON format, refer to
 * <a href="https://github.com/google/gson/blob/master/UserGuide.md#custom-serialization-and-deserialization">
 * GSON User Guide</a>
 */
public class MealSerializer implements JsonSerializer<Meal> {

	/**
	 * Manual implementation that returns the {@link Meal} instance as 
	 * {@link com.google.gson.JsonElement} since {@link com.google.gson.Gson#toJson(Object)}
	 *  treats {@link Meal} as a {@link java.util.Collection}. 
	 * @return {@link com.google.gson.JsonElement} object of {@link Meal} instance
	 */
	@Override
	public JsonElement serialize(Meal mealObj, Type type, JsonSerializationContext context) {
		Gson gson = new Gson();
		JsonObject mealObjJson = new JsonObject();
		
		mealObjJson.add("name", new JsonPrimitive(mealObj.getName()));
		mealObjJson.add("ingredients", gson.toJsonTree(mealObj));
		
		return mealObjJson;
	}

}
