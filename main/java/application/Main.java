//Dependencies
package main.java.application;

import java.util.Locale;

import main.java.interfaces.Prompt;

//-----------------------------------------------------
/*
 * @File.:      TrabalhoPratico
 * @Authors.:   Pedro Carbonaro and Gabriel Todt
 * @StartDate.: 2023-08-17
 * @Copyright.: Copyright (c) 2023
*/
//-----------------------------------------------------

public abstract class Main {

    public static void main(String[] args) throws Exception {

        /*
         * Set locale and decimal pointers as default
         * and starts the application.
        */
        Locale.setDefault(Locale.US);
        new Prompt().buildPrompt();
    }
}
