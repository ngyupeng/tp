package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.MessageCenter;
import seedu.address.model.role.Role;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    public static final String EMERGENCY_MESSAGE_CONSTRAINTS =
            "Emergency contact must be either not provided, or contain both name and phone (ecn/ and ecp/).";
    public static final String EMERGENCY_NUMBER_MESSAGE_CONSTRAINTS =
            "Emergency contact's phone number should not be the same as this person's.";
    public static final String EMERGENCY_NAME_WARNING =
            "Note: Emergency contact of %1$s has the same name as them, please check that this is not a mistake.";

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Role> roles = new HashSet<>();
    private final Set<Tag> tags = new HashSet<>();
    private final EmergencyContact emergencyContact;
    private final EnrollmentYear enrollmentYear;
    private final Pin pin;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Pin pin, Set<Role> roles, Set<Tag> tags,
                  EmergencyContact emergencyContact, EnrollmentYear enrollmentYear) {
        requireAllNonNull(name, phone, email, address, pin, tags, emergencyContact);
        checkValidPerson(name, phone, emergencyContact);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.pin = pin;
        this.roles.addAll(roles);
        this.tags.addAll(tags);
        this.emergencyContact = emergencyContact;
        this.enrollmentYear = enrollmentYear;
    }

    /**
     * Checks if the given parameters form a valid Person.
     *
     * @param phone            Student's phone number.
     * @param emergencyContact Student's emergency contact.
     * @throws IllegalArgumentException through checkArgument if person is invalid.
     */
    public static void checkValidPerson(Name name, Phone phone, EmergencyContact emergencyContact) {
        checkArgument(emergencyContact.isPresent() || emergencyContact.isEmpty(), EMERGENCY_MESSAGE_CONSTRAINTS);
        if (emergencyContact.isPresent()) {
            checkArgument(!emergencyContact.phone.equals(phone), EMERGENCY_NUMBER_MESSAGE_CONSTRAINTS);
            if (emergencyContact.name.equals(name)) {
                MessageCenter.appendEnd(String.format(EMERGENCY_NAME_WARNING, name));
            }
        }
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Pin getPin() {
        return pin;
    }

    /**
     * Returns an immutable role set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public EmergencyContact getEmergencyContact() {
        assert emergencyContact.isPresent() || emergencyContact.isEmpty()
                : "emergency contact cannot be partially filled";
        return emergencyContact;
    }

    public EnrollmentYear getEnrollmentYear() {
        return enrollmentYear;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && roles.equals(otherPerson.roles)
                && tags.equals(otherPerson.tags)
                && pin.equals(otherPerson.pin)
                && enrollmentYear.equals(otherPerson.enrollmentYear);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, roles, tags, enrollmentYear);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("roles", roles)
                .add("tags", tags)
                .add("pin", pin)
                .add("emergencyContact", emergencyContact)
                .add("enrollmentYear", enrollmentYear)
                .toString();
    }

}
