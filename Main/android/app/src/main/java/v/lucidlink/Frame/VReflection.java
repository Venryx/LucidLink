package v.lucidlink.Frame;

import java.lang.reflect.Field;

public class VReflection {
	public static void SetField_Static(Class clazz, String fieldName, Object value) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(null, value);
		} catch (Exception e) {
			throw new Error(e);
		}
	}
}