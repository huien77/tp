package happybit.parser;

import happybit.command.HelpCommand;
import happybit.command.ListHabitsCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputParserTest {
    private static final String COMMAND_LIST_GREETING =
            "Hello! These are all the possible commands for this habit tracker :)";
    private static final String ADD_HABIT_COMMAND =
            "add a habit: add <habit name>";
    private static final String DELETE_HABIT_COMMAND =
            "delete a habit: delete <habit name>";
    private static final String SET_GOAL_COMMAND =
            "set a goal for a habit: set <habit name> <goal name>";
    private static final String REMOVE_GOAL_COMMAND =
            "remove a goal for a habit: remove <habit name> <goal name>";
    private static final String LIST_HABIT_COMMAND =
            "list all habits user has input: list";
    private static final String LIST_GOAL_COMMAND =
            "list all goals for that habit: list -<habit name>";
    private static final String NEWLINE = System.lineSeparator();
    private static final String DASHES = "______________________________________"
            + "__________________________________________________________________________________";

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void parseInput_inputHelp_printCommandList() {
        final HelpCommand helpCommand = new HelpCommand();
        final InputParser iP = new InputParser();
        String input = "help";
        String expectedOutput = DASHES + NEWLINE
                + COMMAND_LIST_GREETING + NEWLINE
                + ADD_HABIT_COMMAND + NEWLINE
                + DELETE_HABIT_COMMAND + NEWLINE
                + SET_GOAL_COMMAND + NEWLINE
                + REMOVE_GOAL_COMMAND + NEWLINE
                + LIST_HABIT_COMMAND + NEWLINE
                + LIST_GOAL_COMMAND + NEWLINE
                + DASHES + NEWLINE;
        iP.parseInput(input);
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    @Test
    void parseInput_inputList_printHabitList() {
        final ListHabitsCommand listHabits = new ListHabitsCommand();
        final InputParser iP = new InputParser();
        String input = "list";
        String expectedOutput = DASHES + NEWLINE
                + "Habit 1" + NEWLINE
                + "Habit 2" + NEWLINE
                + "Habit 3" + NEWLINE
                + "Habit 4" + NEWLINE
                + DASHES + NEWLINE;
        iP.parseInput(input);
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}