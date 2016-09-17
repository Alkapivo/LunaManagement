/**
 * Created by Aleksander on 15.09.2016.
 */
public class ValidatePurchase {
    public static boolean validateName(String val) {
        return !(val.isEmpty());
    }

    public static boolean validateCount(String val) {
        try {
            int i = Integer.parseInt(val);
            return !(val.isEmpty() && (i < 0));
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean validatePercent(String val) {
        try {
            int i = Integer.parseInt(val);
            return !(val.isEmpty() && (i < 0 && i > 100));
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean validatePrice(String val) {
        try {
            double i = Double.parseDouble(val);
            return !(val.isEmpty() && (i < 0));
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
