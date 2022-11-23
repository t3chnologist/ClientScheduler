package util;
/**
 * This is a helper class that provides simple Regular Expression Strings for InputValidation class.
 * @author Batnomin Terbish
 */
public class RegEx {
    /**
     * Method to get the username regex: unicode letters, numeric characters and underscores.
     * @return string for username regex.
     */
    public static String getUserNameRegex() {
        //unicode letters, numeric characters and underscores
        return "^[\\p{L}\\p{N}\\_]+$";
    }
    /**
     * Method to get the text regex: //unicode letters, numeric characters,
     * white space, dashes, commas, and letters between 0-30 char length.
     * @return string for text regex.
     */
    public static String getTextRegex() {
        //unicode letters, numeric characters, white space, dashes, commas,
        // and letters between 0-30 char length
        return "^[\\p{L}\\p{N}\\p{Z}\\-\\,\\.]{0,30}";
    }
    /**
     * Method to get the postcode regex : Numeric characters and unicode letters.
     * @return string for postcode regex.
     */
    public static String getPostcodeRegex() {
        //Numeric characters and unicode letters
        return "^[\\p{L}\\p{N}]+$";
    }
    /**
     * Method to get the phone number regex: Numeric characters and hyphens/dashes.
     * @return string for phone number regex.
     */
    public static String getPhoneRegex() {
        //Numeric characters and hyphens/dashes
        return "^[\\p{N}\\-]+$";
    }
}