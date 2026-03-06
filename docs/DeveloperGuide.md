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

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

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

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

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
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

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

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


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

(For all use cases below, the **System** is `EduConnect` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - Add contact (Must have)**

**Preconditions**

* EduConnect is running.

**Guarantees**

* A new contact is saved in the contact list.

**MSS**

1.  User enters an add command with `/name` and optional `/hp` and `/address` fields.
2.  EduConnect validates the input.
3.  EduConnect adds the contact.
4.  EduConnect shows a success message with the added contact details.

    Use case ends.

**Extensions**

* 1a. User omits the `/name` field.
  * 1a1. EduConnect shows an error message and the expected command format.

    Use case ends.

* 1b. User provides an empty name.
  * 1b1. EduConnect shows an error message.

    Use case ends.

* 1c. User provides invalid field values (e.g., invalid phone format).
  * 1c1. EduConnect shows an error message for the invalid field.

    Use case ends.

* 1d. User repeats the same field more than once.
  * 1d1. EduConnect shows an error message.

    Use case ends.

* 3a. The new contact duplicates an existing contact.
  * 3a1. EduConnect shows a duplicate contact error.

    Use case ends.

**Use case: UC02 - Delete contact (Must have)**

**Preconditions**

* Contact list is displayed.

**Guarantees**

* The selected contact is removed from the contact list.

**MSS**

1.  User enters a `del` command with an index.
2.  EduConnect validates the index against the displayed list.
3.  EduConnect deletes the selected contact.
4.  EduConnect shows a success message with deleted contact details.

    Use case ends.

**Extensions**

* 1a. User does not provide an index.
  * 1a1. EduConnect shows an error message.

    Use case ends.

* 1b. User provides too many arguments.
  * 1b1. EduConnect shows an error message.

    Use case ends.

* 2a. The given index is not an integer.
  * 2a1. EduConnect shows an invalid index error.

    Use case ends.

* 2b. The given index is outside the valid range.
  * 2b1. EduConnect shows an invalid index range error.

    Use case ends.

* 2c. The contact list is empty.
  * 2c1. EduConnect shows a message that there are no contacts to delete.

    Use case ends.

**Use case: UC03 - Tag contact to categorize (Must have)**

**Preconditions**

* At least one contact exists in the current list view.

**Guarantees**

* The selected contact's category tag is set to one of: `Student`, `Parent`, `Tutor`.

**MSS**

1.  User enters a `tag` command with `/index` and `/tag`.
2.  EduConnect validates the index and tag value.
3.  EduConnect applies the new tag to the selected contact.
4.  EduConnect shows a success message.

    Use case ends.

**Extensions**

* 1a. User omits `/index` or `/tag`.
  * 1a1. EduConnect shows an error message and expected format.

    Use case ends.

* 1b. User leaves `/index` or `/tag` empty.
  * 1b1. EduConnect shows an error message.

    Use case ends.

* 1c. User repeats `/index` or `/tag`.
  * 1c1. EduConnect shows an error message.

    Use case ends.

* 2a. The index is invalid or out of range.
  * 2a1. EduConnect shows an invalid index error.

    Use case ends.

* 2b. The tag is not one of `Student`, `Parent`, `Tutor`.
  * 2b1. EduConnect shows an invalid tag error.

    Use case ends.

* 3a. The selected contact already has a tag.
  * 3a1. EduConnect overwrites the existing tag with the new tag and shows a success message.

    Use case ends.

**Use case: UC04 - View phone number and address (Must have)**

**MSS**

1.  User enters `list` (or `view`) to display all contacts.
2.  EduConnect shows each contact's name, phone number and address.

    Use case ends.

**Extensions**

* 2a. The contact list is empty.
  * 2a1. EduConnect shows that no contacts are currently available.

    Use case ends.

* 2b. A contact has missing phone number or address.
  * 2b1. EduConnect shows a clear missing-field indicator for that field.

    Use case ends.

* 2c. Multiple contacts have the same name and tag.
  * 2c1. EduConnect displays all matching contacts and lets the user choose the intended contact.

    Use case ends.

**Use case: UC05 - Search contacts by name (Nice-to-have)**

**MSS**

1.  User enters `find` with one or more keywords.
2.  EduConnect filters contacts whose names match at least one keyword.
3.  EduConnect shows the filtered results and count.

    Use case ends.

**Extensions**

* 1a. User does not provide any keyword.
  * 1a1. EduConnect shows an error message.

    Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Duplicate contacts**: Two contacts are said to be duplicates if they have an identical set of details, across all fields

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
