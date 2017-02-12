package v.lucidlink.Frame;

		import java.lang.reflect.Field;

public class VReflection {
	public static String GetFields(Class clazz) {
		String result = "";
		Class currentClass = clazz;
		while (currentClass != null) {
			for (Field field : currentClass.getFields()) {
				result += field.getName() + ", ";
			}
			currentClass = currentClass.getSuperclass();
		}
		return result;
	}

	public static void SetField_Static(Class clazz, String fieldName, Object value) {
		try {
			Class currentClass = clazz;
			Field field = null;
			while (field == null && currentClass != null) {
				field = currentClass.getDeclaredField(fieldName);
				currentClass = currentClass.getSuperclass();
			}

			field.setAccessible(true);
			field.set(null, value);
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	public static void SetField(Object obj, String fieldName, Object value) {
		try {
			Class currentClass = obj.getClass();
			Field field = null;
			while (field == null && currentClass != null) {
				field = currentClass.getDeclaredField(fieldName);
				currentClass = currentClass.getSuperclass();
			}

			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			throw new Error(e);
		}
	}
}