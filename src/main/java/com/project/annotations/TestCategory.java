package com.project.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación para categorizar casos de prueba en grupos.
 * Permite ejecutar solo tests de categorías específicas usando TestNG Listener.
 * 
 * Uso:
 * @TestCategory({"smoke", "critical"})
 * @Test
 * public void testSomething() { }
 * 
 * Ejecutar: mvn test -DtestCategory=smoke
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestCategory {
    String[] value();
}
