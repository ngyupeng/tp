package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Consolidates all the student's phone number.
 */
public class ConsolidateCommand extends Command {

    public static final String COMMAND_WORD = "consolidate";

    public static final String MESSAGE_SUCCESS = "Consolidate all distinct students' data: \n";

    private static Logger logger = Logger.getLogger(ConsolidateCommand.class.getName());

    /**
     * Lists the categories whose data will be consolidated under ConsolidateCommand.
     */
    public enum ConsolidateCategory {
        NAME, PHONE, EMAIL, ADDRESS
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        logger.log(Level.INFO, "start executing consolidate command");
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        if (model.isPersonEmptyAddressBook()) {
            throw new CommandException(Messages.specifyEmptyUserListMessage(COMMAND_WORD));
        }

        ObservableList<Person> persons = model.getAddressBook().getPersonList();
        List<String> categoryCompilation = new ArrayList<>();
        ConsolidateCategory[] categories = ConsolidateCategory.values();

        for (ConsolidateCategory category : categories) {
            logger.log(Level.INFO, "consolidating " + category);
            categoryCompilation.add(this.consolidateData(category, persons));
        }

        String fullCompilation = String.join("\n\n", categoryCompilation);

        return new CommandResult(MESSAGE_SUCCESS + fullCompilation);
    }

    /**
     * Extracts the data of the specified category of the specified person.
     *
     * @param category Category of the data to be extracted.
     * @param person Person whose data to be extracted.
     * @return Value of data under specified category of specified person.
     */
    public static String getData(ConsolidateCategory category, Person person) {
        requireNonNull(person);
        requireNonNull(category);

        if (category == ConsolidateCategory.NAME) {
            return person.getName().toString();
        } else if (category == ConsolidateCategory.PHONE) {
            return person.getPhone().toString();
        } else if (category == ConsolidateCategory.EMAIL) {
            return person.getEmail().toString();
        } else if (category == ConsolidateCategory.ADDRESS) {
            return person.getAddress().toString();
        } else {
            return "No such category.";
        }
    }

    /**
     * Returns the header that specify what category the consolidated data fall under.
     *
     * @param category Category of the consolidated data.
     * @return Header.
     */
    public static String formatCategoryHeader(ConsolidateCategory category) {
        String categoryName = "";
        if (category == ConsolidateCategory.NAME) {
            categoryName = "name";
        } else if (category == ConsolidateCategory.PHONE) {
            categoryName = "phone";
        } else if (category == ConsolidateCategory.EMAIL) {
            categoryName = "email";
        } else if (category == ConsolidateCategory.ADDRESS) {
            categoryName = "address";
        } else {
            // do nothing
        }
        assert !categoryName.isEmpty() : "categoryName cannot be empty";
        return "- List of students' " + categoryName + ": ";
    }

    private String consolidateData(ConsolidateCategory category, ObservableList<Person> persons) {
        assert !persons.isEmpty() : "Only when there are persons data stored, "
                + "do we call consolidateData function";

        // Get everyone's data pertaining to a particular category, and add to the container
        HashSet<String> container = new HashSet<>();
        for (Person person : persons) {
            String dataOfPerson = this.getData(category, person);
            container.add(dataOfPerson);
        }

        // Sort everyone's data
        ArrayList<String> list = new ArrayList<>(container);
        Collections.sort(list);

        // Compile everyone's data into string spanning multiple line
        String compilation = "";
        for (String s : list) {
            compilation += "\n" + s;
        }

        // Add a header specifying the category to the compiled data
        return formatCategoryHeader(category) + compilation;
    }
}
