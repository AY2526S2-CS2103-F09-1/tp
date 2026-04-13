---
layout: page
title: Basil Kuok Project Portfolio Page
---

### Project: EduConnect

EduConnect is a desktop contact manager designed for tutors who prefer a fast keyboard-driven workflow. It helps tutors manage contacts, weekly lesson timeslots, tags, remarks, and meeting links from a CLI-style interface.

### Summary of contributions

My work focused on improving the tutor-facing data model and edit workflow, especially for weekly scheduling, tag management, and optional contact fields. I contributed across implementation, testing, and documentation so that these changes remained consistent across the parser, model, storage, UI, User Guide, and Developer Guide.

**Code contributed:** [RepoSense link](https://nus-cs2103-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=basilkuok&tabRepo=AY2526S2-CS2103-F09-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=functional-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Enhancements implemented

#### 1. Reworked the contact time field into tutor-oriented weekly timeslots

I helped evolve the original time/date support into a weekly timeslot model that better matches how tutors schedule recurring lessons.

- Replaced the old date-based representation with a `Time` model that stores canonical weekly slots such as `Monday 18:00` and `Wednesday 18:00 - 19:30`. Representative commits: [Replace date field with time](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/9c2abaf1), [Support time ranges in time field](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/ad6b778d), [Support weekday parsing in Time](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/dbe6d910).
- Extended the implementation across parser, model, storage, and UI so users can add, edit, persist, and display weekly timeslots consistently. Main PR: [#203 Add time field for edit command](https://github.com/AY2526S2-CS2103-F09-1/tp/pull/203).
- Added parsing support for multiple accepted input forms such as `Mon 1800`, `Monday 18:00`, and duration-style inputs, while normalizing them into a single stored format.
- Added regression tests covering canonicalization, alternate formats, invalid input rejection, storage compatibility, and rendering behavior.

This work improved product fit for tutors because recurring weekly availability is more useful than storing isolated dates.

#### 2. Enhanced `edit` to support richer tag operations

I extended the `edit` command so tags are no longer treated as a simple overwrite-only field.

- Implemented support for appending tags instead of always replacing the entire tag set. Representative commit: [Append tags in edit command](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/c115fadd).
- Added `tdel/` support for deleting specific tags during edit, and supported mixed add/delete flows in a single command. Main PR: [#200 Implement delete specific tag for edit command](https://github.com/AY2526S2-CS2103-F09-1/tp/pull/200).
- Added validation for invalid tag reset combinations and conflicting edits where the same tag is both added and deleted. Representative commits: [Clarify edit tag reset parsing](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/699f5c65), [Move edit messages to shared Messages class](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/6c72ebf0).
- Standardized tag terminology and shared messages so parser behavior, command behavior, and documentation stayed aligned. Main PR: [#199 Rename all categories to tag](https://github.com/AY2526S2-CS2103-F09-1/tp/pull/199).

I also added regression tests for repeated-tag inputs, tag deletion, reset behavior, and conflicting combinations so that later parser or command changes would be less likely to break edit semantics.

#### 3. Refactored optional contact fields, especially address handling

I contributed to making the product more tolerant of incomplete contact information, which is realistic for tutors who may not have every detail upfront.

- Refactored the model to use `Optional<Address>` rather than assuming every contact must always carry an address. Main PR: [#196 Optional address field](https://github.com/AY2526S2-CS2103-F09-1/tp/pull/196).
- Updated the parser, storage, and UI paths so missing addresses are preserved correctly and rendered clearly.
- Added regression coverage for optional-address behavior, reducing the risk of null-related or display-related regressions. Representative commits: [Refactor person addresses to use Optional](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/8d39368f), [Add regression tests for optional addresses](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/6131bea1).

This improved the product’s usability because tutors can still store a contact even when an address is not yet known.

### Testing contributions

Testing was a recurring part of my contribution, not an afterthought. I added and updated tests whenever I changed parser, model, or command behavior, especially for high-risk flows that span multiple layers.

Examples include:

- time parsing and canonicalization tests in `TimeTest` and `ParserUtilTest`
- edit-command regression tests for tag appending, tag deletion, reset behavior, and empty-prefix behavior
- optional-address regression tests
- UI and storage tests for the new time field and optional-field rendering
- duplicate-contact regression tests after case-insensitive identity behavior was clarified

Representative commits include [Add tests for edit tag deletion](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/3f2732e7), [Add regression tests for optional addresses](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/6131bea1), [Add duplicate tag edit coverage](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/a36ba500), and [Tests: Cover edit duplicate via LogicManager](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/a4ecc7cc).

### Documentation contributions

I also contributed to the project documents so that the implemented behavior remained understandable to both users and developers.

- Added and refined Developer Guide use cases. Main PR: [#53 Add Use Cases in Developer Guide](https://github.com/AY2526S2-CS2103-F09-1/tp/pull/53).
- Updated the User Guide for time-related inputs, tag-editing behavior, and command examples. Representative commits: [Update docs for time field](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/73f46b75), [Update docs for edit tag deletion](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/a24cbe34), [Clarify tag clearing rule in guide](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/a5f69be3).
- Updated supporting visuals and diagrams, including the edit-command sequence diagram and command-result screenshots. Representative PR/commits: [#172 Update edit command sequence UML](https://github.com/AY2526S2-CS2103-F09-1/tp/pull/172), [Add edit command screenshots](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/1393445e), [Add find command screenshots](https://github.com/AY2526S2-CS2103-F09-1/tp/commit/7e98137d), [Add Screenshot for AddCommandResult](https://github.com/AY2526S2-CS2103-F09-1/tp/pull/304).

### Team-based and supporting contributions

My role in the team was **Testing**. In practice, this meant helping ensure that new behaviors were exercised through regression tests and that command behavior stayed aligned with the documented workflow expected by users. I also contributed supporting assets such as the team profile page and image updates early in the project setup. Representative PRs: [#14 Add Basil's Photo](https://github.com/AY2526S2-CS2103-F09-1/tp/pull/14), [#15 Add updated photo](https://github.com/AY2526S2-CS2103-F09-1/tp/pull/15), [#26 Update Basil's About Us Page that includes email](https://github.com/AY2526S2-CS2103-F09-1/tp/pull/26).

Overall, my contributions focused on making EduConnect more practical for tutors by improving recurring schedule support, making `edit` more expressive, and strengthening the product’s handling of incomplete but realistic contact data.
