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

   * `list`: List all contacts.

   * `add n/John Doe p/98765432 a/1A Kent Ridge Rd, 119224`: Add a contact named `John Doe` with a phone number `98765432` and address `1A Kent Ridge Rd, 119224` to the Address Book.

   * `del 3`: Delete the contact with an `ID` of 3.

   * `clear`: Delete all contacts.

   * `exit`: Exit the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/Student` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/Student`, `t/Student t/Parent` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help: `help`

Show a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Add a person to the address book.

Format: `add n/NAME [p/PHONE_NUMBER] [a/ADDRESS] [r/REMARK] [l/MEETING_LINK] [t/TAG]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags (including 0)
</div>

* Only `n/NAME` is required.
* `p/PHONE_NUMBER`, `a/ADDRESS`, `r/REMARK`, `l/MEETING_LINK`, and `t/TAG` are optional.
* `add n/John Doe` and `add n/John Doe p/` are both valid. Both create a contact without a phone number.
* If a phone number is specified, it needs to be 8 digits long, and begin with 6, 8, or 9.
* Similarly, `add n/John Doe` and `add n/John Doe a/` are both valid. Both create a contact without an address.
* If an address is specified, it cannot contain a `/` inside.
* This behaviour is similar for remark and meeting link. `add n/John Doe r/` and `add n/John Doe l/` are both valid.
  They create a contact without a remark and meeting link respectively.
* If the new contact is a duplicate of an existing contact, it will not be added. Duplicate contacts are defined as those with the same name, phone number and address.

Examples:
* `add n/John Doe t/Student p/98765432 a/John street, block 123, #01-01 r/new student`
* `add n/John Doe a/John street, block 123, #01-01 t/Parent t/Tutor`
* `add n/Jane Doe p/98765432`
* `add n/Jane Doe p/`
* `add n/Jane Doe a/`

The first example gives the following expected output:

  ![result for 'add n/John Doe t/Student p/98765432 a/John street, block 123, #01-01 r/new student'](images/AddCommandResult.png)

### Listing all persons: `list`

Show a list of all persons in the address book.

Format: `list`

* Phone number, address, meeting schedule, remark, and meeting link are optional fields.
* If any of those fields is missing, the UI shows a missing-field indicator for that contact.

  ![result for 'list' with missing optional fields](images/missingPhoneNumberAddressAndRemark.png)

### Editing a person: `edit`

Edit an existing person in the address book.

Format: `edit ID [n/NAME] [p/PHONE] [a/ADDRESS] [d/DAY_TIME] [r/REMARK] [l/MEETING_LINK] [t/TAG]… [tdel/TAG]…​`

* `ID` specifies the person to be edited.
* `ID` **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* `p/PHONE` updates the stored phone number. You can remove the stored phone number by typing `p/`
  without specifying any value after it. Similar to [add](#adding-a-person-add), the phone number specified must have 8 digits, and start with 6, 8 or 9.
* `a/ADDRESS` updates the stored address. You can remove the stored address by typing `a/` without
  specifying any value after it. Similar to [add](#adding-a-person-add) command, the address specified cannot contain `/`.
* `d/DAY_TIME` updates the stored meeting schedule. You can store either a single time or a duration,
  both with a weekday.
* `d/DAY_TIME` accepts `Day HH:mm`, `Day HHmm`, `Day HH:mm - HH:mm`, and `Day HHmm - HHmm`.
* `d/DAY_TIME` accepts the weekday values `Monday`, `Tuesday`, `Wednesday`, `Thursday`, `Friday`,
  `Saturday`, and `Sunday`.
* You can remove the stored meeting schedule by typing `d/` without specifying any value after it.
* Schedules are displayed in EduConnect in normalized form. For example, `monday 1800` is shown as `Monday 18:00`, while
  `wednesday 1800 - 1930` is shown as `Wednesday 18:00 - 19:30`.
* `r/REMARK` updates the stored remark. You can remove the stored remark by typing `r/` without
  specifying any value after it.
* `l/MEETING_LINK` updates the stored meeting link. The value must be a valid URL starting with
  `http://` or `https://`. You can remove the stored meeting link by typing `l/` without specifying
  any value after it.
* Use this command for all tag updates. EduConnect does not provide a separate `tag` command.
* `t/TAG` appends the provided tags to the person’s existing tags.
* `tdel/TAG` removes the provided tags from the person’s existing tags.
* Only valid tags may be used: `Student`, `Parent`, `Tutor`.
* Repeating an existing tag has no effect because duplicate tags are not stored.
* Deleting a tag that the person does not currently have has no effect.
* You can remove all the person’s tags by typing `t/` without specifying any tag after it.
* When used to clear tags, bare `t/` must not be combined with tag values or `tdel/` values in the same command.
  It may still be combined with non-tag edits such as `n/`, `p/`, `a/`, `d/`, `r/`, or `l/`.
* The same tag cannot be added and deleted in the same command.

Examples:
*  `edit 1 p/91234567`: Edit the phone number of the person with `ID` 1, changing it to `91234567`.
*  `edit 1 d/Monday 18:00`: Update the stored meeting time of the person with `ID` 1 to `Monday 18:00`.
*  `edit 1 d/Wednesday 1800 - 1930`: Update the stored meeting time of the person with `ID` 1 to `Wednesday 18:00 - 19:30`.
*  `edit 1 l/https://zoom.us/j/123456789`: Update the stored meeting link of the person with `ID` 1.
*  `edit 1 t/Parent`: Append the tag `Parent` to the person with `ID` 1.
*  `edit 1 t/Parent t/Tutor`: Append both `Parent` and `Tutor` to the person with `ID` 1.
*  `edit 1 tdel/Student`: Delete the tag `Student` from the person with `ID` 1.
*  `edit 1 d/Monday 18:00 l/https://zoom.us/j/123456789 t/Tutor tdel/Student`: Update the stored meeting
   time, update the stored meeting link, append the tag `Tutor`, and delete the tag `Student` for the
   person with `ID` 1.
*  `edit 2 n/Betsy Crower t/`: Edit the name of the person with `ID` 2, changing it to `Betsy Crower`, whilst clearing all existing tags.
*  `edit 2 d/`: Clear the stored time of the person with `ID` 2.
*  `edit 2 l/`: Clear the stored meeting link of the person with `ID` 2.

### Locating persons: `find`

Find persons whose specified fields contain any of the given keywords.

Format: `find [n/NAME]... [a/ADDRESS]... [p/PHONE]... [t/TAG]... [r/REMARK]...`

* At least one prefixed keyword must be provided.
* Unprefixed input is not allowed. e.g. `find Ali` is invalid.
* `n/` searches names, `a/` searches addresses, `p/` searches phone numbers, `t/` searches tags and `r/` searches remarks.
* The search is case-insensitive for names, addresses, tags and remarks. e.g. `n/hans` will match `Hans` and `t/student` will match `Student`
* Phone matching is digit-based substring matching. e.g. `p/9435` will match a phone number containing `9435`
* Partial matches are supported. e.g. `n/Han` will match `Hans`
* Persons matching at least one prefixed keyword will be returned (i.e. `OR` search across all provided fields and keywords).
* Repeating the same prefix is allowed. e.g. `find n/Ali n/August`

Examples:
* `find a/119224` returns persons whose address contains `119224`
* `find n/Clement` returns persons whose name contains `Clement`
* `find p/9435` returns persons whose phone number contains `9435`
* `find n/aleX a/seran` returns persons whose name contains `aleX` or whose address contains `seran`
* `find t/student` returns persons whose tags contain `student`
* `find n/Ali n/August` returns persons whose names contain `Ali` or `August`
* `find r/first` returns persons whose remarks contain `first` or `First` (note that the search in case-insensitive)

Notes:
- Every search term must be attached to a prefix.
- Contacts matching multiple keywords still appear only once in the filtered list.
- Contacts without an address will not match `a/` keywords.

### Deleting a person: `del`

Delete the specified person from the address book.

Format: `del ID`

* Deletes the person with the specified `ID`.
* `ID` **must be a positive integer** 1, 2, 3, …​

Examples:
* `del 2`: Delete the person with `ID` 2 from the address book.
* `find n/Betsy` followed by `del 1`: Delete the person with `ID` 1 from the address book. Note that it does not delete the first person in the results of the `find` command.
* `add n/Andrew` followed by `del 1`: Delete the person with `ID` 1 from the address book. Note that it does not delete the contact that was just added.
* `add n/Andrew` followed by `del 1`: Fail if there is no person with `ID` 1 in the address book.

### Copying a person information: `copy`

Copy a specified field of a person from the address book to the user clipboard.

Format: `copy ID FIELD`

* Possible fields include `n/` for name, `p/` for phone number, `a/` for address, and `l/` for meeting link.
* Copy is not supported for the meeting schedule, tags, or remark fields. The `d/`, `t/`, `tdel/`, and `r/`
  fields are invalid for this command.
* If the person's field is empty, then nothing will be copied to the clipboard.

Examples:
* `copy 6 n/`: Copy the name of the person with `ID` 6 to the clipboard.
* `copy 7 p/`: Copy the phone number of the person with `ID` 7 to the clipboard.
* `copy 9 a/`: Copy the address of the person with `ID` 9 to the clipboard.
* `copy 1 l/`: Copy the meeting link of the person with `ID` 1 to the clipboard.
* `copy 1 p/`: Fail if `ID` 1 is not found or the phone number field of the person with `ID` 1 is empty.

### Clearing all entries: `clear`

Clear all entries from the address book, whilst displaying all the contacts that have been removed.

Format: `clear`

### Exiting the program: `exit`

Exit the program.

Format: `exit`

### Saving the data

EduConnect data is saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

EduConnect data is saved automatically as a JSON file `[JAR file location]/data/educonnect.json`. Advanced users are welcome to update data directly by editing that data file.

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
**Add** | `add n/NAME [p/PHONE_NUMBER] [a/ADDRESS] [r/REMARK] [l/MEETING_LINK] [t/TAG]…​` <br> e.g., `add n/James Ho`, `add n/James Ho p/`, `add n/James Ho p/82224444 a/123, Clementi Rd, 1234665 r/new student l/https://zoom.us/j/123456789 t/Parent t/Tutor`
**Clear** | `clear`
**Delete** | `del ID`<br> e.g., `del 3`
**Edit** | `edit ID [n/NAME] [p/PHONE_NUMBER] [a/ADDRESS] [d/DAY_TIME] [r/REMARK] [l/MEETING_LINK] [t/TAG]…​ [tdel/TAG]…​`<br> e.g., `edit 2 d/Monday 18:00 l/https://zoom.us/j/123456789 t/Parent tdel/Tutor`
**Find** | `find [n/NAME]... [a/ADDRESS]... [p/PHONE]... [t/TAG]... [r/REMARK]...`<br> e.g., `find n/James t/Student`
**List** | `list`
**Copy** | `copy ID FIELD`<br> e.g., `copy 1 l/`
**Exit** | `exit`
**Help** | `help`
