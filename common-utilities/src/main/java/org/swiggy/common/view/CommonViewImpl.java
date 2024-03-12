package org.swiggy.common.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * <p>
 * Represents the commonly methods in the application.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class CommonViewImpl implements CommonView {

    private static Scanner scanner;
    private static CommonView commonView;
    private final Logger logger;

    private CommonViewImpl() {
        logger = LogManager.getLogger(CommonViewImpl.class);
    }

    /**
     * <p>
     * Gets the object of the common view implementation class.
     * </p>
     *
     * @return The common view implementation object
     */
    public static CommonView getInstance() {
        if (null == commonView) {
            commonView = new CommonViewImpl();
        }

        return commonView;
    }

    /**
     * <p>
     * Gets the scanner object.
     * </p>
     *
     * @return The scanner object
     */
    private Scanner getScannerInstance() {
        if (null == scanner) {
            scanner = new Scanner(System.in);
        }

        return scanner;
    }

    /**
     * {@inheritDoc}
     *
     * @return The value give by the user
     */
    @Override
    public int getValue() {
        final String choice = getScannerInstance().nextLine().trim();

        if (isBackButton(choice)) {
            return -1;
        } else {
            try {
                return Integer.parseInt(choice);
            } catch (NumberFormatException message) {
                logger.warn("Enter Valid Input");

                return getValue();
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return The information given by the user
     */
    @Override
    public String getInfo() {
        final String info = getScannerInstance().nextLine().trim();

        if (isBackButton(info)) {
            return "back";
        } else {
            return info;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param back The back choice of the user
     * @return True if back condition is satisfied, false otherwise
     */
    @Override
    public boolean isBackButton(final String back) {
        return "*".equals(back);
    }
}
