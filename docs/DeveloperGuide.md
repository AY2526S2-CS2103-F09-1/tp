---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).
* Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://github.com/junit-team/junit5)

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `del 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("del 1")` API call as an example.

![Interactions Inside the Logic Component for the `del 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2526S2-CS2103-F09-1/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**: Part-time private tutors

* Teach many students at the same time, all across Singapore
* Contact parents daily for administrative purposes (payment, scheduling of lessons, location of lessons, etc.)
* In contact with many other private tutors, to exchange teaching ideas and learning materials
* Type very fast, dislike using the mouse
* Face-blind
* Have a bad memory
* Use WhatsApp as their main mode of contact 
* Prefer quick filtering of contacts
* Value speed, efficiency, and minimal friction in workflows

**Value proposition**: To help private tutors seamlessly manage daily tasks in their work. These include contacting parents for announcements, administrative matters or emergencies. They can also involve contacting other tutors for pedagogical discussions or the exchange of learning materials.

In some cases, tutors may teach students at different paces, adapting their teaching methods to each student’s needs, especially in small classes. As a result, a tutor might handle two students at overlapping but not identical times (e.g., one student from 4–6pm and another from 5–7pm). Therefore, we allow users to add tuition timings with overlapping periods.

We also allow parents to include date and time information. This is helpful, especially for parents who wish to meet the tutor regularly (e.g., weekly) to discuss the student’s progress. Overall, we provide users with the flexibility to input the details that best suit their needs.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                                                  | I want to …​                                                           | So that I can…​                                                                                               |
|----------|--------------------------------------------------------------------------|------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| `* * *`  | tutor                                                                    | add new contacts to EduConnect                                         | track the contact details of relevant people involved in my work - such as students, parents and other tutors |
| `* * *`  | clumsy tutor                                                             | delete existing contacts from EduConnect                               | remove details that have been entered incorrectly                                                             |
| `* * *`  | tutor who teaches many students and is in contact with many other tutors | categorise my contact list into 3 groups - students, parents and tutors | filter for the required group more quickly                                                                    |
| `* * *`  | tutor                                                                    | view a student’s home address in EduConnect                            | navigate to the correct location for lessons                                                                  |
| `* *`    | tutor who has many clients                                               | search for a specific contact by name                                       | more swiftly obtain the contact information of the person in question                                         |
| `* *`    | tutor                                                                    | quickly retrieve a student’s phone number from EduConnect and copy it                   | contact the student via WhatsApp without searching manually                                                   |
| `* *`    | tutor whose students’ or parents’ phone numbers change                                                              | easily update their contact details           | avoid contacting the wrong number in the future                                                               |
| `* *`    | tutor who has online lessons       | access an online meeting link for the lesson on EduConnect and copy it into my clipboard with a click    | start the lesson quickly     |
| `*`    | tutor who values efficiency       | learn about EduConnect’s basic controls through a beginner’s tutorial         | pick up the app more quickly       |
| `*`    | face-blind tutor | add pictures of my students to their names and contact details | remember how each of my students look like    |
| `*`    | tutor who teaches many students | pair up the contact details of a student with those of his / her parents | remember who are the parents of a given student |
| `*`    | tutor | save frequently used commands as shortcuts | perform common commands more quickly |
| `*`    | tutor | view shortcuts I added | look it up when I forget |
| `*`    | tutor | edit and remove those shortcuts I added if they become irrelevant | prevent shortcuts from clustering too much or I need to change it to something more convenient |

*{More to be added}*

### Use cases

System: EduConnect

#### Use case: UC01 - Add Contact

Actor: User

Guarantees:
* On successful completion, exactly one new contact is stored.
* If the operation fails, the stored contacts remain unchanged.

MSS:
1. User requests to add a contact by providing a name (required) and any optional fields (phone number, address, weekly timeslot, remark, meeting link, tags).
2. EduConnect validates the provided details.
3. EduConnect adds the contact.
4. EduConnect shows a success message with the added contact details.
Use case ends.

Extensions:
* 1a. User omits a required detail, or provides an empty required detail.
  * 1a1. EduConnect shows an error message and input guidance.
  * 1a2. User re-enters the contact details.
  * Steps 1a1-1a2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 2a. User provides an invalid format for at least one of the fields.
  * 2a1. EduConnect shows an error message.
  * 2a2. User re-enters the contact details.
  * Steps 2a1-2a2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 2b. User provides the same field more than once.
  * 2b1. EduConnect shows an error message.
  * 2b2. User re-enters the contact details.
  * Steps 2b1-2b2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 2c. The new contact is a duplicate of an existing contact.
  * 2c1. EduConnect shows a duplicate contact error.
  * 2c2. User re-enters the contact details.
  * Steps 2c1-2c2 are repeated until valid input is provided.
  * Use case resumes from step 2.

#### Use case: UC02 - Delete Contact
Actor: User

Guarantees:
* On successful completion, all contacts with the specified ID(s) are removed from the stored contacts.
* If any ID is invalid or not found, no contacts are deleted.
* If the operation fails, the stored contacts remain unchanged.

MSS:
1. User requests to delete one or more contacts by specifying one or more contact IDs.
2. EduConnect validates all IDs and checks that all specified IDs exist.
3. EduConnect deletes the specified contacts.
4. EduConnect shows a success message with the deleted contact details.
Use case ends.

Extensions:
* 1a. User provides no IDs.
  * 1a1. EduConnect shows an error message.
  * 1a2. User re-submits the deletion request.
  * Steps 1a1-1a2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 2a. Any ID is not a valid positive integer.
  * 2a1. EduConnect shows an error message.
  * 2a2. User re-submits the deletion request.
  * Steps 2a1-2a2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 2b. Any ID is not found in the address book.
  * 2b1. EduConnect shows an error message.
  * 2b2. User re-submits the deletion request.
  * Steps 2b1-2b2 are repeated until valid input is provided.
  * Use case resumes from step 2.
  
#### Use case: UC03 - Update Contact Tags
Actor: User

Guarantees:
* On successful completion, the selected contact has the updated tags.
* If the operation fails, no contact is modified.

MSS:
1. User requests to edit a contact's tags.
2. EduConnect validates the contact reference and tag value.
3. EduConnect applies the requested tag additions and deletions to the selected contact.
4. EduConnect shows a success message.
Use case ends.

Extensions:
* 1a. User omits required details, or provides an empty required detail.
  * 1a1. EduConnect shows an error message and input guidance.
  * 1a2. User re-submits the edit request.
  * Steps 1a1-1a2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 1b. User provides the same required detail more than once.
  * 1b1. EduConnect shows an error message.
  * 1b2. User re-submits the edit request.
  * Steps 1b1-1b2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 2a. The contact reference is invalid or not found in the address book.
  * 2a1. EduConnect shows an error message.
  * 2a2. User re-submits the edit request.
  * Steps 2a1-2a2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 2b. The provided tag is not a valid tag.
  * 2b1. EduConnect shows an error message.
  * 2b2. User re-submits the edit request.
  * Steps 2b1-2b2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 2c. The user requests to clear all tags and also specifies one or more tags to add or remove.
  * 2c1. EduConnect shows an error message.
  * 2c2. User re-submits the edit request.
  * Steps 2c1-2c2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 2d. The user tries to add and delete the same tag in one command.
  * 2d1. EduConnect shows an error message.
  * 2d2. User re-submits the edit request.
  * Steps 2d1-2d2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 3a. The selected contact already has one or more tags.
  * 3a1. EduConnect appends any missing tags and removes any specified tags that are present.
  * Use case resumes from step 4.
* 3b. The user requests to clear all existing tags.
  * 3b1. EduConnect clears all existing tags for the selected contact.
  * Use case resumes from step 4.
* 3c. The user specifies a tag to delete that the selected contact does not have.
  * 3c1. EduConnect leaves the existing tags unchanged.
  * Use case resumes from step 4.

#### Use case: UC04 - View Contact Details
Actor: User

Guarantees:
* On successful completion, EduConnect displays the stored contacts with their name and any available optional fields (phone number, address, weekly timeslot, remark, meeting link).
* If any optional field is missing, EduConnect indicates that the field is missing.
* This use case does not modify stored contact data.

MSS:
1. User requests to view contact information.
2. EduConnect displays each contact's name and any available optional fields.
Use case ends.

Extensions:
* 2a. There are no contacts.
  * 2a1. EduConnect displays that no contacts are currently available.
  * Use case ends.
* 2b. A contact is missing one or more optional fields.
  * 2b1. EduConnect displays a missing-field indicator for that field.
  * Use case resumes from step 2.
* 2c. Multiple contacts share the same name and tag.
  * 2c1. EduConnect displays all matching contacts distinctly so the user can differentiate them.
  * Use case resumes from step 2.

#### Use case: UC05 - Edit Contact
Actor: User

Guarantees:
* On successful completion, the specified contact is updated with the provided values.
* Name, phone, address, meeting link, remark, and weekly timeslot replace their previous values when provided.
* Provided tags are added cumulatively to the contact's existing tags, unless the user explicitly requests to clear all tags.
* If the operation fails, the stored contacts remain unchanged.

MSS:
1. User requests to edit a contact by specifying the contact ID and one or more fields to update.
2. EduConnect validates the contact ID and edited field values.
3. EduConnect updates the selected contact.
4. EduConnect shows a success message with the updated contact details.
Use case ends.

Extensions:
* 1a. User omits the contact ID or all editable fields.
  * 1a1. EduConnect shows an error message and input guidance.
  * 1a2. User re-submits the edit request.
  * Steps 1a1-1a2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 1b. User repeats a non-tag field.
  * 1b1. EduConnect shows an error message.
  * 1b2. User re-submits the edit request.
  * Use case resumes from step 2.
* 2a. The contact ID is invalid or not found in the address book.
  * 2a1. EduConnect shows an error message.
  * 2a2. User re-submits the edit request.
  * Use case resumes from step 2.
* 2b. The user provides an invalid field value.
  * 2b1. EduConnect shows an error message.
  * 2b2. User re-submits the edit request.
  * Use case resumes from step 2.
* 2c. The user provides an ID with leading zeroes.
  * 2c1. EduConnect accepts the ID and interprets it as the corresponding positive integer.
  * Use case resumes from step 2.
* 3a. The user provides one or more tags.
  * 3a1. EduConnect adds those tags to the contact's existing tags.
  * Use case resumes from step 4.
* 3b. The user requests to clear all tags.
  * 3b1. EduConnect clears all tags from the contact.
  * Use case resumes from step 4.
* 3c. The user requests to clear the stored weekly timeslot.
  * 3c1. EduConnect removes the stored weekly timeslot from the contact.
  * Use case resumes from step 4.
* 3d. The user requests to clear the stored meeting link.
  * 3d1. EduConnect removes the stored meeting link from the contact.
  * Use case resumes from step 4.

#### Use case: UC06 - Copy Contact Field to Clipboard
Actor: User

Guarantees:
* On successful completion, the specified field value of the contact is copied to the system clipboard.
* If the operation fails, the clipboard remains unchanged.

MSS:
1. User requests to copy a field of a contact by specifying the contact ID and a field prefix.
2. EduConnect validates the contact ID and field prefix.
3. EduConnect retrieves the field value of the specified contact.
4. EduConnect copies the value to the clipboard and shows a success message.
Use case ends.

Extensions:
* 1a. User omits the contact ID or the field prefix.
  * 1a1. EduConnect shows an error message and input guidance.
  * 1a2. User re-submits the copy request.
  * Steps 1a1-1a2 are repeated until valid input is provided.
  * Use case resumes from step 2.
* 2a. The contact ID is not found in the address book.
  * 2a1. EduConnect shows an error message.
  * 2a2. User re-submits the copy request.
  * Use case resumes from step 2.
* 2b. The field prefix is not one of the supported fields (`n/`, `p/`, `a/`, `o/`).
  * 2b1. EduConnect shows an error message listing the valid fields.
  * 2b2. User re-submits the copy request.
  * Use case resumes from step 2.
* 3a. The specified field is empty for that contact.
  * 3a1. EduConnect shows an error message indicating there is nothing to copy.
  * Use case ends.

#### Use case: UC07 - Search Contacts by Specified Fields
Actor: User

Guarantees:
* On successful completion, EduConnect shows only those contacts which match the provided field keywords (i.e. name / address / phone number / tags / remark / weekly timeslot). EduConnect also displays the number of contacts found.
* Each matching contact appears at most once in the filtered results.
* Matching is case-insensitive across all supported fields.
* If no contacts match, EduConnect shows an empty filtered result.
* If the operation fails, the currently displayed contacts remain unchanged.

MSS:
1. User requests to search contacts by entering one or more keywords, each marked with a specific field.
2. EduConnect finds contacts whose fields match the given keywords according to the selected match mode.
3. EduConnect shows the filtered results and match count.
Use case ends.

Extensions:
* 1a. User provides no keyword.
  * 1a1. EduConnect shows an error message and requests at least one keyword.
  * 1a2. User re-enters the search input.
  * Steps 1a1-1a2 are repeated until at least one keyword is provided.
  * Use case resumes from step 2.
* 1b. User provides a keyword that is not marked with a field.
  * 1b1. EduConnect shows an error message explaining the required input format.
  * 1b2. User re-enters the search input.
  * Use case resumes from step 2.
* 2a. No contacts match the keywords.
  * 2a1. EduConnect shows empty filtered results and a count of zero.
  * Use case ends.
* 2b. A contact matches multiple keywords.
  * 2b1. EduConnect includes that contact once in the filtered results.
  * Use case ends.
* 2c. A contact does not have the field being searched.
  * 2c1. EduConnect treats that field as absent and does not match the contact on that field.
  * Use case ends.
* 2d. The user searches with flexible time formatting such as mixed `HH:mm` / `HHmm`, or with leading spaces after a prefix.
  * 2d1. EduConnect normalizes the query before matching.
  * Use case resumes from step 3.
* 2e. The user searches for partial non-English text in a free-text field such as address.
  * 2e1. EduConnect performs the usual substring match.
  * Use case resumes from step 3.

#### Use case: UC08 - Clear All Contacts
Actor: User

Guarantees:
* On successful completion, all contacts are removed from the address book.
* If the user does not confirm, no contacts are deleted and the address book contacts list stay the same.

MSS:
1. User types `clear`.
2. EduConnect shows a warning message asking the user to type `clear` again to confirm.
3. User types `clear` again.
4. EduConnect deletes all contacts and shows a success message.
Use case ends.

Extensions:
* 3a. The user types any command other than `clear` after the warning.
  * 3a1. EduConnect resets the confirmation state and handles the other command.
  * 3a2. The user types `clear` again later.
  * Use case resumes from step 1.
* 3b. The user types an invalid command after the warning.
  * 3b1. EduConnect shows the usual error message for the invalid command and resets the confirmation state.
  * 3b2. The user types `clear` again later.
  * Use case resumes from step 3.

### Non-Functional Requirements

1. Should work on any Mainstream OS (Windows, macOS, Linux) as long as it has Java 17 installed.
2. Should respond to all commands within 1 second when up to 1000 contacts are loaded, on a machine with at least 4GB of RAM available.
3. Should not require more than 200MB of total disk space, including the application and all stored data, for up to 1000 contacts.
4. Should respond to any user command within 1 second on a machine with at least 1GB of RAM available.
5. A user typing at 60 words per minute or above should be able to complete common tasks such as adding, editing, or deleting a contact in under 5 seconds, excluding tasks that require typing a meeting link URL.
6. Should not require internet connection to function.
7. A user should be able to transfer all contact data to another computer by transferring a single contact data file.
8. Should ensure no data is lost by saving all changes to disk after every command that modifies contact data.
9. The program should not crash upon reading from a corrupted contact data file.
10. The program should be able to start up within 3 seconds on a machine with at least 4GB of RAM available.
11. A new user should be able to perform basic tasks(eg. add, delete, find) within 5 minutes of reading the User Guide without any prior training.

### Glossary

* **Tutor**: Refers to a private tutor, which is a user of the EduConnect application
* **Student**: Refers to a student whom the tutor is teaching
* **Parent**: Refers to a parent of a student whom the tutor is teaching
* **Mode**: Refers to the `m/` keyword in `find`, which controls how multiple search conditions are combined (OR by default; AND when specified)
* **Phone Number**: Refers to a Singapore phone number (8 digits, typically starting with 6/8/9)
* **Address**: Refers to a Singapore address (e.g. block + street + unit + postal code format)
* **Weekly timeslot**: A contact's stored weekly timeslot, represented as a `Time` value in the canonical format `Day HH:mm` or `Day HH:mm - HH:mm`
* **Meeting link**: A URL (starting with `http://` or `https://`) stored against a contact for online lessons (e.g. a Zoom or Google Meet link. This field is optional and technically any link starting with `http://` or `https://` can be stored here)
* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Duplicate contacts**: Two contacts are said to be duplicates if they have the same name, phone number, and address (name and address comparisons are case-insensitive)
* **CLI (Command Line Interface):** A text-based way of interacting with the application by typing commands into a terminal or command prompt, instead of clicking buttons or menus.
* **GUI (Graphical User Interface):** The visual, point-and-click interface of the application. What you see and interact with on screen, such as buttons, text fields, and windows.
* **JSON (JavaScript Object Notation):** A plain-text file format used to store and organise data in a structured way. It is the format used by this application to save your data.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file.<br>
      Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `del 1`<br>
      Expected: The contact with `ID` 1 is deleted from the address book. Details of the deleted contact shown as a person card. Timestamp in the status bar is updated.

   1. Test case: `del 1 2`<br>
      Expected: Both contacts with `ID` 1 and `ID` 2 are deleted together. If any ID is invalid, none are deleted.

   1. Test case: `del 3 3` (duplicate ID)<br>
      Expected: The contact with `ID` 3 is deleted similar to doing just `del 3`. Duplicate IDs are removed before the deletion starts.

   1. Test case: `del 6 6 7 7` (multiple duplicate IDs)<br>
      Expected: Contacts with `ID` 6 and `ID` 7 are deleted similar to just doing `del 6 7`.

   1. Test case: `del 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Test case: `del 01`<br>
      Expected: The same contact as `del 1` is deleted.

   1. Other incorrect delete commands to try: `del`, `del this`, `del -1`, `del x`, `...` (where `x` is not found in the address book)<br>
      Expected: Similar to previous.

### Copying a contact field

1. Copying a field while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `copy 1 n/`<br>
      Expected: The name of contact `ID` 1 is copied to the clipboard. Success message is then shown.

   1. Test case: `copy 1 p/`<br>
      Expected: The phone number of contact `ID` 1 is copied to the clipboard. Success message is then shown. If the contact has no phone number stored, an error message is shown instead.

   1. Test case: `copy 1 a/`<br>
      Expected: The address of contact `ID` 1 is copied to the clipboard. Success message is then shown. If the contact has no address stored, an error message is shown instead.

   1. Test case: `copy 1 o/`<br>
      Expected: The meeting link of contact `ID` 1 is copied to the clipboard. Success message is then shown. If the contact has no meeting link, an error message is shown instead.

   1. Test case: `copy 1 email` (invalid field)<br>
      Expected: Error message shown listing the valid fields. Nothing is copied to clipboard.

   1. Test case: `copy 0 p/` (invalid ID)<br>
      Expected: Error message shown. Nothing is copied to clipboard.

   1. Test case: `copy 6767 p/` (ID not in address book)<br>
      Expected: Error message shown. Nothing is copied to clipboard.

   1. Test case: `copy 1 p/ extraArg` (extra argument)<br>
      Expected: Error message shown indicating the format is invalid. Nothing is copied to clipboard.

### Clearing all contacts

1. Clearing contacts using the two-step confirmation flow

   1. Prerequisites: Multiple contacts exist in the address book.

   1. Test case: `clear`<br>
      Expected: No contacts are deleted. A warning message is shown asking the user to type `clear` again to confirm.

   1. Test case: `clear` then `clear`<br>
      Expected: All contacts are deleted and the UI is refreshed to show an empty list.

   1. Test case: `clear` then any other command (including an invalid command) then `clear`<br>
      Expected: The second `clear` shows the warning again (the confirmation is reset).

### Finding contacts

1. Finding contacts by weekly timeslot (`d/`)

   1. Prerequisites: Ensure there is at least one contact with a stored weekly timeslot, e.g. `edit 1 d/Friday 17:00`.

   1. Test case: `find d/fri`<br>
      Expected: Shows contacts whose stored weekly timeslot is on Friday.

   1. Test case: `find d/11:00-1200`<br>
      Expected: EduConnect accepts the mixed time format and normalizes it to the same matching behavior as `find d/11:00 - 12:00`.

   1. Test case: `find d/1630-1730`<br>
      Expected: Shows contacts whose stored weekly timeslot is a single time within `16:30 - 17:30` (e.g. `17:00`).

   1. Test case: `find d/1500-1600` (when a contact has `Sunday 15:00 - 15:56` stored)<br>
      Expected: Shows the contact only if the stored duration is an exact match (`15:00 - 16:00`); a stored duration like `15:00 - 15:56` will not match.

### Parser behavior notes

* Argument values are trimmed after tokenization, so spaces immediately after a valid prefix are ignored.
* Prefixes remain lowercase and case-sensitive; capitalized forms such as `N/` and `T/` are rejected.

### Saving data

1. Dealing with a missing data file

   1. Prerequisites: Close the app.

   1. Delete the data file `[JAR file location]/data/educonnect.json` (if it exists).

   1. Launch the app.<br>
      Expected: The app starts with sample data. The data file will be recreated the next time data is saved (after any command that modifies contacts).

1. Dealing with a corrupted data file

   1. Prerequisites: Close the app.

   1. Corrupt the data file `[JAR file location]/data/educonnect.json` (e.g. make it invalid JSON or set an invalid field value).

   1. Launch the app.<br>
      Expected: The app starts with an empty contact list. The file will be overwritten the next time data is saved (after any command that modifies contacts).
