// MainClass.java
package org.example;

import auth.AuthFrame;

import javax.swing.*;

public class MainClass {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthFrame(true));
    }
}


