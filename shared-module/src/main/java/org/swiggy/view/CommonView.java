package org.swiggy.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * <p>
 * Represents the commonly used field and methods in the application
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class CommonView {

    private static Scanner scanner;

    private final Logger logger = LogManager.getLogger(CommonView.class);

    CommonView() {
    }

    /**
     * <p>
     * Gets the scanner object.
     * </p>
     *
     * @return The scanner object
     */
    public static Scanner getScannerInstance() {
        if (null == scanner) {
            scanner = new Scanner(System.in);
        }

        return scanner;
    }

    /**
     * <p>
     * Gets the choice given by the user.
     * </p>
     *
     * @return The choice of the user
     */
    public int getChoice(){
        final String choice = getScannerInstance().nextLine().trim();

        if (isBackButton(choice)) {
            return -1;
        } else {
            try {
                return Integer.parseInt(choice);
            } catch (NumberFormatException message) {
                logger.warn("Enter Valid Input");

                return getChoice();
            }
        }
    }

    /**
     * <p>
     * Gets the information given by the user.
     * </p>
     *
     * @return The information given by the user
     */
    public String getInfo() {
        final String info = getScannerInstance().nextLine().trim();

        if (isBackButton(info)) {
            return "back";
        } else {
            return info;
        }
    }

    /**
     * Validates the user input for the back option.
     *
     * @param back The back choice of the user
     * @return True if back condition is satisfied, false otherwise
     */
    public boolean isBackButton(final String back) {
        return "*".equals(back);
    }
}
