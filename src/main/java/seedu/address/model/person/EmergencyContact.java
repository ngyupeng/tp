package seedu.address.model.person;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person's emergency contact in the address book.
 * Guarantees: immutable; fields name, phone are valid as declared in {@link Name#isValidName},
 * {@link Phone#isValidPhone} respectively.
 * Note that any fields may be null. This is because EmergencyContact class is used in places like EditPersonDescriptor,
 * where it is perfectly valid to have null fields.
 * It is only important for EmergencyContact to have all null fields / all non-null fields when it is used in the
 * Person class, hence we maintain that invariant in Person class instead.
 */
public class EmergencyContact {

    public final Name name;
    public final Phone phone;

    /**
     * Constructs an {@code EmergencyContact}.
     *
     * @param name  A (possibly null) valid name.
     * @param phone A (possibly null) valid phone number.
     */
    public EmergencyContact(String name, String phone) {
        this.name = name == null ? null : new Name(name);
        this.phone = phone == null ? null : new Phone(phone);
    }

    /**
     * Constructs an {@code EmergencyContact}.
     *
     * @param name  A valid name.
     * @param phone A valid phone number.
     */
    public EmergencyContact(Name name, Phone phone) {
        this.name = name;
        this.phone = phone;
    }

    /**
     * Constructs an empty {@code EmergencyContact}.
     */
    public EmergencyContact() {
        this.name = null;
        this.phone = null;
    }

    /**
     * Clones another {@code EmergencyContact}.
     */
    public EmergencyContact(EmergencyContact emergencyContact) {
        this.name = emergencyContact.name;
        this.phone = emergencyContact.phone;
    }

    /**
     * Returns a new emergency contact, which is the result of applying the edits to this emergency contact.
     * Null fields in {@code edits} are treated as no change to the field, but if all fields are null, then this is
     * treated as deleting the emergency contact. If {@code edits} is null, this does not modify anything.
     */
    public EmergencyContact merge(EmergencyContact edits) {
        if (edits == null) {
            return new EmergencyContact(this);
        }
        if (edits.isEmpty()) {
            return new EmergencyContact();
        }

        String editedName = null;
        if (edits.name != null) {
            editedName = edits.name.fullName;
        } else if (this.name != null) {
            editedName = this.name.fullName;
        }

        String editedPhone = null;
        if (edits.phone != null) {
            editedPhone = edits.phone.value;
        } else if (this.phone != null) {
            editedPhone = this.phone.value;
        }

        return new EmergencyContact(editedName, editedPhone);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("name", name).add("phone", phone).toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EmergencyContact)) {
            return false;
        }

        EmergencyContact otherContact = (EmergencyContact) other;
        // name and phone might be null, so we cannot call .equal() on them.
        return Objects.equals(name, otherContact.name) && Objects.equals(phone, otherContact.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone);
    }

    /**
     * Returns true if every field of this emergency contact is not null.
     */
    public boolean isPresent() {
        return this.name != null && this.phone != null;
    }

    /**
     * Returns true if every field of this emergency contact is null.
     */
    public boolean isEmpty() {
        return this.name == null && this.phone == null;
    }
}
