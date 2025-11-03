package seedu.address.storage;

import static seedu.address.model.role.Role.PERSON_MAX_ROLES;
import static seedu.address.model.tag.Tag.PERSON_MAX_TAGS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.MessageCenter;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.EnrollmentYear;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Pin;
import seedu.address.model.role.Role;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    public static final String SIZE_LIMIT_EXCEEDED_FORMAT = "Person's %s count exceeds the maximum size of %d!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String enrollmentYear;
    private final Boolean pin;
    private final JsonAdaptedEmergencyContact emergencyContact;
    private final List<JsonAdaptedRole> roles = new ArrayList<>();
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email, @JsonProperty("address") String address,
                             @JsonProperty("pin") Boolean pin,
                             @JsonProperty("emergencyContact") JsonAdaptedEmergencyContact emergencyContact,
                             @JsonProperty("enrollmentYear") String enrollmentYear,
                             @JsonProperty("roles") List<JsonAdaptedRole> roles,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.pin = pin;
        if (emergencyContact != null) {
            this.emergencyContact = emergencyContact;
        } else {
            this.emergencyContact = new JsonAdaptedEmergencyContact(new EmergencyContact());
        }
        if (enrollmentYear != null) {
            this.enrollmentYear = enrollmentYear;
        } else {
            this.enrollmentYear = "";
        }
        if (roles != null) {
            this.roles.addAll(roles);
        }
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        pin = source.getPin().value;
        if (source.getEmergencyContact().isPresent()) {
            emergencyContact = new JsonAdaptedEmergencyContact(source.getEmergencyContact());
        } else {
            emergencyContact = new JsonAdaptedEmergencyContact(new EmergencyContact());
        }
        enrollmentYear = source.getEnrollmentYear().toString();
        roles.addAll(source.getRoles().stream()
                .map(JsonAdaptedRole::new)
                .collect(Collectors.toList()));
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Role> personRoles = new ArrayList<>();
        for (JsonAdaptedRole role : roles) {
            personRoles.add(role.toModelType());
        }
        if (personRoles.size() > PERSON_MAX_ROLES) {
            throw new IllegalValueException(
                    String.format(SIZE_LIMIT_EXCEEDED_FORMAT, Role.class.getSimpleName(), PERSON_MAX_ROLES));
        }

        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }
        if (personTags.size() > PERSON_MAX_TAGS) {
            throw new IllegalValueException(
                    String.format(SIZE_LIMIT_EXCEEDED_FORMAT, Tag.class.getSimpleName(), PERSON_MAX_TAGS));
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (pin == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Pin.class.getSimpleName()));
        }

        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }

        MessageCenter.appendEnd(Name.getStyleWarningMessage(name));
        MessageCenter.appendEnd(Address.getStyleWarningMessage(address));

        final Name modelName = new Name(name);
        final Phone modelPhone = new Phone(phone);
        final Email modelEmail = new Email(email);
        final Address modelAddress = new Address(address);
        final Pin modelPin = new Pin(pin);
        final EmergencyContact modelEmergencyContact = new EmergencyContact(emergencyContact.toModelType());
        final EnrollmentYear modelEnrollmentYear = new EnrollmentYear(enrollmentYear);
        final Set<Role> modelRoles = new HashSet<>(personRoles);
        final Set<Tag> modelTags = new HashSet<>(personTags);
        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelPin,
                modelRoles, modelTags, modelEmergencyContact, modelEnrollmentYear);
    }

}
