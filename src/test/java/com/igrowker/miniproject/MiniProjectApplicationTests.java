package com.igrowker.miniproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.igrowker.wander.Wander;  // Importa la clase principal

@SpringBootTest(classes = Wander.class)  // Especifica la clase de configuración principal
class MiniProjectApplicationTests {

    @Test
    void contextLoads() {
        // Prueba que el contexto de Spring Boot se carga correctamente
    }
}