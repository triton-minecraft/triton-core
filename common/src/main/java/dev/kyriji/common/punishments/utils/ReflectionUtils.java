package dev.kyriji.common.punishments.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {
	public static void setFieldValue(Object object, String fieldName, Object value) throws Exception {
		Field field = findField(object.getClass(), fieldName);
		if (field != null) {

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & Modifier.FINAL);

			field.setAccessible(true);
			field.set(object, value);
		}
	}

	private static Field findField(Class<?> clazz, String fieldName) {
		while (clazz != null) {
			try {
				return clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}
}