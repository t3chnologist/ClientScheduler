package util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * This is a helper class that provides items like hours, minutes, and months for comboBoxes.
 * @author Batnomin Terbish
 * */
public class ComboBoxItems {
    private static final ArrayList<String> hours = new ArrayList<>();
    private static final ArrayList<String> minutes = new ArrayList<>();
    private static final ArrayList<String> months = new ArrayList<>();
    private static final List<String> sampleAppointmentTypes = Arrays.asList(
            "Planning Session", "De-Briefing", "Status Update", "Decision Making", "Problem Solving"
    );

    /**
     * Method to get hours
     * @return hours
     */
    public static List<String> getHours() {
        if(hours.isEmpty()) {
            for (int i = 0; i < 24; ++i) {
                hours.add(String.valueOf(i));
            }
        }
        return hours;
    }
    /**
     * Method to get minutes
     * @return minutes
     */
    public static List<String> getMinutes() {
        if (minutes.isEmpty()) {
            for (int i = 0; i < 60; ++i) {
                minutes.add(String.valueOf(i));
            }
        }
        return minutes;
    }
    /**
     * Method to get months
     * @return months
     */
    public static List<String> getMonths() {
        if (months.isEmpty()) {
            for (int i = 1; i <= 12; ++i) {
                months.add(String.valueOf(i));
            }
        }
        return months;
    }
    /**
     * Method to get appointment types
     * @return sampleAppointmentTypes
     */
    public static List<String> getSampleAppointmentTypes() {
        return sampleAppointmentTypes;
    }
}
