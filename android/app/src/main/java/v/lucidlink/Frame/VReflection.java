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

	public static Object GetField_Static(Class clazz, String fieldName) {
		return GetField(clazz, null, fieldName);
	}
	public static Object GetField(Object obj, String fieldName) {
		return GetField(obj.getClass(), fieldName);
	}
	static Object GetField(Class clazz, Object obj, String fieldName) {
		try {
			Class currentClass = clazz;
			Field field = null;
			while (field == null && currentClass != null) {
				field = currentClass.getDeclaredField(fieldName);
				currentClass = currentClass.getSuperclass();
			}

			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	public static void SetField_Static(Class clazz, String fieldName, Object value) {
		SetField(clazz, null, fieldName, value);
	}
	public static void SetField(Object obj, String fieldName, Object value) {
		SetField(obj.getClass(), obj, fieldName, value);
	}
	static void SetField(Class clazz, Object obj, String fieldName, Object value) {
		try {
			Class currentClass = clazz;
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