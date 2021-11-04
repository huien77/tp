package happybit.parser;

import happybit.command.Command;
import happybit.command.UpdateGoalEndDateCommand;
import happybit.command.UpdateGoalNameCommand;
import happybit.command.UpdateGoalTypeCommand;
import happybit.command.UpdateHabitIntervalCommand;
import happybit.command.UpdateHabitNameCommand;
import happybit.exception.HaBitParserException;
import happybit.goal.GoalType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateParser extends Parser {

    private static final String ERROR_GOAL_END_DATE_FORMAT = "Use the e/ flag to set the new goal end date"
            + "Eg: e/31122021";
    private static final String ERROR_GOAL_END_DATE_NON_DATE = "Enter your date in the format DDMMYYYY";

    private static final String ERROR_INVALID_UPDATE_COMMAND = "There is no update command for goals in this format, "
            + "do check your parameters one more time. Do not include more or less parameters than necessary.";
    private static final String ERROR_INVALID_CHANGE_COMMAND = "There is no change command for habits in this format, "
            + "do check your parameters one more time. Do not include more or less parameters than necessary.";
    private static final String ERROR_CHANGE_HABIT_NAME_WITH_UPDATE_COMMAND = "Are you perhaps trying to change a "
            + "habit name? Please use the 'change' command instead.";
    private static final String ERROR_CHANGE_HABIT_INTERVAL_WITH_UPDATE_COMMAND = "Are you perhaps trying to change a "
            + "habit interval? Please use the 'change' command instead.";
    private static final String ERROR_UPDATE_GOAL_NAME_WITH_CHANGE_COMMAND = "Are you perhaps trying to change a "
            + "goal name? Please use the 'update' command instead.";
    private static final String ERROR_UPDATE_GOAL_TYPE_WITH_CHANGE_COMMAND = "Are you perhaps trying to change a "
            + "goal type? Please use the 'update' command instead.";
    private static final String ERROR_UPDATE_GOAL_END_DATE_WITH_CHANGE_COMMAND = "Are you perhaps trying to change a "
            + "goal end date? Please use the 'update' command instead.";

    private static final int FLAG_LENGTH = 2;

    /**
     * Examines user input to decide which goal attribute user wants to update.
     *
     * @param input User input.
     * @return Parse command specifically for updating the chosen goal attribute.
     * @throws HaBitParserException Thrown when parameters given are for changing habit rather than updating goal
     *                              or when parameters are not in the same
     */
    public static Command parseUpdateGoalCommands(String input) throws HaBitParserException {
        checkNoDescription(input);
        String[] parameters = splitInput(input);
        if (isUpdateGoalName(parameters)) {
            return parseUpdateGoalNameCommand(input);
        }
        if (isUpdateGoalType(parameters)) {
            return parseUpdateGoalTypeCommand(input);
        }
        if (isUpdateGoalEndDate(parameters)) {
            return parseUpdateGoalEndDate(input);
        }

        if (isChangeHabitName(parameters)) {
            throw new HaBitParserException(ERROR_CHANGE_HABIT_NAME_WITH_UPDATE_COMMAND);
        } else if (isChangeHabitInterval(parameters)) {
            throw new HaBitParserException(ERROR_CHANGE_HABIT_INTERVAL_WITH_UPDATE_COMMAND);
        } else {
            throw new HaBitParserException(ERROR_INVALID_UPDATE_COMMAND);
        }
    }

    public static Command parseUpdateHabitCommands(String input) throws HaBitParserException {
        checkNoDescription(input);
        String[] parameters = splitInput(input);

        if (isChangeHabitName(parameters)) {
            return parseUpdateHabitNameCommand(input);
        }
        if (isChangeHabitInterval(parameters)) {
            return parseUpdateHabitIntervalCommand(input);
        }

        if (isUpdateGoalName(parameters)) {
            throw new HaBitParserException(ERROR_UPDATE_GOAL_NAME_WITH_CHANGE_COMMAND);
        } else if (isUpdateGoalType(parameters)) {
            throw new HaBitParserException(ERROR_UPDATE_GOAL_TYPE_WITH_CHANGE_COMMAND);
        } else if (isUpdateGoalEndDate(parameters)) {
            throw new HaBitParserException(ERROR_UPDATE_GOAL_END_DATE_WITH_CHANGE_COMMAND);
        } else {
            throw new HaBitParserException(ERROR_INVALID_CHANGE_COMMAND);
        }
    }

    /**
     * Parses detail from user into goalIndex and newGoalName (information) to create a new Command.
     *
     * @param input Input typed by the user.
     * @return A Command class with the goalIndex and newGoalName.
     * @throws HaBitParserException If command parameters are not defined, or defined improperly.
     */
    public static Command parseUpdateGoalNameCommand(String input) throws HaBitParserException {
        checkNoDescription(input);
        String[] parameters = splitInput(input);
        int goalIndex = getNumber(parameters, FLAG_GOAL_INDEX) - 1;
        String newGoalName = getName(parameters);
        return new UpdateGoalNameCommand(goalIndex, newGoalName);
    }

    public static Command parseUpdateGoalEndDate(String input) throws HaBitParserException {
        checkNoDescription(input);
        String[] parameters = splitInput(input);
        int goalIndex = getNumber(parameters, FLAG_GOAL_INDEX) - 1;
        Date newDate = getDate(parameters);
        return new UpdateGoalEndDateCommand(goalIndex, newDate);
    }

    /**
     * Parses detail from user into goalIndex and newGoalName (information) to create a new Command.
     *
     * @param input Input typed by the user.
     * @return A Command class with the goalIndex and newGoalName.
     * @throws HaBitParserException If command parameters are not defined, or defined improperly.
     */
    public static Command parseUpdateGoalTypeCommand(String input) throws HaBitParserException {
        checkNoDescription(input);
        String[] parameters = splitInput(input);
        int goalIndex = getNumber(parameters, FLAG_GOAL_INDEX) - 1;
        GoalType newGoalType = getType(parameters);
        return new UpdateGoalTypeCommand(goalIndex, newGoalType);
    }

    /**
     * Parses detail from user into goalIndex, habitIndex, and newHabitName (information) to create a new Command.
     *
     * @param input Input typed by the user.
     * @return A Command class with the goalIndex, habitIndex, and newHabitName.
     * @throws HaBitParserException If command parameters are not defined, or defined improperly.
     */
    public static Command parseUpdateHabitNameCommand(String input) throws HaBitParserException {
        checkNoDescription(input);
        String[] parameters = splitInput(input);
        int goalIndex = getNumber(parameters, FLAG_GOAL_INDEX) - 1;
        int habitIndex = getNumber(parameters, FLAG_HABIT_INDEX) - 1;
        String newHabitName = getName(parameters);
        return new UpdateHabitNameCommand(goalIndex, habitIndex, newHabitName);
    }

    /**
     * Parser to parse user's command of updating interval.
     *
     * @param commandInstruction Input from user.
     * @return A Command instance for UpdateHabitIntervalCommand with the goalIndex, habitIndex and interval
     * @throws HaBitParserException If command parameters are not defined, or defined improperly
     */
    public static Command parseUpdateHabitIntervalCommand(String commandInstruction) throws HaBitParserException {
        checkNoDescription(commandInstruction);
        String[] parameters = splitInput(commandInstruction);
        int goalIndex = getNumber(parameters, FLAG_GOAL_INDEX) - 1;
        int habitIndex = getNumber(parameters, FLAG_HABIT_INDEX) - 1;
        int interval = getNumber(parameters, FLAG_INTERVAL);
        assert (goalIndex >= 0);
        assert (habitIndex >= 0);
        assert (interval >= 0);
        return new UpdateHabitIntervalCommand(goalIndex, habitIndex, interval);
    }

    /*
     * NOTE : ==================================================================
     * The following are private methods that are used to implement SLAP for the
     * above public methods. These methods are positioned at the bottom to better
     * visualise the actual methods that can be called from outside this class.
     * =========================================================================
     */

    private static boolean isUpdateGoalName(String[] parameters) {
        return isContainFlag(parameters, FLAG_GOAL_INDEX) && isContainFlag(parameters, FLAG_NAME)
        && isContainNoOtherFlag(parameters, FLAG_GOAL_INDEX, FLAG_NAME);
    }

    private static boolean isUpdateGoalType(String[] parameters) {
        return isContainFlag(parameters, FLAG_GOAL_INDEX) && isContainFlag(parameters, FLAG_GOAL_TYPE)
                && isContainNoOtherFlag(parameters, FLAG_GOAL_INDEX, FLAG_GOAL_TYPE);
    }

    private static boolean isUpdateGoalEndDate(String[] parameters) {
        return isContainFlag(parameters, FLAG_GOAL_INDEX) && isContainFlag(parameters, FLAG_END_DATE)
                && isContainNoOtherFlag(parameters, FLAG_GOAL_INDEX, FLAG_END_DATE);
    }

    private static boolean isChangeHabitName(String[] parameters) {
        return isContainFlag(parameters, FLAG_GOAL_INDEX) && isContainFlag(parameters, FLAG_HABIT_INDEX)
                && isContainFlag(parameters, FLAG_NAME);
    }

    private static boolean isChangeHabitInterval(String[] parameters) {
        return isContainFlag(parameters, FLAG_GOAL_INDEX) && isContainFlag(parameters, FLAG_HABIT_INDEX)
                && isContainFlag(parameters, FLAG_INTERVAL);
    }

    private static boolean isContainFlag(String[] parameters, String flag) {
        for (String param : parameters) {
            if (param.contains(flag)) {
                return true;
            }
        }
        return false;
    }

    //todo can consider overloading to accommodate three flags rather than just two
    /**
     * Ensures no excess parameters are given.
     * Excess parameters given can happen when user wrongly uses a command.
     * e.g. mixing up "update" command for "change" command, adding an extra habit index parameter.
     * firstFlag and secondFlag are the two flags that is expected from the user.
     *
     * @param parameters Array with parameters from user.
     * @param firstFlag First expected flag from user.
     * @param secondFlag Second expected flag from user.
     * @return True if there is no excess parameters in user input. False otherwise.
     */
    private static boolean isContainNoOtherFlag(String[] parameters, String firstFlag, String secondFlag) {

        for (String param : parameters) {
            if (param.contains(FLAG_GOAL_INDEX) && isExcessFlag(FLAG_GOAL_INDEX, firstFlag, secondFlag)) {
                return false;
            } else if (param.contains(FLAG_NAME) && isExcessFlag(FLAG_NAME, firstFlag, secondFlag)) {
                return false;
            } else if (param.contains(FLAG_GOAL_TYPE) && isExcessFlag(FLAG_GOAL_TYPE, firstFlag, secondFlag)) {
                return false;
            } else if (param.contains(FLAG_INTERVAL) && isExcessFlag(FLAG_INTERVAL, firstFlag, secondFlag)) {
                return false;
            } else if (param.contains(FLAG_START_DATE) && isExcessFlag(FLAG_START_DATE, firstFlag, secondFlag)) {
                return false;
            } else if (param.contains(FLAG_END_DATE) && isExcessFlag(FLAG_END_DATE, firstFlag, secondFlag)) {
                return false;
            } else if (param.contains(FLAG_HABIT_INDEX) && isExcessFlag(FLAG_HABIT_INDEX, firstFlag, secondFlag)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares a reference flag to two other flags to see if reference flag is identical to either of the two flags.
     *
     * @param referenceFlag Flag used to compare to firstFlag and secondFlag.
     * @param firstFlag First expected flag from user.
     * @param secondFlag Second expected flag from user.
     * @return True if referenceFlag is not equal to the 2 expected flags. False otherwise.
     */
    private static boolean isExcessFlag(String referenceFlag, String firstFlag, String secondFlag) {
        if (referenceFlag.equals(firstFlag) || referenceFlag.equals(secondFlag)) {
            return false;
        }
        return true;
    }

    private static Date getDate(String[] parameters) throws HaBitParserException {
        String strEndDate = getParameter(parameters, FLAG_END_DATE);
        if (strEndDate == null || strEndDate.equals(FLAG_END_DATE)) {
            throw new HaBitParserException(ERROR_GOAL_END_DATE_FORMAT);
        }
        return stringToDate(strEndDate.substring(FLAG_LENGTH));
    }

    /**
     * Converts a string formatted date into a Date object.
     *
     * @param strDate Date written in String format
     * @return Date object of strDate
     * @throws HaBitParserException If the String Date fails to be parsed
     */
    private static Date stringToDate(String strDate) throws HaBitParserException {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        formatter.setLenient(false);
        try {
            return formatter.parse(strDate);
        } catch (ParseException e) {
            throw new HaBitParserException(ERROR_GOAL_END_DATE_NON_DATE);
        }
    }

}
