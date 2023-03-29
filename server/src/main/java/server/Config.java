/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;

import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    /**
     * @return a new instance of a number generator
     */
    @Bean
    public Random getRandom() {
        return new Random();
    }

    /**
     * Method which generates a random password of 7 characters
     * which consist of uppercase letters and numbers
     * @return - the generated password
     */
    @Bean
    public String generatePassword() {
        int passwordLength = 7;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder password = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }
        System.out.println(password.toString());
        return password.toString();
    }
}