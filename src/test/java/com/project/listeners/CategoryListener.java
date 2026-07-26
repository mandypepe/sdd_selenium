package com.project.listeners;

import com.project.annotations.TestCategory;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Listener que filtra tests según categoría especificada en -DtestCategory.
 * 
 * Si no se especifica categoría, ejecuta TODOS los tests.
 * Si se especifica categoría, ejecuta SOLO tests etiquetados con esa categoría.
 * 
 * Tests sin anotación @TestCategory se ejecutan siempre (sin restricción).
 */
public class CategoryListener implements IMethodInterceptor {

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        String testCategory = System.getProperty("testCategory");
        
        if (testCategory == null || testCategory.isEmpty()) {
            System.out.println("[CategoryListener] No category specified (-DtestCategory). Running ALL tests.");
            return methods;
        }

        System.out.println("[CategoryListener] Running tests with category: " + testCategory);
        
        return methods.stream()
            .filter(method -> {
                TestCategory annotation = method.getMethod()
                    .getConstructorOrMethod()
                    .getMethod()
                    .getAnnotation(TestCategory.class);
                
                if (annotation == null) {
                    System.out.println("[CategoryListener] ⚪ " + method.getMethod().getMethodName() 
                        + " - No category annotation (INCLUDED by default)");
                    return true;
                }
                
                boolean matches = Arrays.asList(annotation.value())
                    .contains(testCategory);
                
                if (matches) {
                    System.out.println("[CategoryListener] ✅ " + method.getMethod().getMethodName() 
                        + " - Category: " + Arrays.toString(annotation.value()));
                }
                
                return matches;
            })
            .collect(Collectors.toList());
    }
}
