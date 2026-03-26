---
layout: page
title: User Guide
---

EduConnect is a **desktop application that enables private tutors to manage their work contacts, optimized for use via a Command Line Interface (CLI)** while still having the benefits of a Graphical User Interface (GUI). If you can type fast, EduConnect can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103-F09-1/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for EduConnect.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar educonnect.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `del 3` : Deletes the contact with an `ID` of 3.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/CATEGORY]` can be used as `n/John Doe t/Student` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/CATEGORY]…​` can be used as ` ` (i.e. 0 times), `t/Student`, `t/Student t/Parent` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME [p/PHONE_NUMBER] [a/ADDRESS] [t/CATEGORY]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of categories (including 0)
</div>

Examples:
* `add n/John Doe p/98765432 a/John street, block 123, #01-01`
* `add n/Jane Doe p/98765432`
* `add n/Betsy Crowe t/Student a/Newgate Prison p/1234567`

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

* Since phone number and address fields are optional, the UI alerts the user if a particular person has no phone number or address:

  ![result for 'list' with no phone number and address](images/missingPhoneNumberAndAddress.png)

### Editing a person : `edit`

Edits an existing person in the address book.

Format: `edit ID [n/NAME] [p/PHONE] [a/ADDRESS] [t/CATEGORY]…​`

* Edits the person with the specified `ID`. `ID` **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* Use this command for all category updates. EduConnect does not provide a separate `tag` command.
* When editing tags, the provided categories will be appended to the person’s existing tags.
* Only the supported categories may be used as edit tags: `Student`, `Parent`, `Tutor`.
* Repeating an existing category has no effect because duplicate categories are not stored.
* You can remove all the person’s tags by typing `t/` without specifying any category after it.
* `t/` must be used on its own. Do not combine `t/` with category values in the same command.

Examples:
*  `edit 1 p/91234567` Edits the phone number of the person with `ID` 1, changing it to `91234567`.
*  `edit 2 t/Parent` Appends the tag `Parent` to the person with `ID` 2.
*  `edit 2 t/Parent t/Tutor` Appends both `Parent` and `Tutor` to the person with `ID` 2.
*  `edit 2 n/Betsy Crower t/` Edits the name of the person with `ID` 2, changing it to `Betsy Crower`, whilst clearing all existing tags.

### Locating persons: `find`

Finds persons whose name, address, or phone number contains any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]...`
or `find [n/NAME] [a/ADDRESS] [p/PHONE]`

* The search is case-insensitive for names and addresses. e.g. `hans` will match `Hans`
* Partial matches are supported. e.g. `Han` will match `Hans`
* If no prefixes are provided, all supported fields are searched.
* `n/` restricts the search to names only.
* `a/` restricts the search to addresses only.
* `p/` restricts the search to phone numbers only.
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Ali Clementi` will return persons whose name, address, or phone number contains either `Ali` or `Clementi`
* If prefixes are used, all search terms must be prefixed.

Examples:
* `find Ali` returns persons whose name, address, or phone number contains `Ali`
* `find a/119224` returns persons whose address contains `119224`
* `find n/Clement` returns persons whose name contains `Clement`
* `find p/9435` returns persons whose phone number contains `9435`
* `find n/aleX a/seran` returns persons whose name contains `aleX` or whose address contains `seran`
  ![result for 'find n/aleX a/seran'](images/find_AlexSeranResult.png)

Notes:
- If no prefixes are provided, the search is performed across all supported fields.
- If prefixes are used, all search terms must be prefixed.

### Deleting a person : `del`

Deletes the specified person from the address book.

Format: `del ID`

* Deletes the person with the specified `ID`.
* `ID` **must be a positive integer** 1, 2, 3, …​

Examples:
* `del 2` deletes the person with `ID` 2 from the address book.
* `find Betsy` followed by `del 1` deletes the person with `ID` 1 from the address book. Note that it does not delete the first person in the results of the `find` command.
* `add n/Andrew` followed by `del 1` deletes the person with `ID` 1 from the address book. Note that it does not delete the contact that was just added.
* `add n/Andrew` followed by `del 1` will fail if there is no person with `ID` 1 in the address book.

### Copying a person information; `copy`

Copy a specified field of a person from the address book to the user clipboard.

Format: `copy ID field`

* Copies the `field` data for the person with the specified `ID` to the user clipboard.
* Possible fields include `n/` for name, `p/` for phone number, and `a/` for address
* If the person's field is empty, then nothing will be copy to the clipboard.

Examples:
* `copy 6 n/` copy the name of the person with `ID` 6 to the clipboard.
* `copy 7 p/` copy the phone number of the person with `ID` 7 to the clipboard.
* `copy 9 a/` copy the address of the person with `ID` 9 to the clipboard .
* `copy 1 p/` will fail if `ID` 1 is not found or the phone number field of the person with `ID` 1 is empty. 

### Clearing all entries : `clear`

Clears all entries from the address book, whilst displaying all the contacts that have been removed.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

EduConnect data is saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

EduConnect data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, EduConnect will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause EduConnect to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous EduConnect home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME [p/PHONE_NUMBER] [a/ADDRESS] [t/CATEGORY]…​` <br> e.g., `add n/James Ho p/22224444 a/123, Clementi Rd, 1234665 t/Student`
**Clear** | `clear`
**Delete** | `del ID`<br> e.g., `del 3`
**Edit** | `edit ID [n/NAME] [p/PHONE_NUMBER] [a/ADDRESS] [t/CATEGORY]…​`<br> e.g.,`edit 2 t/Parent t/Tutor`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List** | `list`
**Help** | `help`
