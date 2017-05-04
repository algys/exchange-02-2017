package com.cyclic;

import com.cyclic.configs.ResourceManager;
import com.cyclic.configs.RoomConfig;
import com.cyclic.configs.WebSocketServerConfiguration;
import com.cyclic.controllers.WebSocketController;
import com.cyclic.services.game.RoomManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by algys on 08.02.17.
 */


@SuppressWarnings({"DefaultFileTemplate", "SpringFacetCodeInspection", "WeakerAccess"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


